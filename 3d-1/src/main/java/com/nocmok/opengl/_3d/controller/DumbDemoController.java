package com.nocmok.opengl._3d.controller;

import com.nocmok.opengl._3d._3d.BasicScene;
import com.nocmok.opengl._3d._3d.BasicTransformation;
import com.nocmok.opengl._3d._3d.DumbCamera;
import com.nocmok.opengl._3d._3d.PolygonModel;
import com.nocmok.opengl._3d._3d.TriangleModel;
import com.nocmok.opengl._3d.controller.contols.CanvasScreen;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
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
    private Canvas canvas;

    @Override public Parent getRoot() {
        return root;
    }

    private void drawTriangle(com.nocmok.opengl._3d._3d.Screen screen) {
        float[][] polygon = new float[][]{
                {0.5f, 0.4f},
                {0.4f, 0.6f},
                {0.6f, 0.6f}
        };
        int[][] screenPolygon = new int[3][2];
        for (int i = 0; i < 3; ++i) {
            screen.transform(polygon[i], screenPolygon[i]);
        }
        screen.drawPolygon(screenPolygon);
    }

    private PolygonModel sampleModel() {
        float[][] points = new float[][]{
                {100f, 0f, 0f, 1f},
                {0f, 0f, 100f, 1f},
                {0f, -100f, -100f, 1f},
                {0f, 100f, -100f, 1f}
        };
        int[][] topology = new int[][]{
                {0, 1, 2},
                {0, 1, 3},
                {0, 2, 3},
                {1, 2, 3}
        };
        return new TriangleModel(points, topology);
    }

    private float[][] rotation(float rad) {
        float cos = (float) Math.cos(rad);
        float sin = (float) Math.sin(rad);
        return new float[][]{
                {cos, 0f, -sin, 1f},
                {0f, 1f, 0f, 1f},
                {sin, 0f, cos, 1f},
                {0f, 0f, 0f, 1f}
        };
    }

    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        canvas = new Canvas();

        var primaryScreen = Screen.getPrimary().getBounds();
        canvas.setWidth(primaryScreen.getWidth());
        canvas.setHeight(primaryScreen.getHeight());

        frame.getChildren().add(canvas);

        var screen = new CanvasScreen(canvas);
        var camera = new DumbCamera((float) (canvas.getWidth() / 2), (float) (canvas.getHeight() / 2));
        var model = sampleModel();
        var scene = new BasicScene();

        new Thread(() -> {
            float rad = 0f;
            scene.addObject(model, new float[]{0f, 0f, 0f, 1f});

            while (true) {
                scene.getTransformations().clear();
                scene.addTransformation(new BasicTransformation(rotation(rad)));
                rad += 0.02f;
                if (rad > 2 * 3.15) {
                    rad = 0f;
                }

                Platform.runLater(() -> {
                    var g2 = canvas.getGraphicsContext2D();
                    g2.setFill(Color.WHITE);
                    g2.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
                    scene.draw(screen, camera);
                });
                try {
                    Thread.sleep(17);
                } catch (InterruptedException ignore) {
                    throw new RuntimeException(ignore.getMessage());
                }
            }
        }).start();
    }
}
