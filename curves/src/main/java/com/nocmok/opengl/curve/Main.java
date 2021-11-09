package com.nocmok.opengl.curve;

import com.nocmok.opengl.curve.controller.AbstractController;
import com.nocmok.opengl.curve.controller.MainSceneController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        var controller = AbstractController
                .<MainSceneController>getNewController(getClass().getClassLoader().getResource(
                        "layout/main_layout.fxml"));
        stage.setWidth(800);
        stage.setHeight(600);
        stage.setScene(new Scene(controller.getRoot()));
        stage.show();
    }
}
