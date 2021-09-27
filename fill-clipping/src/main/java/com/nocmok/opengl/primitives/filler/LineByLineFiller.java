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

    private int getColor(int x, int y) {
        return checkBoundaries(x, y) ? grid.get(x, y) : -1;
    }

    public void fill(int x0, int y0) {
        int color = grid.get(x0, y0);
        boolean[][] filled = new boolean[grid.ySize()][grid.xSize()];
        var stack = new ArrayDeque<int[]>();
        stack.offerLast(new int[]{x0, y0});

        while (!stack.isEmpty()) {
            int[] point = stack.pollLast();
            int x = point[0];
            int y = point[1];
            if (filled[y][x]) {
                continue;
            }
            for (int c = x; c < grid.xSize() && grid.get(c, y) == color; ++c) {
                grid.set(c, y);
                filled[y][c] = true;
                for (int r : List.of(y + 1, y - 1)) {
                    if (getColor(c, r) == color) {
                        int[] prev = stack.peek();
                        if (prev != null && prev[1] == r && prev[0] == c - 1) {
                            stack.pollLast();
                        }
                        stack.offerLast(new int[]{c, r});
                    }
                }
            }
            for (int c = x - 1; c >= 0 && grid.get(c, y) == color; --c) {
                grid.set(c, y);
                filled[y][c] = true;
                for (int r : List.of(y + 1, y - 1)) {
                    if (getColor(c, r) == color) {
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

}