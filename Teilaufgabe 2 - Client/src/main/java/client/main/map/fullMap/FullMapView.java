package client.main.map.fullMap;

import client.main.map.mapNode.EPlayerState;
import client.main.map.mapNode.MapNode;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

class FullMapView implements PropertyChangeListener {

    private final String BLUE_SQUARE = "\uD83D\uDFE6";
    private final String BROWN_SQUARE = "\uD83D\uDFEB";
    private final String CASTLE = "\uD83C\uDFF0";
    private final String GREEN_SQUARE = "\uD83D\uDFE9";
    private final String MONEY_BAG = "\uD83D\uDCB0";
    private final String TWO_PLAYERS = "\uD83E\uDD3C";
    private final String ENEMY_PLAYER = "\uD83E\uDDDF";
    private final String MY_PLAYER = "\uD83E\uDDB8";


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Object newMap = evt.getNewValue();

        if (newMap instanceof Collection<?> castedCollection)
            printMap((Collection<MapNode>) castedCollection);

    }

    private void printMap(Collection<MapNode> castedCollection){
        int xCoordinate = 0;
        int yCoordinate = 0;
        int xSmallerThan = 9;

        if (determineMapForm(castedCollection) == EMapForm.HORIZONTAL)
            xSmallerThan = 19;

        ArrayList<MapNode> mapNodeArrayList = new ArrayList<>(castedCollection);

        StringBuilder toPrint = new StringBuilder();

        for (int i = 0; i < mapNodeArrayList.size(); ++i){

            if (xCoordinate > xSmallerThan){
                xCoordinate = 0;
                ++yCoordinate;
                toPrint.append("\n");
            }

            for (MapNode mapNode : mapNodeArrayList) {
                if (Objects.equals(mapNode.getCoordinates(), new Point(xCoordinate, yCoordinate))) {

                    if (mapNode.getPlayerState() != EPlayerState.NoPlayerPresent)
                        toPrint.append(getPlayerPosition(mapNode));

                    else if (mapNode.isTreasurePresent())
                        toPrint.append(MONEY_BAG);

                    else if (mapNode.isCastlePresent())
                        toPrint.append(CASTLE);

                    else
                        toPrint.append(getTerrain(mapNode));

                }
            }
            ++xCoordinate;
        }
        System.out.println(toPrint);
    }

    protected String getPlayerPosition(MapNode mapNode){
        switch (mapNode.getPlayerState()){
            case MyPlayerPresent -> {return MY_PLAYER;}
            case EnemyPlayerPresent -> {return ENEMY_PLAYER;}
            case BothPlayerPresent -> {return TWO_PLAYERS;}
            default -> {return "-";}
        }
    }

    protected String getTerrain(MapNode mapNode){
        switch (mapNode.getTerrainType()){
            case WATER -> {return BLUE_SQUARE;}
            case MOUNTAIN -> {return BROWN_SQUARE;}
            default -> {return GREEN_SQUARE;}
        }
    }

    protected EMapForm determineMapForm(Collection<MapNode> newFullMap){
        for (MapNode mapNode : newFullMap)
            if (mapNode.getCoordinates().y > 7)
                return EMapForm.VERTICAL;
        return EMapForm.HORIZONTAL;
    }
}
