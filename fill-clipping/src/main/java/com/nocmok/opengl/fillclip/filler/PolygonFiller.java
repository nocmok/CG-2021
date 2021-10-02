package com.nocmok.opengl.fillclip.filler;

import com.nocmok.opengl.fillclip.controller.control.PixelatedCanvas;
import com.nocmok.opengl.fillclip.drawer.LineDrawer;
import com.nocmok.opengl.fillclip.util.IntPoint;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

public class PolygonFiller {

    private LineDrawer lineDrawer;

    public PolygonFiller(PixelatedCanvas canvas, Color color) {
        this.lineDrawer = new LineDrawer((x, y) -> canvas.setPixel(x, y, color));
    }

    private int getXForY(Line line, int y) {
        int dx = line.x2 - line.x1;
        int dy = line.y2 - line.y1;
        return (y - line.y1) * dx / dy + line.x1;
    }

    public void fill(List<IntPoint> polygon) {
        if (polygon.size() < 3) {
            return;
        }

        polygon = new ArrayList<>(polygon);
        polygon.add(polygon.get(0));

        var lines = new ArrayList<Line>();
        int lineNumber = 0;
        for (int i = 1; i < polygon.size(); ++i) {
            lines.add(new Line(polygon.get(i - 1), polygon.get(i), lineNumber++));
        }

        Function<Line, Integer> getLineSlope = l -> Integer.compare(l.y1, l.y2);
        Function<Line, Line> getNextLine = l -> lines.get((l.n + 1) % lines.size());
        Function<Line, Line> getPrevLine = l -> lines.get((l.n + lines.size() - 1) % lines.size());

        int yMin = lines.stream().flatMapToInt(l -> IntStream.of(l.y1, l.y2)).min().getAsInt();
        int yMax = lines.stream().flatMapToInt(l -> IntStream.of(l.y1, l.y2)).max().getAsInt();

        for (int y = yMin; y <= yMax; ++y) {
            var points = new ArrayList<Integer>();
            boolean removeLastPoint = false;

            for (var line : lines) {
                if (line.y1 != y && line.y2 != y && Integer.compare(line.y1, y) == Integer.compare(line.y2, y)) {
                    continue;
                }

                var prevLine = getPrevLine.apply(line);
                var nextLine = getNextLine.apply(line);

                if (line.y1 == line.y2) {
                    if (getLineSlope.apply(prevLine).equals(getLineSlope.apply(nextLine))) {
                        if (points.isEmpty()) {
                            removeLastPoint = true;
                        } else {
                            points.remove(points.size() - 1);
                        }
                    }
                } else {
                    int lineX = getXForY(line, y);
                    if (line.y1 != y || !getLineSlope.apply(prevLine).equals(getLineSlope.apply(line))) {
                        points.add(lineX);
                    }
                }
            }

            if (removeLastPoint && !points.isEmpty()) {
                points.remove(points.size() - 1);
            }

            points.sort(Integer::compare);

            for (int i = 0; i + 1 < points.size(); i += 2) {
                lineDrawer.drawLine(points.get(i), y, points.get(i + 1), y);
            }
        }
    }

    private static class Line {
        public int x1;
        public int y1;
        public int x2;
        public int y2;
        public int n;

        public Line(int x1, int y1, int x2, int y2, int n) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            this.n = n;
        }

        public Line(IntPoint p1, IntPoint p2, int n) {
            this(p1.x, p1.y, p2.x, p2.y, n);
        }
    }

}
