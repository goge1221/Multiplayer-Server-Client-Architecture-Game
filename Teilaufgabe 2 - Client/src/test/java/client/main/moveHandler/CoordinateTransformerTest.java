package client.main.moveHandler;

import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class CoordinateTransformerTest {

    @Test
    void testNormalConfiguration() {
        assertEquals(0, CoordinateTransformer.transformPointToIndex(new Point(0,0)));
        assertEquals(20, CoordinateTransformer.transformPointToIndex(new Point(0,2)));
        assertEquals(29, CoordinateTransformer.transformPointToIndex(new Point(9,2)));
        assertEquals(9, CoordinateTransformer.transformPointToIndex(new Point(9,0)));
        assertEquals(49, CoordinateTransformer.transformPointToIndex(new Point(9,4)));
        assertEquals(32, CoordinateTransformer.transformPointToIndex(new Point(2,3)));
    }

    @Test
    void testXGreaterConfiguration() {
        assertEquals(0, CoordinateTransformer.transformPointToIndex(new Point(10,0)));
        assertEquals(20, CoordinateTransformer.transformPointToIndex(new Point(10,2)));
        assertEquals(29, CoordinateTransformer.transformPointToIndex(new Point(19,2)));
        assertEquals(9, CoordinateTransformer.transformPointToIndex(new Point(19,0)));
        assertEquals(49, CoordinateTransformer.transformPointToIndex(new Point(19,4)));
        assertEquals(32, CoordinateTransformer.transformPointToIndex(new Point(12,3)));
    }


    @Test
    void testYGreaterConfiguration() {
        assertEquals(0, CoordinateTransformer.transformPointToIndex(new Point(0,5)));
        assertEquals(20, CoordinateTransformer.transformPointToIndex(new Point(0,7)));
        assertEquals(29, CoordinateTransformer.transformPointToIndex(new Point(9,7)));
        assertEquals(9, CoordinateTransformer.transformPointToIndex(new Point(9,5)));
        assertEquals(49, CoordinateTransformer.transformPointToIndex(new Point(9,9)));
        assertEquals(32, CoordinateTransformer.transformPointToIndex(new Point(2,8)));
    }
}