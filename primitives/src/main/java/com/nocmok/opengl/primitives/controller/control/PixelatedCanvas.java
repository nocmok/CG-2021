package com.nocmok.opengl.primitives.controller.control;

import com.nocmok.opengl.primitives.util.Point;
import com.nocmok.opengl.primitives.util.Rectangle;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class PixelatedCanvas extends Canvas {

    private static Map<Integer, Color> rgbCache;

    static {
        rgbCache = new HashMap<>();
    }

    private final int xPixels;
    private final int yPixels;
    private final BufferedImage grid;

    public PixelatedCanvas(int xPixels, int yPixels) {
        this.xPixels = xPixels;
        this.yPixels = yPixels;

        this.grid = new BufferedImage(xPixels, yPixels, BufferedImage.TYPE_INT_RGB);
        //
        var background = java.awt.Color.WHITE;
        var g2 = grid.createGraphics();
        g2.setColor(background);
        g2.fillRect(0, 0, xPixels, yPixels);
    }

    public Graphics2DWrapper createGraphicsWrapper() {
        return new Graphics2DWrapper(grid);
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

    public void setPixel(int x, int y, javafx.scene.paint.Color color) {
        if (!checkBoundaries(x, y)) {
            return;
        }
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

    public int toPixelX(double x) {
        return (int) (x / getPixelXSize());
    }

    public int toPixelY(double y) {
        return (int) (y / getPixelXSize());
    }

    public int getXSizeInPixels() {
        return xPixels;
    }

    public int getYSizeInPixels() {
        return yPixels;
    }

    public class Graphics2DWrapper {

        private final Graphics2D g2;

        private final BufferedImage image;

        private final PixelatedCanvas canvas = PixelatedCanvas.this;

        Graphics2DWrapper(BufferedImage image) {
            this.image = image;
            this.g2 = image.createGraphics();
            g2.setColor(java.awt.Color.BLACK);
        }

        public void drawPixel(int x, int y, java.awt.Color color) {
            if (!canvas.checkBoundaries(x, y)) {
                return;
            }
            image.setRGB(x, y, color.getRGB());
        }

        public void drawLine(int x1, int y1, int x2, int y2, java.awt.Color color) {
            g2.setColor(color);
            g2.drawLine(x1, y1, x2, y2);
        }

        public void fillRect(Rectangle rect, java.awt.Color color) {
            g2.setColor(color);
            g2.fillRect(rect.x, rect.y, rect.w, rect.h);
        }

        public void drawEllipse(int x, int y, int width, int height, java.awt.Color color) {
            g2.setColor(color);
            g2.drawOval(x, y, width, height);
        }

        public void drawCircle(int x, int y, int size, java.awt.Color color) {
            g2.setColor(color);
            g2.drawOval(x, y, size, size);
        }

        public void flush() {
            flushRect(Rectangle.ofSize(0, 0, canvas.xPixels, canvas.yPixels));
        }

        public void flushRect(Rectangle rect) {
            rect = canvas.clamp(rect);
            canvas.setDisable(true);
            for (int x = rect.x; x <= rect.x2(); ++x) {
                for (int y = rect.y; y <= rect.y2(); ++y) {

                    int rgb = image.getRGB(x, y);
                    var color = rgbCache.get(rgb);
                    if (color == null) {
                        double r = ((rgb & (0x00ff0000)) >>> 16) / 255d;
                        double g = ((rgb & (0x0000ff00)) >>> 8) / 255d;
                        double b = ((rgb & (0x000000ff))) / 255d;
                        color = new Color(r, g, b, 1d);
                        rgbCache.put(rgb, color);
                    }
                    canvas.setPixel(x, y, color);
                }
            }
            canvas.setDisable(false);
        }
    }
}
