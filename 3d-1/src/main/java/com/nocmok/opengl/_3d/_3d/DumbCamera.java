package com.nocmok.opengl._3d._3d;

public class DumbCamera implements Camera {

    private final float xSize;
    private final float ySize;
    private final float minX;
    private final float maxX;
    private final float minY;
    private final float maxY;

    // xSize, ySize - Размеры четверти камеры
    public DumbCamera(float xSize, float ySize) {
        this.xSize = xSize;
        this.ySize = ySize;
        this.minX = -xSize;
        this.maxX = xSize;
        this.minY = -ySize;
        this.maxY = ySize;
    }

    private float clip(float n, float min, float max) {
        return Float.min(max, Float.max(min, n));
    }

    private float[] normalize(float[] point) {
        point[0] = clip((point[0] - minX) / 2 / xSize, 0f, 1f);
        point[1] = clip((point[1] - minY) / 2 / ySize, 0f, 1f);
        return point;
    }

    @Override public float[] project(float[] point, float[] projection) {
        projection[0] = point[0];
        projection[1] = point[1];
        return normalize(projection);
    }

    @Override public float[][] project(float[][] points, float[][] projection) {
        for (int i = 0; i < points.length; ++i) {
            project(points[i], projection[i]);
        }
        return projection;
    }
}
