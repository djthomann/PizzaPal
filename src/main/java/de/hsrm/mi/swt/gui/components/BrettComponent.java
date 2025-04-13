package de.hsrm.mi.swt.gui.components;

import de.hsrm.mi.swt.core.application.service.LagerService;
import de.hsrm.mi.swt.core.application.service.ServiceRegistry;
import de.hsrm.mi.swt.core.model.entities.BrettModel;
import de.hsrm.mi.swt.gui.GUIConfig;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public class BrettComponent extends FancyComponent {

    public BrettComponent(Pane parent, BrettModel brettModel) {
        super(parent, brettModel);

        rect1.setFill(GUIConfig.BRETT_FARBE);
    }

}
