package com.nocmok.opengl.fillclip.drawer;

import java.util.function.BiConsumer;

public class EllipseDrawer {

    private Grid grid;

    public EllipseDrawer(Grid grid) {
        this.grid = grid;
    }

    public void drawEllipse(int x0, int y0, int xr, int yr) {

        BiConsumer<Integer, Integer> setPixel;

        if (xr > yr) {
            int tmp;
            tmp = xr;
            xr = yr;
            yr = tmp;
            tmp = x0;
            x0 = y0;
            y0 = tmp;
            setPixel = (x, y) -> grid.set(y, x);
        } else {
            setPixel = (x, y) -> grid.set(x, y);
        }

        int x = 0;
        int y = -yr;

        int xr2 = xr * xr;
        int yr2 = yr * yr;

        int xyr2 = 0;
        int yxr2 = y * xr2;

        int d = 0;

        while (x <= xr && y <= 0) {
            setPixel.accept(x0 + x, y0 + y);
            setPixel.accept(x0 + x, y0 - y);
            setPixel.accept(x0 - x, y0 - y);
            setPixel.accept(x0 - x, y0 + y);

            int dd = d + 2 * xyr2 + 2 * yxr2 + yr2 + xr2;

            if (dd < 0) {
                // inside
                int dh = d + 2 * xyr2 + yr2;
                if (Math.abs(dd) < Math.abs(dh)) {
                    x += 1;
                    y += 1;
                    xyr2 += yr2;
                    yxr2 += xr2;
                    d = dd;
                } else {
                    x += 1;
                    xyr2 += yr2;
                    d = dh;
                }
            } else if (dd > 0) {
                // outside
                int dv = d + 2 * yxr2 + xr2;
                if (Math.abs(dd) < Math.abs(dv)) {
                    x += 1;
                    y += 1;
                    xyr2 += yr2;
                    yxr2 += xr2;
                    d = dd;
                } else {
                    y += 1;
                    yxr2 += xr2;
                    d = dv;
                }
            } else {
                x += 1;
                y += 1;
                xyr2 += yr2;
                yxr2 += xr2;
                d = dd;
            }
        }
    }
}
