package com.nocmok.opengl._3d._3d.transformation;

public class XYZScale extends BasicTransformation {
    public XYZScale(double x, double y, double z) {
        super(getMatrix(x, y, z));
    }

    public static void getMatrix(double x, double y, double z, double[][] matrix) {
        matrix[0][0] = x;
        matrix[0][1] = 0;
        matrix[0][2] = 0;
        matrix[0][3] = 0;

        matrix[1][0] = 0;
        matrix[1][1] = y;
        matrix[1][2] = 0;
        matrix[1][3] = 0;

        matrix[2][0] = 0;
        matrix[2][1] = 0;
        matrix[2][2] = z;
        matrix[2][3] = 0;

        matrix[3][0] = 0;
        matrix[3][1] = 0;
        matrix[3][2] = 0;
        matrix[3][3] = 1;
    }

    public static double[][] getMatrix(double x, double y, double z) {
        double[][] matrix = new double[4][4];
        getMatrix(x, y, z, matrix);
        return matrix;
    }

    public void setXYZ(double x, double y, double z) {
        getMatrix(x, y, z, operator);
    }
}
