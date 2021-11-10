package com.nocmok.opengl.curve.curve_drawer;

import com.nocmok.opengl.curve.util.Point;

import java.util.List;

public class BSpline3 implements CurveDrawer {

    private final double step;

    private final LinearCurve lineDrawer;

    public BSpline3(Grid grid, double step) {
        this.step = step;
        this.lineDrawer = new LinearCurve(grid);
    }

    private Point getPointByT(List<Point> pivots, double t) {
        double k0 = (1 - t) * (1 - t) * (1 - t);
        double k1 = 3 * t * t * t - 6 * t * t + 4;
        double k2 = -3 * t * t * t + 3 * t * t + 3 * t + 1;
        double k3 = t * t * t;

        double x = (k0 * pivots.get(0).x + k1 * pivots.get(1).x + k2 * pivots.get(2).x + k3 * pivots.get(3).x) / 6d;
        double y = (k0 * pivots.get(0).y + k1 * pivots.get(1).y + k2 * pivots.get(2).y + k3 * pivots.get(3).y) / 6d;

        return new Point(x, y);
    }

    @Override public void drawCurve(List<Point> pivots) {
        Point p0 = getPointByT(pivots, 0d);
        for (double t = step; t <= 1d; t += step) {
            var p1 = getPointByT(pivots, t);
            lineDrawer.drawLine(p0.x, p0.y, p1.x, p1.y);
            p0 = p1;
        }
    }
}
