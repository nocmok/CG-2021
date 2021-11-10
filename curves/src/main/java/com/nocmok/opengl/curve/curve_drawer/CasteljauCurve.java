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
        var splines = new ArrayList<Point>();
        int k = 0;
        for (int i = 0; i < pivots.size(); ++i) {
            splines.add(pivots.get(i));

            // добавляем дополнительную точку после всех точек вида
            // 3, 5, 7, ..., 3 + 2 * k
            // для выполнения гладкости на стыках
            if ((3 + 2 * k == i + 1) && (i + 1 < pivots.size())) {
                splines.add(getPointByT(pivots.get(i), pivots.get(i + 1), 0.5d));
                ++k;
            }
        }

        for (int i = 0; i + 1 < splines.size(); i += 3) {
            splineDrawer.drawCurve(splines.subList(i, i + Integer.min(4, splines.size() - i)));
        }
    }
}
