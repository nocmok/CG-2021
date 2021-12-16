package com.nocmok.opengl.curve.curve_drawer;

import com.nocmok.opengl.curve.util.Point;

import java.util.List;

public class LinearCurve implements CurveDrawer {

    private Grid grid;

    public LinearCurve(Grid grid) {
        this.grid = grid;
    }

    @Override public void drawCurve(List<Point> pivots) {
        if (pivots.isEmpty()) {
            return;
        }
        var it = pivots.iterator();
        var p0 = it.next();
        while (it.hasNext()) {
            var p1 = it.next();
            drawLine(p0.x, p0.y, p1.x, p1.y);
            p0 = p1;
        }
    }

    public void drawLine(double x0, double y0, double x1, double y1) {
        grid.drawLine(x0, y0, x1, y1);
    }
}
