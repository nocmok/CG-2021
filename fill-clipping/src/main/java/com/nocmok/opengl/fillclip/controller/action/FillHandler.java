package com.nocmok.opengl.fillclip.controller.action;

import com.nocmok.opengl.fillclip.controller.control.PixelatedCanvas;
import com.nocmok.opengl.fillclip.filler.Grid;
import com.nocmok.opengl.fillclip.filler.DfsFiller;
import javafx.scene.paint.Color;

public class FillHandler {

    private PixelatedCanvas canvas;

    private DfsFiller filler;

    public FillHandler(PixelatedCanvas canvas, Color color) {
        this.canvas = canvas;
        this.filler = new DfsFiller(new PixelatedCanvasGrid(canvas, color));
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
