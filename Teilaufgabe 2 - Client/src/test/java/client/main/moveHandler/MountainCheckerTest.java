package client.main.moveHandler;

import client.main.map.mapNode.EPlayerState;
import client.main.map.mapNode.ETerrainType;
import client.main.map.mapNode.MapNode;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class MountainCheckerTest {

    @Test
    void GivenPlayerIsOnMountain_PlayerLooksAround_ExpectDeleteNeighborNodes(){
        //Arrange
        MountainChecker mountainChecker = new MountainChecker();
        ArrayList<MapNode> mapNodeCollection = new ArrayList<>();
        mapNodeCollection.add(new MapNode(
                new Point(1,0),
                ETerrainType.MOUNTAIN,
                EPlayerState.MyPlayerPresent,
                false,
                false
        ));
        mapNodeCollection.add(new MapNode(
                new Point(2,0),
                ETerrainType.GRASS,
                EPlayerState.NoPlayerPresent,
                false,
                false
        ));
        ArrayList<Integer> movesList = new ArrayList<>();
        movesList.add(CoordinateTransformer.transformPointToIndex(mapNodeCollection.get(0).getCoordinates()));
        movesList.add(CoordinateTransformer.transformPointToIndex(mapNodeCollection.get(1).getCoordinates()));

        //Act
        mountainChecker.playerIsOnMountain(mapNodeCollection, false);
        movesList = (ArrayList<Integer>) mountainChecker.recalculatedRoute(movesList);


        //Assert
        assertEquals(1, movesList.size());
    }

    @Test
    void GivenPlayerIsOnMountain_PlayerLooksAround_ExpectTreasureVisible() {
        //Arrange
        MountainChecker mountainChecker = new MountainChecker();
        Collection<MapNode> mapNodeCollection = new ArrayList<>();
        mapNodeCollection.add(new MapNode(
                new Point(1,0),
                ETerrainType.MOUNTAIN,
                EPlayerState.MyPlayerPresent,
                false,
                false
        ));
        mapNodeCollection.add(new MapNode(
                new Point(2,0),
                ETerrainType.GRASS,
                EPlayerState.NoPlayerPresent,
                true,
                false
        ));

        //Act
        boolean result = mountainChecker.playerIsOnMountain(mapNodeCollection, false);

        //Assert
        assertTrue(result);
    }

    @Test
    void recalculatedRoute() {
    }
}