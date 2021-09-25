package com.nocmok.opengl.primitives.controller.control;

import com.nocmok.opengl.primitives.util.Point;
import com.nocmok.opengl.primitives.util.Rectangle;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PixelatedCanvas extends Canvas {

    private static Map<Integer, Color> rgbCache;
    private static Map<Color, Integer> colorCache;

    static {
        rgbCache = new HashMap<>();
        colorCache = new HashMap<>();
    }

    private final int xPixels;
    private final int yPixels;
    private int[][] colors;

    public PixelatedCanvas(int xPixels, int yPixels) {
        this.xPixels = xPixels;
        this.yPixels = yPixels;
        this.colors = new int[yPixels][xPixels];
    }

    private static int colorToRGB(Color color) {
        return colorCache.computeIfAbsent(color, colorKey -> {
            int r = ((int) (color.getRed() * 255d)) << 16;
            int g = ((int) (color.getGreen() * 255d)) << 8;
            int b = ((int) (color.getBlue() * 255d));
            return r | g | b;
        });
    }

    public static Color rgbToColor(int rgb) {
        return rgbCache.computeIfAbsent(rgb, rgbKey -> {
            double r = ((rgb & (0x00ff0000)) >>> 16) / 255d;
            double g = ((rgb & (0x0000ff00)) >>> 8) / 255d;
            double b = ((rgb & (0x000000ff))) / 255d;
            return new Color(r, g, b, 1.0);
        });
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
        colors[y][x] = colorToRGB(color);
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

    public int getRGB(int x, int y) {
        return colors[y][x];
    }

    public Color getColor(int x, int y) {
        return rgbToColor(colors[y][x]);
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

        int rgb = colorToRGB(color);
        for (int y = rect.y; y <= rect.y2(); ++y) {
            Arrays.fill(colors[y], rect.x, rect.x2() + 1, rgb);
        }
    }

    public void fillRect(int x, int y, int width, int height, javafx.scene.paint.Color color) {
        this.fillRect(new Rectangle(x, y, width, height), color);
    }

    public int toPixelX(double x) {
        return (int) (x / getPixelXSize());
    }

    public int toPixelY(double y) {
        return (int) (y / getPixelXSize());
    }
}
