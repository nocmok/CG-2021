package com.nocmok.opengl.primitives.controller;

import com.nocmok.opengl.primitives.controller.action.CircleDragHandler;
import com.nocmok.opengl.primitives.controller.action.EllipseDragHandler;
import com.nocmok.opengl.primitives.controller.action.G2CircleDragHandler;
import com.nocmok.opengl.primitives.controller.action.G2EllipseDragHandler;
import com.nocmok.opengl.primitives.controller.control.PixelatedCanvas;
import com.nocmok.opengl.primitives.drawer.CircleDrawer;
import com.nocmok.opengl.primitives.drawer.EllipseDrawer;
import com.nocmok.opengl.primitives.drawer.LineDrawer;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;

import java.awt.Color;
import java.net.URL;
import java.util.ResourceBundle;

public class MainSceneController extends AbstractController {

    @FXML
    private GridPane root;
    @FXML
    private StackPane customFrame;
    @FXML
    private StackPane g2Frame;
    private PixelatedCanvas g2Canvas;
    private PixelatedCanvas customCanvas;

    @Override public Parent getRoot() {
        return root;
    }

    private int round(double size, int pixelSize) {
        return (int) size - ((int)size) % pixelSize;
    }

    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        int pixelSize = 5;
        var screen = Screen.getPrimary().getBounds();
        double h = round(screen.getHeight(), pixelSize);
        double w = round(screen.getWidth() / 2, pixelSize);
        int pixelH = (int)(h / pixelSize);
        int pixelW = (int)(w / pixelSize);

        g2Canvas = new PixelatedCanvas(pixelW, pixelH);
        customCanvas = new PixelatedCanvas(pixelW, pixelH);

        g2Canvas.setWidth(w);
        g2Canvas.setHeight(h);

        customCanvas.setWidth(w);
        customCanvas.setHeight(h);
    }
}
