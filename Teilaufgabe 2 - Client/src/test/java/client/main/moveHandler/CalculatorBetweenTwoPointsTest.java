package client.main.moveHandler;

import client.main.exceptions.NodesNotConnectedException;
import client.main.map.halfMap.HalfMapGenerator;
import client.main.moveHandler.pathFinding.PathFindingController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

class CalculatorBetweenTwoPointsTest {

    @Test
    void GivenEmptyArrayList_CalculatePathBetweenTwoPoints_ExpectedNodesNotConnectedException(){
        //Arrange
        CalculatorBetweenTwoPoints calculatorBetweenTwoPoints = new CalculatorBetweenTwoPoints();
        Map.Entry<LinkedList<Integer>[], ArrayList<Integer>> mapEntry =
                new PathFindingController().calculatePathToGoal(new HalfMapGenerator().getHalfMap(), new Point(0,0));

        //Act
        calculatorBetweenTwoPoints.updateAdjListAndMovesList(mapEntry);
        Executable throwTest = () ->
                calculatorBetweenTwoPoints.getShortestDistance(0, -10, 50);

        //Assert
        Assertions.assertThrows(NodesNotConnectedException.class, throwTest);
    }

}