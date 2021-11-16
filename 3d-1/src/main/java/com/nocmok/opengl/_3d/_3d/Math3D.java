package com.nocmok.opengl._3d._3d;

public interface Math3D {
    float[] mul(float[] vec, float[][] matrix, float[] mul);

    float[][] mul(float[][] a, float[][] b, float[][] mul);

    float[] sub3(float[] a, float[] b);

    float[] sum3(float[] a, float[] b, float[] sum);

    float[] neg(float[] a);
}
