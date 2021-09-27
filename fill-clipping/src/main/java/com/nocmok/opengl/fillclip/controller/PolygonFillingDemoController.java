package com.nocmok.opengl.fillclip.controller;

import com.nocmok.opengl.fillclip.controller.action.CircleDragHandler;
import com.nocmok.opengl.fillclip.controller.action.EllipseDragHandler;
import com.nocmok.opengl.fillclip.controller.action.FillHandler;
import com.nocmok.opengl.fillclip.controller.action.LineDragHandler;
import com.nocmok.opengl.fillclip.controller.action.ShapeDragHandler;
import com.nocmok.opengl.fillclip.controller.action.Zoomer;
import com.nocmok.opengl.fillclip.controller.control.PixelatedCanvas;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.ResourceBundle;

public class PolygonFillingDemoController extends AbstractController {


    @FXML
    private GridPane root;
    @FXML
    private StackPane myFrame;
    @FXML
    private HBox header;
    @FXML
    private Button zoomIn;
    @FXML
    private Button zoomOut;
    @FXML
    private Button clear;
    @FXML
    private Button fill;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private ScrollPane myScroll;
    @FXML
    private Button about;
    private PixelatedCanvas myCanvas;

    @Override public Parent getRoot() {
        return root;
    }

    private void setCurrentDragHandlers(ShapeDragHandler myHandler) {
        myHandler.attach(myCanvas);

        myCanvas.setOnMousePressed(e -> {
            myHandler.startDrag(e.getX(), e.getY());
        });

        myCanvas.setOnMouseDragged(e -> {
            myHandler.drag(e.getX(), e.getY());
        });

        myCanvas.setOnMouseReleased(e -> {
            myHandler.stopDrag(e.getX(), e.getY());
        });
    }

    private void setCurrentFiller(FillHandler filler) {
        myCanvas.setOnMousePressed(e -> {
            filler.fill(e.getX(), e.getY());
        });
        myCanvas.setOnMouseDragged(null);
        myCanvas.setOnMouseReleased(null);
    }

    private Button addDrawerButton(String name, ShapeDragHandler myHandler) {
        var button = new Button();
        button.setText(name);

        button.setOnMouseClicked(ee -> {
            setCurrentDragHandlers(myHandler);
        });

        header.getChildren().add(button);
        return button;
    }

    private String getAboutMessage() {
        try {
            var in = getClass().getClassLoader().getResourceAsStream("About.txt");
            return in == null ? null : new String(in.readAllBytes(), StandardCharsets.UTF_8);
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

        myCanvas = new PixelatedCanvas(pixelW, pixelH);

        myCanvas.setWidth(w);
        myCanvas.setHeight(h);

        myCanvas.fillRect(0, 0, pixelW, pixelH, Color.WHITE);
        myFrame.getChildren().add(myCanvas);

        addDrawerButton("Line", new LineDragHandler());
        addDrawerButton("Circle", new CircleDragHandler());
        addDrawerButton("Ellipse", new EllipseDragHandler());

        setCurrentDragHandlers(new LineDragHandler());

        myScroll.addEventFilter(ScrollEvent.SCROLL, e -> {
            myScroll.setHvalue(myScroll.getHvalue() - e.getDeltaX() / myScroll.getWidth());
            myScroll.setVvalue(myScroll.getVvalue() - e.getDeltaY() / myScroll.getHeight());
            e.consume();
        });

        var myZoom = new Zoomer(myCanvas);

        zoomIn.setOnMouseClicked(e -> {
            var myHvalue = myScroll.getHvalue();
            var myVvalue = myScroll.getVvalue();

            myZoom.zoomIn();

            myScroll.setHvalue(myHvalue);
            myScroll.setVvalue(myVvalue);
        });
        zoomOut.setOnMouseClicked(e -> {
            var myHvalue = myScroll.getHvalue();
            var myVvalue = myScroll.getVvalue();

            myZoom.zoomOut();

            myScroll.setHvalue(myHvalue);
            myScroll.setVvalue(myVvalue);
        });

        clear.setOnMouseClicked(e -> {
            myCanvas.fillRect(0, 0, pixelW, pixelH, Color.WHITE);
        });

        fill.setOnMouseClicked(ee -> {
            setCurrentFiller(new FillHandler(myCanvas, colorPicker.getValue()));
        });

        colorPicker.setOnAction(ee -> {
            setCurrentFiller(new FillHandler(myCanvas, colorPicker.getValue()));
        });

        String aboutMessage = Objects.requireNonNullElse(getAboutMessage(), "Cannot load about message");
        about.setOnMouseClicked(e -> {
            var alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("About this program");
            alert.setHeaderText(null);
            alert.setContentText(aboutMessage);
            alert.showAndWait();
        });
    }
}
