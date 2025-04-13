package de.hsrm.mi.swt.core.model.entities;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public abstract class Entity {

    protected int width;
    protected int height;

    protected int posX;
    protected int posY;

    PropertyChangeSupport support;

    public Entity() {
        this.support = new PropertyChangeSupport(this);
    }

    public abstract Entity makeInstance();

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }

    public void setPos(int x, int y) {

        posX = x;
        posY = y;
        support.firePropertyChange("moveX", 0, posX);
        support.firePropertyChange("moveY", 0, posY);
    }

    public void setDimension(int width, int height) {
        this.width = width;
        this.height = height;
        support.firePropertyChange("width", 0, width);
        support.firePropertyChange("height", 0, height);
    }

    public void delete() {
        support.firePropertyChange("delete", null, null);
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

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public String toCSV() {
        StringBuilder csvBuilder = new StringBuilder();
        csvBuilder.append(width).append(",");
        csvBuilder.append(height).append(",");
        csvBuilder.append(posX).append(",");
        csvBuilder.append(posY).append(",");

        return csvBuilder.toString();
    }

}
