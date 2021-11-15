package com.nocmok.opengl._3d._3d;

public interface Camera {
    // видовые координаты точки, нормализованные в диапазон [0,1]
    // в случае тупой изометрической камеры, просто отсекаем координату z
    float[] project(float[] point);
    float[][] project(float[][] points, float[][] projection);
}
