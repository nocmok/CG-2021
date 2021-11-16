package com.nocmok.opengl._3d._3d;

public interface Screen {
    // polygon[i][0] - x - координата i-й точки
    // polygon[i][1] - y - координата i-й точки
    void drawPolygon(int[][] polygon);
    // Преобразует нормализованные координаты точки в диапазоне [0,1] в координаты этого экрана
    int[] transform(float[] point, int[] screenPoint);
    int getXSize();
    int getYSize();
}
