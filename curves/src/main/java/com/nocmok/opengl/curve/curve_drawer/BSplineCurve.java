package com.nocmok.opengl.curve.curve_drawer;

import com.nocmok.opengl.curve.util.Point;

import java.util.List;

public class BSplineCurve implements CurveDrawer {

    private final BSpline3 bSpline3;

    public BSplineCurve(Grid grid, double step) {
        this.bSpline3 = new BSpline3(grid,step);
    }

    @Override public void drawCurve(List<Point> pivots) {
        for(int i = 0; i + 3 < pivots.size(); ++i) {
            bSpline3.drawCurve(pivots.subList(i, i + 4));
        }
    }
}
