package client.main.map.mapNode;

import java.awt.*;

public class MapNode {

    private final Point coordinates;
    private final EPlayerState playerState;
    private ETerrainType terrainType;
    private final boolean treasurePresent;
    private boolean castlePresent;

    public MapNode(Point coordinates, ETerrainType terrainType){
        this.coordinates = coordinates;
        this.terrainType = terrainType;
        playerState = EPlayerState.NoPlayerPresent;
        treasurePresent = false;
        castlePresent = false;
    }

    public MapNode(Point coordinates, ETerrainType terrainType, EPlayerState playerState, boolean treasurePresent, boolean castlePresent){
        this.coordinates = coordinates;
        this.terrainType = terrainType;
        this.playerState = playerState;
        this.treasurePresent = treasurePresent;
        this.castlePresent = castlePresent;
    }

    public Point getCoordinates() {
        return coordinates;
    }

    public EPlayerState getPlayerState() {
        return playerState;
    }

    public void setTerrainType(ETerrainType terrainType){
        this.terrainType = terrainType;
    }
    public ETerrainType getTerrainType() {
        return terrainType;
    }

    public boolean isTreasurePresent() {
        return treasurePresent;
    }

    public boolean isCastlePresent() {
        return castlePresent;
    }

    public void placeCastleOnMap(){
        castlePresent = true;
    }


}
