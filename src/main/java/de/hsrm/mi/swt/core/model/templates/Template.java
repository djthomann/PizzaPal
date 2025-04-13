package de.hsrm.mi.swt.core.model.templates;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashSet;
import java.util.Set;

import de.hsrm.mi.swt.core.model.entities.BrettModel;
import de.hsrm.mi.swt.core.model.entities.Entity;
import de.hsrm.mi.swt.core.model.entities.PaketModel;
import de.hsrm.mi.swt.core.model.entities.StuetzeModel;
import de.hsrm.mi.swt.core.model.zutat.Zutat;
import javafx.scene.paint.Color;

public class Template implements TemplateFactory {

    private PropertyChangeSupport support;

    private Entity entity;

    public Template() {
        this(null);
    }

    public Template(Entity e) {
        entity = e;
        this.support = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {

        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }

    public Entity getEntity() {
        return entity;
    }

    @Override
    public PaketModel createPaket(PaketModel paket) {
        return paket;
    }

    public Entity createEntity() {
        return entity.makeInstance();
    }

    public void fireDelete() {
        support.firePropertyChange("deleted", false, true);
    }

    public String toCSV() {
        return entity.toCSV();
    }

    public static Template fromCSV(String csv) {
        String[] parts = csv.split(",", 3);
        String type = parts[0];
        switch (type) {
            case "Paket" -> {
                return new Template(PaketModel.fromCSV(csv));
            }
            case "Brett" -> {
                return new Template(BrettModel.fromCSV(csv));
            }
            case "Stuetze" -> {
                return new Template(StuetzeModel.fromCSV(csv));
            }
            default -> {
                return null;
            }
        }
    }
}
