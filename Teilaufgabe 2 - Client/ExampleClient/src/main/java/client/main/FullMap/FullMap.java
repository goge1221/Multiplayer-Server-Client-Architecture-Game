package client.main.FullMap;

import MessagesBase.MessagesFromServer.FullMapNode;

import java.util.Collection;
import java.util.HashSet;

public class FullMap {

    private Collection<FullMapNode> fullMap;

    FullMap(Collection<FullMapNode>input){
        fullMap = new HashSet<>(input);
    }

    public Collection<FullMapNode>getFullMap(){
        return fullMap;
    }

    public void setFullMap(Collection<FullMapNode> fullMap) {
        this.fullMap = fullMap;
    }
}
