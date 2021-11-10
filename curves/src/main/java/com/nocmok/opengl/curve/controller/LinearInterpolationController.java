package com.nocmok.opengl.curve.controller;

import com.nocmok.opengl.curve.controller.action.AddPivotHandler;
import com.nocmok.opengl.curve.controller.control.Pivot;
import com.nocmok.opengl.curve.controller.control.PixelatedCanvas;
import com.nocmok.opengl.curve.curve_drawer.LinearCurve;
import com.nocmok.opengl.curve.util.Point;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class LinearInterpolationController extends AbstractController {

    @FXML
    private GridPane root;
    @FXML
    private Pane frame;
    @FXML
    private HBox header;
    @FXML
    private Button clear;
    @FXML
    private ScrollPane scroll;
    @FXML
    private Button about;
    private PixelatedCanvas canvas;

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

        int pixelSize = 1;
        var screen = Screen.getPrimary().getBounds();

        double h = ((int) screen.getHeight()) - ((int) screen.getHeight()) % pixelSize;
        double w = ((int) screen.getWidth()) - ((int) screen.getWidth()) % pixelSize;

        int pixelH = (int) (h / pixelSize);
        int pixelW = (int) (w / pixelSize);

        canvas = new PixelatedCanvas(pixelW, pixelH);

        canvas.setWidth(w);
        canvas.setHeight(h);

        canvas.fillRect(0, 0, pixelW, pixelH, Color.WHITE);
        frame.getChildren().add(canvas);

        scroll.addEventFilter(ScrollEvent.SCROLL, e -> {
            scroll.setHvalue(scroll.getHvalue() - e.getDeltaX() / scroll.getWidth());
            scroll.setVvalue(scroll.getVvalue() - e.getDeltaY() / scroll.getHeight());
            e.consume();
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

        var linearInterpolation = new LinearCurve((x, y) -> canvas.setPixel((int) x, (int) y, Color.ROYALBLUE));
        var pivotsHandler = new AddPivotHandler() {
            @Override public void onPivotsChange(Collection<Pivot> pivots) {
                canvas.clear(Color.WHITE);
                linearInterpolation.drawCurve(
                        pivots.stream()
                                .map(p -> new Point(canvas.toPixelX(p.x()), canvas.toPixelY(p.y())))
                                .collect(Collectors.toList()));
            }
        };
        pivotsHandler.attach(frame);

        clear.setOnMouseClicked(e -> {
            canvas.fillRect(0, 0, pixelW, pixelH, Color.WHITE);
            pivotsHandler.getPivots().clear();
            frame.getChildren().clear();
            frame.getChildren().add(canvas);
        });
    }
}
