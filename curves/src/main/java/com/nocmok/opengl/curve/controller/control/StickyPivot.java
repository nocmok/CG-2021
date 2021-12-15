package com.nocmok.opengl.curve.controller.control;

import javafx.event.Event;
import javafx.scene.input.MouseEvent;

/**
 * Хранит ссылки на своих соседей
 * При движении тянет своих соседей
 */
public class StickyPivot extends Pivot {

    private Pivot slavePivot1;

    private Pivot slavePivot2;

    private double x;

    private double y;

    public StickyPivot(double x, double y, Pivot slavePivot1, Pivot slavePivot2) {
        this.slavePivot1 = slavePivot1;
        this.slavePivot2 = slavePivot2;
        this.x = x;
        this.y = y;

        setLayoutX(x);
        setLayoutY(y);

        getChildren().add(new LabeledCircle("0"));

        setOnMouseDragged(this::onDrag);
        addEventHandler(MouseEvent.ANY, Event::consume);
    }

    @Override public void onDrag(MouseEvent e) {

    }

    @Override public void onChanged() {

    }

    @Override public double x() {
        return 0;
    }

    @Override public double y() {
        return 0;
    }
}
