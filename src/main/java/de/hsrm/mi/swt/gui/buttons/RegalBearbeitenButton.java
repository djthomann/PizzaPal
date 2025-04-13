package de.hsrm.mi.swt.gui.buttons;

import de.hsrm.mi.swt.gui.views.LagerView;
import de.hsrm.mi.swt.gui.views.RegalView;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;

public class RegalBearbeitenButton extends IconButton {

    private static final Image ACTIVE_ICON = loadImage("/icons/regalbearbeiten_active.png");

    public RegalBearbeitenButton(int x, int y) {
        super(ACTIVE_ICON, x, y, new Tooltip("Stützen & Bretter"));
        setOnAction(event -> openWindow());
    }

    private void openWindow() {
        RegalView currentRegalView = new RegalView();
        LagerView.pushFormView(currentRegalView);
    }

}
