package com.nocmok.opengl._3d._3d;

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

    private void transformToScreenCoordinates(Screen screen, float[][] points, int[][] screenPoints) {
        for (int i = 0; i < points.length; ++i) {
            screen.transform(points[i], screenPoints[i]);
        }
    }

    private void shiftPoints(float[][] points, float[] shift) {
        for (var p : points) {
            math3d.sum3(p, shift, p);
        }
    }

    @Override public void addObject(PolygonModel model, float[] position) {
        objects.add(new Object3D(model, position));
    }

    @Override public void addTransformation(Transformation transformation) {
        transformations.add(transformation);
    }

    private boolean shouldDrawObject(Object3D obj) {
        return true;
    }

    private void applyTransformation(float[][] points, Transformation transformation) {
        for (int i = 0; i < points.length; ++i) {
            transformation.apply(points[i]);
        }
    }

    private void applyTransformations(float[][] points, List<Transformation> transformations) {
        for (var t : transformations) {
            applyTransformation(points, t);
        }
    }

    private float[][] deepCopy(float[][] array) {
        float[][] copy = new float[array.length][];
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
        float[][] objPoints = deepCopy(obj.model.getPoints());

        shiftPoints(objPoints, obj.position);

        applyTransformations(objPoints, transformations);

        float[][] viewPoints = new float[objPoints.length][2];
        camera.project(objPoints, viewPoints);

        int[][] screenPoints = new int[objPoints.length][2];
        transformToScreenCoordinates(screen, viewPoints, screenPoints);

        int[][] polygon = new int[obj.model.getPolygonDegree()][2];

        var it = obj.model.getTopology().getPolygons();
        while (it.hasNext()) {
            int[] indexes = it.next();
            for (int i = 0; i < obj.model.getPolygonDegree(); ++i) {
                polygon[i][0] = screenPoints[indexes[i]][0];
                polygon[i][1] = screenPoints[indexes[i]][1];
            }
            screen.drawPolygon(polygon);
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
        float[] position;

        Object3D(PolygonModel model, float[] position) {
            this.model = model;
            this.position = position;
        }
    }
}
