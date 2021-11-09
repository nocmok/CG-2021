package com.nocmok.opengl.curve.controller.action;

import javafx.scene.Node;

public interface ActionHandler <T extends Node> {

    void attach(T node);
}
