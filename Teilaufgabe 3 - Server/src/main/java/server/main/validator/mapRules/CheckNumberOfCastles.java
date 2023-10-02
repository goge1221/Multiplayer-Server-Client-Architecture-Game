package server.main.validator.mapRules;

import messagesBase.UniqueGameIdentifier;
import server.exceptions.InvalidMapException;
import server.main.game.GameHandler;
import server.main.mapHandler.EFortStateServer;
import server.main.mapHandler.MapNode;
import server.main.validator.IRulesValidator;

import java.util.Collection;

public class CheckNumberOfCastles implements IRulesValidator {
    @Override
    public void validateMap(Collection<MapNode> halfMapNodes) {
        int countCastles = 0;

        for (MapNode mapNode : halfMapNodes) {
            if (mapNode.getFortStateServer() == EFortStateServer.MyFortPresent)
                ++countCastles;
        }

        if (countCastles == 0) {
            throw new InvalidMapException("Name: No castle found on map", "Message: There should be one castle present, currently there is none placed.");
        }
        if (countCastles > 1) {
            throw new InvalidMapException("Name: Too many castles present", "Message: There are too many castles placed which is not allowed.");
        }
    }

    @Override
    public void playerChecks(GameHandler gameHandler, UniqueGameIdentifier gameId, String playerId) {

    }

    @Override
    public void gameChecks(GameHandler gameHandler, UniqueGameIdentifier gameId) {

    }
}
