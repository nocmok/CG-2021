package com.nocmok.opengl.curve.controller;

import com.nocmok.opengl.curve.controller.action.BezierCurvePivotHandler;
import com.nocmok.opengl.curve.controller.control.Pivot;
import com.nocmok.opengl.curve.controller.control.PixelatedCanvas;
import com.nocmok.opengl.curve.curve_drawer.BezierCurve;
import com.nocmok.opengl.curve.curve_drawer.BezierSpline3;
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

public class BezierCurveController extends AbstractController {

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

    /**
     * Откладывает точку p2 на продолжении прямой (p0, p1)
     * таким образом, что p0 - начальная точка p2 - конечная, а p1 - находится в центре прямой
     */
    private Point computeClosure(Point p0, Point p1) {
        // точка p1 - посередине
        // точка p0 - начальная
        double x = p1.x + (p1.x - p0.x);
        double y = p1.y + (p1.y - p0.y);
        return new Point(x, y);
    }

    @Override public void initialize(URL url, ResourceBundle resourceBundle) {

        int pixelSize = 2;
        var screen = Screen.getPrimary().getBounds();

        double h = ((int) screen.getHeight()) - ((int) screen.getHeight()) % pixelSize;
        double w = ((int) screen.getWidth()) - ((int) screen.getWidth()) % pixelSize;

        int pixelH = (int) (h / pixelSize);
        int pixelW = (int) (w / pixelSize);

        canvas = new PixelatedCanvas(pixelW, pixelH);

        canvas.setWidth(w);
        canvas.setHeight(h);

        canvas.clear(Color.WHITE);
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

        double step = 1e-2;
        var bezierCurve = new BezierCurve((x, y) -> canvas.setPixel((int) x, (int) y, Color.ROYALBLUE), step);
        var closureDrawer = new BezierSpline3((x, y) -> canvas.setPixel((int) x, (int) y, Color.GREEN), step);
        var linearInterpolation = new LinearCurve((x, y) -> canvas.setPixel((int) x, (int) y, Color.LIGHTGRAY));

        var pivotsHandler = new BezierCurvePivotHandler() {
            @Override public void onPivotsChange(Collection<Pivot> pivots) {
                canvas.clear(Color.WHITE);
                var points = pivots.stream()
                        .map(p -> new Point(canvas.toPixelX(p.x()), canvas.toPixelY(p.y())))
                        .collect(Collectors.toList());
                linearInterpolation.drawCurve(points);
                bezierCurve.drawCurve(points);

                // добавляем замыкание
//                if(points.size() > 2) {
//                    // взять первые две и последние две точки
//
//                    // вычислить еще две опорные точки
//                    // взять вычисленные две, первую и последнюю точку
//
//                    // построить сплайн на полученных 4 точках
//
//                    var closurePivots = List.of(points.get(0),
//                            computeClosure(points.get(1), points.get(0)),
//                            computeClosure(points.get(points.size() - 2), points.get(points.size() - 1)),
//                            points.get(points.size() - 1));
//
//                    closureDrawer.drawCurve(closurePivots);
//                }
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

