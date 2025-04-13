package de.hsrm.mi.swt.core.model.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import de.hsrm.mi.swt.Config;
import de.hsrm.mi.swt.core.model.RegalModel;

public class StuetzeModel extends Entity {

    private RegalModel regal;

    static int counter = 0;
    static Set<Integer> ids = new HashSet<>();
    int id = 0;

    private Set<BrettModel> bretterRechts = new HashSet<>();
    private Set<BrettModel> bretterLinks = new HashSet<>();

    public StuetzeModel(int width, int height) {
        this(width, height, 0);
    }

    public StuetzeModel(int width, int height, int posX) {
        this(counter++, width, height, posX);
    }

    public StuetzeModel(int id, int width, int height, int posX) {
        super();

        while (this.id == 0) {
            if (!ids.contains(id)) {
                this.id = id;
                ids.add(id);
            } else {
                id = counter++;
            }
        }

        this.width = width;
        this.height = height;
        this.posX = posX;
        posY = Config.SCENE_H - height;
    }

    public static void resetCounter() {
        counter = 0;
        ids.clear();
    } 

    public String toCSV() {
        StringBuilder csvBuilder = new StringBuilder();
        csvBuilder.append("Stuetze").append(",");
        csvBuilder.append(id).append(",");
        csvBuilder.append(super.toCSV());

        return csvBuilder.toString();
    }

    public static StuetzeModel fromCSV(String csv) {

        String[] parts = csv.split(",");
        int id = Integer.parseInt(parts[1]);
        int width = Integer.parseInt(parts[2]);
        int height = Integer.parseInt(parts[3]);
        int posX = Integer.parseInt(parts[4]);
        int posY = Integer.parseInt(parts[5]);

        return new StuetzeModel(id, width, height, posX);
    }

    @Override
    public void setPos(int x, int y) {
        posX = x;
        support.firePropertyChange("moveX", 0, posX);

        // Bretter anpassen
        for (BrettModel brett : bretterLinks) {
            int width = brett.getPosX() + brett.getWidth() - this.getPosX();
            brett.setDimension(width, brett.getHeight());
            brett.setPos(brett.getStuetzeLinks().getPosX() + brett.getStuetzeLinks().getWidth(), brett.getPosY());
        }

        for (BrettModel brett : bretterRechts) {
            int width = brett.getPosX() + brett.getWidth() - this.getPosX();
            brett.setDimension(width, brett.getHeight());
            brett.setPos(x + getWidth(), brett.getPosY());
        }

    }

    public List<BrettModel> getBretterLinksImIntervall(int posY, int height) {

        List<BrettModel> gefundeneBretter = new ArrayList<>();

        int oben = posY;
        int unten = posY + height;

        for (BrettModel brett : bretterLinks) {

            if (!(brett.getPosY() > unten || brett.getPosY() + brett.getHeight() < posY)) {
                gefundeneBretter.add(brett);
            }

        }
        return null;
    }

    public BrettModel naechstesBrettDrueberRechts(int y) {
        BrettModel brettDrueber = null;

        for (BrettModel brett : bretterRechts) {
            if (brett.getPosY() < y) {

                if (brettDrueber == null) {
                    brettDrueber = brett;
                    continue;
                } else if (brett.getPosY() > brettDrueber.getPosY()) {
                    brettDrueber = brett;
                }
            }
        }

        return brettDrueber;
    }

    public BrettModel naechstesBrettDrueberLinks(int y) {

        BrettModel brettDrueber = null;

        for (BrettModel brett : bretterLinks) {
            if (brett.getPosY() < y) {

                if (brettDrueber == null) {
                    brettDrueber = brett;
                    continue;
                } else if (brett.getPosY() > brettDrueber.getPosY()) {
                    brettDrueber = brett;
                }
            }
        }

        return brettDrueber;
    }

    public BrettModel naechstesBrettDrunterRechts(int y) {
        BrettModel brettDrunter = null;

        for (BrettModel brett : bretterRechts) {
            if (brett.getPosY() > y) {

                if (brettDrunter == null) {
                    brettDrunter = brett;
                    continue;
                } else if (brett.getPosY() < brettDrunter.getPosY()) {
                    brettDrunter = brett;
                }
            }
        }

        return brettDrunter;
    }

    public BrettModel naechstesBrettDrunterLinks(int y) {

        BrettModel brettDrueber = null;

        for (BrettModel brett : bretterLinks) {
            if (brett.getPosY() > y) {

                if (brettDrueber == null) {
                    brettDrueber = brett;
                    continue;
                } else if (brett.getPosY() < brettDrueber.getPosY()) {
                    brettDrueber = brett;
                }
            }
        }

        return brettDrueber;
    }

    public List<BrettModel> getBretterRechtsImIntervall(int oben, int unten) {

        List<BrettModel> gefundeneBretter = new ArrayList<>();

        for (BrettModel brett : bretterRechts) {

            if (!(brett.getPosY() > unten || brett.getPosY() + brett.getHeight() < oben)) {
                gefundeneBretter.add(brett);
            }

        }
        return gefundeneBretter;
    }

    public Iterator<BrettModel> bretterRechts() {
        return bretterRechts.iterator();
    }

    public Iterator<BrettModel> bretterLinks() {
        return bretterLinks.iterator();
    }

    public boolean hatBretterRechts() {
        return !bretterRechts.isEmpty();
    }

    public boolean hatBretterLinks() {
        return !bretterLinks.isEmpty();
    }

    public int anzahlBretterRechts() {
        return bretterRechts.size();
    }

    public int anzahlBretterLinks() {
        return bretterLinks.size();
    }

    public void loeseBrett(BrettModel b) {
        if (!bretterLinks.remove(b)) {
            bretterRechts.remove(b);
        }
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

    public RegalModel getRegal() {
        return regal;
    }

    public void setRegal(RegalModel regal) {
        this.regal = regal;
    }

    @Override
    public Entity makeInstance() {
        return new StuetzeModel(width, height, posX);
    }

    public void addBrettRechts(BrettModel b) {
        bretterRechts.add(b);
    }

    public void addBrettLinks(BrettModel b) {
        bretterLinks.add(b);
    }

    public void removeBrettRechts(BrettModel b) {
        bretterRechts.remove(b);
    }

    public void removeBrettLinks(BrettModel b) {
        bretterLinks.remove(b);
    }

    public Set<BrettModel> getBretterRechts() {
        return bretterRechts;
    }

    public void setBretterRechts(Set<BrettModel> bretterRechts) {
        this.bretterRechts = bretterRechts;
    }

    public Set<BrettModel> getBretterLinks() {
        return bretterLinks;
    }

    public void setBretterLinks(Set<BrettModel> bretterLinks) {
        this.bretterLinks = bretterLinks;
    }

    public int getId() {
        return id;
    }

}
