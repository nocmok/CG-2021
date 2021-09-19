package com.nocmok.opengl.primitives.controller;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;

import java.io.IOException;
import java.net.URL;

public abstract class AbstractController implements Initializable {

    public static URL getLayout(String layoutName) {
        return AbstractController.class.getClassLoader().getResource("layout/" + layoutName);
    }

    public static <T extends AbstractController> T getNewController(String layoutName) throws IOException {
        var loader = new FXMLLoader(getLayout(layoutName));
        loader.load();
        return loader.getController();
    }

    public abstract Parent getRoot();
}
