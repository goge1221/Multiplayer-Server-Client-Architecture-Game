package server.main.validator;

import messagesBase.UniqueGameIdentifier;
import server.exceptions.InvalidGameIdException;
import server.main.game.GameHandler;
import server.main.mapHandler.MapNode;

import java.util.Collection;

public class CheckIfGameIdValid implements IRulesValidator{
    @Override
    public void validateMap(Collection<MapNode> halfMapNodes) {

    }

    @Override
    public void playerChecks(GameHandler gameHandler, UniqueGameIdentifier gameId, String playerId) {

    }

    @Override
    public void gameChecks(GameHandler gameHandler, UniqueGameIdentifier gameId) {
        if (!gameHandler.gameIsActive(gameId))
            throw new InvalidGameIdException("Name: Invalid game id", "Message: The game id does not exist");
    }
}
