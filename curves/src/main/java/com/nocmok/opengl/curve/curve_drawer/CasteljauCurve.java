package com.nocmok.opengl.curve.curve_drawer;

import com.nocmok.opengl.curve.util.Point;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CasteljauCurve implements CurveDrawer {

    private final Grid grid;

    private final double step;

    public CasteljauCurve(Grid grid, double step) {
        this.grid = grid;
        this.step = step;
    }

    private Point getPointByT(Point p0, Point p1, double t) {
        double dx = p1.x - p0.x;
        double dy = p1.y - p0.y;
        return new Point(p0.x + t * dx, p0.y + t * dy);
    }

    private void drawPoint(Collection<Point> pivots, double t) {
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

    private void drawOneSpline(Collection<Point> pivots) {
        for (double t = 0d; t < 1d; t += step) {
            drawPoint(pivots, t);
        }
    }

    @Override public void drawCurve(List<Point> pivots) {
        var splines = new ArrayList<Point>();
        int k = 0;
        for(int i = 0; i < pivots.size(); ++i) {
            splines.add(pivots.get(i));

            // добавляем дополнительную точку после всех точек вида
            // для выполнения гладкости на стыках
            // 3, 5, 7
            // 3 + 2 * k
            if((3 + 2 * k == i + 1) && (i + 1 < pivots.size())) {
                splines.add(getPointByT(pivots.get(i), pivots.get(i + 1), 0.5d));
                ++k;
            }
        }

        for(int i = 0; i + 1 < splines.size(); i += 3) {
            drawOneSpline(splines.subList(i, i + Integer.min(4, splines.size() - i)));
        }
    }
}
