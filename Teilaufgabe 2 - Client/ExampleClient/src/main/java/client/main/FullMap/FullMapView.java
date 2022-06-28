package client.main.FullMap;

import MessagesBase.MessagesFromClient.ETerrain;
import MessagesBase.MessagesFromServer.EFortState;
import MessagesBase.MessagesFromServer.EPlayerPositionState;
import MessagesBase.MessagesFromServer.ETreasureState;
import MessagesBase.MessagesFromServer.FullMapNode;

import java.util.ArrayList;
import java.util.Collection;

public class FullMapView {
    private String BLUE_SQUARE = "\uD83D\uDFE6";
    private String BROWN_SQUARE = "\uD83D\uDFEB";
    private String CASTLE = "\uD83C\uDFF0";
    private String ENEMY_FORT = "\uD83C\uDFEF";
    private String GREEN_SQUARE = "\uD83D\uDFE9";
    private String MONEY_BAG = "\uD83D\uDCB0";

    private String TWO_PLAYERS = "\uD83E\uDD3C";
    private String ENEMY_PLAYER = "\uD83E\uDDDF";
    private String MY_PLAYER = "\uD83E\uDDB8";
    private boolean horizontalMap = false;
    private boolean firstIteration = true;

    public void printFullMap(Collection<FullMapNode> fullMap){
        int x = 0;
        int y = 0;


        ArrayList<FullMapNode> arrayList = new ArrayList<>(fullMap);
        String toPrint = "The following FullMap was received: \n";

        int xInLoop = 7;
        if(firstIteration){
            xInLoop = determineMapForm(arrayList);
            firstIteration = false;
        }
        if(horizontalMap) xInLoop = 15;


        for(int i = 0; i < fullMap.size(); ++i){

            if(x > xInLoop){
                toPrint += "\n";
                x = 0;
                ++y;
            }

            for(int k = 0; k < fullMap.size(); ++k){
                if(arrayList.get(k).getX() == x && arrayList.get(k).getY() == y){
                    FullMapNode currentMapNode = arrayList.get(k);

                    if(currentMapNode.getPlayerPositionState() != EPlayerPositionState.NoPlayerPresent){
                        //Someone is on the field
                        toPrint += getPlayerPosition(currentMapNode);
                    }
                    else if(currentMapNode.getFortState() != EFortState.NoOrUnknownFortState){
                        //Some Fort is present here
                        toPrint += getFortState(currentMapNode);
                    }
                    else if(currentMapNode.getTreasureState() != ETreasureState.NoOrUnknownTreasureState){
                        //Some Treasure is present on the field
                        toPrint += MONEY_BAG;

                    }
                    else{
                        //Nothing is present on the field except of base structures
                        toPrint += getTerrain(currentMapNode);
                    }
                }
            }
            ++x;
        }
        System.out.println(toPrint);
    }

    public boolean getMapForm(){
        return horizontalMap;
    }

    private String getFortState(FullMapNode fullMapNode){
        if(fullMapNode.getFortState() == EFortState.EnemyFortPresent) return ENEMY_FORT;
        return CASTLE;
    }

    private String getPlayerPosition(FullMapNode fullMapNode){
        switch (fullMapNode.getPlayerPositionState()){
            case MyPlayerPosition -> {
                return MY_PLAYER;
            }
            case EnemyPlayerPosition -> {
                return ENEMY_PLAYER;
            }
            case BothPlayerPosition -> {
                return TWO_PLAYERS;
            }
        }
        return "";
    }

    private String getTerrain(FullMapNode fullMapNode){
        switch (fullMapNode.getTerrain()){
            case Water -> {
                return BLUE_SQUARE;
            }
            case Grass -> {
                return GREEN_SQUARE;
            }
            case Mountain -> {
                return BROWN_SQUARE;
            }
        }
        return "";
    }


    private int determineMapForm(ArrayList<FullMapNode>input){
        for(int i = 0; i < input.size(); ++i){
            if(input.get(i).getX() > 7){
                horizontalMap = true;
                return 15;
            }
        }
        return 7;
    }

}
