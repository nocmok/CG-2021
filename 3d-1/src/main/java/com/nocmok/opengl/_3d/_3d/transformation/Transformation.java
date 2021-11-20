package com.nocmok.opengl._3d._3d.transformation;

public interface Transformation {

    // меняет переданный вектор
    double[] apply(double[] vector, double[] transformed);
}
