package com.nocmok.opengl._3d._3d.camera;

import com.nocmok.opengl._3d._3d.DumbMath3d;
import com.nocmok.opengl._3d._3d.Math3D;

public class CentralProjectionCamera implements Camera {

    private static final Math3D math3d = new DumbMath3d();

    private final double[][] operator;
    private final double xSize;
    private final double ySize;
    private final double minX;
    private final double minY;

    public CentralProjectionCamera(double pov, double xSize, double ySize) {
        this.operator = getMatrix(pov);
        this.xSize = xSize;
        this.ySize = ySize;
        this.minX = -xSize;
        this.minY = -ySize;
    }

    public static void getMatrix(double pov, double[][] matrix) {
        matrix[0][0] = 1;
        matrix[0][1] = 0;
        matrix[0][2] = 0;
        matrix[0][3] = 0;

        matrix[1][0] = 0;
        matrix[1][1] = 1;
        matrix[1][2] = 0;
        matrix[1][3] = 0;

        matrix[2][0] = 0;
        matrix[2][1] = 0;
        matrix[2][2] = 0;
        matrix[2][3] = -1 / pov;

        matrix[3][0] = 0;
        matrix[3][1] = 0;
        matrix[3][2] = 0;
        matrix[3][3] = 1;
    }

    public static double[][] getMatrix(double pov) {
        double[][] matrix = new double[4][4];
        getMatrix(pov, matrix);
        return matrix;
    }

    public void setPov(double pov) {
        getMatrix(pov, operator);
    }

    private double clip(double n, double min, double max) {
        return Double.min(max, Double.max(min, n));
    }

    private double[] normalize(double[] point) {
        point[0] = clip((point[0] - minX) / 2 / xSize, 0f, 1f);
        point[1] = clip((point[1] - minY) / 2 / ySize, 0f, 1f);
        return point;
    }

    @Override public double[][] project(double[][] points, double[][] projection) {
        for (int i = 0; i < points.length; ++i) {
            project(points[i], projection[i]);
        }
        return projection;
    }

    @Override public double[] project(double[] point, double[] projection) {
        var res = new double[4];
        math3d.mul(point, operator, res);
        math3d.mul(res, 1 / res[3], res);

        projection[0] = res[0];
        projection[1] = -res[1];

        return normalize(projection);
    }
}
