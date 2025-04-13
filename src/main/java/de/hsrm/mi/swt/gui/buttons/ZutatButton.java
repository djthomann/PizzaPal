package de.hsrm.mi.swt.gui.buttons;

import de.hsrm.mi.swt.gui.views.LagerView;
import de.hsrm.mi.swt.gui.views.ZutatView;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;

public class ZutatButton extends IconButton {

    private static final Image ACTIVE_ICON = loadImage("/icons/zutat_active.png");

    public ZutatButton(int x, int y) {
        super(ACTIVE_ICON, x, y, new Tooltip("Zutaten"));
        setOnAction(event -> openWindow());
    }

    private void openWindow() {
        ZutatView tempZutatView = new ZutatView();
        LagerView.pushFormView(tempZutatView);
    }
}
