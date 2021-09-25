package com.nocmok.opengl.primitives.drawer;

public class CircleDrawer {

    private Grid grid;

    public CircleDrawer(Grid grid) {
        this.grid = grid;
    }

    public void drawCircle(int x0, int y0, int r) {
        int x = 0;
        int y = -r;
        int d = 0;

        while (y <= 0) {
            grid.set(x0 + x, y0 + y);
            grid.set(x0 + x, y0 - y);
            grid.set(x0 - x, y0 - y);
            grid.set(x0 - x, y0 + y);

            int dd = d + 2 * x + 2 * y + 2;

            if (dd < 0) {
                // inside
                int dh = d + 2 * x + 1;
                if (Math.abs(dd) < Math.abs(dh)) {
                    x += 1;
                    y += 1;
                    d = dd;
                } else {
                    x += 1;
                    d = dh;
                }
            } else if (dd > 0) {
                // outside
                int dv = d + 2 * y + 1;
                if (Math.abs(dd) < Math.abs(dv)) {
                    x += 1;
                    y += 1;
                    d = dd;
                } else {
                    y += 1;
                    d = dv;
                }
            } else {
                x += 1;
                y += 1;
                d = dd;
            }
        }
    }
}
