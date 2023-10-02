package client.main.map.fullMap;

import client.main.map.halfMap.HalfMapGenerator;
import client.main.map.mapNode.EPlayerState;
import client.main.map.mapNode.ETerrainType;
import client.main.map.mapNode.MapNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class FullMapViewTest {

    @Test
    void GivenMapNodes_DetermineMapForm_ExpectHorizontalMapForm(){

        //Arrange
        FullMapView fullMapView = new FullMapView();
        ArrayList<MapNode> mapNodes = (ArrayList<MapNode>) new HalfMapGenerator().getHalfMap();
        mapNodes.add(new MapNode(new Point(10, 2), ETerrainType.WATER));

        //Act
        EMapForm result = fullMapView.determineMapForm(mapNodes);

        //Assert
        assertEquals(result, EMapForm.HORIZONTAL);
    }


    @Test
    void GivenMapNodeOnlyMyPlayerIsOn_RecognizeIfPlayerIsPresent_ExpectRecognizedCorrectly() {
        //Arrange
        MapNode mapNode = new MapNode(
                new Point(0, 0),
                ETerrainType.GRASS,
                EPlayerState.MyPlayerPresent,
                false,
                false
        );
        FullMapView fullMapView = new FullMapView();
        String MY_PLAYER = "\uD83E\uDDB8";

        //Act
        String result = fullMapView.getPlayerPosition(mapNode);

        //Assess
        assertEquals(MY_PLAYER, result);
    }

    @ParameterizedTest
    @MethodSource("provideMapNodesForTest")
    void GivenMapNode_RecognizeTerrainType_ExpectGrassRecognizedCorrectly(Map.Entry<MapNode, String> mapEntry){
        //Arrange
        FullMapView fullMapView = new FullMapView();
        String expectedType = mapEntry.getValue();
        MapNode mapNode = mapEntry.getKey();

        //Act
        String result = fullMapView.getTerrain(mapNode);

        //Assess
        assertEquals(expectedType, result);
    }


    private static Stream<Map.Entry<MapNode, String>> provideMapNodesForTest(){
        MapNode grassMapNode = new MapNode(new Point(0,0), ETerrainType.GRASS);
        MapNode waterMapNode = new MapNode(new Point(0,0), ETerrainType.WATER);
        MapNode mountainMapNode = new MapNode(new Point(0,0), ETerrainType.MOUNTAIN);

        String BLUE_SQUARE = "\uD83D\uDFE6";
        String BROWN_SQUARE = "\uD83D\uDFEB";
        String GREEN_SQUARE = "\uD83D\uDFE9";

        return Stream.of(
                Map.entry(grassMapNode, GREEN_SQUARE),
                Map.entry(waterMapNode, BLUE_SQUARE),
                Map.entry(mountainMapNode, BROWN_SQUARE)
        );
    }



}