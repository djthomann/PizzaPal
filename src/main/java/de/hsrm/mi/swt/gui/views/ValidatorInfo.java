package de.hsrm.mi.swt.gui.views;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import de.hsrm.mi.swt.Helper;
import de.hsrm.mi.swt.core.application.service.ServiceRegistry;
import de.hsrm.mi.swt.core.validation.LagerValidator;
import de.hsrm.mi.swt.gui.buttons.IconButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class ValidatorInfo extends BorderPane {

    private Text textField;

    private Button closeButton;

    private static Image closeIcon = Helper.loadImage("/icons/x_white.png");
    private ImageView closeIconView;

    LagerValidator validator = ServiceRegistry.getInstance().getLagerService().getLagerValidator();

    public ValidatorInfo() {
        this.setBackground(new Background(new BackgroundFill(new Color(1, 0, 0, 1), CornerRadii.EMPTY, Insets.EMPTY)));
        this.setMaxHeight(100);

        textField = new Text();
        textField.setFill(Color.WHITE);
        textField.setFont(new Font(18));

        setMargin(textField, new Insets(0, 0, 0, 10));
        setAlignment(textField, Pos.CENTER_LEFT);

        closeButton = new Button();
        closeButton.setStyle("-fx-background-color: transparent;");
        closeButton.setOnMouseEntered(e -> {
            closeButton.setCursor(Cursor.HAND);
        });

        closeIconView = new ImageView(closeIcon);
        closeIconView.setFitHeight(20);
        closeIconView.setFitWidth(20);
        closeButton.setGraphic(closeIconView);

        this.setLeft(textField);
        this.setRight(closeButton);

        validator.addPropertyChangeListener((PropertyChangeEvent e) -> {

            if (e.getPropertyName().equals("new info")) {
                this.setVisible(true);
                textField.setText(e.getNewValue().toString());
            }
        });

        this.setVisible(false);

        init();
    }

    public void init() {
        closeButton.setOnAction(e -> {
            this.setVisible(false);
        });
    }

}
