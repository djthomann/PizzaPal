package de.hsrm.mi.swt;

import java.io.InputStream;

import de.hsrm.mi.swt.gui.buttons.IconButton;
import javafx.scene.image.Image;

public class Helper {

    public static Image loadImage(String path) {
        InputStream input = IconButton.class.getResourceAsStream(path);
        if (input == null) {
            throw new IllegalArgumentException("Bild nicht gefunden: " + path);
        }
        return new Image(input);
    }

}
