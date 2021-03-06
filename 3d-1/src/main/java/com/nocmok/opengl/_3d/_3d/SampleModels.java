package com.nocmok.opengl._3d._3d;

import com.nocmok.opengl._3d._3d.model.PolygonModel;
import com.nocmok.opengl._3d._3d.model.RectangleModel;
import com.nocmok.opengl._3d._3d.model.TriangleModel;

public class SampleModels {

    public static PolygonModel tetrahedronModel(double side) {
        double[][] points = new double[][]{
                {0, side * 2 / 3, 0, 1},
                {0, -side * 1 / 3, side, 1},
                {(-side * Math.cos(Math.PI / 6)), -side * 1 / 3, (-100 * Math.sin(Math.PI / 6)), 1},
                {(side * Math.cos(Math.PI / 6)), -side * 1 / 3, (-100 * Math.sin(Math.PI / 6)), 1}
        };
        int[][] topology = new int[][]{
                {0, 1, 2},
                {0, 1, 3},
                {0, 2, 3},
                {1, 2, 3}
        };
        return new TriangleModel(points, topology);
    }

    public static PolygonModel cubeModel(double side) {
        double[][] points = new double[][]{
                {-side / 2, side / 2, side / 2, 1},
                {-side / 2, side / 2, -side / 2, 1},
                {side / 2, side / 2, -side / 2, 1},
                {side / 2, side / 2, side / 2, 1},

                {-side / 2, -side / 2, side / 2, 1},
                {-side / 2, -side / 2, -side / 2, 1},
                {side / 2, -side / 2, -side / 2, 1},
                {side / 2, -side / 2, side / 2, 1},
        };
        int[][] topology = new int[][]{
                {0, 1, 2, 3},
                {0, 3, 7, 4},
                {4, 5, 6, 7},
                {1, 2, 6, 5},
                {0, 1, 5, 4},
                {3, 2, 6, 7},
        };
        return new RectangleModel(points, topology);
    }
}
