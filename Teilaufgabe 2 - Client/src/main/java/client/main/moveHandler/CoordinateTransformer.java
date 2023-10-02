package client.main.moveHandler;

import java.awt.*;

public class CoordinateTransformer {
    public static int transformPointToIndex(Point inputPoint){
        if (inputPoint.x > 9)
            return (inputPoint.y * 10 + (inputPoint.x - 10));
        if (inputPoint.y > 4)
            return (inputPoint.y -5) * 10 + inputPoint.x;
        return inputPoint.y * 10 + inputPoint.x;
    }
}
