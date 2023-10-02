package server.main.validator;

import messagesBase.UniqueGameIdentifier;
import server.exceptions.TooManyPlayersException;
import server.main.game.GameHandler;
import server.main.mapHandler.MapNode;

import java.util.Collection;

public class CheckPlayerCount implements IRulesValidator{

    @Override
    public void validateMap(Collection<MapNode> halfMapNodes) {

    }

    @Override
    public void playerChecks(GameHandler gameHandler, UniqueGameIdentifier gameId, String playerId) {
        if (gameHandler.getGameById(gameId).getPlayers().size() > 2)
            throw new TooManyPlayersException("Name: Too many players registered", "Message: The game already has 2 players registered to it");
    }

    @Override
    public void gameChecks(GameHandler gameHandler, UniqueGameIdentifier gameId) {

    }
}
