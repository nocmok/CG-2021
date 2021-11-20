package com.nocmok.opengl._3d._3d.transformation;

import com.nocmok.opengl._3d._3d.DumbMath3d;
import com.nocmok.opengl._3d._3d.Math3D;

// not thread safe class
public class BasicTransformation implements Transformation {

    protected static final Math3D math3d = new DumbMath3d();
    protected final double[][] operator;

    public BasicTransformation(double[][] operator) {
        this.operator = operator;
    }

    @Override public double[] apply(double[] vector, double[] transformed) {
        math3d.mul(vector, operator, transformed);
        return transformed;
    }
}
