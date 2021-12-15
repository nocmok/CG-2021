package com.nocmok.opengl.curve.controller.control;

import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Хранит ссылки на своих соседей
 * При движении тянет своих соседей
 */
public abstract class StickyPivot extends Pivot {

    private List<Pivot> slaves = new ArrayList<>();

    private double x;

    private double y;

    public StickyPivot(double x, double y, List<Pivot> slaves, String label) {
        this.x = x;
        this.y = y;

        this.slaves.addAll(slaves);

        setAlignment(Pos.CENTER);

        getChildren().add(new LabeledCircle(label, Color.ORANGE));

        addEventHandler(MouseEvent.MOUSE_DRAGGED, this::onDrag);
        addEventHandler(MouseEvent.MOUSE_CLICKED, Event::consume);
    }

    @Override protected void layoutChildren() {
        super.layoutChildren();
        setTranslateX(x - getWidth() / 2);
        setTranslateY(y - getHeight() / 2);
    }

    private void shiftSlaves(double deltaX, double deltaY) {
        for (var slave : slaves) {
            slave.setX(slave.x() + deltaX);
            slave.setY(slave.y() + deltaY);
        }
    }

    @Override public void onDrag(MouseEvent e) {
        double deltaX = e.getX() + this.getTranslateX() - x;
        double deltaY = e.getY() + this.getTranslateY() - y;

        x = e.getX() + this.getTranslateX();
        y = e.getY() + this.getTranslateY();

        setManaged(false);
        setTranslateX(e.getX() + this.getTranslateX() - getWidth() / 2);
        setTranslateY(e.getY() + this.getTranslateY() - getHeight() / 2);
        e.consume();

        shiftSlaves(deltaX, deltaY);

        onChanged();
    }

    public abstract void onChanged();

    public List<Pivot> getSlaves() {
        return slaves;
    }

    @Override public double x() {
        return x;
    }

    @Override public double y() {
        return y;
    }

    @Override public void setX(double x) {
        this.x = x;
        setTranslateX(x - getWidth() / 2);
    }

    @Override public void setY(double y) {
        this.y = y;
        setTranslateY(y - getHeight() / 2);
    }
}
