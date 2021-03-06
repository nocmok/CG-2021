package com.nocmok.opengl.fillclip.controller;

import com.nocmok.opengl.fillclip.controller.action.PixelatedCanvasDragHandler;
import com.nocmok.opengl.fillclip.controller.action.Zoomer;
import com.nocmok.opengl.fillclip.clipper.CohenSutherlandClipper;
import com.nocmok.opengl.fillclip.controller.control.PixelatedCanvas;
import com.nocmok.opengl.fillclip.drawer.Grid;
import com.nocmok.opengl.fillclip.drawer.LineDrawer;
import com.nocmok.opengl.fillclip.drawer.RectangleDrawer;
import com.nocmok.opengl.fillclip.util.IntPoint;
import com.nocmok.opengl.fillclip.util.Rectangle;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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
import java.util.function.Function;

public class ClippingDemoController extends AbstractController {

    @FXML
    private GridPane root;
    @FXML
    private StackPane inFrame;
    @FXML
    private StackPane outFrame;
    @FXML
    private Button zoomIn;
    @FXML
    private Button zoomOut;
    @FXML
    private Button clear;
    @FXML
    private ScrollPane inScroll;
    @FXML
    private ScrollPane outScroll;
    @FXML
    private Button about;
    @FXML
    private Button drawLineButton;
    @FXML
    private Button drawWindowButton;

    private PixelatedCanvas inCanvas;
    private PixelatedCanvas outCanvas;

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

        inCanvas = new PixelatedCanvas(pixelW, pixelH);
        outCanvas = new PixelatedCanvas(pixelW, pixelH);

        inCanvas.setWidth(w);
        inCanvas.setHeight(h);
        outCanvas.setWidth(w);
        outCanvas.setHeight(h);

        inCanvas.fillRect(0, 0, pixelW, pixelH, Color.WHITE);
        inFrame.getChildren().add(inCanvas);
        outCanvas.fillRect(0, 0, pixelW, pixelH, Color.WHITE);
        outFrame.getChildren().add(outCanvas);

        inScroll.addEventFilter(ScrollEvent.SCROLL, e -> {
            inScroll.setHvalue(inScroll.getHvalue() - e.getDeltaX() / inScroll.getWidth());
            inScroll.setVvalue(inScroll.getVvalue() - e.getDeltaY() / inScroll.getHeight());
            outScroll.setHvalue(inScroll.getHvalue() - e.getDeltaX() / inScroll.getWidth());
            outScroll.setVvalue(inScroll.getVvalue() - e.getDeltaY() / inScroll.getHeight());
            e.consume();
        });
        outScroll.addEventFilter(ScrollEvent.SCROLL, e -> {
            inScroll.setHvalue(inScroll.getHvalue() - e.getDeltaX() / inScroll.getWidth());
            inScroll.setVvalue(inScroll.getVvalue() - e.getDeltaY() / inScroll.getHeight());
            outScroll.setHvalue(inScroll.getHvalue() - e.getDeltaX() / inScroll.getWidth());
            outScroll.setVvalue(inScroll.getVvalue() - e.getDeltaY() / inScroll.getHeight());
            e.consume();
        });

        var inZoom = new Zoomer(inCanvas);
        var outZoom = new Zoomer(outCanvas);

