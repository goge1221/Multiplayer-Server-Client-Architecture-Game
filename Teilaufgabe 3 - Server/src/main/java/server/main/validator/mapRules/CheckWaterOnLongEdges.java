package server.main.validator.mapRules;

import messagesBase.UniqueGameIdentifier;
import server.exceptions.InvalidMapException;
import server.main.game.GameHandler;
import server.main.mapHandler.ETerrainServer;
import server.main.mapHandler.MapNode;
import server.main.validator.IRulesValidator;

import java.util.Collection;

public class CheckWaterOnLongEdges implements IRulesValidator {
    @Override
    public void validateMap(Collection<MapNode> halfMapNodes) {
        int waterOnTop = 0;
        int waterOnBottom = 0;

        for (MapNode mapNode : halfMapNodes) {
            if (mapNode.getTerrainType() == ETerrainServer.Water) {
                if (mapNode.getyCoordinate() == 0)
                    ++waterOnTop;
                if (mapNode.getyCoordinate() == 4)
                    ++waterOnBottom;
            }
        }
        if (waterOnTop > 4) {
            throw new InvalidMapException("Name: Too much water on edges", "Message: Too much water on the top edge, allowed 4 but got " + waterOnTop);
        }
        if (waterOnBottom > 4) {
            throw new InvalidMapException("Name: Too much water on edges", "Message: Too much water on the bottom edge, allowed 4 but got " + waterOnBottom);
        }
    }

    @Override
    public void playerChecks(GameHandler gameHandler, UniqueGameIdentifier gameId, String playerId) {

    }

    @Override
    public void gameChecks(GameHandler gameHandler, UniqueGameIdentifier gameId) {

    }
}
