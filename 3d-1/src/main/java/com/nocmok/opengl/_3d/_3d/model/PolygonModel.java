package com.nocmok.opengl._3d._3d.model;

public interface PolygonModel {
    ModelTopology getTopology();
    double[][] getPoints();
    int getNPolygons();
    int getPolygonDegree();
}
