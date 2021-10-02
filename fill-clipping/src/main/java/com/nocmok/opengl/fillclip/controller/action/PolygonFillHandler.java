package com.nocmok.opengl.fillclip.controller.action;

import com.nocmok.opengl.fillclip.controller.control.PixelatedCanvas;
import com.nocmok.opengl.fillclip.drawer.LineDrawer;
import com.nocmok.opengl.fillclip.filler.PolygonFiller;
import com.nocmok.opengl.fillclip.util.IntPoint;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class PolygonFillHandler {

    private PixelatedCanvas canvas;

    private PolygonFiller polygonFiller;

    private LineDrawer lineDrawer;

    private List<IntPoint> points;

    public PolygonFillHandler(PixelatedCanvas canvas, Color color) {
        this.canvas = canvas;
        this.polygonFiller = new PolygonFiller(canvas, color);
        this.lineDrawer = new LineDrawer((x, y) -> canvas.setPixel(x, y, Color.BLACK));
        this.points = new ArrayList<>();
    }

    public void newPoint(double x, double y) {
        var point = new IntPoint(canvas.toPixelX(x), canvas.toPixelY(y));
        points.add(point);
        canvas.fillRect(0, 0, canvas.getxPixels(), canvas.getyPixels(), Color.WHITE);
        if (points.size() >= 2) {
            if (points.size() > 2) {
                polygonFiller.fill(points);
            }
            // draw lines
            var it = points.iterator();
            var p0 = it.next();
            while (it.hasNext()) {
                var p1 = it.next();
                lineDrawer.drawLine(p0.x, p0.y, p1.x, p1.y);
                p0 = p1;
            }
            lineDrawer.drawLine(p0.x, p0.y, points.get(0).x, points.get(0).y);

        } else {
            // draw point
            lineDrawer.drawLine(point.x, point.y, point.x, point.y);
        }
    }
}
