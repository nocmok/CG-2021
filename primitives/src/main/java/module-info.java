module com.nocmok.opengl.primitives {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.swing;

    opens com.nocmok.opengl.primitives to javafx.fxml;
    exports com.nocmok.opengl.primitives;
    exports com.nocmok.opengl.primitives.controller;
    opens com.nocmok.opengl.primitives.controller to javafx.fxml;
}