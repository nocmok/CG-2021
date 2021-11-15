package com.nocmok.opengl._3d._3d;

public interface Scene {
    // Добавляет объект на сцену в указанной позиции
    void addObject(PolygonModel object, float[] position);
    // применяет трансформацию ко всем объектам на сцене
    void applyTransformation(Transformation transformation);
    // Делает проекцию. Тут применяется проекция
    void draw(Screen screen);
}
