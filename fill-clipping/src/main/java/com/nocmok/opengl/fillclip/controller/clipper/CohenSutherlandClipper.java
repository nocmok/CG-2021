package com.nocmok.opengl.fillclip.controller.clipper;

import com.nocmok.opengl.fillclip.util.IntPoint;
import com.nocmok.opengl.fillclip.util.Rectangle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CohenSutherlandClipper {

    // | left | right | up | down |
    private static final int LEFT = 8;
    private static final int RIGHT = 4;
    private static final int TOP = 2;
    private static final int BOTTOM = 1;
    private static final int CENTER = 0;

    public CohenSutherlandClipper() {

    }

    private int encodePoint(IntPoint point, Rectangle window) {
        int code = 0;
        if (point.x < window.x) {
            code |= LEFT;
        } else if (point.x > window.x2()) {
            code |= RIGHT;
        }
        if (point.y < window.y) {
            code |= TOP;
        } else if (point.y > window.y2()) {
            code |= BOTTOM;
        }
        return code;
    }

    public List<IntPoint[]> clip(List<IntPoint[]> lines, Rectangle window) {
        var clipped = new ArrayList<IntPoint[]>();
        var toClip = new ArrayList<IntPoint[]>();
        for (var line : lines) {
            int p1Code = encodePoint(line[0], window);
            int p2Code = encodePoint(line[1], window);
            if ((p1Code & p2Code) > 0) {
                continue;
            }
            if (p1Code == 0 && p2Code == 0) {
                clipped.add(line);
            } else {
                toClip.add(line);
            }
        }
        clipped.addAll(clipDumb(toClip, window));
        return clipped;
    }

    private int intersectX(IntPoint[] line, int x) {
        int dx = line[1].x - line[0].x;
        int dy = line[1].y - line[0].y;
        if (dx == 0) {
            return Integer.MAX_VALUE;
        }
        double k = (double) dy / dx;
        double b = line[0].y - k * line[0].x;
        return (int) Math.round(k * x + b);
    }

    private int intersectY(IntPoint[] line, int y) {
        int dx = line[1].x - line[0].x;
        int dy = line[1].y - line[0].y;
        if (dx == 0) {
            return line[0].x;
        }
        double k = (double) dy / dx;
        double b = line[0].y - k * line[0].x;
        return (int) Math.round((y - b) / k);
    }

    private boolean checkBounds(int value, int min, int max) {
        return value >= min && value <= max;
    }

    // Отсекает прямые которые не лежат в окне целиком
    //

    // корнер кейсы, которые не учитывал:
    // 1) прямые, которые проходят по границам окна
    // 2) вырожденное окно
    // 3) прямая пересекает по углу окна, то есть сразу четыре стороны
    private List<IntPoint[]> clipDumb(List<IntPoint[]> lines, Rectangle window) {
        var clipped = new ArrayList<IntPoint[]>();
        for (var line : lines) {
            List<IntPoint> points = new ArrayList<>();
            if (checkBounds(intersectX(line, window.x), window.y, window.y2())) {
                points.add(new IntPoint(window.x, intersectX(line, window.x)));
            }
            if (checkBounds(intersectX(line, window.x2()), window.y, window.y2())) {
                points.add(new IntPoint(window.x2(), intersectX(line, window.x2())));
            }
            if (checkBounds(intersectY(line, window.y), window.x, window.x2())) {
                points.add(new IntPoint(intersectY(line, window.y), window.y));
            }
            if (checkBounds(intersectY(line, window.y2()), window.x, window.x2())) {
                points.add(new IntPoint(intersectY(line, window.y2()), window.y2()));
            }
            points = points.stream().distinct().collect(Collectors.toList());
            if (points.size() == 2) {
                int p1Code = encodePoint(line[0], window);
                int p2Code = encodePoint(line[1], window);
                if (p1Code == 0 || p2Code == 0) {

                    var internalPoint = p1Code == 0 ? line[0] : line[1];
                    var externalPointCode = p1Code == 0 ? p2Code : p1Code;

                    if ((externalPointCode & (LEFT | RIGHT)) > 0) {
                        points.sort(Comparator.comparingInt(pp -> pp.x));
                        if ((externalPointCode & LEFT) > 0) {
                            clipped.add(new IntPoint[]{internalPoint, points.get(0)});
                        } else {
                            clipped.add(new IntPoint[]{internalPoint, points.get(1)});
                        }
                    } else {
                        points.sort(Comparator.comparingInt(pp -> pp.y));
                        if ((externalPointCode & TOP) > 0) {
                            clipped.add(new IntPoint[]{internalPoint, points.get(0)});
                        } else {
                            clipped.add(new IntPoint[]{internalPoint, points.get(1)});
                        }
                    }
                } else {
                    clipped.add(new IntPoint[]{points.get(0), points.get(1)});
                }
            }
        }
        return clipped;
    }
}
