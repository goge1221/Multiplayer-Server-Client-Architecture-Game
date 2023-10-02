package server.main.validator;

import messagesBase.UniqueGameIdentifier;
import server.exceptions.PlayerNotRegisteredToGameException;
import server.main.game.GameHandler;
import server.main.mapHandler.MapNode;

import java.util.Collection;

public class CheckIfPlayerIsRegisteredToGame implements IRulesValidator{
    @Override
    public void validateMap(Collection<MapNode> halfMapNodes) {

    }

    @Override
    public void playerChecks(GameHandler gameHandler, UniqueGameIdentifier gameId, String playerId) {
        if (!gameHandler.isPlayerRegisteredToGame(gameId, playerId))
            throw new PlayerNotRegisteredToGameException("Name: Player not registered to game", "Message:" +
                    "The player is not registered to any game.");
    }

    @Override
    public void gameChecks(GameHandler gameHandler, UniqueGameIdentifier gameId) {

    }
}
