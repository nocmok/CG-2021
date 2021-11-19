package com.nocmok.opengl._3d._3d.camera;

// Параллельная проекция
// Плоскость перпендикулярна оси z
// Лучи проецирования перпендикулярны плоскости (параллельны оси z)
public class DumbCamera implements Camera {

    private final double xSize;
    private final double ySize;
    private final double minX;
    private final double maxX;
    private final double minY;
    private final double maxY;

    // xSize, ySize - Размеры четверти камеры
    public DumbCamera(double xSize, double ySize) {
        this.xSize = xSize;
        this.ySize = ySize;
        this.minX = -xSize;
        this.maxX = xSize;
        this.minY = -ySize;
        this.maxY = ySize;
    }

    private double clip(double n, double min, double max) {
        return Double.min(max, Double.max(min, n));
    }

    private double[] normalize(double[] point) {
        point[0] = clip((point[0] - minX) / 2 / xSize, 0f, 1f);
        point[1] = clip((point[1] - minY) / 2 / ySize, 0f, 1f);
        return point;
    }

    @Override public double[] project(double[] point, double[] projection) {
        projection[0] = point[0];
        projection[1] = -point[1];
        return normalize(projection);
    }

    @Override public double[][] project(double[][] points, double[][] projection) {
        for (int i = 0; i < points.length; ++i) {
            project(points[i], projection[i]);
        }
        return projection;
    }
}
