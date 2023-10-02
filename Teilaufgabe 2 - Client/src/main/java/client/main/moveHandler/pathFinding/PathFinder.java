package client.main.moveHandler.pathFinding;

import client.main.map.mapNode.ETerrainType;
import client.main.map.mapNode.MapNode;
import client.main.moveHandler.CoordinateTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.*;

class PathFinder {

    private final Logger logger = LoggerFactory.getLogger(PathFinder.class);

    //Need to pass on the collection as a parameter for the path calculator
    //He does not need to know how the map is ordered and structured internally
    //as long as it knows which elements are connected with which
    public Map.Entry<LinkedList<Integer>[], ArrayList<Integer>> calculatePath(Collection<MapNode> mapNodes, Point playerPosition){
        logger.debug("The path finder received: " + mapNodes.size() + " map nodes.");

        //When receiving a full map, need to sort out the enemy half map so that it contains only their nodes
        int halfMapSize = 50;
        if (mapNodes.size() > halfMapSize)
            return new GraphPathCalculator(
                    getHalfMapNodesFromSideThatPlayerIsOn(mapNodes, playerPosition),
                    CoordinateTransformer.transformPointToIndex(playerPosition)
            ).getReturnMoves();

        //In the beginning, we can use my generated half map directly without needing to fetch it
        return new GraphPathCalculator(
                    transformPointsToIndexes(mapNodes),
                    CoordinateTransformer.transformPointToIndex(playerPosition)
            ).getReturnMoves();

    }


    //This function sorts only the halfMapNodes that a player has and
    // returns them in form of transformed indexes
    private HashSet<Integer> getHalfMapNodesFromSideThatPlayerIsOn(Collection<MapNode> mapNodes, Point playerPosition){

        int xAtHalfMap = 9;
        //This means that the map is horizontal and player has the right part of the map
        if (playerPosition.x > xAtHalfMap)
            return getHorizontalMapNodes(mapNodes);

        //The map is vertical and player has the down side of the map
        if (playerPosition.y > 4)
            return getVerticalMapNodes(mapNodes, 5);

        return getVerticalMapNodes(mapNodes, 0);
    }

    private HashSet<Integer> getVerticalMapNodes(Collection<MapNode> mapNodes, int yStartingPoint){

        HashSet<MapNode> myHalfMap = new HashSet<>();

        int xCoordinate = 0;
        int yCoordinate = yStartingPoint;
        int xMapEnd = 9;

        for (int i = 0; i < mapNodes.size()/2; ++i){
            if (xCoordinate > xMapEnd){
                xCoordinate = 0;
                ++yCoordinate;
            }
            for (MapNode mapNode : mapNodes)
                if (mapNode.getCoordinates().x == xCoordinate && mapNode.getCoordinates().y == yCoordinate) {
                    myHalfMap.add(mapNode);
                    break;
                }
            ++xCoordinate;
        }
        return transformPointsToIndexes(myHalfMap);
    }

    private HashSet<Integer> getHorizontalMapNodes(Collection<MapNode> mapNodes){

        HashSet<MapNode> myHalfMap = new HashSet<>();

        int xCoordinate;
        int yCoordinate = 0;
        int xStartingPoint = 10;
        int mapEnd = 19;

        xCoordinate = xStartingPoint;

        for (int i = 0; i < mapNodes.size()/2; ++i){

            if (xCoordinate > mapEnd){
                xCoordinate = xStartingPoint;
                ++yCoordinate;
            }

            for (MapNode mapNode : mapNodes)
                if (mapNode.getCoordinates().x == xCoordinate && mapNode.getCoordinates().y == yCoordinate) {
                    myHalfMap.add(mapNode);
                    break;
                }

            ++xCoordinate;
        }
        return transformPointsToIndexes(myHalfMap);
    }

    //Transforms all the node fields which do not consist of water and turns them into
    //indexes which range from 0 to 49
    protected HashSet<Integer> transformPointsToIndexes(Collection<MapNode>mapNodes){
        HashSet<Integer> allIndexesWhichHaveNoWater = new HashSet<>();

        for(MapNode mapNode : mapNodes)
            if (mapNode.getTerrainType() != ETerrainType.WATER)
                allIndexesWhichHaveNoWater.add(CoordinateTransformer.transformPointToIndex(mapNode.getCoordinates()));

        logger.debug("The path finder transformed the nodes it received into the following indexes: "
                + allIndexesWhichHaveNoWater);
        return allIndexesWhichHaveNoWater;
    }
}
