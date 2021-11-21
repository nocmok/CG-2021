package com.nocmok.opengl._3d._3d;

import com.nocmok.opengl._3d._3d.camera.Camera;
import com.nocmok.opengl._3d._3d.model.PolygonModel;
import com.nocmok.opengl._3d._3d.transformation.Transformation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

// This class not thread-safe
public class BasicScene implements Scene {

    private static final Math3D math3d = new DumbMath3d();
    private List<Object3D> objects = new ArrayList<>();

    private List<Transformation> transformations = new ArrayList<>();

    public BasicScene() {
    }

    private void transformToScreenCoordinates(Screen screen, double[][] points, int[][] screenPoints) {
        for (int i = 0; i < points.length; ++i) {
            screen.transform(points[i], screenPoints[i]);
        }
    }

    private void shiftPoints(double[][] points, double[] shift) {
        for (var p : points) {
            math3d.sum3(p, shift, p);
        }
    }

    @Override public void addObject(PolygonModel model, double[] position) {
        objects.add(new Object3D(model, position));
    }

    @Override public void addTransformation(Transformation transformation) {
        transformations.add(transformation);
    }

    private boolean shouldDrawObject(Object3D obj) {
        return true;
    }

    private void applyTransformation(double[][] points, Transformation transformation) {
        double[] buf = new double[4];
        for (double[] point : points) {
            transformation.apply(point, buf);
            System.arraycopy(buf, 0, point, 0, 4);
        }
    }

    private void applyTransformations(double[][] points, List<Transformation> transformations) {
        for (var t : transformations) {
            applyTransformation(points, t);
        }
    }

    private double[][] deepCopy(double[][] array) {
        double[][] copy = new double[array.length][];
        for (int i = 0; i < array.length; ++i) {
            copy[i] = Arrays.copyOf(array[i], array[i].length);
        }
        return copy;
    }

    public Collection<Transformation> getTransformations() {
        return transformations;
    }

    // Метод предполагает, что объект валиден и виден на камере
    private void drawObject(Screen screen, Camera camera, Object3D obj) {
        double[][] objPoints = deepCopy(obj.model.getPoints());

        shiftPoints(objPoints, obj.position);

        applyTransformations(objPoints, transformations);

        double[][] viewPoints = new double[objPoints.length][2];
        camera.project(objPoints, viewPoints);

        int[][] screenPoints = new int[objPoints.length][2];
        transformToScreenCoordinates(screen, viewPoints, screenPoints);

        int[][] polygon = new int[obj.model.getPolygonDegree()][2];

        var it = obj.model.getTopology().getPolygons();
        while (it.hasNext()) {
            int[] indexes = it.next();
            for (int i = 0; i < indexes.length; ++i) {
                polygon[i][0] = screenPoints[indexes[i]][0];
                polygon[i][1] = screenPoints[indexes[i]][1];
            }
            screen.drawPolygon(polygon, indexes.length);
        }
    }

    @Override public void draw(Screen screen, Camera camera) {
        for (Object3D obj : objects) {
            if (shouldDrawObject(obj)) {
                drawObject(screen, camera, obj);
            }
        }
    }

    private static class Object3D {
        PolygonModel model;
        double[] position;

        Object3D(PolygonModel model, double[] position) {
            this.model = model;
            this.position = position;
        }
    }

    @Override public void clear() {
        objects.clear();
    }
}
