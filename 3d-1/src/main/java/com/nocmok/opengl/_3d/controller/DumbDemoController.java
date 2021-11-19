package com.nocmok.opengl._3d.controller;

import com.nocmok.opengl._3d._3d.BasicScene;
import com.nocmok.opengl._3d._3d.camera.CentralProjectionCamera;
import com.nocmok.opengl._3d._3d.model.PolygonModel;
import com.nocmok.opengl._3d._3d.model.RectangleModel;
import com.nocmok.opengl._3d._3d.model.TriangleModel;
import com.nocmok.opengl._3d._3d.transformation.BasicTransformation;
import com.nocmok.opengl._3d.controller.contols.CanvasScreen;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;

import java.net.URL;
import java.util.ResourceBundle;

public class DumbDemoController extends AbstractController {

    @FXML
    private GridPane root;
    @FXML
    private Pane frame;
    @FXML
    private Slider povSlider;
    @FXML
    private Slider xShift;
    @FXML
    private Slider yShift;
    @FXML
    private Slider zShift;
    private Canvas canvas;

    @Override public Parent getRoot() {
        return root;
    }

    private PolygonModel tetrahedronModel() {
        double[][] points = new double[][]{
                {0f, 100f, 0f, 1f},
                {0f, 0f, 100f, 1f},
                {(-100 * Math.cos(Math.PI / 6)), 0f, (-100 * Math.sin(Math.PI / 6)), 1f},
                {(100 * Math.cos(Math.PI / 6)), 0f, (-100 * Math.sin(Math.PI / 6)), 1f}
        };
        int[][] topology = new int[][]{
                {0, 1, 2},
                {0, 1, 3},
                {0, 2, 3},
                {1, 2, 3}
        };
        return new TriangleModel(points, topology);
    }

    private PolygonModel cubeModel(double side) {
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

    private double[][] yRotation(double rad) {
        double cos = Math.cos(rad);
        double sin = Math.sin(rad);
        return new double[][]{
                {cos, 0, -sin, 0},
                {0, 1, 0, 0},
                {sin, 0, cos, 0},
                {0, 0, 0, 1}
        };
    }

    private double[][] xRotation(double rad) {
        double cos = Math.cos(rad);
        double sin = Math.sin(rad);
        return new double[][]{
                {1, 0, 0, 0},
                {0, cos, sin, 0},
                {0, -sin, cos, 0},
                {0, 0, 0, 1}
        };
    }

    private double[][] zRotation(double rad) {
        double cos = Math.cos(rad);
        double sin = Math.sin(rad);
        return new double[][]{
                {cos, sin, 0, 0},
                {-sin, cos, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        };
    }

    private void clearCanvas(Canvas canvas) {
        var g2 = canvas.getGraphicsContext2D();
        g2.setFill(Color.grayRgb(244));
        g2.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        canvas = new Canvas();

        var primaryScreen = Screen.getPrimary().getBounds();
        canvas.setWidth(primaryScreen.getWidth());
        canvas.setHeight(primaryScreen.getHeight());

        frame.getChildren().add(canvas);

        povSlider.setMin(0);
        povSlider.setMax(1000);
        povSlider.setValue(500);

        xShift.setMin(-500);
        xShift.setMax(500);
        xShift.setValue(0);

        yShift.setMin(-500);
        yShift.setMax(500);
        yShift.setValue(0);

        zShift.setMin(-500);
        zShift.setMax(500);
        zShift.setValue(0);

        var screen = new CanvasScreen(canvas);

        double cameraXSize = canvas.getWidth() / 2;
        double cameraYSize = canvas.getHeight() / 2;

        var camera = new CentralProjectionCamera(1000, cameraXSize, cameraYSize);

        var tetrahedron = tetrahedronModel();
        var cube = cubeModel(100);

        var scene = new BasicScene();

        scene.addObject(cube, new double[]{0, 0, 0, 1});
        scene.addObject(cube, new double[]{200, 200, -500, 1});
        scene.addObject(cube, new double[]{-200, -200, 500, 1});


        scene.draw(screen, new CentralProjectionCamera(povSlider.getValue(), cameraXSize, cameraYSize));

        new Thread(() -> {
            double rad = 0f;

            while (true) {
                scene.getTransformations().clear();
//                scene.addTransformation(new BasicTransformation(xRotation(rad)));
                scene.addTransformation(new BasicTransformation(yRotation(rad)));
//                scene.addTransformation(new BasicTransformation(zRotation(rad)));
                rad += 0.02f;
                if (rad > 2 * 3.15) {
                    rad = 0f;
                }

                Platform.runLater(() -> {
                    var g2 = canvas.getGraphicsContext2D();
                    clearCanvas(canvas);
                    scene.draw(screen, camera);
                });
                try {
                    Thread.sleep(17);
                } catch (InterruptedException ignore) {
                    throw new RuntimeException(ignore.getMessage());
                }
            }
        }).start();

//        povVal.valueProperty().addListener((val) -> {
//            clearCanvas(canvas);
//
//            scene.draw(screen, new CentralProjectionCamera(povVal.getValue(), cameraXSize, cameraYSize));
//        });
//        povVal.valueProperty().addListener((val) -> {
//            povLabel.setText(Objects.toString(povVal.getValue()));
//        });
//
//        xVal.valueProperty().addListener((val) -> {
//            clearCanvas(canvas);
//            scene.clear();
//            scene.addObject(cube, new double[] { xVal.getValue(),yVal.getValue(),zVal.getValue(),1 });
//            scene.draw(screen, new CentralProjectionCamera(povVal.getValue(), cameraXSize, cameraYSize));
//        });
//        xVal.valueProperty().addListener((val) -> {
//            xLabel.setText(Objects.toString(xVal.getValue()));
//        });
//
//        yVal.valueProperty().addListener((val) -> {
//            clearCanvas(canvas);
//            scene.clear();
//            scene.addObject(cube, new double[] { xVal.getValue(),yVal.getValue(),zVal.getValue(),1 });
//            scene.draw(screen, new CentralProjectionCamera(povVal.getValue(), cameraXSize, cameraYSize));
//        });
//        yVal.valueProperty().addListener((val) -> {
//            yLabel.setText(Objects.toString(yVal.getValue()));
//        });
//
//        zVal.valueProperty().addListener((val) -> {
//            clearCanvas(canvas);
//            scene.clear();
//            scene.addObject(cube, new double[] { xVal.getValue(),yVal.getValue(),zVal.getValue(),1 });
//            scene.draw(screen, new CentralProjectionCamera(povVal.getValue(), cameraXSize, cameraYSize));
//        });
//        zVal.valueProperty().addListener((val) -> {
//            zLabel.setText(Objects.toString(zVal.getValue()));
//        });
    }
}
