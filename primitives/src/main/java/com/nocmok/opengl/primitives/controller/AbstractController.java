package com.nocmok.opengl.primitives.controller;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;

import java.io.IOException;
import java.net.URL;

public abstract class AbstractController implements Initializable {

    public static <T extends AbstractController> T getNewController(URL layout) throws IOException {
        var loader = new FXMLLoader(layout);
        loader.load();
        return loader.getController();
    }

    public abstract Parent getRoot();
}
