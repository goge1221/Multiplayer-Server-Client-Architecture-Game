package server.main.game;

import messagesBase.UniqueGameIdentifier;
import messagesBase.UniquePlayerIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.exceptions.InvalidMapException;
import server.main.mapHandler.FullMapServer;
import server.main.mapHandler.MapNode;
import server.main.player.EPlayerServerState;
import server.main.player.Player;
import server.main.validator.RulesValidator;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Game {

    private final UniqueGameIdentifier uniqueGameIdentifier;
    private final Set<Player> playerSet;
    private final Set<String> playersWhoSendMap = new HashSet<>();
    private final Logger logger = LoggerFactory.getLogger(Game.class);
    private final FullMapServer fullMapServer;

    public Game(UniqueGameIdentifier uniqueGameIdentifier, RulesValidator rulesValidator){
        this.uniqueGameIdentifier = uniqueGameIdentifier;
        playerSet = new HashSet<>();
        fullMapServer = new FullMapServer(rulesValidator);
    }
    public UniqueGameIdentifier getGameId(){
        return uniqueGameIdentifier;
    }

    public void addPlayerToGame(Player player){
        playerSet.add(player);
        logger.info("GameId[" + uniqueGameIdentifier + "], registered player[" + player.getUniquePlayerIdentifier() + "]");
    }

    public boolean containsPlayer(String uniquePlayerId) {
        for (Player player : playerSet)
            if (player.getUniquePlayerIdentifier().getUniquePlayerID().equals(uniquePlayerId))
                return true;
        return false;
    }

    public Collection<MapNode>getMapNodes(UniquePlayerIdentifier playerId){
        return fullMapServer.getMapNodes(playerId);
    }

    public Set<Player> getPlayers(){
        return playerSet;
    }


    public void makeOnePlayerSendHisMap() {
        boolean firstPlayerSet = false;
        for (Player player : playerSet){
            if (!firstPlayerSet){
                firstPlayerSet = true;
                player.setPlayerState(EPlayerServerState.MustAct);
                logger.info("GameId[" + uniqueGameIdentifier + "], " + player + " This player was set to act");
            }
            else
                logger.info("GameId[" + uniqueGameIdentifier + "], " + player + " This player needs to wait still");
        }

    }

    public void tellSecondPlayerToSendMap() {
        for (Player player : playerSet){
            if (playersWhoSendMap.contains(player.getUniquePlayerIdentifier().getUniquePlayerID())){
                player.setPlayerState(EPlayerServerState.MustWait);
                logger.info("GameId[" + uniqueGameIdentifier + "," + player + " This player just acted, present in MapSend [" +
                        playersWhoSendMap.contains(player.getUniquePlayerIdentifier().getUniquePlayerID()) + "]");
            }
            else {
                player.setPlayerState(EPlayerServerState.MustAct);
                logger.info("GameId[" + uniqueGameIdentifier + "," + player + " This player waited and now needs to act, present in MapSend(false) [" +
                        playersWhoSendMap.contains(player.getUniquePlayerIdentifier().getUniquePlayerID()) + "]");
            }

        }
    }

    public void assembleHalfMap(Collection<MapNode> mapNodes, String uniquePlayerID) {
        boolean addToFirstMap = (playersWhoSendMap.size() == 0);
        for (MapNode mapNode : mapNodes)
            fullMapServer.addMapNode(mapNode, addToFirstMap);

        if (playersWhoSendMap.size() == 0)
            fullMapServer.setFirstPlayerId(uniquePlayerID);
        else if (playersWhoSendMap.size() == 1)
            fullMapServer.setSecondPlayerId(uniquePlayerID);

        rememberPlayerSendMap(uniquePlayerID);

        if (playersWhoSendMap.size() == 2)
            fullMapServer.assembleToFullMap();
    }

    public boolean playerAlreadySentMap(String playerId){
        for (String player : playersWhoSendMap)
            if (player.equals(playerId)) {
                logger.info("Recognized that a player already sent a map: " + playerId + " " + playersWhoSendMap);
                return true;
            }
        return false;
    }

    private void rememberPlayerSendMap(String playerId){
        playersWhoSendMap.add(playerId);
        logger.info("GameId[" + uniqueGameIdentifier + ", [" + playerId + "] send his map");
        if (playersWhoSendMap.size() == 2) {
            makeOtherPlayerAct();
            try{
                fullMapServer.bothMapsOk();
            }catch (InvalidMapException invalidMapException){
                setLoser(fullMapServer.getCulprit());
                throw invalidMapException;
            }
        }
    }

    private void setLoser(String playerId){
        for (Player player : playerSet) {
            if (player.getUniquePlayerIdentifier().getUniquePlayerID().equals(playerId)) {
                player.setPlayerState(EPlayerServerState.Lost);
                logger.info("GameId[" + uniqueGameIdentifier + ", [" + playerId + "] LOST");
            }
            else {
                player.setPlayerState(EPlayerServerState.Won);
                logger.info("GameId[" + uniqueGameIdentifier + ", [" + playerId + "] WON");
            }
        }
    }

    private void makeOtherPlayerAct(){
        for (Player player : playerSet){
            if (player.getServerState() == EPlayerServerState.MustAct){
                player.setPlayerState(EPlayerServerState.MustWait);
            }
            else
                player.setPlayerState(EPlayerServerState.MustAct);
        }
    }

    public int playerSentMapSize(){
        return playersWhoSendMap.size();
    }

}
