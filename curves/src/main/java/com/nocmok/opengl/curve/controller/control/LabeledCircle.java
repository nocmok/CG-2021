package com.nocmok.opengl.curve.controller.control;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class LabeledCircle extends StackPane {

    public LabeledCircle(String label, Color color) {
        setWidth(20);
        setHeight(20);

        setAlignment(Pos.CENTER);

        var circle = new Circle();
        circle.setFill(color);
        circle.setRadius(10);
        circle.setStroke(Color.BLACK);

        var labelNode = new Label(label);
        getChildren().add(circle);
        getChildren().add(labelNode);
    }
}
