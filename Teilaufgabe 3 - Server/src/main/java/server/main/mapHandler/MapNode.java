package server.main.mapHandler;

public class MapNode {


    private EFortStateServer fortStateServer;
    private EPlayerPositionServer playerState;
    private final ETerrainServer terrainType;
    private final boolean treasurePresent;
    private int xCoordinate;
    private int yCoordinate;

    public MapNode(EFortStateServer fortState, EPlayerPositionServer playerState,
                   ETerrainServer terrainType, boolean treasurePresent, int xCoordinate, int yCoordinate) {
        fortStateServer = fortState;
        this.playerState = playerState;
        this.terrainType = terrainType;
        this.treasurePresent = treasurePresent;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
    }
    public EFortStateServer getFortStateServer() {
        return fortStateServer;
    }

    public void setFortStateServer(EFortStateServer fortStateServer){
        this.fortStateServer = fortStateServer;
    }

    public EPlayerPositionServer getPlayerState() {
        return playerState;
    }

    public void setPlayerState(EPlayerPositionServer playerState){
        this.playerState = playerState;
    }

    public ETerrainServer getTerrainType() {
        return terrainType;
    }

    public boolean isTreasurePresent() {
        return treasurePresent;
    }

    public void setxCoordinate(int newCoordinate){
        this.xCoordinate = newCoordinate;
    }

    public void setyCoordinate(int newCoordinate){
        this.yCoordinate = newCoordinate;
    }

    public int getxCoordinate() {
        return xCoordinate;
    }

    public int getyCoordinate() {
        return yCoordinate;
    }

    @Override
    public String toString() {
        return "MapNode{" +
                "fortStateServer=" + fortStateServer +
                ", playerState=" + playerState +
                ", terrainType=" + terrainType +
                '}';
    }
}
