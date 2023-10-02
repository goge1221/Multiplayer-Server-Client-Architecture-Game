package client.main.moveHandler.pathFinding;

import client.main.map.halfMap.HalfMapGenerator;
import client.main.map.mapNode.MapNode;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PathFinderTest {

    @Test
    void GivenMapNodes_TransformAllNodesToIndexes_CheckSizeOfPoints(){

        //Arrange
        PathFinder pathFinder = new PathFinder();
        Collection<MapNode> mapNodes = new HalfMapGenerator().getHalfMap();

        //Act
        int result = pathFinder.transformPointsToIndexes(mapNodes).size();

        //Assert
        assertEquals(result, 40);
    }

}