package com.nocmok.opengl.curve.controller.control;

import javafx.event.Event;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

public abstract class Pivot extends Circle {

    private double x;
    private double y;

    public Pivot(double x, double y) {
        super();
        this.x = x;
        this.y = y;
        setRadius(5d);

        setOnMouseDragged(this::onDrag);
        addEventHandler(MouseEvent.ANY, Event::consume);
    }

    private void onDrag(MouseEvent e) {
        this.x = e.getX();
        this.y = e.getY();

        setCenterX(x);
        setCenterY(y);

        onChanged();
    }

    public double x() {
        return x;
    }

    public double y() {
        return y;
    }

    public abstract void onChanged();
}
