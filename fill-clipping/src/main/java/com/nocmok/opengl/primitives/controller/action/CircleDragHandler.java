package com.nocmok.opengl.primitives.controller.action;

import com.nocmok.opengl.primitives.controller.control.PixelatedCanvas;
import com.nocmok.opengl.primitives.drawer.CircleDrawer;
import com.nocmok.opengl.primitives.util.Rectangle;

import java.awt.Color;

public class CircleDragHandler extends ShapeDragHandler {

    private PixelatedCanvas canvas;

    private PixelatedCanvas.Graphics2DWrapper g2;

    private CircleDrawer drawer;

    private int dragX0;

    private int dragY0;

    private int dragX1;

    private int dragY1;

    @Override public void attach(PixelatedCanvas canvas) {
        super.attach(canvas);
        this.canvas = canvas;
        this.g2 = canvas.createGraphicsWrapper();
        this.drawer = new CircleDrawer((x, y) -> g2.drawPixel(x, y, Color.BLACK));
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

    @Override public void drag(double mouseX, double mouseY) {
        int newDragX1 = canvas.toPixelX(mouseX);
        int newDragY1 = canvas.toPixelY(mouseY);
        if (newDragX1 == dragX1 && newDragY1 == dragY1) {
            return;
        }

        var oldCircleArea = getCircleAreaByCapture(dragX0, dragY0, dragX1, dragY1);
        var newCircleArea = getCircleAreaByCapture(dragX0, dragY0, newDragX1, newDragY1);

        var oldActualArea = Rectangle.squareOfSize(oldCircleArea.x, oldCircleArea.y, oldCircleArea.w + 2);
        var newActualArea = Rectangle.squareOfSize(newCircleArea.x, newCircleArea.y, newCircleArea.w + 2);

        g2.fillRect(oldActualArea, Color.WHITE);
        int r = (newCircleArea.w + 1) / 2;
        drawer.drawCircle(newCircleArea.x + r, newCircleArea.y + r, r);
        g2.flushRect(oldActualArea.add(newActualArea));

        dragX1 = newDragX1;
        dragY1 = newDragY1;
    }
}
