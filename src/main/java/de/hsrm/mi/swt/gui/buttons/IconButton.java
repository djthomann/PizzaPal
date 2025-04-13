package de.hsrm.mi.swt.gui.buttons;

import java.io.InputStream;

import de.hsrm.mi.swt.gui.GUIConfig;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

public class IconButton extends Button {

    private Image icon;
    private int posX;
    private int posY;

    public static final int SIZE = 50;
    public static final Shape shape = new Circle(SIZE);
    ImageView imageView;

    public IconButton() {
        this(null, 100, 100, null);
    }

    public IconButton(Image icon, int posX, int posY, Tooltip tooltip) {
        this.icon = icon;
        this.posX = posX;
        this.posY = posY;
        this.setTooltip(tooltip);
        setHeight(SIZE);
        setWidth(SIZE);
        setShape(shape);
        shape.setFill(GUIConfig.BUTTON_FARBE);
        setBackground(getBackground());
        // setBackground(new Background(new BackgroundFill(GUIConfig.BUTTON_FARBE,
        // CornerRadii.EMPTY, Insets.EMPTY)));

        if (icon != null) {
            imageView = new ImageView(icon);
            imageView.setFitWidth(SIZE / 2); // Größe des Icons
            imageView.setFitHeight(SIZE / 2); // Größe des Icons
            setGraphic(imageView);
        }

        this.setOnMouseEntered(event -> {
            this.setCursor(Cursor.HAND);
        });
    }

    public void setIcon(Image icon) {
        imageView.setImage(icon);
    }

    protected static Image loadImage(String path) {
        InputStream input = IconButton.class.getResourceAsStream(path);
        if (input == null) {
            throw new IllegalArgumentException("Bild nicht gefunden: " + path);
        }
        return new Image(input);
    }

}
