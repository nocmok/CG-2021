package com.nocmok.opengl.primitives.controller.action;

import com.nocmok.opengl.primitives.controller.control.PixelatedCanvas;
import com.nocmok.opengl.primitives.drawer.LineDrawer;
import com.nocmok.opengl.primitives.util.Rectangle;

import java.awt.Color;

public class LineDragHandler extends ShapeDragHandler {

    private PixelatedCanvas canvas;

    private LineDrawer drawer;

    private PixelatedCanvas.Graphics2DWrapper g2;

    private int dragX0;

    private int dragY0;

    private int dragX1;

    private int dragY1;

    @Override public void attach(PixelatedCanvas canvas) {
        super.attach(canvas);
        this.canvas = canvas;
        this.g2 = canvas.createGraphicsWrapper();
        this.drawer = new LineDrawer((x, y) -> g2.drawPixel(x, y, Color.ORANGE));
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
        g2.fillRect(areaToClear, Color.WHITE);
        drawer.drawLine(dragX0, dragY0, newDragX1, newDragY1);
        var newShapeArea = Rectangle.ofPoints(dragX0, dragY0, newDragX1, newDragY1);
        g2.flushRect(areaToClear.add(newShapeArea));
        dragX1 = newDragX1;
        dragY1 = newDragY1;
    }
}
