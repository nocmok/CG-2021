package com.nocmok.opengl.curve.curve_drawer;

import com.nocmok.opengl.curve.util.Point;

import java.util.ArrayList;
import java.util.List;

public class BezierCurve implements CurveDrawer {

    private static final int MAX_PIVOTS = 4;
    private static final double[] fact = new double[MAX_PIVOTS + 1];

    static {
        fact[0] = 1;
        for (int i = 1; i <= MAX_PIVOTS; ++i) {
            fact[i] = fact[i - 1] * i;
        }
    }

    private final Grid grid;

    private final double step;

    public BezierCurve(Grid grid, double step) {
        this.grid = grid;
        this.step = step;
    }

    private static double fact(int n) {
        return fact[n];
    }

    private Point getPointByT(Point p0, Point p1, double t) {
        double dx = p1.x - p0.x;
        double dy = p1.y - p0.y;
        return new Point(p0.x + t * dx, p0.y + t * dy);
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

    private void drawOneSpline(List<Point> pivots) {
        for (double t = 0d; t < 1d; t += step) {
            drawPoint(pivots, t);
        }
    }

    @Override public void drawCurve(List<Point> pivots) {
        var splines = new ArrayList<Point>();
        int k = 0;
        for (int i = 0; i < pivots.size(); ++i) {
            splines.add(pivots.get(i));

            // добавляем дополнительную точку после всех точек вида
            // для выполнения гладкости на стыках
            // 3, 5, 7
            // 3 + 2 * k
            if ((3 + 2 * k == i + 1) && (i + 1 < pivots.size())) {
                splines.add(getPointByT(pivots.get(i), pivots.get(i + 1), 0.5d));
                ++k;
            }
        }

        for (int i = 0; i + 1 < splines.size(); i += 3) {
            drawOneSpline(splines.subList(i, i + Integer.min(4, splines.size() - i)));
        }
    }
}
