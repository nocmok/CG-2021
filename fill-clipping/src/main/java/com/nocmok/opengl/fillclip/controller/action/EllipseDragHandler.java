package com.nocmok.opengl.fillclip.controller.action;

import com.nocmok.opengl.fillclip.controller.control.PixelatedCanvas;
import com.nocmok.opengl.fillclip.drawer.EllipseDrawer;
import com.nocmok.opengl.fillclip.util.Rectangle;
import javafx.scene.paint.Color;

public class EllipseDragHandler extends PixelatedCanvasDragHandler {

    private EllipseDrawer drawer;

    private EllipseDrawer cleaner;

    private EllipseDrawer flusher;

    @Override public void attach(PixelatedCanvas canvas) {
        super.attach(canvas);
        this.drawer = new EllipseDrawer((x, y) -> canvas.drawPixel(x, y, Color.BLACK));
        this.cleaner = new EllipseDrawer((x, y) -> canvas.drawPixel(x, y, canvas.getColor(x, y)));
        this.flusher = new EllipseDrawer((x, y) -> canvas.setPixel(x, y, Color.BLACK));
    }

    private void drawEllipse(EllipseDrawer drawer, int cx0, int cy0, int cx1, int cy1) {
        var capture = Rectangle.ofPoints(cx0, cy0, cx1, cy1);

        int xr = (capture.w) / 2;
        int yr = (capture.h) / 2;
        int x0 = capture.x + xr;
        int y0 = capture.y + yr;

        drawer.drawEllipse(x0, y0, xr, yr);
    }

    @Override public void proceedDrag(int dragX0, int dragY0, int dragX1, int dragY1, int newDragX1, int newDragY1) {
        if (newDragX1 == dragX1 && newDragY1 == dragY1) {
            return;
        }

        drawEllipse(cleaner, dragX0, dragY0, dragX1, dragY1);
        drawEllipse(drawer, dragX0, dragY0, newDragX1, newDragY1);
    }

    @Override public void stopDrag(int dragX0, int dragY0, int dragX1, int dragY1, int newDragX1, int newDragY1) {
        drawEllipse(flusher, dragX0, dragY0, dragX1, dragY1);
    }
}
