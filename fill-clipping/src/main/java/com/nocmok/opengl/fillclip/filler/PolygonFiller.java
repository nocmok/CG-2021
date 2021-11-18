package com.nocmok.opengl.fillclip.filler;

import com.nocmok.opengl.fillclip.drawer.Grid;
import com.nocmok.opengl.fillclip.drawer.LineDrawer;
import com.nocmok.opengl.fillclip.util.IntPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

public class PolygonFiller {

    private LineDrawer lineDrawer;

    public PolygonFiller(Grid grid) {
        this.lineDrawer = new LineDrawer(grid);
    }

    // Считает x для точки пересечения прямой с y
    private int getXForY(Line line, int y) {
        int dx = line.x2 - line.x1;
        int dy = line.y2 - line.y1;
        return (y - line.y1) * dx / dy + line.x1;
    }

    // Принимает полигон заданный списком точек
    public void fill(List<IntPoint> polygon) {
        if (polygon.size() < 3) {
            return;
        }

        polygon = new ArrayList<>(polygon);

        // добавляем в конец списка точек первую точку, чтобы удобно было доставать
        // замыкающий отрезок полигона
        polygon.add(polygon.get(0));

        // получаем список отрезков полигона
        var lines = new ArrayList<Line>();
        int lineNumber = 0;
        for (int i = 1; i < polygon.size(); ++i) {
            // горизонтальные прямые пропускаем
            if (polygon.get(i - 1).y == polygon.get(i).y) {
                continue;
            }
            lines.add(new Line(polygon.get(i - 1), polygon.get(i), lineNumber++));
        }

        Function<Line, Integer> getLineSlope = l -> Integer.compare(l.y1, l.y2);
        Function<Line, Line> getPrevLine = l -> lines.get((l.n + lines.size() - 1) % lines.size());

        int yMin = lines.stream().flatMapToInt(l -> IntStream.of(l.y1, l.y2)).min().getAsInt();
        int yMax = lines.stream().flatMapToInt(l -> IntStream.of(l.y1, l.y2)).max().getAsInt();

        // проходимся по всем строкам
        for (int y = yMin; y <= yMax; ++y) {
            var points = new ArrayList<Integer>();

            // Проходимся по отрезкам в порядке обхода
            for (var line : lines) {
                // если отрезок полигона не пересекает текущую строку, то пропускаем прямую
                if (line.y1 != y && line.y2 != y && Integer.compare(line.y1, y) == Integer.compare(line.y2, y)) {
                    continue;
                }

                var prevLine = getPrevLine.apply(line);

                // ищем точку пересечения текущего отрезка со строкой
                int lineX = getXForY(line, y);

                // добавляем точку пересечения отрезка со строкой если
                // 1) Это не точка соединения двух отрезков
                // 2) Если это точка соединения двух отрезков, но текущий отрезок идет первее,
                //      либо если два соседних отрезка образуют пик (локальный экстремум)
                if (line.y1 != y || !getLineSlope.apply(prevLine).equals(getLineSlope.apply(line))) {
                    points.add(lineX);
                }
            }

            // Сортируем все полученные на строке точки, разбиваем их на пары и проводим между ними прямые
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
        // номер линии в порядке обхода точек полигона
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
