package com.nocmok.opengl.primitives.controller;

import com.nocmok.opengl.primitives.controller.action.CircleDragHandler;
import com.nocmok.opengl.primitives.controller.action.EllipseDragHandler;
import com.nocmok.opengl.primitives.controller.action.G2CircleDragHandler;
import com.nocmok.opengl.primitives.controller.action.G2EllipseDragHandler;
import com.nocmok.opengl.primitives.controller.action.G2LineDragHandler;
import com.nocmok.opengl.primitives.controller.action.LineDragHandler;
import com.nocmok.opengl.primitives.controller.action.ShapeDragHandler;
import com.nocmok.opengl.primitives.controller.control.PixelatedCanvas;
import com.nocmok.opengl.primitives.util.Rectangle;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;

import java.net.URL;
import java.util.ResourceBundle;

public class MainSceneController extends AbstractController {

    @FXML
    private GridPane root;
    @FXML
    private StackPane customFrame;
    @FXML
    private StackPane g2Frame;
    @FXML
    private HBox header;
    private PixelatedCanvas g2Canvas;
    private PixelatedCanvas customCanvas;

    @Override public Parent getRoot() {
        return root;
    }

    private int round(double size, int pixelSize) {
        return (int) size - ((int) size) % pixelSize;
    }

    private void addDrawerButton(String name, ShapeDragHandler customHandler, ShapeDragHandler g2Handler) {
        var button = new Button();
        button.setText(name);

        button.setOnMouseClicked(ee -> {
            customHandler.attach(customCanvas);
            g2Handler.attach(g2Canvas);

            customCanvas.setOnMousePressed(e -> {
                customHandler.startDrag(e.getX(), e.getY());
                g2Handler.startDrag(e.getX(), e.getY());
            });

            customCanvas.setOnMouseDragged(e -> {
                customHandler.drag(e.getX(), e.getY());
                g2Handler.drag(e.getX(), e.getY());
            });
        });

        header.getChildren().add(button);
    }

    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        int pixelSize = 5;
        var screen = Screen.getPrimary().getBounds();
        double h = round(screen.getHeight(), pixelSize);
        double w = round(screen.getWidth() / 2, pixelSize);
        int pixelH = (int) (h / pixelSize);
        int pixelW = (int) (w / pixelSize);

        g2Canvas = new PixelatedCanvas(pixelW, pixelH);
        customCanvas = new PixelatedCanvas(pixelW, pixelH);

        g2Canvas.setWidth(w);
        g2Canvas.setHeight(h);

        customCanvas.setWidth(w);
        customCanvas.setHeight(h);

        customFrame.getChildren().add(customCanvas);
        g2Frame.getChildren().add(g2Canvas);

        addDrawerButton("Line", new LineDragHandler(), new G2LineDragHandler());
        addDrawerButton("Circle", new CircleDragHandler(), new G2CircleDragHandler());
        addDrawerButton("Ellipse", new EllipseDragHandler(), new G2EllipseDragHandler());

    }
}
