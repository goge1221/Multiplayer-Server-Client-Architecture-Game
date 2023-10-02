package client.main.moveHandler;

import client.main.map.mapNode.EPlayerState;
import client.main.map.mapNode.ETerrainType;
import client.main.map.mapNode.MapNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * The mountain checker checks if the player is on a mountain if the treasure
 * or castle is visible and returns the new target accordingly.
 * If not visible then it removes all the points around the mountain.
 */
public class MountainChecker {

    private final Logger logger = LoggerFactory.getLogger(MountainChecker.class);
    private boolean treasureIsVisible = false;

    private boolean castleIsVisible = false;
    private final HashSet<Integer> toRemove = new HashSet<>();

    private int targetLocation = 0;
    private int playerPosition = 0;

    private Point playerCastlePosition = new Point();

    public boolean playerIsOnMountain(Collection<MapNode>fullMap, boolean playerIsOnEnemyHalfMap){


        if (playerIsOnEnemyHalfMap)
            treasureIsVisible = false;

        for (MapNode mapNode : fullMap) {
            if (mapNode.getPlayerState() == EPlayerState.BothPlayerPresent || mapNode.getPlayerState() == EPlayerState.MyPlayerPresent) {
                playerPosition = CoordinateTransformer.transformPointToIndex(mapNode.getCoordinates());
                if (mapNode.getTerrainType() == ETerrainType.MOUNTAIN) {
                    checkIfCastleOrTreasurePresent(fullMap, playerIsOnEnemyHalfMap);
                    if (!treasureIsVisible)
                        removePlayerSurroundings(fullMap, CoordinateTransformer.transformPointToIndex(mapNode.getCoordinates()));
                    if (!castleIsVisible && playerIsOnEnemyHalfMap)
                        removePlayerSurroundings(fullMap, CoordinateTransformer.transformPointToIndex(mapNode.getCoordinates()));
                    return true;
                }

                if (!playerIsOnEnemyHalfMap && mapNode.isCastlePresent()) {
                    playerCastlePosition = mapNode.getCoordinates();
                    logger.info("PLAYER CASTLE IS LOCATED ON " + playerCastlePosition);
                }
            }
        }
        return false;
    }

    private void removePlayerSurroundings(Collection<MapNode> fullMap, int playerPosition) {
        for (MapNode mapNode : fullMap)
            if (fieldLocatedNearPlayer(CoordinateTransformer.transformPointToIndex(mapNode.getCoordinates()), playerPosition))
                toRemove.add(CoordinateTransformer.transformPointToIndex(mapNode.getCoordinates()));
    }

    private boolean fieldLocatedNearPlayer(int mapNodePosition, int playerPostion){
        int horizontalDistanceBetweenPoints = 10;
        int verticalDistanceBetweenPoints = 1;
        int diagonalDistanceUpSide = 9;
        int diagonalDistanceDownSide = 11;

        if (mapNodePosition == playerPostion - horizontalDistanceBetweenPoints)
            return true;
        if (mapNodePosition == playerPostion + horizontalDistanceBetweenPoints)
            return true;
        if (mapNodePosition == playerPostion - verticalDistanceBetweenPoints)
            return true;
        if (mapNodePosition == playerPostion - diagonalDistanceDownSide)
            return true;
        if (mapNodePosition == playerPostion + diagonalDistanceDownSide)
            return true;
        if (mapNodePosition == playerPostion - diagonalDistanceUpSide)
            return true;
        if (mapNodePosition == playerPostion + diagonalDistanceUpSide)
            return  true;
        return mapNodePosition == playerPostion + verticalDistanceBetweenPoints;
    }

    private void checkIfCastleOrTreasurePresent(Collection<MapNode>fullMap, boolean playerIsOnEnemyHalfMap){
        for (MapNode mapNode : fullMap) {
            if (!playerIsOnEnemyHalfMap) {
                if (mapNode.isTreasurePresent()) {
                    targetLocation = CoordinateTransformer.transformPointToIndex(mapNode.getCoordinates());
                    treasureIsVisible = true;
                }
            }
            else {
                if (mapNode.isCastlePresent() && !playerCastlePosition.equals(mapNode.getCoordinates())){
                    targetLocation = CoordinateTransformer.transformPointToIndex(mapNode.getCoordinates());
                    logger.debug("TARGET CASTLE IS " + targetLocation + " compared mapNode: " + mapNode.getCoordinates() + " and " + playerCastlePosition);
                    castleIsVisible = true;
                }
            }

        }
    }

    public List<Integer> recalculatedRoute(List<Integer> currentRoute){

        List<Integer> actualizedRoute = new ArrayList<>();

        actualizedRoute.add(playerPosition);

        if (treasureIsVisible) {
            logger.info("The treasure is visible and located at: " + targetLocation);
            actualizedRoute.add(targetLocation);
            //This may not be necessary and is added as a safe mechanism to ensure
            //that the ki does not stop
            actualizedRoute.add(0);
            return actualizedRoute;
        }

        if (castleIsVisible){
            logger.info("The castle is visible and located at: " + targetLocation);
            actualizedRoute.add(targetLocation);
            actualizedRoute.add(0);
            return actualizedRoute;
        }

        logger.info("Need to remove following nodes: " + toRemove);

        //If neither the castle or treasure is visible from the mountain remove the
        //fields around them
        for (int point : currentRoute)
            if (!toRemove.contains(point))
                if (point != playerPosition && point != currentRoute.get(0))
                    actualizedRoute.add(point);

        toRemove.clear();
        return actualizedRoute;
    }

}