        zoomIn.setOnMouseClicked(e -> {
            var hValue = inScroll.getHvalue();
            var vValue = inScroll.getVvalue();

            inZoom.zoomIn();
            outZoom.zoomIn();

            inScroll.setHvalue(hValue);
            inScroll.setVvalue(vValue);
            outScroll.setHvalue(hValue);
            outScroll.setVvalue(vValue);
        });
        zoomOut.setOnMouseClicked(e -> {
            var hValue = inScroll.getHvalue();
            var vValue = inScroll.getVvalue();

            inZoom.zoomOut();
            outZoom.zoomOut();

            inScroll.setHvalue(hValue);
            inScroll.setVvalue(vValue);
            outScroll.setHvalue(hValue);
            outScroll.setVvalue(vValue);
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

        var linesToClip = new ArrayList<IntPoint[]>();
        var clippingWindow = new ArrayList<Rectangle>(1);
        clippingWindow.add(Rectangle.ofPoints(0, 0, pixelW, pixelH));
        var cliper = new CohenSutherlandClipper();

        drawLineButton.setOnMouseClicked(e -> new PixelatedCanvasDragHandler() {

            LineDrawer drawer = new LineDrawer((x, y) -> inCanvas.drawPixel(x, y, Color.BLACK));
            LineDrawer cleaner = new LineDrawer((x, y) -> inCanvas.drawPixel(x, y, inCanvas.getColor(x, y)));
            ColorfulGrid grid = new ColorfulGrid(inCanvas);
            LineDrawer flusher = new LineDrawer(grid);

            @Override public void proceedDrag(int x0, int y0, int x1, int y1, int x2, int y2) {
                cleaner.drawLine(x0, y0, x1, y1);
                drawer.drawLine(x0, y0, x2, y2);
            }

            @Override public void stopDrag(int x0, int y0, int x1, int y1, int x2, int y2) {
                var newLine = new IntPoint[]{new IntPoint(x0, y0), new IntPoint(x1, y1)};
                linesToClip.add(newLine);

                var areaToClip = clippingWindow.get(0);
                var clippedLine = cliper.clip(List.<IntPoint[]>of(newLine), areaToClip);

                grid.color = getLineColor(newLine, areaToClip);
                flusher.drawLine(x0, y0, x1, y1);

                drawLinesOnCanvas(clippedLine, outCanvas);
                drawRectangleOnCanvas(areaToClip, outCanvas);
            }
        }.attach(inCanvas));

        drawWindowButton.setOnMouseClicked(e -> new PixelatedCanvasDragHandler() {

            RectangleDrawer drawer = new RectangleDrawer((x, y) -> inCanvas.drawPixel(x, y, Color.BLACK));
            RectangleDrawer cleaner = new RectangleDrawer((x, y) -> inCanvas.drawPixel(x, y, inCanvas.getColor(x, y)));
            RectangleDrawer flusher = new RectangleDrawer((x, y) -> inCanvas.setPixel(x, y, Color.BLACK));

            @Override public void startDrag(int x, int y) {
                inCanvas.fillRect(0, 0, pixelW, pixelH, Color.WHITE);
                drawLinesOnCanvas(linesToClip, inCanvas);
            }

            @Override public void proceedDrag(int x0, int y0, int x1, int y1, int x2, int y2) {
                var oldCapture = Rectangle.ofPoints(x0, y0, x1, y1);
                var newCapture = Rectangle.ofPoints(x0, y0, x2, y2);
                cleaner.drawRectangle(oldCapture.x, oldCapture.y, oldCapture.w, oldCapture.h);
                drawer.drawRectangle(newCapture.x, newCapture.y, newCapture.w, newCapture.h);
            }

            @Override public void stopDrag(int x0, int y0, int x1, int y1, int x2, int y2) {
                var capture = Rectangle.ofPoints(x0, y0, x1, y1);
                clippingWindow.set(0, capture);
                var clippedLines = cliper.clip(linesToClip, capture);

                inCanvas.fillRect(0, 0, pixelW, pixelH, Color.WHITE);
                drawAndColorLinesOnCanvas(linesToClip, l -> getLineColor(l, capture), inCanvas);

                flusher.drawRectangle(capture.x, capture.y, capture.w, capture.h);

                outCanvas.fillRect(0, 0, pixelW, pixelH, Color.WHITE);
                drawLinesOnCanvas(clippedLines, outCanvas);
                drawRectangleOnCanvas(capture, outCanvas);
            }

        }.attach(inCanvas));

        clear.setOnMouseClicked(e -> {
            linesToClip.clear();
            clippingWindow.set(0, Rectangle.ofSize(0, 0, pixelW, pixelH));
            inCanvas.fillRect(0, 0, pixelW, pixelH, Color.WHITE);
            outCanvas.fillRect(0, 0, pixelW, pixelH, Color.WHITE);
        });
    }

    private void drawLinesOnCanvas(List<IntPoint[]> linesToDraw, PixelatedCanvas canvas) {
        var lineDrawer = new LineDrawer((x, y) -> canvas.setPixel(x, y, Color.BLACK));
        for (var line : linesToDraw) {
            lineDrawer.drawLine(line[0].x, line[0].y, line[1].x, line[1].y);
        }
    }

    private void drawAndColorLinesOnCanvas(List<IntPoint[]> linesToDraw, Function<IntPoint[], Color> linesColor,
                                           PixelatedCanvas canvas) {
        var grid = new ColorfulGrid(canvas);
        var lineDrawer = new LineDrawer(grid);
        for (var line : linesToDraw) {
            grid.color = linesColor.apply(line);
            lineDrawer.drawLine(line[0].x, line[0].y, line[1].x, line[1].y);
        }
    }

    private int encodePoint(IntPoint point, Rectangle window) {
        int LEFT = 8;
        int RIGHT = 4;
        int TOP = 2;
        int BOTTOM = 1;

        int code = 0;
        if (point.x < window.x) {
            code |= LEFT;
        } else if (point.x > window.x2()) {
            code |= RIGHT;
        }
        if (point.y < window.y) {
            code |= TOP;
        } else if (point.y > window.y2()) {
            code |= BOTTOM;
        }
        return code;
    }

    private Color getLineColor(IntPoint[] line, Rectangle window) {
        if (encodePoint(line[0], window) == 0 && encodePoint(line[1], window) == 0) {
            return Color.valueOf("#00CA4E");
        }
        if ((encodePoint(line[0], window) & encodePoint(line[1], window)) != 0) {
            return Color.valueOf("#FF605C");
        }
        return Color.valueOf("#FFBD44");
    }

    private void drawRectangleOnCanvas(Rectangle rectangle, PixelatedCanvas canvas) {
        new RectangleDrawer((x, y) -> canvas.setPixel(x, y, Color.BLACK))
                .drawRectangle(rectangle.x, rectangle.y, rectangle.w, rectangle.h);
    }

    private static class ColorfulGrid implements Grid {

        PixelatedCanvas canvas;

        Color color;

        ColorfulGrid(PixelatedCanvas canvas) {
            this.canvas = canvas;
        }

        @Override public void set(int x, int y) {
            canvas.setPixel(x, y, color);
        }
    }
}
