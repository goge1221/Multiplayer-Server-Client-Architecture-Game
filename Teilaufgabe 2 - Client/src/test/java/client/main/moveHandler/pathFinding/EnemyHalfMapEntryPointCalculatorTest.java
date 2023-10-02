package client.main.moveHandler.pathFinding;

import client.main.map.halfMap.HalfMapGenerator;
import client.main.map.mapNode.EPlayerState;
import client.main.map.mapNode.ETerrainType;
import client.main.map.mapNode.MapNode;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class EnemyHalfMapEntryPointCalculatorTest {

    @Test
    void GivenTwoArraysWithMapEdges_CalculateHorizontalEntryPoint_ExpectClosestPointToPlayer(){

        //Arrange
        EnemyHalfMapEntryPointCalculator entryPointCalculator = new EnemyHalfMapEntryPointCalculator();
        ArrayList<MapNode> mapNodes = (ArrayList<MapNode>) new HalfMapGenerator().getHalfMap();
        MapNode mapNodeWithPlayerOn = new MapNode(
                new Point(3,2),
                ETerrainType.GRASS,
                EPlayerState.MyPlayerPresent,
                false,
                false
        );
        mapNodes.add(mapNodeWithPlayerOn);
        for (int i = 0; i < 5; ++i)
            mapNodes.add(new MapNode(
                    new Point(10, i),
                    ETerrainType.GRASS,
                    EPlayerState.NoPlayerPresent,
                    false,
                    false
            ));

        //Act
        Point result = entryPointCalculator.getPointFromMapBeginning(mapNodes);

        //Assert
        assertEquals(mapNodeWithPlayerOn.getCoordinates().y, result.y);
    }



    @Test
    void GivenMapNodes_CalculateMapForm_ExpectHorizontalMapForm(){

        //Arrange
        EnemyHalfMapEntryPointCalculator entryPointCalculator = new EnemyHalfMapEntryPointCalculator();
        ArrayList<MapNode> mapNodes = (ArrayList<MapNode>) new HalfMapGenerator().getHalfMap();
        MapNode horizontalMapNode = new MapNode(new Point(10, 0), ETerrainType.GRASS);
        mapNodes.add(horizontalMapNode);

        //Act
        boolean result = entryPointCalculator.mapIsHorizontal(mapNodes);

        //Assert
        assertTrue(result);

    }

    @Test
    void GivenMapNodes_CalculateMapForm_ExpectVerticalMapForm(){

        //Arrange
        EnemyHalfMapEntryPointCalculator entryPointCalculator = new EnemyHalfMapEntryPointCalculator();
        ArrayList<MapNode> mapNodes = (ArrayList<MapNode>) new HalfMapGenerator().getHalfMap();
        MapNode verticallMapNode = new MapNode(new Point(9, 5), ETerrainType.GRASS);
        mapNodes.add(verticallMapNode);

        //Act
        boolean result = entryPointCalculator.mapIsHorizontal(mapNodes);

        //Assert
        assertFalse(result);

    }
}