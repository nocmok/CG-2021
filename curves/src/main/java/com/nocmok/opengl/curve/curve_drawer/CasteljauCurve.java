package com.nocmok.opengl.curve.curve_drawer;

import com.nocmok.opengl.curve.util.Point;

import java.util.ArrayList;
import java.util.List;

public class CasteljauCurve implements CurveDrawer {

    private final CasteljauSpline splineDrawer;

    public CasteljauCurve(Grid grid, double step) {
        this.splineDrawer = new CasteljauSpline(grid, step);
    }

    private Point getPointByT(Point p0, Point p1, double t) {
        double dx = p1.x - p0.x;
        double dy = p1.y - p0.y;
        return new Point(p0.x + t * dx, p0.y + t * dy);
    }

    @Override public void drawCurve(List<Point> pivots) {
        for (int i = 0; i + 3 < pivots.size(); i += 3) {
            splineDrawer.drawCurve(pivots.subList(i, i + Integer.min(4, pivots.size() - i)));
        }
    }
}
