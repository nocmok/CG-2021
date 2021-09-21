package com.nocmok.opengl.primitives.controller.action;

import com.nocmok.opengl.primitives.controller.control.PixelatedCanvas;

public abstract class ShapeDragHandler {

    public void attach(PixelatedCanvas canvas) {
//        canvas.setOnMousePressed(e -> startDrag(e.getX(), e.getY()));
//        canvas.setOnMouseDragged(e -> drag(e.getX(), e.getY()));
//        canvas.setOnMouseDragReleased(e -> stopDrag(e.getX(), e.getY()));
    }

    public void startDrag(double mouseX, double mouseY) {

    }

    public void drag(double mouseX, double mouseY) {

    }

    public void stopDrag(double mouseX, double mouseY) {

    }
}
