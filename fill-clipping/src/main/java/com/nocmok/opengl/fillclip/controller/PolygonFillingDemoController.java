package com.nocmok.opengl.fillclip.controller;

import com.nocmok.opengl.fillclip.controller.action.Zoomer;
import com.nocmok.opengl.fillclip.controller.control.PixelatedCanvas;
import com.nocmok.opengl.fillclip.drawer.LineDrawer;
import com.nocmok.opengl.fillclip.filler.PolygonFiller;
import com.nocmok.opengl.fillclip.util.IntPoint;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class PolygonFillingDemoController extends AbstractController {


    @FXML
    private GridPane root;
    @FXML
    private StackPane myFrame;
    @FXML
    private Button zoomIn;
    @FXML
    private Button zoomOut;
    @FXML
    private Button clear;
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

        var points = new ArrayList<IntPoint>();

        clear.setOnMouseClicked(e -> {
            myCanvas.fillRect(0, 0, pixelW, pixelH, Color.WHITE);
            points.clear();
        });

        colorPicker.setValue(Color.ROYALBLUE);
        colorPicker.setOnAction(ee -> {
            myCanvas.fillRect(0, 0, pixelW, pixelH, Color.WHITE);
            drawFilledPolygon(points);
        });

        myCanvas.setOnMouseClicked(e -> {
            myCanvas.fillRect(0, 0, pixelW, pixelH, Color.WHITE);
            points.add(new IntPoint(myCanvas.toPixelX(e.getX()), myCanvas.toPixelY(e.getY())));
            drawFilledPolygon(points);
        });

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

    private void drawFilledPolygon(List<IntPoint> points) {
        var polygonFiller = new PolygonFiller((x, y) -> myCanvas.setPixel(x, y, colorPicker.getValue()));
        var lineDrawer = new LineDrawer((x, y) -> myCanvas.setPixel(x, y, Color.BLACK));
        if (points.size() >= 2) {
            if (points.size() > 2) {
                polygonFiller.fill(points);
            }
            // draw lines
            var it = points.iterator();
            var p0 = it.next();
            while (it.hasNext()) {
                var p1 = it.next();
                lineDrawer.drawLine(p0.x, p0.y, p1.x, p1.y);
                p0 = p1;
            }
            lineDrawer.drawLine(p0.x, p0.y, points.get(0).x, points.get(0).y);

        } else {
            // draw point
            for (var point : points) {
                lineDrawer.drawLine(point.x, point.y, point.x, point.y);
            }
        }
    }
}
