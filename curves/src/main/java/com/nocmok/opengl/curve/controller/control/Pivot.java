package com.nocmok.opengl.curve.controller.control;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public abstract class Pivot extends StackPane {

    public abstract void onDrag(MouseEvent e);

    public abstract void onChanged();

    public abstract double x();

    public abstract double y();

    public abstract void setX(double x);

    public abstract void setY(double y);

}
