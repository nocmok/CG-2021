package com.nocmok.opengl.fillclip.controller.action;

import com.nocmok.opengl.fillclip.controller.control.PixelatedCanvas;
import com.nocmok.opengl.fillclip.drawer.LineDrawer;
import javafx.scene.paint.Color;

public class LineDragHandler extends PixelatedCanvasDragHandler {

    private LineDrawer drawer;

    private LineDrawer cleaner;

    private LineDrawer flusher;

    @Override public void attach(PixelatedCanvas canvas) {
        super.attach(canvas);

        this.drawer = new LineDrawer((x, y) -> canvas.drawPixel(x, y, Color.BLACK));
        this.cleaner = new LineDrawer((x, y) -> canvas.drawPixel(x, y, canvas.getColor(x, y)));
        this.flusher = new LineDrawer((x, y) -> canvas.setPixel(x, y, Color.BLACK));
    }


    @Override public void proceedDrag(int dragX0, int dragY0, int dragX1, int dragY1, int newDragX1, int newDragY1) {
        if (newDragX1 == dragX1 && newDragY1 == dragY1) {
            return;
        }

        cleaner.drawLine(dragX0, dragY0, dragX1, dragY1);
        drawer.drawLine(dragX0, dragY0, newDragX1, newDragY1);
    }

    @Override public void stopDrag(int dragX0, int dragY0, int dragX1, int dragY1, int newDragX1, int newDragY1) {
        flusher.drawLine(dragX0, dragY0, dragX1, dragY1);
    }
}
