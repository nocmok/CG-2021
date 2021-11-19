package com.nocmok.opengl._3d._3d;

import com.nocmok.opengl._3d._3d.camera.Camera;
import com.nocmok.opengl._3d._3d.model.PolygonModel;
import com.nocmok.opengl._3d._3d.transformation.Transformation;

import java.util.Collection;

public interface Scene {
    // Добавляет объект на сцену в указанной позиции
    void addObject(PolygonModel model, double[] position);

    // Добавляет трансформацию, которая будет применена ко всем объектам при следующей отрисовке экрана.
    // Трансформации будут применяться в том порядке в котором они были добавлены
    void addTransformation(Transformation transformation);

    Collection<Transformation> getTransformations();

    // Отрисовывает сцену на экране с указанной камерой
    void draw(Screen screen, Camera camera);

    void clear();
}
