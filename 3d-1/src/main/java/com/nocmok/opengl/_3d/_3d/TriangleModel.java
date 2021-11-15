package com.nocmok.opengl._3d._3d;

public class TriangleModel implements PolygonModel {

    private float[][] points;
    private int[][] topology;

    @Override public ModelTopology getTopology() {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override public float[][] getPoints() {
        throw new UnsupportedOperationException("not implemented");
    }
}
