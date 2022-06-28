package client.main.HalfMap;

import MessagesBase.MessagesFromClient.HalfMap;
import MessagesBase.MessagesFromClient.HalfMapNode;

import java.util.ArrayList;

public class HalfMapView {

    private String BLUE_SQUARE = "\uD83D\uDFE6";
    private String BROWN_SQUARE = "\uD83D\uDFEB";
    private String GREEN_SQUARE = "\uD83D\uDFE9";

    public void printMessage(String message){
        System.out.println(message);
    }
    public void printHalfMap(HalfMap halfMap){
        int x = 0;
        int y = 0;
        ArrayList<HalfMapNode> arrayList = new ArrayList<>(halfMap.getMapNodes());
        String toPrint = "The following HalfMap was created: \n";
        for(int i = 0; i < halfMap.getMapNodes().size(); ++i){

            if(x > 7){
                toPrint += "\n";
                x = 0;
                ++y;
            }

            for(int k = 0; k < halfMap.getMapNodes().size(); ++k){
                if(arrayList.get(k).getX() == x && arrayList.get(k).getY() == y){
                    //     toPrint += "[x:" + arrayList.get(k).getX() + ", y: " + arrayList.get(k).getY();
                    //   toPrint += ", F:" + arrayList.get(k).isFortPresent() + ", T:" + arrayList.get(k).getTerrain().toString() + "]" + COLOR;
                    //  toPrint += "[" + arrayList.get(k).getTerrain().toString() +  "], ";
                    switch (arrayList.get(k).getTerrain()){
                        case Water -> {
                            toPrint += BLUE_SQUARE;
                            break;
                        }
                        case Grass -> {
                            toPrint += GREEN_SQUARE;
                            break;
                        }
                        case Mountain -> {
                            toPrint += BROWN_SQUARE;
                            break;
                        }
                    }
                }
            }
            ++x;
        }
        System.out.println(toPrint);
    }

}
