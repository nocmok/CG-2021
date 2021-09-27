package com.nocmok.opengl.fillclip.controller.action;

import com.nocmok.opengl.fillclip.controller.control.PixelatedCanvas;
import com.nocmok.opengl.fillclip.drawer.LineDrawer;
import javafx.scene.paint.Color;

public class LineDragHandler extends ShapeDragHandler {

    private PixelatedCanvas canvas;

    private LineDrawer drawer;

    private LineDrawer cleaner;

    private LineDrawer flusher;

    private int dragX0;

    private int dragY0;

    private int dragX1;

    private int dragY1;

    @Override public void attach(PixelatedCanvas canvas) {
        super.attach(canvas);
        this.canvas = canvas;
        this.drawer = new LineDrawer((x, y) -> canvas.drawPixel(x, y, Color.BLACK));
        this.cleaner = new LineDrawer((x, y) -> canvas.drawPixel(x, y, canvas.getColor(x, y)));
        this.flusher = new LineDrawer((x, y) -> canvas.setPixel(x, y, Color.BLACK));
    }

    @Override public void startDrag(double mouseX, double mouseY) {
        dragX0 = dragX1 = canvas.toPixelX(mouseX);
        dragY0 = dragY1 = canvas.toPixelY(mouseY);
    }

    @Override public void drag(double mouseX, double mouseY) {
        int newDragX1 = canvas.toPixelX(mouseX);
        int newDragY1 = canvas.toPixelY(mouseY);
        if (newDragX1 == dragX1 && newDragY1 == dragY1) {
            return;
        }

        cleaner.drawLine(dragX0, dragY0, dragX1, dragY1);
        drawer.drawLine(dragX0, dragY0, newDragX1, newDragY1);

        dragX1 = newDragX1;
        dragY1 = newDragY1;
    }

    @Override public void stopDrag(double mouseX, double mouseY) {
        flusher.drawLine(dragX0, dragY0, dragX1, dragY1);
    }
}
