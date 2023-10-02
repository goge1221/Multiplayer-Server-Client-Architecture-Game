package client.main.moveHandler.pathFinding;

import client.main.map.mapNode.EPlayerState;
import client.main.map.mapNode.ETerrainType;
import client.main.map.mapNode.MapNode;
import client.main.moveHandler.CoordinateTransformer;

import java.awt.*;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;

/**
 * The MapTransitioner calculates the shortest path to an enemy border
 */
public class EnemyHalfMapEntryPointCalculator {

    public int calculatePathToEnemyHalfMap(Collection<MapNode> fullMap, Point playerPosition) {
        //It means the map has a vertical form and the player is on the down side of it
        if (playerPosition.y > 4)
            return CoordinateTransformer.transformPointToIndex(new Point(getVerticalEntryPoint(fullMap)));
        //The Map is Horizontal and the player is on the Right side of it
        if (playerPosition.x > 9)
            return CoordinateTransformer.transformPointToIndex(new Point(getHorizontalEntryPoint(fullMap)));
        return CoordinateTransformer.transformPointToIndex(new Point(getPointFromMapBeginning(fullMap)));
    }

    protected Point getPointFromMapBeginning(Collection<MapNode> fullMap) {

        if (mapIsHorizontal(fullMap))
            return new Point(9, getHorizontalEntryPoint(fullMap).y);

        return new Point(getVerticalEntryPoint(fullMap).x, 4);
    }

    protected boolean mapIsHorizontal(Collection<MapNode> fullMap) {
        for (MapNode mapNode : fullMap)
            if (mapNode.getCoordinates().x > 9)
                return true;
        return false;
    }

    protected Point getHorizontalEntryPoint(Collection<MapNode> fullMap) {
        HashSet<Integer> pointsOnEnemiesBorder = new HashSet<>();
        HashSet<Integer> pointsOnMyBorder = new HashSet<>();
        Point playerPosition = new Point();

        for (MapNode mapNode : fullMap)
            if (mapNode.getTerrainType() != ETerrainType.WATER) {
                if (mapNode.getCoordinates().x == 9)
                    pointsOnEnemiesBorder.add(mapNode.getCoordinates().y);
                if (mapNode.getCoordinates().x == 10)
                    pointsOnMyBorder.add(mapNode.getCoordinates().y);
                if (myPlayerIsPresent(mapNode))
                    playerPosition = mapNode.getCoordinates();
            }

        int closestPoint = getClosestEntryPoint(getOnlyCommonEntryPoints(pointsOnMyBorder, pointsOnEnemiesBorder), playerPosition.y);
        return new Point(10, closestPoint);
    }


    protected Point getVerticalEntryPoint(Collection<MapNode> fullMap) {
        HashSet<Integer> pointsOnEnemiesBorder = new HashSet<>();
        HashSet<Integer> pointsOnMyBorder = new HashSet<>();
        Point playerPosition = new Point();

        for (MapNode mapNode : fullMap)
            if (mapNode.getTerrainType() != ETerrainType.WATER) {
                if (mapNode.getCoordinates().y == 4)
                    pointsOnEnemiesBorder.add(mapNode.getCoordinates().x);
                if (mapNode.getCoordinates().y == 5)
                    pointsOnMyBorder.add(mapNode.getCoordinates().x);
                if (myPlayerIsPresent(mapNode))
                    playerPosition = mapNode.getCoordinates();
            }

        int closestNumber = getClosestEntryPoint(getOnlyCommonEntryPoints(pointsOnMyBorder, pointsOnEnemiesBorder), playerPosition.x);
        return new Point(closestNumber, 5);
    }

    protected int getClosestEntryPoint(HashSet<Integer>commonPoints, int playerPosition){
        return commonPoints.stream().min(Comparator.comparingInt(i -> Math.abs(i - playerPosition)))
                .orElseThrow();
    }


    protected HashSet<Integer> getOnlyCommonEntryPoints(HashSet<Integer> pointsOnMyBorder, HashSet<Integer> pointsOnEnemyBorder){
        HashSet<Integer> commonPoints = new HashSet<>();

        for (int point : pointsOnEnemyBorder)
            if (point != 0 && point != 9 && point != 40 && point != 49)
                if (pointsOnMyBorder.contains(point))
                    commonPoints.add(point);

        return commonPoints;
    }

    protected boolean myPlayerIsPresent(MapNode mapNode) {
        return mapNode.getPlayerState() == EPlayerState.MyPlayerPresent || mapNode.getPlayerState() == EPlayerState.BothPlayerPresent;
    }

}
