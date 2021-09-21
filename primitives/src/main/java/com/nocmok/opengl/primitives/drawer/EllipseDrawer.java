package com.nocmok.opengl.primitives.drawer;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class EllipseDrawer {

    private Grid grid;

    public EllipseDrawer(Grid grid) {
        this.grid = grid;
    }

    public void setPixelXY(int x, int y) {
        grid.set(x, y);
    }

    public void setPixelYX(int y, int x) {
        grid.set(x, y);
    }

    public void drawEllipse(int x0, int y0, int xr, int yr) {
        BiConsumer<Integer, Integer> setPixel = this::setPixelXY;

        if(xr > yr){
            int tmp;
            tmp = xr;
            xr = yr;
            yr = tmp;
            tmp = x0;
            x0 = y0;
            y0 = tmp;
            setPixel = this::setPixelYX;
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
