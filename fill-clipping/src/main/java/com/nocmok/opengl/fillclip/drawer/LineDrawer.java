package com.nocmok.opengl.fillclip.drawer;

public class LineDrawer {

    private Grid grid;

    public LineDrawer(Grid grid) {
        this.grid = grid;
    }

    public void drawLine(int x0, int y0, int x1, int y1) {

        int deltaX = Math.abs(x1 - x0);
        int deltaY = Math.abs(y1 - y0);

        int error = 0;

        int x = x0;
        int y = y0;

        int incY = 0;
        int incX = 0;

        int deltaError;
        int errorThreshold;

        int xDir = 0; // x adjustment direction in order to fix error
        int yDir = 0; // y adjustment direction in order to fix error

        if (deltaX >= deltaY) {
            yDir = Integer.compare(y1, y0);
            incX = Integer.compare(x1, x0);
            deltaError = deltaY;
            errorThreshold = deltaX;
        } else {
            xDir = Integer.compare(x1, x0);
            incY = Integer.compare(y1, y0);
            deltaError = deltaX;
            errorThreshold = deltaY;
        }

        for (int i = 0; i <= Integer.max(deltaX, deltaY); ++i) {
            grid.set(x, y);
            error += deltaError;
            if (2 * error >= errorThreshold) {
                y += yDir;
                x += xDir;
                error -= errorThreshold;
            }
            x += incX;
            y += incY;
        }
    }
}
