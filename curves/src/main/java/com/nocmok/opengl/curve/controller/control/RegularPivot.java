package com.nocmok.opengl.curve.controller.control;

import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public abstract class RegularPivot extends Pivot {

    private double x;
    private double y;

    public RegularPivot(double x, double y, String label) {
        super();
        this.x = x;
        this.y = y;

        setAlignment(Pos.CENTER);

        getChildren().add(new LabeledCircle(label, Color.WHITE));

        addEventHandler(MouseEvent.MOUSE_DRAGGED, this::onDrag);
        addEventHandler(MouseEvent.MOUSE_CLICKED, Event::consume);
    }

    @Override protected void layoutChildren() {
        super.layoutChildren();
        setTranslateX(x - getWidth() / 2);
        setTranslateY(y - getHeight() / 2);
    }

    public void onDrag(MouseEvent e) {
        this.x = e.getX() + this.getTranslateX();
        this.y = e.getY() + this.getTranslateY();

        setManaged(false);
        setTranslateX(e.getX() + this.getTranslateX() - getWidth() / 2);
        setTranslateY(e.getY() + this.getTranslateY() - getHeight() / 2);
        e.consume();

        onChanged();
    }

    @Override public void setX(double x) {
        this.x = x;
        setTranslateX(x - getWidth() / 2);
    }

    @Override public void setY(double y) {
        this.y = y;
        setTranslateY(y - getWidth() / 2);
    }

    public double x() {
        return x;
    }

    public double y() {
        return y;
    }

    public abstract void onChanged();
}
