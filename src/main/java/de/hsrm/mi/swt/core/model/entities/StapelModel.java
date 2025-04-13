package de.hsrm.mi.swt.core.model.entities;

import java.util.Iterator;
import java.util.List;

public class StapelModel extends Entity {

    private int gewicht;
    private int width;
    private int height;
    List<PaketModel> pakete;

    public StapelModel(int gewicht, int width, int height, List<PaketModel> pakete) {
        this.gewicht = gewicht;
        this.width = width;
        this.height = height;
        this.pakete = pakete;
    }

    public String toCSV() {
        return "Stapel";
    }

    public int getGewicht() {
        return gewicht;
    }

    public void setGewicht(int gewicht) {
        this.gewicht = gewicht;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public List<PaketModel> getPakete() {
        return pakete;
    }

    public void setPakete(List<PaketModel> pakete) {
        this.pakete = pakete;
    }

    public void addPaket(PaketModel paket) {
        this.pakete.add(paket);
    }

    public void removePaket(PaketModel paket) {
        this.pakete.remove(paket);
    }

    public Iterator<PaketModel> paketIterator() {
        return pakete.iterator();
    }

    @Override
    public Entity makeInstance() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'makeInstance'");
    }

}
