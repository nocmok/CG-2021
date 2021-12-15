package com.nocmok.opengl.curve.controller.action;

import com.nocmok.opengl.curve.controller.control.Pivot;
import com.nocmok.opengl.curve.controller.control.RegularPivot;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class LinearCurvePivotHandler implements ActionHandler<Pane> {

    private Pane parent;

    private List<Pivot> pivots = new ArrayList<>();

    @Override public void attach(Pane node) {
        this.parent = node;
        this.parent.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onMouseClicked);
    }

    private void onMouseClicked(MouseEvent e) {
        addPivot(e.getX(), e.getY());
    }

    private void addPivot(double x, double y) {
        var pivot = new RegularPivot(x, y, Integer.toString(pivots.size())) {
            @Override public void onChanged() {
                LinearCurvePivotHandler.this.onPivotsChange(LinearCurvePivotHandler.this.pivots);
            }
        };
        parent.getChildren().add(pivot);
        pivots.add(pivot);

        onPivotsChange(pivots);
    }

    public Collection<Pivot> getPivots() {
        return pivots;
    }

    public abstract void onPivotsChange(Collection<Pivot> pivots);
}
