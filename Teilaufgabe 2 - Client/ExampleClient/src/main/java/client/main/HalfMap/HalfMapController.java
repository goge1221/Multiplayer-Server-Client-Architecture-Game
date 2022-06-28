package client.main.HalfMap;

import MessagesBase.MessagesFromClient.HalfMap;

public class HalfMapController {
    private InternalHalfMapCreator halfMapCreator;
    private HalfMapView halfMapView;

    private InternalHalfMap internalHalfMap;
    public HalfMapController(){
        halfMapCreator = new InternalHalfMapCreator();
        halfMapView = new HalfMapView();
    }

    public void createHalfMap(String uniquePlayerID){
        halfMapCreator.createHalfMapNodeCollection();
        internalHalfMap = new InternalHalfMap(uniquePlayerID ,halfMapCreator.getHalfMapNodeCollection());
        halfMapView.printMessage("Internal HalfMap was created successfully.");
    }

    public HalfMap getHalfMap(){
        return internalHalfMap.getHalfMap();
    }
    public void getViewUpdates(){
        halfMapView.printHalfMap(internalHalfMap.getHalfMap());
    }


}
