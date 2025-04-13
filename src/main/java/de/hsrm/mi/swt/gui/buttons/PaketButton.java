package de.hsrm.mi.swt.gui.buttons;

import de.hsrm.mi.swt.gui.views.LagerView;
import de.hsrm.mi.swt.gui.views.PaketView;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;

public class PaketButton extends IconButton {

    private static final Image ACTIVE_ICON = loadImage("/icons/paketanlegen_active.png");

    public PaketButton(int x, int y) {
        super(ACTIVE_ICON, x, y, new Tooltip("Paket erstellen"));
        setOnAction(event -> openWindow());
    }

    private void openWindow() {
        PaketView tempPaketView = new PaketView();
        LagerView.pushFormView(tempPaketView);
        // System.out.println("UI-View push: Paket");
    }
}