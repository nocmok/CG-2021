package com.nocmok.opengl.primitives.controller.action;

import com.nocmok.opengl.primitives.controller.control.PixelatedCanvas;
import javafx.scene.input.MouseEvent;

public abstract class ShapeDragHandler {

    public void attach(PixelatedCanvas canvas) {
        canvas.setOnMousePressed(this::onDragStarted);
        canvas.setOnMouseDragged(this::onDrag);
        canvas.setOnMouseDragReleased(this::onDragStopped);
    }

    public void onDragStarted(MouseEvent e) {

    }

    public void onDrag(MouseEvent e) {

    }

    public void onDragStopped(MouseEvent e) {

    }
}
