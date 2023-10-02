package client.main.moveHandler.pathFinding;

import client.main.map.mapNode.MapNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

public class PathFindingController {

    private final PathFinder pathFinder = new PathFinder();

    private final Logger logger = LoggerFactory.getLogger(PathFindingController.class);
    private final EnemyHalfMapEntryPointCalculator enemyHalfMapEntryPointCalculator = new EnemyHalfMapEntryPointCalculator();

    public Map.Entry<LinkedList<Integer>[], ArrayList<Integer>> calculatePathToGoal(Collection<MapNode>mapNodes, Point playerPosition){
        return pathFinder.calculatePath(mapNodes, playerPosition);
    }

    public int getEntryPointToEnemyHalfMap(Collection<MapNode> mapNodes, Point playerPosition){
        int entryPoint = enemyHalfMapEntryPointCalculator.calculatePathToEnemyHalfMap(mapNodes, playerPosition);
        logger.info("Calculating entry point to enemy half map where both borders are not made of water.");
        logger.info("Calculated entry point is: " + entryPoint);
        return entryPoint;
    }

}
