package com.nocmok.opengl.primitives.controller.action;

import com.nocmok.opengl.primitives.controller.control.PixelatedCanvas;
import com.nocmok.opengl.primitives.drawer.EllipseDrawer;
import com.nocmok.opengl.primitives.util.Rectangle;

import java.awt.Color;

public class EllipseDragHandler extends ShapeDragHandler {

    private PixelatedCanvas canvas;

    private PixelatedCanvas.Graphics2DWrapper g2;

    private EllipseDrawer drawer;

    private int dragX0;

    private int dragY0;

    private int dragX1;

    private int dragY1;

    @Override public void attach(PixelatedCanvas canvas) {
        super.attach(canvas);
        this.canvas = canvas;
        this.g2 = canvas.createGraphicsWrapper();
        this.drawer = new EllipseDrawer((x, y) -> g2.drawPixel(x, y, Color.BLACK));
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

        var oldCapture = Rectangle.ofPoints(dragX0, dragY0, dragX1, dragY1);
        var newCapture = Rectangle.ofPoints(dragX0, dragY0, newDragX1, newDragY1);

        var oldActualArea = Rectangle.ofSize(oldCapture.x, oldCapture.y, oldCapture.w + 2, oldCapture.h + 2);
        var newActualArea = Rectangle.ofSize(newCapture.x, newCapture.y, newCapture.w + 2, newCapture.h + 2);

        g2.fillRect(oldActualArea, Color.WHITE);

        int xr = (newCapture.w + 1) / 2;
        int yr = (newCapture.h + 1) / 2;
        int x0 = newCapture.x + xr;
        int y0 = newCapture.y + yr;

        drawer.drawEllipse(x0, y0, xr, yr);
        g2.flushRect(oldActualArea.add(newActualArea));

        dragX1 = newDragX1;
        dragY1 = newDragY1;
    }
}
