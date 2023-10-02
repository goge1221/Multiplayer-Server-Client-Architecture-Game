package client.main.map.halfMap;

import client.main.map.mapNode.ETerrainType;
import client.main.map.mapNode.MapNode;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HalfMapGeneratorTest {

    @Test
    void GivenGeneratedMap_CreateMap_TotalNumberOfFieldsCorrect(){
        //Arrange
        HalfMapGenerator halfMapGenerator = new HalfMapGenerator();
        int fieldsInTotal = 50;
        //Act
        var generatedHalfMap = halfMapGenerator.getHalfMap();
        //Assert
        assertEquals(generatedHalfMap.size(), fieldsInTotal);
    }

    @Test
    void GivenGeneratedMap_PlaceCastle_CastlePlacedOnGrassField(){
        //Arrange
        HalfMapGenerator halfMapGenerator = new HalfMapGenerator();
        //Act
        var generatedHalfMap = halfMapGenerator.getHalfMap();
        //Assert
        assertTrue(castleLocatedOnGrass(generatedHalfMap));
    }

    @Test
    void GivenGeneratedMap_TerrainTypesModified_MinimumWaterFieldsPresent(){
        //Arrange
        HalfMapGenerator halfMapGenerator = new HalfMapGenerator();
        int minimumWaterFieldsRequired = 7;

        //Act
        var generatedHalfMap = halfMapGenerator.getHalfMap();
        //Assert
        assertTrue(countFields(generatedHalfMap, ETerrainType.WATER) >= minimumWaterFieldsRequired);
    }

    @Test
    void GivenGeneratedMap_TerrainTypesModified_MinimumGrassFieldsPresent(){
        //Arrange
        HalfMapGenerator halfMapGenerator = new HalfMapGenerator();
        int minimumGrassFieldsRequired = 24;

        //Act
        var generatedHalfMap = halfMapGenerator.getHalfMap();
        //Assert
        assertTrue(countFields(generatedHalfMap, ETerrainType.GRASS) >= minimumGrassFieldsRequired);
    }


    @Test
    void GivenGeneratedMap_TerrainTypesModified_MinimumMountainFieldsPresent(){
        //Arrange
        HalfMapGenerator halfMapGenerator = new HalfMapGenerator();
        int minimumMountainFieldsRequired = 5;

        //Act
        var generatedHalfMap = halfMapGenerator.getHalfMap();
        //Assert
        assertTrue(countFields(generatedHalfMap, ETerrainType.MOUNTAIN) >= minimumMountainFieldsRequired);
    }


    private int countFields(Collection<MapNode> generatedHalfMap, ETerrainType terrainType) {
        int fieldCounter = 0;
        for (MapNode mapNode : generatedHalfMap)
            if (mapNode.getTerrainType().equals(terrainType))
                ++fieldCounter;
        return fieldCounter;
    }


    private boolean castleLocatedOnGrass(Collection<MapNode> generatedHalfMap){
        for (MapNode mapNode : generatedHalfMap)
            if (mapNode.isCastlePresent())
                if (mapNode.getTerrainType() == ETerrainType.GRASS)
                    return true;
        return false;
    }
}