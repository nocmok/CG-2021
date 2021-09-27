package com.nocmok.opengl.fillclip.controller;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;

import java.io.IOException;
import java.net.URL;

public abstract class AbstractController implements Initializable {

    public static <T extends AbstractController> T getNewController(URL layout) {
        try {
            var loader = new FXMLLoader(layout);
            loader.load();
            return loader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract Parent getRoot();
}
