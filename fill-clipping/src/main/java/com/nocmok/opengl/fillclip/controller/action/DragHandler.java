package com.nocmok.opengl.fillclip.controller.action;

import javafx.scene.Node;

public abstract class DragHandler implements ActionHandler<Node> {

    private double dragX0;

    private double dragY0;

    private double dragX1;

    private double dragY1;

    public void attach(Node node) {
        node.setOnMousePressed(e -> _startDrag(e.getX(), e.getY()));
        node.setOnMouseDragged(e -> _proceedDrag(e.getX(), e.getY()));
        node.setOnMouseReleased(e -> _stopDrag(e.getX(), e.getY()));
    }

    private void _startDrag(double x, double y) {
        dragX0 = x;
        dragY0 = y;
        startDrag(x, y);
    }

    private void _proceedDrag(double x, double y) {
        proceedDrag(dragX0, dragY0, dragX1, dragY1, x, y);
        dragX1 = x;
        dragY1 = y;
    }

    private void _stopDrag(double x, double y) {
        stopDrag(dragX0, dragY0, dragX1, dragY1, x, y);
    }

    public void startDrag(double x, double y) {
    }

    public void proceedDrag(double x0, double y0, double x1, double y1, double x2, double y2) {
    }

    public void stopDrag(double x0, double y0, double x1, double y1, double x2, double y2) {
    }
}
