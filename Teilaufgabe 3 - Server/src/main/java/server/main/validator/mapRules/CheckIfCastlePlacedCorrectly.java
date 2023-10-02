package server.main.validator.mapRules;

import messagesBase.UniqueGameIdentifier;
import server.exceptions.InvalidMapException;
import server.main.game.GameHandler;
import server.main.mapHandler.EFortStateServer;
import server.main.mapHandler.ETerrainServer;
import server.main.mapHandler.MapNode;
import server.main.validator.IRulesValidator;

import java.util.Collection;

public class CheckIfCastlePlacedCorrectly implements IRulesValidator {
    @Override
    public void validateMap(Collection<MapNode> halfMapNodes) {
        for (MapNode mapNode : halfMapNodes) {
            if (mapNode.getFortStateServer() == EFortStateServer.MyFortPresent) {
                if (mapNode.getTerrainType() != ETerrainServer.Grass)
                    throw new InvalidMapException("Name: Castle not placed on grass", "Message: Castle should be placed on a grass field and not on "
                            + mapNode.getTerrainType().toString());
            }
        }
    }

    @Override
    public void playerChecks(GameHandler gameHandler, UniqueGameIdentifier gameId, String playerId) {

    }

    @Override
    public void gameChecks(GameHandler gameHandler, UniqueGameIdentifier gameId) {

    }
}
