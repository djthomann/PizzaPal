package de.hsrm.mi.swt.gui.components;

import java.io.Serializable;

import de.hsrm.mi.swt.core.application.service.LagerService;
import de.hsrm.mi.swt.core.application.service.ServiceRegistry;
import de.hsrm.mi.swt.core.model.entities.PaketModel;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;

public class PaketComponent extends FancyComponent implements Serializable {

    LagerService service = ServiceRegistry.getInstance().getLagerService();

    public PaketComponent(Pane parent, PaketModel paketModel) {
        super(parent, paketModel);

        Tooltip t = new Tooltip(paketModel.getInhalt().getName());
        Tooltip.install(this, t);
         

        rect1.setFill(paketModel.getInhalt().getColor());

    }

}
