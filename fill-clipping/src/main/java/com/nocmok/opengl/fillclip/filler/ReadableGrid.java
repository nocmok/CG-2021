package com.nocmok.opengl.fillclip.filler;

public interface ReadableGrid {

    void set(int x, int y);

    int get(int x, int y);

    int xSize();

    int ySize();
}
