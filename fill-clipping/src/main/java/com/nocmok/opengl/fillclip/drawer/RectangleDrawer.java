package com.nocmok.opengl.fillclip.drawer;

public class RectangleDrawer {

    private Grid grid;

    public RectangleDrawer(Grid grid) {
        this.grid = grid;
    }

    public void drawRectangle(int x0, int y0, int w, int h) {
        for (int x = x0; x < x0 + w; ++x) {
            grid.set(x, y0);
            grid.set(x, y0 + h - 1);
        }
        for (int y = y0; y < y0 + h; ++y) {
            grid.set(x0, y);
            grid.set(x0 + w - 1, y);
        }
    }
}
