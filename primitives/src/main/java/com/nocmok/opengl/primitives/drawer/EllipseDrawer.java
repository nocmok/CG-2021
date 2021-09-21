package com.nocmok.opengl.primitives.drawer;

public class EllipseDrawer {

    private Grid grid;

    public EllipseDrawer(Grid grid) {
        this.grid = grid;
    }

    public void drawEllipse(int x0, int y0, int xr, int yr) {
        int x = 0;
        int y = -yr;

        int xr2 = xr * xr;
        int yr2 = yr * yr;

        int d = 0;

        while (x <= xr && y <= 0) {
            grid.set(x0 + x, y0 + y);
            grid.set(x0 + x, y0 - y);
            grid.set(x0 - x, y0 - y);
            grid.set(x0 - x, y0 + y);

            int dh = d + 2 * x * yr2 + yr2;
            int dd = d + 2 * x * yr2 + 2 * y * xr2 + yr2 + xr2;
            int dv = d + 2 * y * xr2 + xr2;

            if (dd < 0) {
                // inside
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
