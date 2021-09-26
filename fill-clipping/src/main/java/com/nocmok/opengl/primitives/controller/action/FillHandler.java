package com.nocmok.opengl.primitives.controller.action;

import com.nocmok.opengl.primitives.controller.control.PixelatedCanvas;
import com.nocmok.opengl.primitives.filler.Grid;
import com.nocmok.opengl.primitives.filler.LineByLineFiller;
import javafx.scene.paint.Color;

public class FillHandler {

    private PixelatedCanvas canvas;

    private LineByLineFiller filler;

    public FillHandler(PixelatedCanvas canvas, Color color) {
        this.canvas = canvas;
        this.filler = new LineByLineFiller(new PixelatedCanvasGrid(canvas, color));
    }

    public void fill(double mouseX, double mouseY) {
        filler.fill(canvas.toPixelX(mouseX), canvas.toPixelY(mouseY));
    }

    private static class PixelatedCanvasGrid implements Grid {

        private PixelatedCanvas canvas;

        private Color color;

        public PixelatedCanvasGrid(PixelatedCanvas canvas, Color color) {
            this.canvas = canvas;
            this.color = color;
        }

        @Override public void set(int x, int y) {
            canvas.setPixel(x, y, color);
        }

        @Override public int get(int x, int y) {
            return canvas.getRGB(x, y);
        }

        @Override public int xSize() {
            return canvas.getxPixels();
        }

        @Override public int ySize() {
            return canvas.getyPixels();
        }
    }
}
