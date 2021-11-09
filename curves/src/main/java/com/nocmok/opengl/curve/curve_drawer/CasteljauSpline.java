package com.nocmok.opengl.curve.curve_drawer;

import com.nocmok.opengl.curve.util.Point;

import java.util.ArrayList;
import java.util.Collection;

public class CasteljauSpline implements CurveDrawer {

    private final Grid grid;

    private final double step;

    public CasteljauSpline(Grid grid, double step) {
        this.grid = grid;
        this.step = step;
    }

    private Point getPointByT(Point p0, Point p1, double t) {
        double dx = p1.x - p0.x;
        double dy = p1.y - p0.y;
        return new Point(p0.x + t * dx, p0.y + t * dy);
    }

    private void _drawCurve(Collection<Point> pivots, double t) {
        while(pivots.size() > 2) {
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
        var point = getPointByT(it.next(), it.next(), t);
        grid.setPixel(point.x, point.y);
    }

    @Override public void drawCurve(Collection<Point> pivots) {
        if(pivots.size() < 2) {
            return;
        }
        for (double t = 0d; t < 1d; t += step) {
            _drawCurve(pivots, t);
        }
    }
}
