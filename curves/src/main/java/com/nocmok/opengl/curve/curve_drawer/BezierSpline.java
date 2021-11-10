package com.nocmok.opengl.curve.curve_drawer;

import com.nocmok.opengl.curve.util.Point;

import java.util.List;

public class BezierSpline implements CurveDrawer {

    private static final int MAX_PIVOTS = 100;
    private static final double[] fact = new double[MAX_PIVOTS + 1];

    static {
        fact[0] = 1;
        for (int i = 1; i <= MAX_PIVOTS; ++i) {
            fact[i] = fact[i - 1] * i;
        }
    }

    private final double step;

    private final LinearCurve lineDrawer;

    public BezierSpline(Grid grid, double step) {
        this.step = step;
        this.lineDrawer = new LinearCurve(grid);
    }

    private static double fact(int n) {
        return fact[n];
    }

    private Point getPointByT(List<Point> pivots, double t) {
        var point = new Point(0d, 0d);
        int n = pivots.size() - 1;
        for (int i = 0; i < pivots.size(); ++i) {
            var factor = fact(n) * Math.pow(t, i) * Math.pow(1 - t, n - i) / fact(i) / fact(n - i);
            point.x += pivots.get(i).x * factor;
            point.y += pivots.get(i).y * factor;
        }
        return point;
    }

    @Override public void drawCurve(List<Point> pivots) {
        if (pivots.size() < 2) {
            return;
        }
        pivots = pivots.subList(0, Integer.min(MAX_PIVOTS, pivots.size()));
        Point p0 = getPointByT(pivots, 0d);
        for (double t = step; t <= 1d; t += step) {
            var p1 = getPointByT(pivots, t);
            lineDrawer.drawLine(p0.x, p0.y, p1.x, p1.y);
            p0 = p1;
        }
    }
}
