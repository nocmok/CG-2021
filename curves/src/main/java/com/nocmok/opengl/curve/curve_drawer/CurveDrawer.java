package com.nocmok.opengl.curve.curve_drawer;

import com.nocmok.opengl.curve.util.Point;

import java.util.Collection;

public interface CurveDrawer {

    void drawCurve(Collection<Point> pivots);
}
