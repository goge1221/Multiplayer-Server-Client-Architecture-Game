package client.main.map.fullMap;

import client.main.map.mapNode.MapNode;

import java.awt.*;
import java.util.Collection;

public class FullMapController {

    private final FullMapModel fullMapModel = new FullMapModel();

    public FullMapController(){
        FullMapView fullMapView = new FullMapView();
        fullMapModel.registerView(fullMapView);
    }

    public void updateFullMap(Collection<MapNode> mapNodeCollection){
        fullMapModel.updateFullMap(mapNodeCollection);
    }


    public Collection<MapNode> getFullMap(){
        return fullMapModel.getFullMap();
    }

    public Point getPlayerPosition(){
        return fullMapModel.getPlayerPosition();
    }

}
