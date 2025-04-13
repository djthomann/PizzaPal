package de.hsrm.mi.swt.core.model.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import de.hsrm.mi.swt.core.application.repository.RepositoryRegistry;
import de.hsrm.mi.swt.core.application.repository.ZutatRepository;
import de.hsrm.mi.swt.core.model.entities.exceptions.NichtMehrGenugPlatzException;
import de.hsrm.mi.swt.core.model.entities.exceptions.PaketHaengtUeberException;
import de.hsrm.mi.swt.core.model.entities.exceptions.PaketZuSchwerException;
import de.hsrm.mi.swt.core.model.entities.exceptions.StapelException;
import de.hsrm.mi.swt.core.model.zutat.Zutat;
import de.hsrm.mi.swt.core.validation.exceptions.MuessteGestapeltWerdenException;
import de.hsrm.mi.swt.core.validation.exceptions.PaketSchneidetAnderesPaket;
import javafx.scene.paint.Color;

public class PaketModel extends Ablageflaeche {

    static int counter = 0;
    static Set<Integer> ids = new HashSet<>();
    int id = 0;

    private int weight;
    private Zutat inhalt;
    private int gewicht;
    private int tragkraft;

    private Ablageflaeche ablageFlaeche; // Paket gehört ja zum Stapel, und der Stapel dann zum Brett, muss sich das
                                         // Paket das Brett merken?

    List<PaketModel> paketeDrauf;

    public PaketModel(int width, int height, int xPos, int yPos, int weight, Zutat zutat) {
        this(width, height, xPos, yPos, weight, 0, zutat);
    }

    public PaketModel(int width, int height, int xPos, int yPos, int weight, Zutat zutat, BrettModel brett) {
        this(width, height, xPos, yPos, weight, 0, zutat, brett);
    }

    public PaketModel(int width, int height, int xPos, int yPos, int weight, int tragkraft, Zutat zutat) {
        this(width, height, xPos, yPos, weight, tragkraft, zutat, null);
    }

    public PaketModel(int width, int height, int xPos, int yPos, int weight, int tragkraft, Zutat zutat,
            Ablageflaeche ablageflaeche) {
        this(counter++, width, height, xPos, yPos, weight, tragkraft, zutat, null);
    }

    public PaketModel(int id, int width, int height, int xPos, int yPos, int weight, int tragkraft, Zutat zutat,
            Ablageflaeche ablageflaeche) {

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
        this.tragkraft = tragkraft;
        if (ablageflaeche == null) {
            posX = xPos;
            posY = yPos;
        } else {
            posX = ablageflaeche.getPosX() + xPos;
            posY = ablageflaeche.getPosY() - height;
        }
        this.weight = weight;
        inhalt = zutat;
        ablageFlaeche = ablageflaeche;
        paketeDrauf = new ArrayList<>();
    }

    public static void resetCounter() {
        counter = 0;
        ids.clear();
    } 

    public String toCSV() {
        StringBuilder csvBuilder = new StringBuilder();
        csvBuilder.append("Paket").append(",");
        csvBuilder.append(id).append(",");
        csvBuilder.append(width).append(",");
        csvBuilder.append(height).append(",");
        if (ablageFlaeche != null) {
            csvBuilder.append(posX - ablageFlaeche.getPosX()).append(",");
        } else {
            csvBuilder.append(posX).append(",");
        }
        csvBuilder.append(posY).append(",");
        csvBuilder.append(weight).append(",");
        csvBuilder.append(inhalt.getName()).append(",");
        csvBuilder.append(tragkraft).append(",");

        return csvBuilder.toString();
    }

    public static PaketModel fromCSV(String csv) {
        String[] parts = csv.split(",");
        int id = Integer.parseInt(parts[1]);
        int width = Integer.parseInt(parts[2]);
        int height = Integer.parseInt(parts[3]);
        int posX = Integer.parseInt(parts[4]);
        int posY = Integer.parseInt(parts[5]);
        int weight = Integer.parseInt(parts[6]);
        int tragkraft = Integer.parseInt(parts[8]);

        return new PaketModel(id, width, height, posX, posY, weight, tragkraft, null, null);
    }

    @Override
    public void setPos(int x, int y) {

        // Stapel mitnehmen
        for (PaketModel paket : paketeDrauf) {
            int relX = paket.getPosX() - posX;
            int newX = x + (relX);
            int newY = y - paket.getHeight();
            paket.setPos(newX, newY);
        }

        super.setPos(x, y);
    }

