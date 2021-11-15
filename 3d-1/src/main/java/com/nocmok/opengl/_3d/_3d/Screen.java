package com.nocmok.opengl._3d._3d;

public interface Screen {
    // polygon[i][0] - x - координата i-й точки
    // polygon[i][1] - y - координата i-й точки
    void drawPolygon(int[][] polygon);
    int getXSize();
    int getYSize();
}
