package de.hsrm.mi.swt.gui.views;

import de.hsrm.mi.swt.gui.GUIConfig;
import de.hsrm.mi.swt.gui.buttons.IconButton;
import de.hsrm.mi.swt.gui.buttons.LoadButton;
import de.hsrm.mi.swt.gui.buttons.PaketButton;
import de.hsrm.mi.swt.gui.buttons.RegalBearbeitenButton;
import de.hsrm.mi.swt.gui.buttons.SaveButton;
import de.hsrm.mi.swt.gui.buttons.ZutatButton;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;

public class ToolBarView extends HBox {

    IconButton regalBearbeitenButton;
    IconButton paketeButton;
    IconButton inventarButton;
    IconButton zutatButton;
    IconButton paketButton;
    IconButton saveButton;
    IconButton loadButton;

    TemplateBar templateBar;

    public ToolBarView() {

        this.setMaxHeight(100);

        saveButton = new SaveButton(100, 100);
        this.getChildren().add(saveButton);

        loadButton = new LoadButton(100, 100);
        this.getChildren().add(loadButton);

        regalBearbeitenButton = new RegalBearbeitenButton(100, 100);
        this.getChildren().add(regalBearbeitenButton);

        zutatButton = new ZutatButton(200, 200);
        this.getChildren().add(zutatButton);

        paketButton = new PaketButton(200, 200);
        this.getChildren().add(paketButton);

        templateBar = new TemplateBar();
        this.getChildren().add(templateBar);

        this.setBackground(
                new Background(new BackgroundFill(GUIConfig.TOOLBAR_HINTERGRUND, CornerRadii.EMPTY, Insets.EMPTY)));
    }

}
