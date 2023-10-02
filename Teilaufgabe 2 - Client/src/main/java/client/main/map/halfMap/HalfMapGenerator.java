package client.main.map.halfMap;

import client.main.map.mapNode.ETerrainType;
import client.main.map.mapNode.MapNode;

import java.awt.*;
import java.util.*;

public class HalfMapGenerator {
    public HalfMapGenerator() {}
    private final int xUpperBound = 9;
    private Collection<MapNode> generateHalfMap() {

        ArrayList<MapNode> mapNodes = generateHalfMapFullOfGrassFields();
        ArrayList<Integer> randomPositions = getRandomXPositionsToPlaceWaterOrMountainsOn();

        for (int i = 0; i < mapNodes.size(); ++i) {

            //Every time we are on a new row generate another 3 random positions to place elements on.
            if (i % 10 == 0)
                randomPositions = getRandomXPositionsToPlaceWaterOrMountainsOn();

            if (nodeIsOnFirstOrLastRow(mapNodes.get(i)))
                changeTerrainTypeToWaterOrMountain(mapNodes.get(i), randomPositions, ETerrainType.WATER);

            else if (nodeIsOnSecondOrFourthRow(mapNodes.get(i)))
                changeTerrainTypeToWaterOrMountain(mapNodes.get(i), randomPositions, ETerrainType.MOUNTAIN);

            else if (nodeIsOnTheMiddleRow(mapNodes.get(i)))
                placeWaterAndMountainsOnRow(mapNodes.get(i), randomPositions);

        }

        placeCastleOnMap(mapNodes);
        return mapNodes;
    }

    private boolean nodeIsOnSecondOrFourthRow(MapNode mapNode){
        return mapNode.getCoordinates().y == 1 || mapNode.getCoordinates().y == 3;
    }

    private boolean nodeIsOnTheMiddleRow(MapNode mapNode){
        return  mapNode.getCoordinates().y == 2;
    }

    private boolean nodeIsOnFirstOrLastRow(MapNode mapNode){
        return mapNode.getCoordinates().y == 0 || mapNode.getCoordinates().y == 4;
    }

    private void placeWaterAndMountainsOnRow(MapNode mapNode, ArrayList<Integer> randomPositions){
        if (mapNode.getTerrainType() == ETerrainType.GRASS) {
            if (mapNode.getCoordinates().x == randomPositions.get(2) || mapNode.getCoordinates().x == randomPositions.get(0))
                mapNode.setTerrainType(ETerrainType.WATER);

            else if (randomPositions.contains(mapNode.getCoordinates().x))
                mapNode.setTerrainType(ETerrainType.MOUNTAIN);
        }
    }

    private void changeTerrainTypeToWaterOrMountain(MapNode mapNode, ArrayList<Integer> positionsToPlaceWaterOn, ETerrainType terrainType) {
        if (mapNode.getTerrainType() == ETerrainType.GRASS)
            if (positionsToPlaceWaterOn.contains(mapNode.getCoordinates().x))
                mapNode.setTerrainType(terrainType);
    }


    private void placeCastleOnMap(Collection<MapNode> mapNodes) {
        for (MapNode mapNode : mapNodes)
            if (mapNode.getCoordinates().x == 0 && mapNode.getCoordinates().y == 0)
                if (mapNode.getTerrainType() == ETerrainType.GRASS) {
                    mapNode.placeCastleOnMap();
                    return;
                }
    }


    private ArrayList<Integer> getRandomXPositionsToPlaceWaterOrMountainsOn() {
        Random random = new Random();
        ArrayList<Integer> toReturn = new ArrayList<>();

        for (int i = 0; i < 4; ++i) {
            int randomNumber = random.nextInt(xUpperBound);
            if (randomNumber == 0)
                ++randomNumber;

            while (toReturn.contains(randomNumber)) {
                randomNumber = random.nextInt(xUpperBound);
                if (randomNumber == 0)
                    ++randomNumber;
            }
            toReturn.add(randomNumber);
        }
        return toReturn;
    }

    private ArrayList<MapNode> generateHalfMapFullOfGrassFields() {
        ArrayList<MapNode> generatedHalfMap = new ArrayList<>();

        int xCoordinate = 0;
        int yCoordinate = 0;
        int givenMapSize = 50;
        int xUpperBound = 9;

        for (int i = 0; i < givenMapSize; ++i) {

            if (xCoordinate > xUpperBound) {
                xCoordinate = 0;
                ++yCoordinate;
            }
            MapNode newMapNode = new MapNode(new Point(xCoordinate, yCoordinate), ETerrainType.GRASS);
            generatedHalfMap.add(newMapNode);
            ++xCoordinate;
        }
        return generatedHalfMap;
    }

    public Collection<MapNode> getHalfMap() {
        Collection<MapNode> generatedMap = generateHalfMap();
        printHalfMap(generatedMap);
        return generatedMap;
    }


    private void printHalfMap(Collection<MapNode> generatedMap) {

        int x = 0;
        int y = 0;

        String BLUE_SQUARE = "\uD83D\uDFE6";
        String BROWN_SQUARE = "\uD83D\uDFEB";
        String GREEN_SQUARE = "\uD83D\uDFE9";

        ArrayList<MapNode> mapNodeArrayList = new ArrayList<>(generatedMap);

        StringBuilder toPrint = new StringBuilder("\nFollowing half map was created: \n");

        for (int i = 0; i < generatedMap.size(); ++i) {

            if (x > xUpperBound) {
                x = 0;
                ++y;
                toPrint.append("\n");
            }

            for (int k = 0; k < generatedMap.size(); ++k) {
                if (Objects.equals(mapNodeArrayList.get(k).getCoordinates(), new Point(x, y))) {
                    switch (mapNodeArrayList.get(k).getTerrainType()) {
                        case WATER -> toPrint.append(BLUE_SQUARE);
                        case GRASS -> toPrint.append(GREEN_SQUARE);
                        case MOUNTAIN -> toPrint.append(BROWN_SQUARE);
                    }
                }
            }
            ++x;
        }
        System.out.println(toPrint);

    }

}
