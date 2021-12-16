package com.nocmok.opengl.curve.controller.control;

import com.nocmok.opengl.curve.curve_drawer.Grid;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class CanvasGrid implements Grid {

    private GraphicsContext gc;
    private Color color;
    private double width;

    public CanvasGrid(Canvas canvas, Color color, double width) {
        this.gc = canvas.getGraphicsContext2D();
        this.color = color;
        this.width = width;
    }

    @Override public void drawLine(double x1, double y1, double x2, double y2) {
        gc.setLineWidth(width);
        gc.setStroke(color);
        gc.strokeLine(x1, y1, x2, y2);
    }
}
