package client.main.SummerFindTreasure;

import MessagesBase.MessagesFromClient.HalfMap;
import MessagesBase.MessagesFromClient.HalfMapNode;
import client.main.FullMap.FullMapView;
import client.main.HalfMap.HalfMapView;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class SearchMapHandler {

    private final HalfMap halfMap;
    private final HalfMapView halfMapView;
    private ArrayList<HalfMapNode> halfMapNodes;

    private Point playerPosition;

    public SearchMapHandler(HalfMap halfMap){
        this.halfMap = halfMap;
        halfMapView = new HalfMapView();
        halfMapNodes = new ArrayList<>(halfMap.getMapNodes());
        initializePlayerPosition();
    }

    private void initializePlayerPosition(){
        for(HalfMapNode halfMapNode : halfMapNodes)
            if(halfMapNode.isFortPresent())
                playerPosition = new Point(halfMapNode.getX(), halfMapNode.getY());
    }


    public void printHalfMap(){
        System.out.println("\n----------TESTING BEGINNS HERE-----------");
        halfMapView.printHalfMap(halfMap);
    }

}
