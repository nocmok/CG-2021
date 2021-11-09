package com.nocmok.opengl.curve.curve_drawer;

import com.nocmok.opengl.curve.util.IntPoint;

import java.util.Collection;

public interface CurveDrawer {

    void drawCurve(Collection<IntPoint> pivots);
}
