package com.nocmok.opengl.curve.curve_drawer;

import com.nocmok.opengl.curve.util.Point;

import java.util.List;

public class BezierSpline3 implements CurveDrawer {

    private final double step;

    private final LinearCurve lineDrawer;

    public BezierSpline3(Grid grid, double step) {
        this.step = step;
        this.lineDrawer = new LinearCurve(grid);
    }

    private Point getPointByT(List<Point> pivots, double t) {
        var p0 = pivots.get(0);
        var p1 = pivots.get(1);
        var p2 = pivots.get(2);
        var p3 = pivots.get(3);

        double x =
                (1d - t) * (1d - t) * (1d - t) * p0.x + 3 * t * (1 - t) * (1 - t) * p1.x + 3 * t * t * (1 - t) * p2.x +
                        t * t * t * p3.x;
        double y =
                (1d - t) * (1d - t) * (1d - t) * p0.y + 3 * t * (1 - t) * (1 - t) * p1.y + 3 * t * t * (1 - t) * p2.y +
                        t * t * t * p3.y;

        return new Point(x, y);
    }

    @Override public void drawCurve(List<Point> pivots) {
        if (pivots.size() != 4) {
            throw new UnsupportedOperationException("pivots size expected to be 4");
        }
        Point p0 = null;
        for (double t = 0d; t <= 1d; t += step) {
            var p1 = getPointByT(pivots, t);
            if (p0 != null) {
                lineDrawer.drawLine(p0.x, p0.y, p1.x, p1.y);
            }
            p0 = p1;
        }
    }
}
