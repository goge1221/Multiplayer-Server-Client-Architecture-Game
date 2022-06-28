package client.main.HalfMap;

import MessagesBase.MessagesFromClient.HalfMap;
import MessagesBase.MessagesFromClient.HalfMapNode;

import java.util.Collection;

public class InternalHalfMap {

    private HalfMap halfMap;

    InternalHalfMap(String uniquePlayerIdentifier, Collection<HalfMapNode>halfMapNodes){
        halfMap = new HalfMap(uniquePlayerIdentifier, halfMapNodes);
    }

    public HalfMap getHalfMap() {
        return halfMap;
    }


}
