package com.nocmok.opengl._3d._3d;

// not thread safe class
public class BasicTransformation implements Transformation {

    private static final Math3D math3d = new DumbMath3d();
    private final float[][] operator;
    private final float[] buf;

    public BasicTransformation(float[][] operator) {
        this.operator = operator;
        this.buf = new float[operator.length];
    }

    @Override public float[] apply(float[] vec) {
        math3d.mul(vec, operator, buf);
        System.arraycopy(buf, 0, vec, 0, vec.length);
        return vec;
    }
}
