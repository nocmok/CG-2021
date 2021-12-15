package com.nocmok.opengl.curve.curve_drawer;

import com.nocmok.opengl.curve.util.Point;

import java.util.ArrayList;
import java.util.List;

public class BezierCurve implements CurveDrawer {

    private final BezierSpline3 spline3Drawer;

    public BezierCurve(Grid grid, double step) {
        this.spline3Drawer = new BezierSpline3(grid, step);
    }

    private Point getPointByT(Point p0, Point p1, double t) {
        double dx = p1.x - p0.x;
        double dy = p1.y - p0.y;
        return new Point(p0.x + t * dx, p0.y + t * dy);
    }

    @Override public void drawCurve(List<Point> pivots) {
        // из скольки сплайнов будет состоять кривая
        int nSplines = 0;
        if (pivots.size() >= 4) {
            // каждые 3 новые точки дают еще один сплайн

            // nSplines += (pivots2.size() - 4 + 3 - 1) / 3;
            nSplines = 1 + (pivots.size() - 4) / 3;
        }

        for (int i = 0, k = 0; k < nSplines; i += 3, k += 1) {
            spline3Drawer.drawCurve(pivots.subList(i, i + 4));
        }
    }
}
