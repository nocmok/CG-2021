package com.nocmok.opengl.primitives.drawer;

import java.awt.Color;
import java.awt.Graphics2D;

public class G2LineDrawer {

    public void drawLine(int x0, int y0, int x1, int y1, Graphics2D g2) {
        g2.setColor(Color.BLACK);
        g2.drawLine(x0,y0,x1,y1);
    }
}
