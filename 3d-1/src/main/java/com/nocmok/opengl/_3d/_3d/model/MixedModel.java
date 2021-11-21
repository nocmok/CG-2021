package com.nocmok.opengl._3d._3d.model;

public class MixedModel implements PolygonModel {

    private final double[][] points;
    private final ModelTopology topology;
    private final int nPolygons;
    private final int polygonDeg;

    public MixedModel(double[][] points, int[][] topology, int polygonDeg) {
        this.points = points;
        this.nPolygons = topology.length;
        this.topology = new BasicTopology(topology);
        this.polygonDeg = polygonDeg;
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
        return polygonDeg;
    }
}
