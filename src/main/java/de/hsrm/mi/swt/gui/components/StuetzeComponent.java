package de.hsrm.mi.swt.gui.components;

import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import de.hsrm.mi.swt.core.application.service.LagerService;
import de.hsrm.mi.swt.core.application.service.ServiceRegistry;
import de.hsrm.mi.swt.core.model.entities.StuetzeModel;
import de.hsrm.mi.swt.gui.GUIConfig;
import de.hsrm.mi.swt.Config;

public class StuetzeComponent extends FancyComponent {

    public StuetzeComponent(Pane parent, StuetzeModel model) {
        super(parent, model);

        rect1.setFill(GUIConfig.STUETZE_FARBE);

        this.setOnMouseDragged(event -> {
            toFront();
            moving = true;

            rect1.setX(event.getX());
        });
    }
}
