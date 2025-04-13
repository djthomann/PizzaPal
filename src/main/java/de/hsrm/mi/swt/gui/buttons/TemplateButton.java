package de.hsrm.mi.swt.gui.buttons;

import java.beans.PropertyChangeEvent;

import de.hsrm.mi.swt.core.application.service.LagerService;
import de.hsrm.mi.swt.core.application.service.ServiceRegistry;
import de.hsrm.mi.swt.core.application.service.TemplateService;
import de.hsrm.mi.swt.core.model.entities.BrettModel;
import de.hsrm.mi.swt.core.model.entities.Entity;
import de.hsrm.mi.swt.core.model.entities.PaketModel;
import de.hsrm.mi.swt.core.model.templates.Template;
import de.hsrm.mi.swt.gui.GUIConfig;
import de.hsrm.mi.swt.gui.views.TemplateBar;
import javafx.scene.Cursor;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class TemplateButton extends Pane {

    private class DeleteTemplateButton extends StackPane {

        private static Image deleteIcon = IconButton.loadImage("/icons/x.png");
        private Rectangle background = new Rectangle(12, 12, Color.WHITE);

        private DeleteTemplateButton() {

            ImageView imageView = new ImageView(deleteIcon);

            imageView.setFitHeight(10);
            imageView.setFitWidth(10);
            imageView.setImage(deleteIcon);

            this.setOnMouseClicked(event -> {
                templateService.loescheTemplate(template);
            });

            this.getChildren().addAll(background, imageView);

        }

    }

    ServiceRegistry sr = ServiceRegistry.getInstance();
    TemplateService templateService = sr.getTemplateService();
    LagerService lagerService = sr.getLagerService();

    private Rectangle preview;
    private Rectangle background = new Rectangle(30, 30, Color.LIGHTGRAY);
    private DeleteTemplateButton deleteTemplateButton;
    private Template template;
    private TemplateBar root;

    public TemplateButton(TemplateBar root, Template template) {

        this.template = template;
        this.root = root;

        // Make and scale preview
        Entity entity = template.getEntity();
        int previewHeight = entity.getHeight();
        int previewWidth = entity.getWidth();
        Color previewColor = null;
        if (entity instanceof PaketModel) {
            PaketModel paket = (PaketModel) entity;
            previewColor = paket.getInhalt().getColor();
            Tooltip.install(this, new Tooltip(paket.getInhalt().getName()));
        } else if (entity instanceof BrettModel) {
            Tooltip.install(this, new Tooltip("Brett"));
            previewColor = GUIConfig.BRETT_FARBE;
        } else {
            Tooltip.install(this, new Tooltip("Stütze"));
            previewColor = GUIConfig.STUETZE_FARBE;
        }

        int maxDimension = Math.max(previewHeight, previewWidth);
        float scale = 25f / maxDimension;
        int previewHeightScaled = (int) (previewHeight * scale);
        int previewWidthScaled = (int) (previewWidth * scale);
        preview = new Rectangle(previewWidthScaled, previewHeightScaled, previewColor);

        template.addPropertyChangeListener((PropertyChangeEvent e) -> {

            if (e.getPropertyName().equals("deleted")) {
                deleteSelf();
            }
        });

        this.setWidth(30);
        this.setHeight(30);

        this.getChildren().add(background);
        this.getChildren().add(preview);

        deleteTemplateButton = new DeleteTemplateButton();
        this.getChildren().add(deleteTemplateButton);

        // Zentrieren des Delete-Buttons
        deleteTemplateButton.setTranslateX(this.getWidth() - 10 - 2);
        deleteTemplateButton.setTranslateY(this.getHeight() - 10 - 2);

        // Zentrieren des Preview-Elements
        preview.setTranslateX((this.getWidth() - preview.getWidth()) / 2);
        preview.setTranslateY((this.getHeight() - preview.getHeight()) / 2);

        init();
    }

    public void init() {
        this.setOnMouseClicked(event -> {
            // Leeres MouseClicked-Event
        });

        this.setOnMouseEntered(event -> {
            this.setCursor(Cursor.HAND);
        });

        this.setOnMouseDragged(event -> {
            preview.setTranslateX(event.getX() - preview.getWidth() / 2);
            preview.setTranslateY(event.getY() - preview.getHeight() / 2);
            this.setCursor(Cursor.HAND);
        });

        this.setOnMouseExited(event -> {
            this.setCursor(Cursor.DEFAULT);
        });

        this.setOnMouseReleased(event -> {

            double x = event.getX();
            double y = event.getY();

            if (x > 0 && y > 0 && x < getWidth() && y < getHeight()) {
                // Wenn Mouse im Button losgelassen wird, passiert nichts
                return;
            }

            // Entity erstellen anfangen
            lagerService.addEntity(template.createEntity(), (int) event.getSceneX(), (int) event.getSceneY());

            // Preview-Element wieder zentrieren
            preview.setTranslateX((this.getWidth() - preview.getWidth()) / 2);
            preview.setTranslateY((this.getHeight() - preview.getHeight()) / 2);
        });
    }

    public void deleteSelf() {
        root.getChildren().remove(this);
    }

}
