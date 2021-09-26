package com.nocmok.opengl.primitives.filler;

import java.util.ArrayDeque;
import java.util.List;

public class LineByLineFiller {

    private Grid grid;

    public LineByLineFiller(Grid grid) {
        this.grid = grid;
    }

    private boolean checkBoundaries(int x, int y) {
        return x >= 0 && x < grid.xSize() && y >= 0 && y < grid.ySize();
    }

    public void fill(int x, int y) {
        if (!checkBoundaries(x, y)) {
            return;
        }
        boolean[][] filled = new boolean[grid.ySize()][grid.xSize()];
        fill(x, y, grid.get(x, y), filled);
    }

    private int getColor(int x, int y) {
        return checkBoundaries(x, y) ? grid.get(x, y) : -1;
    }

    private void fill(int x, int y, int color, boolean[][] filled) {
        var stack = new ArrayDeque<int[]>();

        for (int c = x; c < grid.xSize() && grid.get(c, y) == color; ++c) {
            grid.set(c, y);
            filled[y][c] = true;
            for (int r : List.of(y + 1, y - 1)) {
                if (getColor(c, r) == color && (getColor(c + 1, r) != color || getColor(c + 1, y) != color)) {
                    stack.offerLast(new int[]{c, r});
                }
            }
        }
        for (int c = x - 1; c >= 0 && grid.get(c, y) == color; --c) {
            grid.set(c, y);
            filled[y][c] = true;
            for (int r : List.of(y + 1, y - 1)) {
                if (getColor(c, r) == color && getColor(c + 1, r) != color) {
                    stack.offerLast(new int[]{c, r});
                }
            }
        }

        while (!stack.isEmpty()) {
            int[] next = stack.pollLast();
            if (!filled[next[1]][next[0]]) {
                fill(next[0], next[1], color, filled);
            }
        }
    }

}