package com.nocmok.opengl.primitives.controller.action;

import com.nocmok.opengl.primitives.controller.control.PixelatedCanvas;
import com.nocmok.opengl.primitives.drawer.CircleDrawer;
import com.nocmok.opengl.primitives.util.Rectangle;
import javafx.scene.paint.Color;

public class CircleDragHandler extends ShapeDragHandler {

    private PixelatedCanvas canvas;

    private CircleDrawer drawer;

    private CircleDrawer cleaner;

    private CircleDrawer flusher;

    private int dragX0;

    private int dragY0;

    private int dragX1;

    private int dragY1;

    @Override public void attach(PixelatedCanvas canvas) {
        super.attach(canvas);
        this.canvas = canvas;
        this.drawer = new CircleDrawer((x, y) -> canvas.drawPixel(x, y, Color.ROYALBLUE));
        this.cleaner = new CircleDrawer((x, y) -> canvas.drawPixel(x, y, canvas.getColor(x, y)));
        this.flusher = new CircleDrawer((x, y) -> canvas.setPixel(x, y, Color.ROYALBLUE));
    }

    @Override public void startDrag(double mouseX, double mouseY) {
        dragX0 = dragX1 = canvas.toPixelX(mouseX);
        dragY0 = dragY1 = canvas.toPixelY(mouseY);
    }

    private Rectangle getCircleAreaByCapture(int x0, int y0, int x1, int y1) {
        var capture = Rectangle.ofPoints(x0, y0, x1, y1);
        var radius = (capture.w + 1) / 2;
        int circleX0 = capture.x;
        int circleY0 = y0 - radius;
        return Rectangle.squareOfSize(circleX0, circleY0, capture.w);
    }

    private void drawCircle(CircleDrawer drawer, int x0, int y0, int x1, int y1) {
        var area = getCircleAreaByCapture(x0, y0, x1, y1);
        int r = (area.w + 1) / 2;
        drawer.drawCircle(area.x + r, area.y + r, r);
    }

    @Override public void drag(double mouseX, double mouseY) {
        int newDragX1 = canvas.toPixelX(mouseX);
        int newDragY1 = canvas.toPixelY(mouseY);
        if (newDragX1 == dragX1 && newDragY1 == dragY1) {
            return;
        }

        drawCircle(cleaner, dragX0, dragY0, dragX1, dragY1);
        drawCircle(drawer, dragX0, dragY0, newDragX1, newDragY1);

        dragX1 = newDragX1;
        dragY1 = newDragY1;
    }

    @Override public void stopDrag(double mouseX, double mouseY) {
        drawCircle(flusher, dragX0, dragY0, dragX1, dragY1);
    }
}
