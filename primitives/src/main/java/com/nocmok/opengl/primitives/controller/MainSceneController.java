package com.nocmok.opengl.primitives.controller;

import com.nocmok.opengl.primitives.controller.action.G2LineDragHandler;
import com.nocmok.opengl.primitives.controller.control.PixelatedCanvas;
import com.nocmok.opengl.primitives.drawer.G2LineDrawer;
import com.nocmok.opengl.primitives.drawer.LineDrawer;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.awt.Color;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class MainSceneController extends AbstractController {

    private final Map<Class<?>, Object> customDrawers = new HashMap<>();
    private final Map<Class<?>, Object> graphics2dDrawers = new HashMap<>();
    @FXML
    private GridPane root;
    @FXML
    private StackPane customFrame;
    @FXML
    private StackPane g2Frame;
    private PixelatedCanvas g2Canvas;
    private PixelatedCanvas customCanvas;

    {
        customDrawers.put(LineDrawer.class, new LineDrawer());
        graphics2dDrawers.put(G2LineDrawer.class, new G2LineDrawer());
    }

    @Override public Parent getRoot() {
        return root;
    }

    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        g2Canvas = new PixelatedCanvas(100, 140);
        customCanvas = new PixelatedCanvas(100,140);

        g2Canvas.setWidth(500);
        g2Canvas.setHeight(700);

        customCanvas.setWidth(500);
        customCanvas.setHeight(700);

        // grag & drop
        new G2LineDragHandler().attach(g2Canvas);
        new G2LineDragHandler().attach(customCanvas);

        g2Frame.getChildren().add(g2Canvas);
        customFrame.getChildren().add(customCanvas);

        drawFrame(g2Canvas);
        drawFrame(customCanvas);
    }

    // subject to delete
    private void drawFrame(PixelatedCanvas canvas) {
        var g2 = canvas.createGraphicsWrapper();
        int x = canvas.getXSizeInPixels() - 1;
        int y = canvas.getYSizeInPixels() - 1;
        g2.drawLine(0, 0, x, 0, Color.BLACK);
        g2.drawLine(0, 0, 0, y, Color.BLACK);
        g2.drawLine(x, y, x, 0, Color.BLACK);
        g2.drawLine(x, y, 0, y, Color.BLACK);
        g2.flush();
    }
}
