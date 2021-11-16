package com.nocmok.opengl._3d._3d;

public class TriangleModel implements PolygonModel {

    private final float[][] points;
    private final ModelTopology topology;
    private final int nPolygons;

    public TriangleModel(float[][] points, int[][] topology) {
        this.points = points;
        this.topology = new BasicTopology(topology);
        this.nPolygons = topology.length;
    }

    @Override public ModelTopology getTopology() {
        return topology;
    }

    @Override public float[][] getPoints() {
        return points;
    }

    @Override public int getNPolygons() {
        return nPolygons;
    }

    @Override public int getPolygonDegree() {
        return 3;
    }
}
