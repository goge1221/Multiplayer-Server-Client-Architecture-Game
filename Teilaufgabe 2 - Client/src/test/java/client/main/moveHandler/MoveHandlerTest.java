package client.main.moveHandler;

import client.main.map.halfMap.HalfMapGenerator;
import client.main.map.mapNode.ETerrainType;
import client.main.map.mapNode.MapNode;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class MoveHandlerTest {

    @Test
    void GivenPlayerHasTreasure_CalculatePathToEnemyHalfMap_ExpectValidPath(){

        //Arrange
        MoveHandler moveHandler = new MoveHandler();
        Collection<MapNode> mapNodes = new HalfMapGenerator().getHalfMap();
        moveHandler.initializeTreasureFinder(mapNodes, new Point(2,3));
        for (int i = 0; i < 5; ++i)
            if (i % 2 == 0)
                mapNodes.add(new MapNode(new Point(10, i), ETerrainType.GRASS));
        //Act
        moveHandler.transitionToEnemyHalfMap(mapNodes, new Point(2, 3));

        int result = 29;
        assertEquals(result, moveHandler.getLastElementFromMovesThatNeedToBeMade());

    }


    @Test
    void GivenGeneratedHalfMap_CalculateAlternativePath_ExpectSize3(){

        //Arrange
        MoveHandler moveHandler = new MoveHandler();
        Collection<MapNode> halfMapNodes = new HalfMapGenerator().getHalfMap();

        //Act
        moveHandler.initializeTreasureFinder(halfMapNodes, new Point(0,0));
        moveHandler.clearMovesThatNeedToBeMade();
        moveHandler.addToMovesThatNeedToBeMade(0);
        moveHandler.addToMovesThatNeedToBeMade(30);
        moveHandler.addToMovesThatNeedToBeMade(40);
        moveHandler.getNextMove(new Point(0,0));
        //Assert
        int expectedValue = 4;

        assertEquals(expectedValue, moveHandler.getTempMovesSize());
    }


    @Test
    void GivenGeneratedHalfMap_CalculatePath_ExpectSameAmountsOfNodeToVisitAsNonWaterFields(){

        //Arrange
        MoveHandler moveHandler = new MoveHandler();
        Collection<MapNode> halfMapNodes = new HalfMapGenerator().getHalfMap();

        //Act
        moveHandler.initializeTreasureFinder(halfMapNodes, new Point(0,0));

        //Assert
        int expectedValue = 40;

        assertEquals(expectedValue, moveHandler.getMovesSize());
    }



    @Test
    void GivenTwoPoints_CalculatePathBetweenThem_ExpectRightMoveReturned(){

        //Arrange
        MoveHandler moveHandler = new MoveHandler();
        int startingPoint = 0;
        int endPoint = 10;
        EMoveClient expectedValue = EMoveClient.DOWN;

        //Act
        EMoveClient result = moveHandler.getMoveBasedOnPosition(startingPoint, endPoint);

        //Assert
        assertEquals(expectedValue, result);
    }

    @Test
    void GivenTwoPoints_CheckIfTheyAreDirectlyConnected_ExpectFalse(){

        //Arrange
        MoveHandler moveHandler = new MoveHandler();
        int startingPoint = 0;
        int endPoint = 11;

        //Act
        boolean result = moveHandler.pointsDirectlyConnected(startingPoint, endPoint);

        //Assert
        assertFalse(result);
    }

}