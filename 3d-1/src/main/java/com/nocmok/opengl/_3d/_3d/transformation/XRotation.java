package com.nocmok.opengl._3d._3d.transformation;

public class XRotation extends BasicTransformation {

    public XRotation(double radians) {
        super(getMatrix(radians));
    }

    public void setRadians(double radians) {
        getMatrix(radians, operator);
    }

    public static void getMatrix(double radians, double[][] matrix) {
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);

        matrix[0][0] = 1;
        matrix[0][1] = 0;
        matrix[0][2] = 0;
        matrix[0][3] = 0;

        matrix[1][0] = 0;
        matrix[1][1] = cos;
        matrix[1][2] = sin;
        matrix[1][3] = 0;

        matrix[2][0] = 0;
        matrix[2][1] = -sin;
        matrix[2][2] = cos;
        matrix[2][3] = 0;

        matrix[3][0] = 0;
        matrix[3][1] = 0;
        matrix[3][2] = 0;
        matrix[3][3] = 1;
    }

    public static double[][] getMatrix(double radians) {
        double[][] matrix = new double[4][4];
        getMatrix(radians, matrix);
        return matrix;
    }
}
