package com.nocmok.opengl._3d._3d;

public interface PolygonModel {
    ModelTopology getTopology();
    float[][] getPoints();
    int getNPolygons();
    int getPolygonDegree();
}
