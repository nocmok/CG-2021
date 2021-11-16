package com.nocmok.opengl._3d.controller.contols;

import com.nocmok.opengl._3d._3d.Screen;
import javafx.scene.canvas.Canvas;

public class CanvasScreen implements Screen {

    private final Canvas canvas;

    public CanvasScreen(Canvas canvas) {
        this.canvas = canvas;
    }

    @Override public void drawPolygon(int[][] polygon) {
        var g2 = canvas.getGraphicsContext2D();
        for (int i = 1; i < polygon.length; ++i) {
            g2.strokeLine(polygon[i][0], polygon[i][1], polygon[i - 1][0], polygon[i - 1][1]);
        }
        g2.strokeLine(polygon[polygon.length - 1][0], polygon[polygon.length - 1][1], polygon[0][0], polygon[0][1]);
    }

    private int clip(int n, int min, int max) {
        return Integer.max(min, Integer.min(n, max));
    }

    @Override public int[] transform(float[] point, int[] screenPoint) {
        screenPoint[0] = clip((int) (point[0] * getXSize()), 0, getXSize());
        screenPoint[1] = clip((int) (point[1] * getYSize()), 0, getYSize());
        return screenPoint;
    }

    @Override public int getXSize() {
        return (int) canvas.getWidth();
    }

    @Override public int getYSize() {
        return (int) canvas.getHeight();
    }
}
