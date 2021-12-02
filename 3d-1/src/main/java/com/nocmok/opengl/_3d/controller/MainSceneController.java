package com.nocmok.opengl._3d.controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainSceneController extends AbstractController {

    @FXML
    private GridPane root;
    @FXML
    private AnchorPane demoContainer;
    @FXML
    private HBox demos;
    @FXML
    private Button about;

    @Override public Parent getRoot() {
        return root;
    }

    private String getAboutMessage() {
        try {
            var in = getClass().getClassLoader().getResourceAsStream("About.txt");
            return in == null ? null : new String(in.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException ignore) {
        }
        return null;
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
        selectDemo(addDemo("tetrahedron_demo_layout.fxml", "Tetrahedron"));
        addDemo("cube_demo_layout.fxml", "Cube");
        addDemo("human_demo_layout.fxml", "Human");
        addDemo("dog_demo_layout.fxml", "Dog");

        String aboutMessage = Objects.requireNonNullElse(getAboutMessage(), "Cannot load about message");
        about.setOnMouseClicked(e -> {
            var alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("About this program");
            alert.setHeaderText(null);
            alert.setContentText(aboutMessage);
            alert.setResizable(true);
            alert.getDialogPane().setMinWidth(600);
            alert.showAndWait();
        });
    }
}
