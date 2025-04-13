package de.hsrm.mi.swt.core.model.zutat;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;

import de.hsrm.mi.swt.core.application.repository.ZutatRepository;
import javafx.scene.paint.Color;

public class Zutat {
    @JsonBackReference
    private String name;
    private Color color;
    private Set<Zutat> unvertraeglichkeiten;
    PropertyChangeSupport support;

    public Zutat(String name, Color color) {
        this(name, color, null);
        this.support = new PropertyChangeSupport(this);
    }

    public Zutat(String name, Color color, Set<Zutat> unvertraeglichkeiten) {
        this.name = name;
        this.color = color;
        this.support = new PropertyChangeSupport(this);
        if (unvertraeglichkeiten != null) {
            this.unvertraeglichkeiten = unvertraeglichkeiten;
        } else {
            unvertraeglichkeiten = new HashSet<>();
        }
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Set<Zutat> getUnvertraeglichkeiten() {
        return unvertraeglichkeiten;
    }

    public void setUnvertraeglichkeiten(Set<Zutat> unvertraeglichkeiten) {
        Set<Zutat> oldValue = this.unvertraeglichkeiten;
        Set<Zutat> newValue = unvertraeglichkeiten;
        this.unvertraeglichkeiten = unvertraeglichkeiten;
        support.firePropertyChange("set unvertraeglichkeiten", oldValue, newValue);
    }

    public void addUnvertraeglichkeit(Zutat unvertraeglichkeit) {
        Zutat newValue = unvertraeglichkeit;
        this.unvertraeglichkeiten.add(unvertraeglichkeit);
        support.firePropertyChange("add unvertraeglichkeit", null, newValue);
    }

    public void removeUnvertraeglichkeit(Zutat unvertraeglichkeit) {
        this.unvertraeglichkeiten.remove(unvertraeglichkeit);
        support.firePropertyChange("remove unvertraeglichkeit", unvertraeglichkeit, null);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    public String unvertraeglichkeitenToCSV() {
        StringBuilder csvBuilder = new StringBuilder();
        csvBuilder.append(name).append(",");

        for (Zutat z : unvertraeglichkeiten) {
            csvBuilder.append(z.getName()).append(";");
        }

        // Remove the last semicolon
        if (!unvertraeglichkeiten.isEmpty()) {
            csvBuilder.setLength(csvBuilder.length() - 1);
        }

        return csvBuilder.toString();
    }

    public String toCSV() {
        StringBuilder csvBuilder = new StringBuilder();
        csvBuilder.append(name).append(",");
        csvBuilder.append(color.toString()).append(",");

        return csvBuilder.toString();
    }

    public static Zutat fromCSV(String csv) {
        String[] parts = csv.split(",", 3);
        String name = parts[0];
        Color color = Color.valueOf(parts[1]);
        Set<Zutat> unvertraeglichkeiten = new HashSet<>();

        /*
         * if (parts.length == 3 && !parts[2].isEmpty()) {
         * String[] unvertraeglichkeitenNames = parts[2].split(";");
         * for (String unvertraeglichkeitName : unvertraeglichkeitenNames) {
         * Zutat unvertraeglichkeit =
         * zutatRepository.getZutaten().get(unvertraeglichkeitName);
         * if (unvertraeglichkeit != null) {
         * unvertraeglichkeiten.add(unvertraeglichkeit);
         * }
         * }
         * }
         */

        return new Zutat(name, color, unvertraeglichkeiten);
    }

}
