package com.nocmok.opengl._3d.controller;

import com.nocmok.opengl._3d._3d.BasicScene;
import com.nocmok.opengl._3d._3d.SampleModels;
import com.nocmok.opengl._3d._3d.Scene;
import com.nocmok.opengl._3d._3d.Screen;
import com.nocmok.opengl._3d._3d.camera.Camera;
import com.nocmok.opengl._3d._3d.camera.CentralProjectionCamera;
import com.nocmok.opengl._3d._3d.camera.DumbCamera;
import com.nocmok.opengl._3d._3d.model.PolygonModel;
import com.nocmok.opengl._3d._3d.transformation.Transformation;
import com.nocmok.opengl._3d._3d.transformation.XRotation;
import com.nocmok.opengl._3d._3d.transformation.XYZScale;
import com.nocmok.opengl._3d._3d.transformation.XYZShift;
import com.nocmok.opengl._3d._3d.transformation.YRotation;
import com.nocmok.opengl._3d._3d.transformation.ZRotation;
import com.nocmok.opengl._3d.controller.contols.CanvasScreen;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Transform;

import java.net.URL;
import java.util.ResourceBundle;

public class CubeDemoController extends AbstractController {

    @FXML
    private GridPane root;
    @FXML
    private Pane frame;
    @FXML
    private Slider povSlider;

    @FXML
    private Slider xShift;
    @FXML
    private Slider yShift;
    @FXML
    private Slider zShift;

    @FXML
    private Slider xRotation;
    @FXML
    private Slider yRotation;
    @FXML
    private Slider zRotation;

    @FXML
    private Slider xScale;
    @FXML
    private Slider yScale;
    @FXML
    private Slider zScale;
    @FXML
    private Slider xyzScale;

    @FXML
    private RadioButton shiftThenRotation;
    @FXML
    private RadioButton rotationThenShift;

    @FXML
    private RadioButton parallelProjection;
    @FXML
    private RadioButton singlePointProjection;

    private Canvas canvas;

    private Scene scene;
    private Screen screen;
    private final PolygonModel model = SampleModels.cubeModel(100);

    private Camera camera;
    private DumbCamera parallelProjectionCamera;
    private CentralProjectionCamera singlePointProjectionCamera;

    private final XYZScale scaling = new XYZScale(1, 1, 1);
    private final XYZShift shift = new XYZShift(0, 0, 0);
    private final XRotation rotationX = new XRotation(0);
    private final YRotation rotationY = new YRotation(0);
    private final ZRotation rotationZ = new ZRotation(0);

    private final Transformation[] transformations = new Transformation[]{
            scaling,
            shift,
            rotationX,
            rotationY,
            rotationZ
    };
    private final int[] shiftThenRotationOrder = new int[]{0, 1, 2, 3, 4};
    private final int[] rotationThenShiftOrder = new int[]{0, 2, 3, 4, 1};
    private int[] transformationsOrder = shiftThenRotationOrder;

    @Override public Parent getRoot() {
        return root;
    }

