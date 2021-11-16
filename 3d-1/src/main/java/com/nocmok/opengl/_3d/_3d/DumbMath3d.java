package com.nocmok.opengl._3d._3d;

public class DumbMath3d implements Math3D {

    @Override public float[] mul(float[] vec, float[][] matrix, float[] mul) {
        int m = matrix.length;
        int n = matrix[0].length;
        for (int j = 0; j < n; ++j) {
            int sum = 0;
            for (int i = 0; i < m; ++i) {
                sum += vec[i] * matrix[i][j];
            }
            mul[j] = sum;
        }
        return mul;
    }

    @Override public float[][] mul(float[][] a, float[][] b, float[][] mul) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override public float[] sub3(float[] a, float[] b) {
        float[] sub = new float[4];
        sub[3] = 1f;
        sub[0] = a[0] - b[0];
        sub[1] = a[1] - b[1];
        sub[2] = a[2] - b[2];
        return sub;
    }

    @Override public float[] sum3(float[] a, float[] b, float[] sum) {
        sum[3] = 1f;
        sum[0] = a[0] + b[0];
        sum[1] = a[1] + b[1];
        sum[2] = a[2] + b[2];
        return sum;
    }

    @Override public float[] neg(float[] a) {
        a[0] = -a[0];
        a[1] = -a[1];
        a[2] = -a[2];
        return a;
    }
}
