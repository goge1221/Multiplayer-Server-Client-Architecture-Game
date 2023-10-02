package server.main.validator.mapRules;

import messagesBase.UniqueGameIdentifier;
import server.exceptions.InvalidMapException;
import server.main.game.GameHandler;
import server.main.mapHandler.MapNode;
import server.main.validator.IRulesValidator;

import java.util.Collection;

public class CountTerrainTypes implements IRulesValidator {
    @Override
    public void validateMap(Collection<MapNode> halfMapNodes) {
        int countWaterFields = 0;
        int countMountainFields = 0;
        int countGrassFields = 0;

        for (MapNode mapNode : halfMapNodes) {
            switch (mapNode.getTerrainType()) {
                case Grass -> ++countGrassFields;
                case Mountain -> ++countMountainFields;
                case Water -> ++countWaterFields;
            }
        }

        if (countMountainFields < 5) {
            throw new InvalidMapException("Name: Not enough mountain fields", "Message: The map needs to have at least 5 mountains, it currently has only " + countMountainFields);
        }
        if (countWaterFields < 7) {
            throw new InvalidMapException("Name: Not enough water fields", "Message: The map needs to have at least 7 water fields, it currently has only " + countWaterFields);
        }
        if (countGrassFields < 24) {
            throw new InvalidMapException("Name: Not enough grass fields", "Message: The map needs to have at least 25 grass fields, it currently has only " + countGrassFields);
        }
        if (countGrassFields + countWaterFields + countMountainFields != 50){
            throw new InvalidMapException("Name: Not enough fields", "Message: The map needs to have exactly 50 fields, it currently has only " + (
                    countGrassFields + countMountainFields + countWaterFields
            ));
        }
    }

    @Override
    public void playerChecks(GameHandler gameHandler, UniqueGameIdentifier gameId, String playerId) {

    }

    @Override
    public void gameChecks(GameHandler gameHandler, UniqueGameIdentifier gameId) {

    }
}
