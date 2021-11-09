package com.nocmok.opengl.curve.curve_drawer;

import com.nocmok.opengl.curve.util.Point;

import java.util.List;

public class BezierSpline implements CurveDrawer {

    private static final int MAX_PIVOTS = 100;
    private static final double[] fact = new double[MAX_PIVOTS + 1];

    static {
        fact[0] = 1;
        for(int i = 1; i <= MAX_PIVOTS; ++i) {
            fact[i] = fact[i - 1] * i;
        }
    }

    private final Grid grid;
    private final double step;

    public BezierSpline(Grid grid, double step) {
        this.grid = grid;
        this.step = step;
    }

    private static double fact(int n) {
        return fact[n];
    }

    private void drawPoint(List<Point> pivots, double t) {
        var point = new Point(0d, 0d);
        int n = pivots.size() - 1;
        for (int i = 0; i < pivots.size(); ++i) {
            var factor = fact(n) * Math.pow(t, i) * Math.pow(1 - t, n - i) / fact(i) / fact(n - i);
            point.x += pivots.get(i).x * factor;
            point.y += pivots.get(i).y * factor;
        }
        grid.setPixel(point.x, point.y);
    }

    @Override public void drawCurve(List<Point> pivots) {
        if (pivots.size() < 2) {
            return;
        }
        int nPivots = Integer.min(MAX_PIVOTS, pivots.size());
        for (double t = 0d; t <= 1d; t += step) {
            drawPoint(pivots.subList(0, nPivots), t);
        }
    }
}
