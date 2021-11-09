package com.nocmok.opengl.curve.controller.control;

import com.nocmok.opengl.curve.util.Point;
import com.nocmok.opengl.curve.util.Rectangle;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

public class PixelatedCanvas extends Canvas {

    private final int xPixels;
    private final int yPixels;

    public PixelatedCanvas(int xPixels, int yPixels) {
        this.xPixels = xPixels;
        this.yPixels = yPixels;
    }

    private Point getPixelCoordinates(int x, int y) {
        double xPixel = x * getPixelXSize();
        double yPixel = y * getPixelYSize();
        return new Point(xPixel, yPixel);
    }

    private double getPixelXSize() {
        return getWidth() / xPixels;
    }

    private double getPixelYSize() {
        return getHeight() / yPixels;
    }

    private Rectangle2D getPixelRectangle(int x, int y) {
        var start = getPixelCoordinates(x, y);
        return new Rectangle2D(start.x, start.y, getPixelXSize(), getPixelYSize());
    }

    private boolean checkBoundaries(int x, int y) {
        return x >= 0 && x < xPixels && y >= 0 && y < yPixels;
    }

    public void setPixel(int x, int y, Color color) {
        if (!checkBoundaries(x, y)) {
            return;
        }
        _drawPixel(x, y, color);
    }

    public void drawPixel(int x, int y, Color color) {
        if (!checkBoundaries(x, y)) {
            return;
        }
        _drawPixel(x, y, color);
    }

    private void _drawPixel(int x, int y, Color color) {
        var pixel = getPixelRectangle(x, y);
        var gc = super.getGraphicsContext2D();
        gc.setFill(color);
        gc.fillRect(pixel.getMinX(), pixel.getMinY(), pixel.getWidth(), pixel.getHeight());
    }

    private int clamp(int value, int minInclusive, int maxInclusive) {
        return Integer.max(minInclusive, Integer.min(value, maxInclusive));
    }

    private Rectangle clamp(Rectangle rect) {
        return Rectangle.ofPoints(
                clamp(rect.x, 0, xPixels - 1),
                clamp(rect.y, 0, yPixels - 1),
                clamp(rect.x2(), 0, xPixels - 1),
                clamp(rect.y2(), 0, yPixels - 1));
    }

    public void fillRect(Rectangle rect, javafx.scene.paint.Color color) {
        rect = clamp(rect);

        var gc = super.getGraphicsContext2D();
        var start = getPixelCoordinates(rect.x, rect.y);

        gc.setFill(color);
        gc.fillRect(start.x, start.y, rect.w * getPixelXSize(), rect.h * getPixelXSize());
    }

    public void fillRect(int x, int y, int width, int height, javafx.scene.paint.Color color) {
        this.fillRect(new Rectangle(x, y, width, height), color);
    }

    public void clear(Color color) {
        var gc = super.getGraphicsContext2D();

        gc.setFill(color);
        gc.fillRect(0, 0, getWidth(), getHeight());
    }

    public int toPixelX(double x) {
        return (int) (x / getPixelXSize());
    }

    public int toPixelY(double y) {
        return (int) (y / getPixelXSize());
    }

    public int getxPixels() {
        return xPixels;
    }

    public int getyPixels() {
        return yPixels;
    }
}
