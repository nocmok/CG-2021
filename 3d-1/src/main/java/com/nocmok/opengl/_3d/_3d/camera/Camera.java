package com.nocmok.opengl._3d._3d.camera;

public interface Camera {
    double[][] project(double[][] points, double[][] projection);

    // Возвращает видовые координаты точки, нормализованные в диапазон [0,1]
    double[] project(double[] point, double[] projection);
}
