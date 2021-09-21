package com.nocmok.opengl.primitives.controller.action;

import javafx.scene.Node;
import javafx.scene.transform.Scale;

public class Zoomer {

    private int scale = 1;

    private int scaleFactor = 4;

    private int maxScale = 4;

    private int minScale = 1;

    private Node node;

    public Zoomer(Node node) {
        this.node = node;
    }

    public void zoomIn() {
        if (scale >= maxScale) {
            return;
        }
        scale = Integer.min(maxScale, scale * scaleFactor);
        node.setScaleX(scale);
        node.setScaleY(scale);
    }

    public void zoomOut() {
        if (scale <= minScale) {
            return;
        }
        this.scale = Integer.max(minScale, this.scale / scaleFactor);
        node.setScaleX(scale);
        node.setScaleY(scale);
    }
}
