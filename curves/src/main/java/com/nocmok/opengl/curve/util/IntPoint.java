package com.nocmok.opengl.curve.util;

import java.util.Objects;

public class IntPoint {

    public int x;

    public int y;

    public IntPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override public String toString() {
        return "(" + x + ", " + y + ")";
    }

    @Override public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof IntPoint)) {
            return false;
        }
        IntPoint point = (IntPoint) obj;
        return point.x == x && point.y == y;
    }

    @Override public int hashCode() {
        return Objects.hash(x, y);
    }
}
