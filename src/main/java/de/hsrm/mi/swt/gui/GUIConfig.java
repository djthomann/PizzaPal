package de.hsrm.mi.swt.gui;

import javafx.scene.paint.Color;

public class GUIConfig {

    public static final Color BRETT_FARBE = colorFromRGB(148, 104, 70);
    public static final Color STUETZE_FARBE = colorFromRGB(87, 61, 28);

    public static final Color LAGER_HINTERGRUND = new Color(0.8, 0.8, 0.8, 1);
    public static final Color TOOLBAR_HINTERGRUND = new Color(0.7, 0.7, 0.7, 1);

    public static final Color BUTTON_FARBE = new Color(0, 0, 0, 1);

    public static Color colorFromRGB(int r, int g, int b) {
        return new Color(r / 256f, g / 265f, b / 256f, 1);
    }

}
