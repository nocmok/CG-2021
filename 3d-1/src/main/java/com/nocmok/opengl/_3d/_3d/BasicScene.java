package com.nocmok.opengl._3d._3d;

import java.util.List;

public class BasicScene implements Scene {

    private List<PolygonModel> objects;
    private Screen screen;
    private Camera camera;

    private void transformToScreenCoordinates(Screen scree, float[][] points, int[][] screenPoints) {
        for (int i = 0; i < points.length; ++i) {
            screenPoints[i][0] = clip((int) (points[i][0] * screen.getXSize()), 0, screen.getXSize());
            screenPoints[i][1] = clip((int) (points[i][1] * screen.getYSize()), 0, screen.getYSize());
        }
    }

    @Override public void addObject(PolygonModel object, float[] position) {

    }

    @Override public void applyTransformation(Transformation transformation) {

    }

    private int clip(int n, int min, int max) {
        return Integer.max(min, Integer.min(n, max));
    }

    private boolean shouldDrawObject(PolygonModel obj) {
        return true;
    }

    // Метод предполагает, что объект валиден и виден на камере
    private void drawObject(Screen screen, PolygonModel obj) {
        float[][] viewPoints = new float[obj.getPoints().length][2];
        camera.project(obj.getPoints(), viewPoints);

        int[][] screenPoints = new int[obj.getPoints().length][2];
        transformToScreenCoordinates(screen, viewPoints, screenPoints);

        int polygonSize = obj.getTopology().getPolygons().next().length;
        int[][] polygon = new int[polygonSize][2];

        var it = obj.getTopology().getPolygons();
        while (it.hasNext()) {
            int[] indexes = it.next();
            for (int i = 0; i < polygonSize; ++i) {
                polygon[i][0] = screenPoints[indexes[i]][0];
                polygon[i][1] = screenPoints[indexes[i]][1];
            }
            screen.drawPolygon(polygon);
        }
    }

    @Override public void draw(Screen screen) {
        for (PolygonModel obj : objects) {
            if (shouldDrawObject(obj)) {
                drawObject(screen, obj);
            }
        }
    }
}
