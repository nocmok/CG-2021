package com.nocmok.opengl.curve.util;

public class Rectangle {

    /**
     * top left
     */
    public int x;

    /**
     * top left
     */
    public int y;

    public int w;

    public int h;

    public Rectangle(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public static Rectangle ofPoints(int x1, int y1, int x2, int y2) {
        return new Rectangle(Integer.min(x1, x2),
                Integer.min(y1, y2),
                Math.abs(x1 - x2) + 1,
                Math.abs(y1 - y2) + 1);
    }

    public static Rectangle ofSize(int x, int y, int w, int h) {
        return new Rectangle(x, y, w, h);
    }

    public static Rectangle squareOfSize(int x, int y, int s) {
        return new Rectangle(x, y, s, s);
    }

    /**
     * inclusive
     */
    public int x2() {
        return x + w - 1;
    }

    /**
     * inclusive
     */
    public int y2() {
        return y + h - 1;
    }

    public Rectangle add(Rectangle other) {
        return Rectangle.ofPoints(Integer.min(x, other.x),
                Integer.min(y, other.y),
                Integer.max(x2(), other.x2()),
                Integer.max(y2(), other.y2()));
    }
}
