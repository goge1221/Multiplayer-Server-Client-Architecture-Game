package server.main.validator.mapRules;

import messagesBase.UniqueGameIdentifier;
import server.exceptions.InvalidMapException;
import server.main.game.GameHandler;
import server.main.mapHandler.ETerrainServer;
import server.main.mapHandler.MapNode;
import server.main.validator.IRulesValidator;

import java.util.Collection;

public class CheckWaterOnShortEdges implements IRulesValidator {
    @Override
    public void validateMap(Collection<MapNode> halfMapNodes) {
        int waterOnRightEdge = 0;
        int waterOnLeftEdge = 0;

        for (MapNode mapNode : halfMapNodes) {
            if (mapNode.getxCoordinate() == 0 && mapNode.getTerrainType() == ETerrainServer.Water) {
                ++waterOnLeftEdge;
            }
            if (mapNode.getxCoordinate() == 9 && mapNode.getTerrainType() == ETerrainServer.Water) {
                ++waterOnRightEdge;
            }
        }
        if (waterOnLeftEdge > 2) {
            throw new InvalidMapException("Name: Too much water on edges", "Message: Too much water on the left short edge, allowed 2 but got " + waterOnLeftEdge);
        }
        if (waterOnRightEdge > 2) {
            throw new InvalidMapException("Name: Too much water on edges", "Message: Too much water on the right short edges, allowed 2 but got " + waterOnRightEdge);
        }
    }

    @Override
    public void playerChecks(GameHandler gameHandler, UniqueGameIdentifier gameId, String playerId) {

    }

    @Override
    public void gameChecks(GameHandler gameHandler, UniqueGameIdentifier gameId) {

    }
}
