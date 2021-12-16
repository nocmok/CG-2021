package com.nocmok.opengl.curve.controller.action;

import com.nocmok.opengl.curve.controller.control.Pivot;
import com.nocmok.opengl.curve.controller.control.ShakyPivot;
import com.nocmok.opengl.curve.controller.control.StickyPivot;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class BezierCurvePivotHandler implements ActionHandler<Pane> {

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
        var pivotsToAdd = new ArrayList<Pivot>();
        int n = pivots.size();
        if (n < 3 || (n - 3) % 3 != 0) {
            pivotsToAdd.add(new ShakyPivot(x, y, Integer.toString(pivots.size())) {
                @Override public void onChanged() {
                    BezierCurvePivotHandler.this.onPivotsChange(BezierCurvePivotHandler.this.pivots);
                }
            });
        } else {
            var shaky1 = (ShakyPivot) pivots.get(pivots.size() - 1);
            var shaky2 = new ShakyPivot(x + x - shaky1.x(), y + y - shaky1.y(), Integer.toString(pivots.size() + 1)) {
                @Override public void onChanged() {
                    BezierCurvePivotHandler.this.onPivotsChange(BezierCurvePivotHandler.this.pivots);
                }
            };
            var center = new StickyPivot(x, y, List.of(shaky1, shaky2), Integer.toString(pivots.size())) {
                @Override public void onChanged() {
                    BezierCurvePivotHandler.this.onPivotsChange(BezierCurvePivotHandler.this.pivots);
                }
            };
            shaky1.setCenterPivot(center);
            shaky2.setCenterPivot(center);
            shaky1.setPartnerPivot(shaky2);
            shaky2.setPartnerPivot(shaky1);

            pivotsToAdd.add(center);
            pivotsToAdd.add(shaky2);
        }

        parent.getChildren().addAll(pivotsToAdd);
        pivots.addAll(pivotsToAdd);

        onPivotsChange(pivots);
    }

    public List<Pivot> getPivots() {
        return pivots;
    }

    public abstract void onPivotsChange(Collection<Pivot> pivots);
}
