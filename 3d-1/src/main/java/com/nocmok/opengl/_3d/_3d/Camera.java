package com.nocmok.opengl._3d._3d;

public interface Camera {
    float[][] project(float[][] points, float[][] projection);

    // Возвращает видовые координаты точки, нормализованные в диапазон [0,1]
    float[] project(float[] points, float[] projection);
}
