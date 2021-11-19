package com.nocmok.opengl._3d._3d;

public class DumbMath3d implements Math3D {

    @Override public double[] mul(double[] vec, double s, double[] res) {
        for (int i = 0; i < vec.length; ++i) {
            res[i] = vec[i] * s;
        }
        return vec;
    }

    @Override public double[] mul(double[] vec, double[][] matrix, double[] mul) {
        int m = matrix.length;
        int n = matrix[0].length;
        for (int j = 0; j < n; ++j) {
            double sum = 0;
            for (int i = 0; i < m; ++i) {
                sum += vec[i] * matrix[i][j];
            }
            mul[j] = sum;
        }
        return mul;
    }

    @Override public double[][] mul(double[][] a, double[][] b, double[][] mul) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override public double[] sub3(double[] a, double[] b) {
        double[] sub = new double[4];
        sub[3] = 1f;
        sub[0] = a[0] - b[0];
        sub[1] = a[1] - b[1];
        sub[2] = a[2] - b[2];
        return sub;
    }

    @Override public double[] sum3(double[] a, double[] b, double[] sum) {
        sum[3] = 1f;
        sum[0] = a[0] + b[0];
        sum[1] = a[1] + b[1];
        sum[2] = a[2] + b[2];
        return sum;
    }

    @Override public double[] neg(double[] a) {
        a[0] = -a[0];
        a[1] = -a[1];
        a[2] = -a[2];
        return a;
    }
}