    public int getTragkraft() {
        return tragkraft;
    }

    public void setTragkraft(int tragkraft) {
        this.tragkraft = tragkraft;
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

    public Zutat getInhalt() {
        return inhalt;
    }

    public void setInhalt(Zutat inhalt) {
        this.inhalt = inhalt;
    }

    public Ablageflaeche getAblageFlaeche() {
        return ablageFlaeche;
    }

    public void setAblageFlaeche(Ablageflaeche ablageFlaeche) {
        if (this.ablageFlaeche == null) {
            posX = ablageFlaeche.getPosX() + posX;
        }
        this.ablageFlaeche = ablageFlaeche;
    }

    public int getWeight() {
        return weight;
    }

    public int breitePaketeDrauf() {
        int width = 0;
        for (PaketModel paket : paketeDrauf) {
            width += paket.getWidth();
        }
        return width;
    }

    public int gewichtPaketeDrauf() {
        int weight = 0;
        for (PaketModel paket : paketeDrauf) {
            weight += paket.getWeight();
        }
        return weight;
    }

    public PaketModel getPaketAnPosition(int posX) {

        for (PaketModel paket : paketeDrauf) {
            if (paket.getPosX() < posX && paket.getPosX() + paket.getWidth() > posX) {
                return paket;
            }
        }
        return null;

    }

    public void stellePaketDrauf(PaketModel paket) throws StapelException {
        stellePaketDrauf(paket, this.getPosX());
    }

    public void stellePaketDrauf(PaketModel paket, int posX) throws StapelException {

        // Steht das Paket schon drauf?
        boolean neuesPaket = !paketeDrauf.contains(paket);

        // Würde das Paket in meinen Grenzen stehen?
        if ((posX > this.posX && this.posX + this.width < posX + paket.getWidth())) {
            throw new PaketHaengtUeberException("Paket steht nicht in den Grenzen");
        }

        // Ist an dieser Stelle Platz?
        PaketModel paketAnPosition = getPaketAnPosition(posX);
        if (paketAnPosition != null && paketAnPosition != paket) {
            paketAnPosition.stellePaketDrauf(paket, posX);
        }

        if (neuesPaket) {
            // Ist noch genug Platz da, allgemein?
            int restlicherPlatz = getWidth() - breitePaketeDrauf();
            if (restlicherPlatz < paket.getWidth()) {
                throw new NichtMehrGenugPlatzException("Nicht mehr genug Platz");
            }

            // Ist Paket nicht zu schwer?
            int restlicheTragkraft = getTragkraft() - gewichtPaketeDrauf();
            if (restlicheTragkraft < paket.getWeight()) {
                // System.out.println("Zu schwer");
                throw new PaketZuSchwerException("Paket ist zu schwer für Stapel");
            }

            paketeDrauf.add(paket);
            paketeDrauf.sort(new Comparator<PaketModel>() {

                @Override
                public int compare(PaketModel o1, PaketModel o2) {
                    return o1.getPosX() - o2.getPosX();
                }

            });

        }

        paket.setAblageFlaeche(this);
        // System.out.println("Drauf stellen");
        paket.setPos(posX, posY - paket.getHeight());

    }

    public int getStapelHoehe() {
        List<Integer> hoehen = new ArrayList<>();
        for (PaketModel paket : paketeDrauf) {
            hoehen.add(paket.getStapelHoehe());
        }
        if (hoehen.size() > 0) {
            return height + Collections.max(hoehen);
        } else {
            return height;
        }
    }

    public List<PaketModel> getPakete() {
        return paketeDrauf;
    }

    public Iterator<PaketModel> paketIterator() {
        return paketeDrauf.iterator();
    }

    @Override
    public Entity makeInstance() {
        return new PaketModel(width, height, posX, posY, weight, tragkraft, inhalt);
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public void nehmePaketRunter(PaketModel paket) {
        paketeDrauf.remove(paket);
    }

    @Override
    public void delete() {
        super.delete();
        // Aufräumen
        if(ablageFlaeche != null) {
            this.getAblageFlaeche().nehmePaketRunter(this);
        }

        // Aufräumen --> Stapel löschen
        Iterator<PaketModel> paketIterator = paketIterator();
        while (paketIterator.hasNext()) {
            PaketModel paketPointer = paketIterator.next();
            paketIterator.remove();
            paketPointer.delete();
        }
    }

    public int getId() {
        return id;
    }
}