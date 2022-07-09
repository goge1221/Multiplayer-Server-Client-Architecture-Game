package client.main.PathFinder;

import java.awt.*;

public class CoordinateTransformer {

    public int transformPlayerCoordinate(int x, int y){
        if(y == 0){
            if(x <= 7) return x;
            return x - 8;
        } else if(y == 1){
            if(x <= 7)return x+8;
            return x;
        } else if (y == 2) {
            if(x <= 7) return x + 16;
            return x + 8;
        }else{
            if(x <= 7) return x +24;
            return x + 16;
        }
    }


    public Point getPosition(int integer){
        if(integer <= 7){
            return new Point(integer, 0);
        }
        else if(integer <= 15){
            return new Point(integer-8, 1);
        }
        else if(integer <= 23) return new Point(integer-16, 2);
        else return new Point(integer-24, 3);
    }
}
