package com.nocmok.opengl.curve.curve_drawer;

import com.nocmok.opengl.curve.util.Point;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CasteljauSpline implements CurveDrawer {

    private final double step;

    private final LinearCurve lineDrawer;

    public CasteljauSpline(Grid grid, double step) {
        this.step = step;
        this.lineDrawer = new LinearCurve(grid);
    }

    private Point getPointByT(Point p0, Point p1, double t) {
        double dx = p1.x - p0.x;
        double dy = p1.y - p0.y;
        return new Point(p0.x + t * dx, p0.y + t * dy);
    }

    private Point getPointByT(Collection<Point> pivots, double t) {
        while (pivots.size() > 2) {
            var newPivots = new ArrayList<Point>(pivots.size() - 1);
            var it = pivots.iterator();
            var p0 = it.next();
            while (it.hasNext()) {
                var p1 = it.next();
                newPivots.add(getPointByT(p0, p1, t));
                p0 = p1;
            }
            pivots = newPivots;
        }
        var it = pivots.iterator();
        return getPointByT(it.next(), it.next(), t);
    }

    @Override public void drawCurve(List<Point> pivots) {
        if (pivots.size() < 2) {
            return;
        }
        Point p0 = getPointByT(pivots, 0d);
        for (double t = step; t <= 1d; t += step) {
            var p1 = getPointByT(pivots, t);
            lineDrawer.drawLine(p0.x, p0.y, p1.x, p1.y);
            p0 = p1;
        }
    }
}
