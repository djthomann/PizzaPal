package de.hsrm.mi.swt.gui.components;

import java.beans.PropertyChangeEvent;

import de.hsrm.mi.swt.core.application.service.LagerService;
import de.hsrm.mi.swt.core.application.service.ServiceRegistry;
import de.hsrm.mi.swt.core.model.entities.Entity;
import de.hsrm.mi.swt.gui.GUIConfig;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class FancyComponent extends Pane {

    Entity model;

    Pane parent;

    protected Rectangle rect1;
    protected Rectangle rect2;
    private static final long DOUBLE_CLICK_THRESHOLD = 200;

    boolean moving = false;
    private long lastClickTime = 0;

    LagerService service = ServiceRegistry.getInstance().getLagerService();

    public FancyComponent(Pane parent, Entity model) {

        this.model = model;
        this.parent = parent;

        rect1 = new Rectangle(model.getWidth(), model.getHeight(), Color.GRAY);
        rect2 = new Rectangle(model.getWidth(), model.getHeight(), Color.GRAY);

        model.addPropertyChangeListener(e -> {

            if (e.getPropertyName().equals("moveX")) {
                setLayoutX((int) e.getNewValue());
            }

            if (e.getPropertyName().equals("moveY")) {
                setLayoutY((int) e.getNewValue());
            }

            if (e.getPropertyName().equals("width")) {
                setWidth((int) e.getNewValue());
                rect1.setWidth((int) e.getNewValue());
                rect2.setWidth((int) e.getNewValue());
            }

            if (e.getPropertyName().equals("height")) {
                setHeight((int) e.getNewValue());
                rect1.setHeight((int) e.getNewValue());
                rect2.setHeight((int) e.getNewValue());
            }

            if (e.getPropertyName().equals("delete")) {
                deleteComponent();
            }
        });

        this.getChildren().addAll(rect2, rect1);

        setLayoutX(model.getPosX());
        setLayoutY(model.getPosY());

        initDragAndDrop();
    }

    private void initDragAndDrop() {
        setOnMousePressed(this::onMousePressed);
        setOnMouseReleased(this::onMouseReleased);
        setOnMouseDragged(this::onMouseDragged);
        setOnMouseEntered(this::onMouseEntered);
    }

    private void onMousePressed(MouseEvent event) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastClickTime < DOUBLE_CLICK_THRESHOLD) {
            onDoubleClick(event);
        }
        lastClickTime = currentTime;
    }

    public void onDoubleClick(MouseEvent event) {
        service.deleteEntity(model);
    }

    public void onMouseEntered(MouseEvent event) {
        this.setCursor(Cursor.HAND);
    }

    public void onMouseDragged(MouseEvent event) {
        toFront();
        moving = true;

        this.setCursor(Cursor.HAND);
        rect1.setX(event.getX());
        rect1.setY(event.getY());
    }

    private void onMouseReleased(MouseEvent event) {

        if (moving) {

            rect1.setX(0);
            rect1.setY(0);

            // Stoße Bewegung an
            service.bewegeEntity(model, event.getSceneX(), event.getSceneY());
            moving = false;
        }
    }

    private void deleteComponent() {
       parent.getChildren().remove(this);
    }

}
