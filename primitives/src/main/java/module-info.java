module primitives_demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.swing;

    opens com.nocmok.opengl.primitives;
    opens com.nocmok.opengl.primitives.controller;
    opens com.nocmok.opengl.primitives.controller.action;
    opens com.nocmok.opengl.primitives.controller.control;
    opens com.nocmok.opengl.primitives.drawer;
    opens com.nocmok.opengl.primitives.util;

    exports com.nocmok.opengl.primitives;
    exports com.nocmok.opengl.primitives.controller;
}