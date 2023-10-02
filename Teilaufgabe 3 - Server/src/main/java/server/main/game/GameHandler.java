package server.main.game;

import messagesBase.UniqueGameIdentifier;
import messagesBase.UniquePlayerIdentifier;
import messagesBase.messagesFromClient.PlayerHalfMapNode;
import messagesBase.messagesFromServer.FullMap;
import messagesBase.messagesFromServer.GameState;
import messagesBase.messagesFromServer.PlayerState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.exceptions.TooManyHalfMapsSendException;
import server.main.Converter;
import server.main.RandomGameIdGenerator;
import server.main.mapHandler.MapNode;
import server.main.player.Player;
import server.main.validator.RulesValidator;

import java.util.*;

public class GameHandler {

    private final Converter converter = new Converter();
    private final List<Game>ongoingGames = new ArrayList<>();

    private final Logger logger = LoggerFactory.getLogger(GameHandler.class);

    public void addNewGame(UniqueGameIdentifier gameId, RulesValidator rulesValidator){
        if (ongoingGames.size() > 999){
            ongoingGames.remove(0);
        }
        ongoingGames.add(new Game(gameId, rulesValidator));
    }

    public boolean gameIsActive(UniqueGameIdentifier gameId){
        return getGameById(gameId) != null;
    }

    public void addPlayerToGame(UniqueGameIdentifier gameId, Player player){
        getGameById(gameId).addPlayerToGame(player);
    }

    public boolean isPlayerRegisteredToGame(UniqueGameIdentifier gameId, String uniquePlayerId){
        return getGameById(gameId).containsPlayer(uniquePlayerId);
    }

    public void addMapNodes(UniqueGameIdentifier gameId, Collection<PlayerHalfMapNode> mapNodes, String uniquePlayerID){

        if (getGameById(gameId).playerAlreadySentMap(uniquePlayerID))
            throw new TooManyHalfMapsSendException("Name: HalfMap send twice exception", "Message: " +
                    "You already have sent your half map, can't send it twice");

        Collection<MapNode> converterMapNodes = new HashSet<>();
        for (PlayerHalfMapNode playerHalfMapNode : mapNodes)
            converterMapNodes.add(converter.toMapNode(playerHalfMapNode));

        logger.info("Player [" + uniquePlayerID + "] send map: " + converterMapNodes);
        getGameById(gameId).assembleHalfMap(converterMapNodes, uniquePlayerID);
    }

    public GameState getGameState(UniqueGameIdentifier gameId, UniquePlayerIdentifier playerId){

        if (!bothPlayersSendHalfMaps(gameId)) {
            return getGameStateBeforeMapIsAssembled(gameId, playerId);
        }
        FullMap fullMap = converter.toFullMap(getGameById(gameId).getMapNodes(playerId));

        Collection<PlayerState> playerStates = new HashSet<>();
        for (Player player : getGameById(gameId).getPlayers()) {

            if (player.getUniquePlayerIdentifier().equals(playerId))
                playerStates.add(converter.toPlayerState(player, false));
            else
                playerStates.add(converter.toPlayerState(player, true));
        }
        return new GameState(Optional.ofNullable(fullMap), playerStates, gameId.getUniqueGameID());
    }

    private GameState getGameStateBeforeMapIsAssembled(UniqueGameIdentifier gameId, UniquePlayerIdentifier playerId) {

        String gameStateId = gameId.getUniqueGameID();

        if (bothPlayersRegistered(gameId)){
            if (noPlayerSendHisHalfMap(gameId)){
                getGameById(gameId).makeOnePlayerSendHisMap();
            }
            else if (onePlayerSendHalfMap(gameId)){
                getGameById(gameId).tellSecondPlayerToSendMap();
                gameStateId = RandomGameIdGenerator.generateRandomGameId().getUniqueGameID();
            }
            else if (bothPlayersSendHalfMaps(gameId))
                gameStateId = RandomGameIdGenerator.generateRandomGameId().getUniqueGameID();
        }
        Collection<PlayerState> playerStates = new HashSet<>();

        for (Player player : getGameById(gameId).getPlayers()) {
            if (player.getUniquePlayerIdentifier().equals(playerId))
                playerStates.add(converter.toPlayerState(player, false));
            else
                playerStates.add(converter.toPlayerState(player, true));
        }

        return new GameState(playerStates, gameStateId);
    }

    private boolean bothPlayersSendHalfMaps(UniqueGameIdentifier gameId){
        return getGameById(gameId).playerSentMapSize() == 2;
    }

    private boolean onePlayerSendHalfMap(UniqueGameIdentifier gameId){
        return getGameById(gameId).playerSentMapSize() == 1;
    }
    private boolean bothPlayersRegistered(UniqueGameIdentifier gameId){
        return getGameById(gameId).getPlayers().size() == 2;
    }

    private boolean noPlayerSendHisHalfMap(UniqueGameIdentifier gameId){
        return getGameById(gameId).playerSentMapSize() == 0;
    }

    public Game getGameById(UniqueGameIdentifier gameId){
        for (Game game : ongoingGames)
            if (game.getGameId().equals(gameId))
                return game;
        return null;
    }



}
