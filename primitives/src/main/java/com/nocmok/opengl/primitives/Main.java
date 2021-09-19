package com.nocmok.opengl.primitives;

import com.nocmok.opengl.primitives.controller.AbstractController;
import com.nocmok.opengl.primitives.controller.MainSceneController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        var controller = AbstractController.<MainSceneController>getNewController("main_layout.fxml");
        stage.setWidth(800);
        stage.setHeight(600);
        stage.setScene(new Scene(controller.getRoot()));
        stage.show();
    }
}
