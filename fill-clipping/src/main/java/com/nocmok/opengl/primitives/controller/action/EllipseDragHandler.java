package com.nocmok.opengl.primitives.controller.action;

import com.nocmok.opengl.primitives.controller.control.PixelatedCanvas;
import com.nocmok.opengl.primitives.drawer.EllipseDrawer;
import com.nocmok.opengl.primitives.util.Rectangle;
import javafx.scene.paint.Color;

public class EllipseDragHandler extends ShapeDragHandler {

    private PixelatedCanvas canvas;

    private EllipseDrawer drawer;

    private EllipseDrawer cleaner;

    private EllipseDrawer flusher;

    private int dragX0;

    private int dragY0;

    private int dragX1;

    private int dragY1;

    @Override public void attach(PixelatedCanvas canvas) {
        super.attach(canvas);
        this.canvas = canvas;
        this.drawer = new EllipseDrawer((x, y) -> canvas.drawPixel(x, y, Color.BLACK));
        this.cleaner = new EllipseDrawer((x, y) -> canvas.drawPixel(x, y, canvas.getColor(x, y)));
        this.flusher = new EllipseDrawer((x, y) -> canvas.setPixel(x, y, Color.BLACK));
    }

    @Override public void startDrag(double mouseX, double mouseY) {
        dragX0 = dragX1 = canvas.toPixelX(mouseX);
        dragY0 = dragY1 = canvas.toPixelY(mouseY);
    }

    private void drawEllipse(EllipseDrawer drawer, int cx0, int cy0, int cx1, int cy1) {
        var capture = Rectangle.ofPoints(cx0, cy0, cx1, cy1);

        int xr = (capture.w) / 2;
        int yr = (capture.h) / 2;
        int x0 = capture.x + xr;
        int y0 = capture.y + yr;

        drawer.drawEllipse(x0, y0, xr, yr);
    }

    @Override public void drag(double mouseX, double mouseY) {
        int newDragX1 = canvas.toPixelX(mouseX);
        int newDragY1 = canvas.toPixelY(mouseY);
        if (newDragX1 == dragX1 && newDragY1 == dragY1) {
            return;
        }

        drawEllipse(cleaner, dragX0, dragY0, dragX1, dragY1);
        drawEllipse(drawer, dragX0, dragY0, newDragX1, newDragY1);

        dragX1 = newDragX1;
        dragY1 = newDragY1;
    }

    @Override public void stopDrag(double mouseX, double mouseY) {
        drawEllipse(flusher, dragX0, dragY0, dragX1, dragY1);
    }
}
