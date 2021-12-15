package com.nocmok.opengl.curve.controller.control;

import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public abstract class ShakyPivot extends Pivot {

    private List<Pivot> partnerPivots = Collections.emptyList();

    private Optional<Pivot> centerPivot = Optional.empty();

    private double x;

    private double y;

    public ShakyPivot(double x, double y, String label) {
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

    private void movePartner() {
        if (centerPivot.isEmpty()) {
            return;
        }

        var c = centerPivot.get();
        double dx = c.x() - x;
        double dy = c.y() - y;

        for (var p : partnerPivots) {
//            double coef = Math.hypot(p.x()-c.x(), p.y()-c.y()) / Math.hypot(dx, dy);

            p.setX(c.x() + dx * Math.hypot(p.x()-c.x(), p.y()-c.y()) / Math.hypot(dx, dy));
            p.setY(c.y() + dy * Math.hypot(p.x()-c.x(), p.y()-c.y()) / Math.hypot(dx, dy));
        }
    }

    @Override public void onDrag(MouseEvent e) {
        this.x = e.getX() + this.getTranslateX();
        this.y = e.getY() + this.getTranslateY();

        setManaged(false);
        setTranslateX(e.getX() + this.getTranslateX() - getWidth() / 2);
        setTranslateY(e.getY() + this.getTranslateY() - getHeight() / 2);
        e.consume();

        movePartner();

        onChanged();
    }

    public abstract void onChanged();

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

    public void setCenterPivot(Pivot pivot) {
        this.centerPivot = Optional.ofNullable(pivot);
    }

    public void setPartnerPivot(Pivot pivot) {
        this.partnerPivots = List.of(pivot);
    }
}
