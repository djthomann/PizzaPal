package de.hsrm.mi.swt.core.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LagerModel {

    private List<RegalModel> regale;
    private int width;
    private int height;

    public LagerModel(int width, int height) {
        regale = new ArrayList<>();
        this.width = width;
        this.height = height;
    }

    public Iterator<RegalModel> regalIterator() {
        return regale.iterator();
    }

    public int anzahlRegale() {
        return regale.size();
    }

    public void addRegal(RegalModel regal) {
        regale.add(regal);
    }

    public void removeRegal(RegalModel regal) {
        regale.remove(regal);
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

    public List<RegalModel> getRegale() {
        return regale;
    }

    public void setRegale(List<RegalModel> regale) {
        this.regale = regale;
    }

    

}
