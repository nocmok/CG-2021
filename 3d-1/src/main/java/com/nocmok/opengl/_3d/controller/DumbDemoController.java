package com.nocmok.opengl._3d.controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
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

    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        canvas = new Canvas();

        var primaryScreen = Screen.getPrimary().getBounds();
        canvas.setWidth(primaryScreen.getWidth());
        canvas.setHeight(primaryScreen.getHeight());

        frame.getChildren().add(canvas);
    }
}
