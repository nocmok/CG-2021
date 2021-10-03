package com.nocmok.opengl.fillclip.controller.action;

import com.nocmok.opengl.fillclip.controller.control.PixelatedCanvas;
import com.nocmok.opengl.fillclip.drawer.CircleDrawer;
import com.nocmok.opengl.fillclip.util.Rectangle;
import javafx.scene.paint.Color;

public class CircleDragHandler extends PixelatedCanvasDragHandler {

    private CircleDrawer drawer;

    private CircleDrawer cleaner;

    private CircleDrawer flusher;

    @Override public void attach(PixelatedCanvas canvas) {
        super.attach(canvas);
        this.drawer = new CircleDrawer((x, y) -> canvas.drawPixel(x, y, Color.BLACK));
        this.cleaner = new CircleDrawer((x, y) -> canvas.drawPixel(x, y, canvas.getColor(x, y)));
        this.flusher = new CircleDrawer((x, y) -> canvas.setPixel(x, y, Color.BLACK));
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

    @Override public void proceedDrag(int dragX0, int dragY0, int dragX1, int dragY1, int newDragX1, int newDragY1) {
        if (newDragX1 == dragX1 && newDragY1 == dragY1) {
            return;
        }
        drawCircle(cleaner, dragX0, dragY0, dragX1, dragY1);
        drawCircle(drawer, dragX0, dragY0, newDragX1, newDragY1);
    }

    @Override public void stopDrag(int dragX0, int dragY0, int dragX1, int dragY1, int newDragX1, int newDragY1) {
        drawCircle(flusher, dragX0, dragY0, dragX1, dragY1);
    }
}
