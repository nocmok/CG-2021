package com.nocmok.opengl.fillclip.controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

public class MainSceneController extends AbstractController {

    @FXML
    private GridPane root;
    @FXML
    private AnchorPane demoContainer;
    @FXML
    private Button dfsFillingDemo;
    @FXML
    private Button polygonFillingDemo;
    @FXML
    private Button clippingDemo;

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

    private URL getLayoutUri(String layoutName) {
        return getClass().getClassLoader().getResource("layout/" + layoutName);
    }

    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        var dfsFillingDemoController = AbstractController.<DfsFillingDemoController>getNewController(
                getLayoutUri("dfs_filling_demo_layout.fxml"));
        var polygonFillingDemoController = AbstractController.<PolygonFillingDemoController>getNewController(
                getLayoutUri("polygon_filling_demo_layout.fxml"));
        var clippingDemoController = AbstractController.<ClippingDemoController>getNewController(
                getLayoutUri("clipping_demo_layout.fxml"));

        dfsFillingDemo.setOnMouseClicked(e -> selectDemo(dfsFillingDemoController));
        polygonFillingDemo.setOnMouseClicked(e -> selectDemo(polygonFillingDemoController));
        clippingDemo.setOnMouseClicked(e -> selectDemo(clippingDemoController));

        selectDemo(dfsFillingDemoController);
    }
}