    private void clearCanvas(Canvas canvas) {
        var g2 = canvas.getGraphicsContext2D();
        g2.setFill(Color.grayRgb(244));
        g2.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void drawArrow(Canvas canvas, double x0, double y0, double x1, double y1) {
        var g2 = canvas.getGraphicsContext2D();
        g2.setStroke(Color.BLACK);
        g2.setFill(Color.BLACK);

        g2.strokeLine(x0, y0, x1, y1);

        double angle = Math.toDegrees(Math.atan2(y1 - y0, x1 - x0)) + 90;
        var rotation = Transform.rotate(angle, x1, y1);

        double side = 10;
        double cos30 = 0.87;
        double[] points = new double[]{x1 - side / 2, y1, x1 + side / 2, y1, x1, y1 - cos30 * side};
        double[] rotated = new double[6];

        rotation.transform2DPoints(points, 0, rotated, 0, 3);

        g2.fillPolygon(new double[]{rotated[0], rotated[2], rotated[4]},
                new double[]{rotated[1], rotated[3], rotated[5]}, 3);
    }

    private void drawCoordinateArrows(Canvas canvas, double xArrowSize, double yArrowSize) {
        var g2 = canvas.getGraphicsContext2D();
        g2.setStroke(Color.BLACK);
        double x0 = canvas.getWidth() / 2;
        double y0 = canvas.getHeight() / 2;

        drawArrow(canvas, x0, y0, x0 + xArrowSize, y0);
        drawArrow(canvas, x0, y0, x0, y0 - yArrowSize);

        g2.strokeText("x", x0 + xArrowSize + 20, y0);
        g2.strokeText("y", x0, y0 - yArrowSize - 20);

        g2.setFill(Color.WHITE);
        g2.fillOval(x0 - 6, y0 - 6, 12, 12);
        g2.strokeOval(x0 - 6, y0 - 6, 12, 12);
        g2.setFill(Color.BLACK);
        g2.fillOval(x0 - 2, y0 - 2, 4, 4);
    }

    private void redrawScene() {
        clearCanvas(canvas);
        drawCoordinateArrows(canvas, 200, 200);
        scene.getTransformations().clear();
        for (var next : transformationsOrder) {
            scene.addTransformation(transformations[next]);
        }
        scene.draw(screen, camera);
    }

    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        canvas = new Canvas();

        var primaryScreen = javafx.stage.Screen.getPrimary().getBounds();
        canvas.setWidth(primaryScreen.getWidth());
        canvas.setHeight(primaryScreen.getHeight());

        frame.getChildren().add(canvas);

        double cameraXSize = canvas.getWidth() / 2;
        double cameraYSize = canvas.getHeight() / 2;

        this.screen = new CanvasScreen(canvas);
        this.parallelProjectionCamera = new DumbCamera(cameraXSize, cameraYSize);
        this.singlePointProjectionCamera = new CentralProjectionCamera(500, cameraXSize, cameraYSize);
        this.camera = parallelProjectionCamera;
        this.scene = new BasicScene();

        scene.addObject(model, new double[]{0, 0, 0, 1});
        scene.addTransformation(scaling);
        scene.addTransformation(shift);
        scene.addTransformation(rotationX);
        scene.addTransformation(rotationY);
        scene.addTransformation(rotationZ);

        redrawScene();

        /* Order */
        shiftThenRotation.setSelected(true);

        shiftThenRotation.selectedProperty().addListener((val) -> {
            if (shiftThenRotation.isSelected()) {
                transformationsOrder = shiftThenRotationOrder;
                redrawScene();
            }
        });
        rotationThenShift.selectedProperty().addListener((val) -> {
            if (rotationThenShift.isSelected()) {
                transformationsOrder = rotationThenShiftOrder;
                redrawScene();
            }
        });

        /* Camera */
        povSlider.setMin(0);
        povSlider.setMax(1000);
        povSlider.setValue(500);

        povSlider.valueProperty().addListener((val) -> {
            this.singlePointProjectionCamera.setPov(povSlider.getValue());
            redrawScene();
        });

        parallelProjection.setSelected(true);

        parallelProjection.selectedProperty().addListener((val) -> {
            if (parallelProjection.isSelected()) {
                this.camera = this.parallelProjectionCamera;
                redrawScene();
            }
        });

        singlePointProjection.selectedProperty().addListener((val) -> {
            if (singlePointProjection.isSelected()) {
                this.camera = this.singlePointProjectionCamera;
                redrawScene();
            }
        });

        /* Shifts */
        xShift.setMin(-500);
        xShift.setMax(500);
        xShift.setValue(0);

        yShift.setMin(-500);
        yShift.setMax(500);
        yShift.setValue(0);

        zShift.setMin(-500);
        zShift.setMax(500);
        zShift.setValue(0);

        xShift.valueProperty().addListener((val) -> {
            shift.setXYZ(xShift.getValue(), yShift.getValue(), zShift.getValue());
            redrawScene();
        });
        yShift.valueProperty().addListener((val) -> {
            shift.setXYZ(xShift.getValue(), yShift.getValue(), zShift.getValue());
            redrawScene();
        });
        zShift.valueProperty().addListener((val) -> {
            shift.setXYZ(xShift.getValue(), yShift.getValue(), zShift.getValue());
            redrawScene();
        });

        /* Rotations */
        xRotation.setMin(0);
        xRotation.setMax(360);
        xRotation.setValue(0);

        yRotation.setMin(0);
        yRotation.setMax(360);
        yRotation.setValue(0);

        zRotation.setMin(0);
        zRotation.setMax(360);
        zRotation.setValue(0);

        xRotation.valueProperty().addListener((val) -> {
            rotationX.setRadians(Math.toRadians(xRotation.getValue()));
            redrawScene();
        });
        yRotation.valueProperty().addListener((val) -> {
            rotationY.setRadians(Math.toRadians(yRotation.getValue()));
            redrawScene();
        });
        zRotation.valueProperty().addListener((val) -> {
            rotationZ.setRadians(Math.toRadians(zRotation.getValue()));
            redrawScene();
        });

        /* Scaling */
        xScale.setMin(1);
        xScale.setMax(5);
        xScale.setValue(1);

        yScale.setMin(1);
        yScale.setMax(5);
        yScale.setValue(1);

        zScale.setMin(1);
        zScale.setMax(5);
        zScale.setValue(1);

        xyzScale.setMin(1);
        xyzScale.setMax(5);
        xyzScale.setValue(1);

        xScale.valueProperty().addListener((val) -> {
            scaling.setXYZ(xScale.getValue(), yScale.getValue(), zScale.getValue());
            redrawScene();
        });
        yScale.valueProperty().addListener((val) -> {
            scaling.setXYZ(xScale.getValue(), yScale.getValue(), zScale.getValue());
            redrawScene();
        });
        zScale.valueProperty().addListener((val) -> {
            scaling.setXYZ(xScale.getValue(), yScale.getValue(), zScale.getValue());
            redrawScene();
        });
        xyzScale.valueProperty().addListener((val) -> {
            scaling.setXYZ(xyzScale.getValue(), xyzScale.getValue(), xyzScale.getValue());
            xScale.setValue(xyzScale.getValue());
            yScale.setValue(xyzScale.getValue());
            zScale.setValue(xyzScale.getValue());
            redrawScene();
        });

    }
}
