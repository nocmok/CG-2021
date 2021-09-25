package com.nocmok.opengl.primitives.controller.action;

import com.nocmok.opengl.primitives.controller.control.PixelatedCanvas;
import com.nocmok.opengl.primitives.drawer.LineDrawer;
import com.nocmok.opengl.primitives.util.Rectangle;
import javafx.scene.paint.Color;

public class LineDragHandler extends ShapeDragHandler {

    private PixelatedCanvas canvas;

    private LineDrawer drawer;

    private int dragX0;

    private int dragY0;

    private int dragX1;

    private int dragY1;

    @Override public void attach(PixelatedCanvas canvas) {
        super.attach(canvas);
        this.canvas = canvas;
        this.drawer = new LineDrawer((x, y) -> canvas.setPixel(x, y, Color.ROYALBLUE));
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

        var areaToClear = Rectangle.ofPoints(dragX0, dragY0, dragX1, dragY1);

        canvas.fillRect(areaToClear, Color.WHITE);
        drawer.drawLine(dragX0, dragY0, newDragX1, newDragY1);

        dragX1 = newDragX1;
        dragY1 = newDragY1;
    }
}
