package client.main.HalfMap;

import MessagesBase.MessagesFromClient.ETerrain;
import MessagesBase.MessagesFromClient.HalfMapNode;

import java.util.*;

public class InternalHalfMapCreator {
    //x ist kleiner als 7
    private int xWert;
    //y ist kleiner als 4
    private int yWert;
    private int anzahlWiesenFelder;
    private int anzhlBergFelder;
    private int anzahlWasserFelder;

    private int halfMapGroesse;

    private boolean isCastleOnMap;
    //jede karte muss mindestend 3 berg 15 wiesen und 4 wasser beinhalten
    private Collection<HalfMapNode>halfMapNodeCollection;

    InternalHalfMapCreator(){
        xWert = 0;
        yWert = 0;
        anzahlWasserFelder = 0;
        anzahlWiesenFelder = 0;
        anzhlBergFelder = 0;
        //Die HalfMap sollte 4x8 sein von daher besitzt sie 32 felder
        halfMapGroesse = 32;
        isCastleOnMap = false;
        halfMapNodeCollection = new HashSet<>();
    }

    public Collection<HalfMapNode> getHalfMapNodeCollection() {
        return halfMapNodeCollection;
    }

    //The method populates the collection in regard to the restrictions.
    public void createHalfMapNodeCollection(){
        for(int i = 0; i < halfMapGroesse; ++i){
            ETerrain terrainType = determineTerrainType();
            if(xWert > 7){
                xWert = 0;
                ++yWert;
            }
            HalfMapNode toAdd = new HalfMapNode(xWert,yWert,isFortPresent(terrainType), terrainType);
            ++xWert;
            halfMapNodeCollection.add(toAdd);
        }
    }

    private ETerrain determineTerrainType(){
        Random random = new Random();
        int randomNumber = random.nextInt(3);
        if (this.anzahlWasserFelder >= 4 && randomNumber == 0) {
            ++randomNumber;
        }

        if (this.anzhlBergFelder > 4 && randomNumber == 2) {
            --randomNumber;
        }

        if (this.anzahlWiesenFelder > 4) {
            switch (randomNumber) {
                case 0:
                    ++this.anzahlWasserFelder;
                    return ETerrain.Water;
                case 1:
                    ++this.anzahlWiesenFelder;
                    return ETerrain.Grass;
                case 2:
                    ++this.anzhlBergFelder;
                    return ETerrain.Mountain;
            }
        }

        ++this.anzahlWiesenFelder;
        return ETerrain.Grass;
    }

    //This method ensures the fort is placed somewhere in the middle and also on a grass field.
    private boolean isFortPresent(ETerrain terrain){
        if(terrain.equals(ETerrain.Grass) && !isCastleOnMap){
            if((xWert == 2  || xWert == 1 || xWert == 3) && (yWert == 1 || yWert == 2)){
                isCastleOnMap = true;
                return true;
            }
        }
        return false;
    }

}
