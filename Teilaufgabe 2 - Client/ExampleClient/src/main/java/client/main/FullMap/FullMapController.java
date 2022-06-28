package client.main.FullMap;

import MessagesBase.MessagesFromClient.ETerrain;
import MessagesBase.MessagesFromServer.FullMapNode;

import java.awt.*;
import java.util.Collection;
import java.util.HashSet;

public class FullMapController {
    private FullMap fullMap;
    private FullMapView fullMapView;

    public FullMapController(Collection<FullMapNode> input){
        fullMap = new FullMap(input);
        fullMapView = new FullMapView();
    }

    public void setFullMap(Collection<FullMapNode>input){
        fullMap.setFullMap(input);
    }

    public Collection<FullMapNode> getFullMapCollection(){
        return fullMap.getFullMap();
    }

    public void printFullMap() {
        fullMapView.printFullMap(fullMap.getFullMap());
    }

    public boolean isMapHorizontal(){
        return fullMapView.getMapForm();
    }

    public boolean isFullMapNodeValid(Point point){
        HashSet<FullMapNode> mapNodeHashSet = new HashSet<>(getFullMapCollection());
        boolean mapNodePresent = false;
        for(FullMapNode fullMapNode : mapNodeHashSet)
            if(fullMapNode.getX() == point.x && fullMapNode.getY() == point.y) {
                mapNodePresent = true;
                if (fullMapNode.getTerrain() == ETerrain.Water) return false;
            }
        return mapNodePresent;
    }

}
