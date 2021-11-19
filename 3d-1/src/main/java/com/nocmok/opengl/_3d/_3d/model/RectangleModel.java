package com.nocmok.opengl._3d._3d.model;

public class RectangleModel implements PolygonModel {

    private final double[][] points;
    private final ModelTopology topology;
    private final int nPolygons;

    public RectangleModel(double[][] points, int[][] topology) {
        this.points = points;
        this.nPolygons = topology.length;
        this.topology = new BasicTopology(topology);
    }

    @Override public ModelTopology getTopology() {
        return topology;
    }

    @Override public double[][] getPoints() {
        return points;
    }

    @Override public int getNPolygons() {
        return nPolygons;
    }

    @Override public int getPolygonDegree() {
        return 4;
    }
}
