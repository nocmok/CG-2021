package com.nocmok.opengl._3d._3d;

public interface Math3D {
    double[] mul(double[] vec, double s, double[] mul);

    double[] mul(double[] vec, double[][] matrix, double[] mul);

    double[][] mul(double[][] a, double[][] b, double[][] mul);

    double[] sub3(double[] a, double[] b);

    double[] sum3(double[] a, double[] b, double[] sum);

    double[] neg(double[] a);
}
