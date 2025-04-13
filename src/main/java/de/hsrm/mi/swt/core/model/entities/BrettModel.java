package de.hsrm.mi.swt.core.model.entities;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import de.hsrm.mi.swt.core.model.RegalModel;

public class BrettModel extends Ablageflaeche {

    static int counter = 0;
    static Set<Integer> ids = new HashSet<>();
    int id = 0;

    private StuetzeModel stuetzeLinks;
    private StuetzeModel stuetzeRechts;

    private PaketModel paketModel;
    private List<PaketModel> pakete = new ArrayList<>();

    private int tragkraft;

    public BrettModel(int width, int height, int tragkraft) {
        this(width, height, 0, null, null, tragkraft);
    }

    public BrettModel(int width, int height, int yPos, StuetzeModel stuetzeLinks, StuetzeModel stuetzeRechts,
            int tragkraft) {
        this(counter++, width, height, yPos, stuetzeLinks, stuetzeRechts, tragkraft);
    }

    public BrettModel(int id, int width, int height, int yPos, StuetzeModel stuetzeLinks, StuetzeModel stuetzeRechts,
            int tragkraft) {
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
        this.posY = yPos;

        if (stuetzeLinks != null) {
            this.posX = stuetzeLinks.getPosX() + stuetzeLinks.getWidth();
            this.posY = stuetzeLinks.getPosY() + stuetzeLinks.getHeight() - yPos - height;
        }

        this.stuetzeLinks = stuetzeLinks;
        this.stuetzeRechts = stuetzeRechts;
        this.tragkraft = tragkraft;
    }

    public static void resetCounter() {
        counter = 0;
        ids.clear();
    } 

    public String toCSV() {
        StringBuilder csvBuilder = new StringBuilder();
        csvBuilder.append("Brett").append(",");
        csvBuilder.append(id).append(",");
        csvBuilder.append(width).append(",");
        csvBuilder.append(height).append(",");
        csvBuilder.append(posX).append(",");
        if (stuetzeLinks != null) {
            csvBuilder.append(posY - stuetzeLinks.getPosY()).append(",");
        } else {
            csvBuilder.append(0).append(",");
        }
        csvBuilder.append(tragkraft).append(",");

        return csvBuilder.toString();
    }

    public static BrettModel fromCSV(String csv) {

        String[] parts = csv.split(",");
        int id = Integer.parseInt(parts[1]);
        int width = Integer.parseInt(parts[2]);
        int height = Integer.parseInt(parts[3]);
        int posX = Integer.parseInt(parts[4]);
        int posY = Integer.parseInt(parts[5]);
        int tragkraft = Integer.parseInt(parts[6]);

        return new BrettModel(id, width, height, posY, null, null, tragkraft);
    }

    @Override
    public void setPos(int x, int y) {

        super.setPos(x, y);

        // Breite anpassen
        breiteAnpassen();

        // Stapel mitnehmen
        for (PaketModel paket : pakete) {
            paket.setPos(x + paket.getPosX() - getPosX(), y - paket.getHeight());
        }
    }

    public void breiteAnpassen() {
        if (stuetzeLinks != null && stuetzeRechts != null) {

            int width = this.getStuetzeRechts().getPosX() - this.getStuetzeRechts().getWidth()
                    - this.getStuetzeLinks().getPosX();
            setDimension(width, getHeight());
        }
    }

    public void positionAnpassen() {
        if (stuetzeLinks != null) {
            this.posX = stuetzeLinks.getPosX() + stuetzeLinks.getWidth();
            this.posY = stuetzeLinks.getPosY() + stuetzeLinks.getHeight() - posY - height;
        }
    }

    public PaketModel getPaketAnPosition(int posX) {

        for (PaketModel paket : pakete) {
            if (paket.getPosX() < posX && paket.getPosX() + paket.getWidth() > posX) {
                return paket;
            }
        }
        return null;

    }

    public PaketModel getPaketModel() {
        return paketModel;
    }

    public List<PaketModel> getPakete() {
        return pakete;
    }

    public void setPaketModel(PaketModel paketModel) {
        this.paketModel = paketModel;
    }

    public void stellePaketDrauf(PaketModel paket) {
        paketModel = paket;
        pakete.add(paket);
        pakete.sort(new Comparator<PaketModel>() {

            @Override
            public int compare(PaketModel o1, PaketModel o2) {
                return o1.getPosX() - o2.getPosX();
            }

        });
        paket.setAblageFlaeche(this);
    }

    public void nehmePaketRunter(PaketModel paket) {
        // paket.setAblageFlaeche(null);
        pakete.remove(paket);
    }

    public int getTragkraft() {
        return tragkraft;
    }

    public void setTragkraft(int tragkraft) {
        this.tragkraft = tragkraft;
    }

    // public StapelModel[] getStapel() {

    // return stapelModel;
    // }

    public StuetzeModel getStuetzeLinks() {
        return stuetzeLinks;
    }

    public void setStuetzeLinks(StuetzeModel stuetzeLinks) {
        if (this.stuetzeLinks == null) {
            posY = stuetzeLinks.getPosY() + posY;
        }
        this.stuetzeLinks = stuetzeLinks;
        // if (stuetzeLinks != null) {
        // this.posX = stuetzeLinks.getPosX() + stuetzeLinks.getWidth();
        // this.posY = stuetzeLinks.getPosY() + stuetzeLinks.getHeight() - this.posY -
        // height;
        // }
    }

    public StuetzeModel getStuetzeRechts() {
        return stuetzeRechts;
    }

    public void setStuetzeRechts(StuetzeModel stuetzeRechts) {

        this.stuetzeRechts = stuetzeRechts;
        // if (stuetzeRechts != null) {
        // this.posX = stuetzeRechts.getPosX() + stuetzeRechts.getWidth();
        // this.posY = stuetzeRechts.getPosY() + stuetzeRechts.getHeight() - height -
        // this.posY;
        // }
    }

    @Override
    public Entity makeInstance() {
        return new BrettModel(width, height, posY, stuetzeLinks, stuetzeRechts, tragkraft);
    }

    public void setPakete(List<PaketModel> pakete) {
        this.pakete = pakete;
    }

    @Override
    public void delete() {
        super.delete();

        // Aufräumen --> Pakete löschen
        List<PaketModel> zuLoeschendePakete = pakete.stream().collect(Collectors.toList());

        for (PaketModel paket : zuLoeschendePakete) {
            paket.delete();
        }

        // Aufräumen --> Von Stütze lösen
        StuetzeModel stuetzeLinks = this.getStuetzeLinks();
        StuetzeModel stuetzeRechts = this.getStuetzeRechts();
        stuetzeLinks.loeseBrett(this);
        stuetzeRechts.loeseBrett(this);

        // Aufräumen aus Regal löschen
        RegalModel regalVonBrett = stuetzeLinks.getRegal();
        if (regalVonBrett != null) {
            regalVonBrett.removeBrett(this);
        }

    }

    public int getId() {
        return id;
    }

}
