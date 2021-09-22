package com.nocmok.opengl.primitives.controller;

import com.nocmok.opengl.primitives.controller.action.CircleDragHandler;
import com.nocmok.opengl.primitives.controller.action.EllipseDragHandler;
import com.nocmok.opengl.primitives.controller.action.G2CircleDragHandler;
import com.nocmok.opengl.primitives.controller.action.G2EllipseDragHandler;
import com.nocmok.opengl.primitives.controller.action.G2LineDragHandler;
import com.nocmok.opengl.primitives.controller.action.LineDragHandler;
import com.nocmok.opengl.primitives.controller.action.ShapeDragHandler;
import com.nocmok.opengl.primitives.controller.action.Zoomer;
import com.nocmok.opengl.primitives.controller.control.PixelatedCanvas;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainSceneController extends AbstractController {

    @FXML
    private GridPane root;
    @FXML
    private StackPane myFrame;
    @FXML
    private StackPane g2Frame;
    @FXML
    private HBox header;
    @FXML
    private Button zoomIn;
    @FXML
    private Button zoomOut;
    @FXML
    private ScrollPane g2Scroll;
    @FXML
    private ScrollPane myScroll;
    @FXML
    private Button about;
    private PixelatedCanvas g2Canvas;
    private PixelatedCanvas myCanvas;

    @Override public Parent getRoot() {
        return root;
    }

    private void addDrawerButton(String name, ShapeDragHandler customHandler, ShapeDragHandler g2Handler) {
        var button = new Button();
        button.setText(name);

        button.setOnMouseClicked(ee -> {
            customHandler.attach(myCanvas);
            g2Handler.attach(g2Canvas);

            myCanvas.setOnMousePressed(e -> {
                customHandler.startDrag(e.getX(), e.getY());
                g2Handler.startDrag(e.getX(), e.getY());
            });

            myCanvas.setOnMouseDragged(e -> {
                customHandler.drag(e.getX(), e.getY());
                g2Handler.drag(e.getX(), e.getY());
            });
        });

        header.getChildren().add(button);
    }

    private String getAboutMessage() {
        try {
            var in = getClass().getClassLoader().getResourceAsStream("About.txt");
            return in == null ? null : new String(in.readAllBytes(), StandardCharsets.US_ASCII);
        } catch (IOException ignore) {
        }
        return null;
    }

    @Override public void initialize(URL url, ResourceBundle resourceBundle) {

        int pixelSize = 4;
        var screen = Screen.getPrimary().getBounds();

        double h = ((int) screen.getHeight()) - ((int) screen.getHeight()) % pixelSize;
        double w = ((int) screen.getWidth()) - ((int) screen.getWidth()) % pixelSize;

        int pixelH = (int) (h / pixelSize);
        int pixelW = (int) (w / pixelSize);

        g2Canvas = new PixelatedCanvas(pixelW, pixelH);
        myCanvas = new PixelatedCanvas(pixelW, pixelH);

        g2Canvas.setWidth(w);
        g2Canvas.setHeight(h);

        myCanvas.setWidth(w);
        myCanvas.setHeight(h);

        g2Canvas.createGraphicsWrapper().flush();
        myCanvas.createGraphicsWrapper().flush();

        myFrame.getChildren().add(myCanvas);
        g2Frame.getChildren().add(g2Canvas);

        addDrawerButton("Line", new LineDragHandler(), new G2LineDragHandler());
        addDrawerButton("Circle", new CircleDragHandler(), new G2CircleDragHandler());
        addDrawerButton("Ellipse", new EllipseDragHandler(), new G2EllipseDragHandler());

        g2Scroll.addEventFilter(ScrollEvent.SCROLL, e -> {
            myScroll.setHvalue(myScroll.getHvalue() - e.getDeltaX() / myScroll.getWidth());
            g2Scroll.setHvalue(g2Scroll.getHvalue() - e.getDeltaX() / g2Scroll.getWidth());
            myScroll.setVvalue(myScroll.getVvalue() - e.getDeltaY() / myScroll.getHeight());
            g2Scroll.setVvalue(g2Scroll.getVvalue() - e.getDeltaY() / g2Scroll.getHeight());
            e.consume();
        });
        myScroll.addEventFilter(ScrollEvent.SCROLL, e -> {
            myScroll.setHvalue(myScroll.getHvalue() - e.getDeltaX() / myScroll.getWidth());
            g2Scroll.setHvalue(g2Scroll.getHvalue() - e.getDeltaX() / g2Scroll.getWidth());
            myScroll.setVvalue(myScroll.getVvalue() - e.getDeltaY() / myScroll.getHeight());
            g2Scroll.setVvalue(g2Scroll.getVvalue() - e.getDeltaY() / g2Scroll.getHeight());
            e.consume();
        });

        var myZoom = new Zoomer(myCanvas);
        var g2Zoom = new Zoomer(g2Canvas);

        zoomIn.setOnMouseClicked(e -> {
            var myHvalue = myScroll.getHvalue();
            var myVvalue = myScroll.getVvalue();

            myZoom.zoomIn();
            g2Zoom.zoomIn();

            myScroll.setHvalue(myHvalue);
            myScroll.setVvalue(myVvalue);
            g2Scroll.setHvalue(myHvalue);
            g2Scroll.setVvalue(myVvalue);
        });
        zoomOut.setOnMouseClicked(e -> {
            var myHvalue = myScroll.getHvalue();
            var myVvalue = myScroll.getVvalue();

            myZoom.zoomOut();
            g2Zoom.zoomOut();

            myScroll.setHvalue(myHvalue);
            myScroll.setVvalue(myVvalue);
            g2Scroll.setHvalue(myHvalue);
            g2Scroll.setVvalue(myVvalue);
        });

        String aboutMessage = Objects.requireNonNullElse(getAboutMessage(), "Cannot load about message");
        about.setOnMouseClicked(e -> {
            var alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("About this program");
            alert.setHeaderText("About this program");
            alert.setContentText(aboutMessage);
            alert.showAndWait();
        });
    }
}
