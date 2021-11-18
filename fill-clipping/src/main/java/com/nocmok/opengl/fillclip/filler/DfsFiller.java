package com.nocmok.opengl.fillclip.filler;

import java.util.ArrayDeque;
import java.util.List;

/**
 * Реализация алгоритма построчной затравки.
 */
public class DfsFiller {

    private ReadableGrid grid;

    public DfsFiller(ReadableGrid grid) {
        this.grid = grid;
    }

    private boolean checkBoundaries(int x, int y) {
        return x >= 0 && x < grid.xSize() && y >= 0 && y < grid.ySize();
    }

    private int getColor(int x, int y) {
        return checkBoundaries(x, y) ? grid.get(x, y) : -1;
    }

    public void fill(int x0, int y0) {
        // Получаем цвет, который будем закрашивать
        int color = grid.get(x0, y0);
        // Заводим массив, для того, чтобы отслеживать закрашенные точки
        // Можно обойтись и без него. Добавлен для понятности кода
        boolean[][] filled = new boolean[grid.ySize()][grid.xSize()];

        // на стек складываем координаты точек из которых нужно попытаться сделать закраску строки
        var stack = new ArrayDeque<int[]>();
        stack.offerLast(new int[]{x0, y0});

        while (!stack.isEmpty()) {
            int[] point = stack.pollLast();
            int x = point[0];
            int y = point[1];
            if (filled[y][x]) {
                continue;
            }

            // Просматриваем все точки справа от текущей, пока цвет точки совпадает с тем, который нужно закрашивать
            for (int c = x; c < grid.xSize() && grid.get(c, y) == color; ++c) {
                grid.set(c, y);
                filled[y][c] = true;
                // Смотрим на точки сверху и снизу
                for (int r : List.of(y + 1, y - 1)) {
                    // если точка сверху/снизу не того цвета, то ее закрашивать не нужно - пропускаем ее
                    if (getColor(c, r) != color) {
                        continue;
                    }

                    // если до этого уже добавляли точку на верхний/нижний сплошной фрагмент, то удалим ее
                    // и вместо нее добавим новую, чтобы потом обработать этот сплошной фрагмент ровно 1 раз
                    int[] prev = stack.peek();
                    if (prev != null && prev[1] == r && prev[0] == c - 1) {
                        stack.pollLast();
                    }

                    // Должны закрасить точку сверху/снизу так как она достижима и имеет нужный цвет
                    stack.offerLast(new int[]{c, r});
                }
            }

            // Делаем то же самое, но просматриваем точки слева от текущей
            for (int c = x - 1; c >= 0 && grid.get(c, y) == color; --c) {
                grid.set(c, y);
                filled[y][c] = true;
                for (int r : List.of(y + 1, y - 1)) {
                    if (getColor(c, r) != color) {
                        continue;
                    }
                    int[] prev = stack.peek();
                    if (prev != null && prev[1] == r && prev[0] == c - 1) {
                        stack.pollLast();
                    }
                    stack.offerLast(new int[]{c, r});
                }
            }
        }
    }

}