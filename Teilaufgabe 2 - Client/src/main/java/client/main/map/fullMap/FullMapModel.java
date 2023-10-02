package client.main.map.fullMap;

import client.main.map.mapNode.EPlayerState;
import client.main.map.mapNode.MapNode;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collection;

class FullMapModel {

    private final PropertyChangeSupport listener = new PropertyChangeSupport(this);
    private Collection<MapNode> fullMap;
    private Point playerPosition;

    public void registerView(PropertyChangeListener newView){
        listener.addPropertyChangeListener(newView);
    }

    public void updateFullMap(Collection<MapNode> newFullMap){
        this.listener.firePropertyChange("map",  fullMap, newFullMap);
        fullMap = newFullMap;
        updatePlayerPosition();
    }

    private void updatePlayerPosition(){
        for (MapNode mapNode : fullMap)
            if (mapNode.getPlayerState() == EPlayerState.MyPlayerPresent || mapNode.getPlayerState() == EPlayerState.BothPlayerPresent) {
                playerPosition = mapNode.getCoordinates();
                return;
            }
    }

    public Collection<MapNode> getFullMap(){
        return fullMap;
    }

    public Point getPlayerPosition(){
        return playerPosition;
    }

}
