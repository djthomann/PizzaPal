package de.hsrm.mi.swt.gui.views;

import java.beans.PropertyChangeEvent;
import java.util.Iterator;

import de.hsrm.mi.swt.core.application.repository.LagerRepository;
import de.hsrm.mi.swt.core.application.repository.RepositoryRegistry;
import de.hsrm.mi.swt.core.application.service.LagerService;
import de.hsrm.mi.swt.core.application.service.ServiceRegistry;
import de.hsrm.mi.swt.core.model.entities.BrettModel;
import de.hsrm.mi.swt.core.model.entities.PaketModel;
import de.hsrm.mi.swt.core.model.entities.StuetzeModel;
import de.hsrm.mi.swt.gui.components.BrettComponent;
import de.hsrm.mi.swt.gui.components.PaketComponent;
import de.hsrm.mi.swt.gui.components.StuetzeComponent;
import javafx.application.Platform;
import javafx.scene.layout.Pane;

public class ComponentView extends Pane {

    LagerService service = ServiceRegistry.getInstance().getLagerService();

    LagerRepository repository = RepositoryRegistry.getInstance().getLagerRepository();

    public ComponentView() {

        repository.addPropertyChangeListener((PropertyChangeEvent e) -> {
            switch(e.getPropertyName()) {
                case "new paket" -> {
                    PaketModel paket = (PaketModel) e.getNewValue();
                    // System.out.println(paket.getPosX());
                    Platform.runLater(() -> {
                        this.getChildren().add(new PaketComponent(this, (PaketModel) e.getNewValue()));
                    });
                }
                case "new stuetze" -> {
                    Platform.runLater(()-> {
                        this.getChildren().add(new StuetzeComponent(this, (StuetzeModel) e.getNewValue()));
                    });
                }
                case "new brett" -> {
                    Platform.runLater(()->
                    {this.getChildren().add(new BrettComponent(this, (BrettModel) e.getNewValue()));}
                    );
                }
            }
            
        });
        // initComponents();
    }

    public void initComponents() {

        Iterator<BrettModel> bretterIterator = service.holeBretter();
        while (bretterIterator.hasNext()) {
            this.getChildren().add(new BrettComponent(this, bretterIterator.next()));
        }

        Iterator<StuetzeModel> stuetzenIterator = service.holeStuetzen();
        while (stuetzenIterator.hasNext()) {
            this.getChildren().add(new StuetzeComponent(this, stuetzenIterator.next()));
        }

        Iterator<PaketModel> paketIterator = service.holePakete();
        while (paketIterator.hasNext()) {
            PaketModel nextPaket = paketIterator.next();
            this.getChildren().add(new PaketComponent(this, nextPaket));
        }

    }

}
