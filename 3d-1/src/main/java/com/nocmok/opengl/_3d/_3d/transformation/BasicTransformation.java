package com.nocmok.opengl._3d._3d.transformation;

import com.nocmok.opengl._3d._3d.DumbMath3d;
import com.nocmok.opengl._3d._3d.Math3D;

// not thread safe class
public class BasicTransformation implements Transformation {

    private static final Math3D math3d = new DumbMath3d();
    private final double[][] operator;
    private final double[] buf;

    public BasicTransformation(double[][] operator) {
        this.operator = operator;
        this.buf = new double[operator.length];
    }

    @Override public double[] apply(double[] vec) {
        math3d.mul(vec, operator, buf);
        System.arraycopy(buf, 0, vec, 0, vec.length);
        return vec;
    }
}
