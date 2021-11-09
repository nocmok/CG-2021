package com.nocmok.opengl.curve.controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class MainSceneController extends AbstractController {

    @FXML
    private GridPane root;
    @FXML
    private AnchorPane demoContainer;
    @FXML
    private HBox demos;

    @Override public Parent getRoot() {
        return root;
    }

    private void selectDemo(AbstractController controller) {
        demoContainer.getChildren().clear();
        AnchorPane.setLeftAnchor(controller.getRoot(), 0d);
        AnchorPane.setRightAnchor(controller.getRoot(), 0d);
        AnchorPane.setTopAnchor(controller.getRoot(), 0d);
        AnchorPane.setBottomAnchor(controller.getRoot(), 0d);
        demoContainer.getChildren().add(controller.getRoot());
    }

    private AbstractController addDemo(String layoutName, String title) {
        var controller = AbstractController.getNewController(
                getLayoutUri(layoutName));
        var button = new Button(title);
        button.setOnMouseClicked(e -> selectDemo(controller));
        demos.getChildren().add(button);
        return controller;
    }

    private URL getLayoutUri(String layoutName) {
        return getClass().getClassLoader().getResource("layout/" + layoutName);
    }

    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        selectDemo(addDemo("linear_interpolation_demo_layout.fxml", "Linear Interpolation"));
        addDemo("casteljau_spline_demo_layout.fxml", "Casteljau spline");
    }
}
