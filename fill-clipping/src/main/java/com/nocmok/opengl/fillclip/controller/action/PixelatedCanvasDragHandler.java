package com.nocmok.opengl.fillclip.controller.action;

import com.nocmok.opengl.fillclip.controller.control.PixelatedCanvas;

public abstract class PixelatedCanvasDragHandler {


    public PixelatedCanvasDragHandler() {

    }

    public void attach(PixelatedCanvas canvas) {
        new DragHandler() {
            @Override public void startDrag(double x, double y) {
                PixelatedCanvasDragHandler.this.startDrag(canvas.toPixelX(x), canvas.toPixelY(y));
            }

            @Override public void proceedDrag(double x0, double y0, double x1, double y1, double x2, double y2) {
                PixelatedCanvasDragHandler.this.proceedDrag(
                        canvas.toPixelX(x0),
                        canvas.toPixelY(y0),
                        canvas.toPixelX(x1),
                        canvas.toPixelY(y1),
                        canvas.toPixelX(x2),
                        canvas.toPixelY(y2)
                );
            }

            @Override public void stopDrag(double x0, double y0, double x1, double y1, double x2, double y2) {
                PixelatedCanvasDragHandler.this.stopDrag(
                        canvas.toPixelX(x0),
                        canvas.toPixelY(y0),
                        canvas.toPixelX(x1),
                        canvas.toPixelY(y1),
                        canvas.toPixelX(x2),
                        canvas.toPixelY(y2)
                );
            }
        }.attach(canvas);
    }

    public void startDrag(int x, int y) {
    }

    public void proceedDrag(int x0, int y0, int x1, int y1, int x2, int y2) {
    }

    public void stopDrag(int x0, int y0, int x1, int y1, int x2, int y2) {
    }
}
