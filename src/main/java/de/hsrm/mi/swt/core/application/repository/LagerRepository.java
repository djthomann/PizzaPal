package de.hsrm.mi.swt.core.application.repository;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.hsrm.mi.swt.Config;
import de.hsrm.mi.swt.core.model.LagerModel;
import de.hsrm.mi.swt.core.model.RegalModel;
import de.hsrm.mi.swt.core.model.entities.BrettModel;
import de.hsrm.mi.swt.core.model.entities.PaketModel;
import de.hsrm.mi.swt.core.model.entities.StuetzeModel;
import de.hsrm.mi.swt.core.model.entities.exceptions.StapelException;

public class LagerRepository {

    List<RegalModel> regale;
    Map<Integer, BrettModel> bretter;
    Map<Integer, StuetzeModel> stuetzen;
    Map<Integer, PaketModel> pakete;

    ZutatRepository zutatRepository;

    LagerModel lager;

    PropertyChangeSupport support;

    public LagerRepository(ZutatRepository zutatRepository) {

        support = new PropertyChangeSupport(this);
        this.zutatRepository = zutatRepository;
        lager = new LagerModel(Config.FRAME_WIDTH, Config.SCENE_H);

        regale = new ArrayList<>();
        bretter = new HashMap<>();
        stuetzen = new HashMap<>();
        pakete = new HashMap<>();

        // testData();
    }

    public void reset() {

        for(BrettModel brett : bretter.values()) {
            brett.delete();
        }

        for(PaketModel paket : pakete.values()) {
            paket.delete();
        }

        for(StuetzeModel stuetze : stuetzen.values()) {
            stuetze.delete();
        }

        this.regale.clear();
        this.bretter.clear();
        this.stuetzen.clear();
        this.pakete.clear();        

        PaketModel.resetCounter();
        BrettModel.resetCounter();
        StuetzeModel.resetCounter();

        // this.lager = null;
        support.firePropertyChange("lager reset", null, this);
    }

    public String saveLager() {

        StringBuilder sb = new StringBuilder();

        // Entities abspeichern
        for (Entry<Integer, StuetzeModel> entry : stuetzen.entrySet()) {
            sb.append(entry.getValue().toCSV());
            sb.append("\n");
        }
        for (Entry<Integer, BrettModel> entry : bretter.entrySet()) {
            sb.append(entry.getValue().toCSV());
            sb.append("\n");
        }
        for (PaketModel paket : pakete.values()) {
            sb.append(paket.toCSV());
            sb.append("\n");
        }

        // Verknüpfungen abspeichern Stuetzen <--> Bretter
        sb.append(DavidsFileHandler.VERKNUEPFUNGEN_STUETZEN_BRETTER);
        sb.append("\n");
        for (StuetzeModel stuetze : stuetzen.values()) {
            Iterator<BrettModel> bretterRechts = stuetze.bretterRechts();
            while (bretterRechts.hasNext()) {
                BrettModel brett = bretterRechts.next();
                sb.append("l").append(",").append(stuetze.getId()).append(",").append(brett.getId());
                sb.append("\n");
            }
            Iterator<BrettModel> bretterLinks = stuetze.bretterLinks();
            while (bretterLinks.hasNext()) {
                BrettModel brett = bretterLinks.next();
                sb.append("r").append(",").append(stuetze.getId()).append(",").append(brett.getId());
                sb.append("\n");
            }
        }

        // Verknüfungen abspeichern Bretter <--> Pakete
        sb.append(DavidsFileHandler.VERKNUEPFUNGEN_BRETTER_PAKETE);
        sb.append("\n");
        for (BrettModel brett : bretter.values()) {
            for (PaketModel paket : brett.getPakete()) {
                sb.append(brett.getId()).append(",").append(paket.getId());
                sb.append("\n");
            }
        }

        // Verknüfungen abspeichern Paket <--> Paket
        sb.append(DavidsFileHandler.VERKNUEPFUNGEN_PAKETE_PAKETE);
        sb.append("\n");
        for (PaketModel paket : pakete.values()) {
            Iterator<PaketModel> paketIterator = paket.paketIterator();
            while (paketIterator.hasNext()) {
                PaketModel paketDrauf = paketIterator.next();
                sb.append(paket.getId()).append(",").append(paketDrauf.getId());
                sb.append("\n");
            }
        }

        // Regale abspeichern
        sb.append(DavidsFileHandler.REGALE);
        sb.append("\n");
        for (RegalModel regal : lager.getRegale()) {
            sb.append("Regal").append(",").append(regal.getStuetzeLinks().getId()).append(",")
                    .append(regal.getStuetzeRechts().getId()).append(",");
            sb.append("\n");
        }

        return sb.toString();
    }

    public void loadLager(String csv) {
        String[] parts = csv.split(",");
        String type = parts[0];
        switch (type) {
            case "Paket" -> {
                // System.out.println("Paket");
                PaketModel paket = PaketModel.fromCSV(csv);
                paket.setInhalt(zutatRepository.getZutat(csv.split(",")[7]));
                addPaket(paket);
                // System.out.println(pakete.size());
            }
            case "Stuetze" -> {
                // System.out.println("Stuetze");
                StuetzeModel stuetze = StuetzeModel.fromCSV(csv);
                addStuetze(stuetze);
            }
            case "Brett" -> {
                // System.out.println("Brett");
                BrettModel brett = BrettModel.fromCSV(csv);
                addBrett(brett);
                // System.out.println(bretter.size());
            }
        }
    }

    public void verknuepfeBretterUndStuetzen(String csv) {
        // System.out.println("Verknüpfe");
        String[] parts = csv.split(",");
        StuetzeModel stuetze = stuetzen.get(Integer.parseInt(parts[1]));
        BrettModel brett = bretter.get(Integer.parseInt(parts[2]));
        if (parts[0].equals("l")) {
            stuetze.addBrettRechts(brett);
            brett.setStuetzeLinks(stuetze);
        } else if (parts[0].equals("r")) {
            stuetze.addBrettLinks(brett);
            brett.setStuetzeRechts(stuetze);
        }
        stuetze.setPos(stuetze.getPosX(), stuetze.getPosY());
    }

    public void verknuepfeBretterUndPakete(String csv) {
        String[] parts = csv.split(",");
        BrettModel brett = bretter.get(Integer.parseInt(parts[0]));
        PaketModel paket = pakete.get(Integer.parseInt(parts[1]));
        brett.stellePaketDrauf(paket);
        paket.setAblageFlaeche(brett);
        brett.setPos(brett.getPosX(), brett.getPosY());
    }

    public void verknuepfePaketeUndPakete(String csv) {

    }

    public void loadRegale(String csv) {
        // System.out.println("Lade Regal");
        String[] parts = csv.split(",");
        int stuetzeLinksId = Integer.parseInt(parts[1]);
        int stuetzeRechtsId = Integer.parseInt(parts[2]);
        StuetzeModel stuetzeLinks = stuetzen.get(stuetzeLinksId);
        StuetzeModel stuetzeRechts = stuetzen.get(stuetzeRechtsId);
        RegalModel regal = new RegalModel(stuetzeLinks, stuetzeRechts, null);
        regale.add(regal);
        lager.addRegal(regal);
        stuetzeLinks.setRegal(regal);
        stuetzeRechts.setRegal(regal);
    }

    public void testData() {

        // Beispiel-Stützen
        StuetzeModel stuetze1 = new StuetzeModel(20, 250, 200);
        stuetzen.put(stuetze1.getId(), stuetze1);
        StuetzeModel stuetze2 = new StuetzeModel(20, 350, 320);
        stuetzen.put(stuetze2.getId(), stuetze2);

        // Beispiel-Bretter
        BrettModel brett1 = new BrettModel(120, 10, 50, stuetzen.get(0), stuetzen.get(1), 50);
        bretter.put(brett1.getId(), brett1);
        BrettModel brett2 = new BrettModel(120, 20, 100, stuetzen.get(0), stuetzen.get(1), 100);
        bretter.put(brett2.getId(), brett2);
        BrettModel brett3 = new BrettModel(120, 30, 200, stuetzen.get(0), stuetzen.get(1), 50);
        bretter.put(brett3.getId(), brett3);

        // Beispiel-Regale
        RegalModel regal1 = new RegalModel(stuetze1, stuetze2, bretter.values());
        regale.add(regal1);

        stuetze1.setRegal(regal1);
        stuetze2.setRegal(regal1);

        // Beispiel-Pakete auf den Brettern
        PaketModel paket3 = new PaketModel(80, 20, 40, 10, 10, 200,
                zutatRepository.getZutaten().get("Käse"), brett3);
        PaketModel paket2 = new PaketModel(60, 20, 0, 10, 10, 100,
                zutatRepository.getZutaten().get("Käse"), paket3);
        PaketModel paket1 = new PaketModel(40, 20, 0, 10, 10, 100,
                zutatRepository.getZutaten().get("Käse"), paket2);

        pakete.put(paket1.getId(), paket1);
        pakete.put(paket2.getId(), paket2);
        pakete.put(paket3.getId(), paket3);

        // Pakete auf Bretter stellen
        try {
            paket3.stellePaketDrauf(paket2);
        } catch (StapelException e) {
            e.printStackTrace();
        }
        try {
            paket2.stellePaketDrauf(paket1);
        } catch (StapelException e) {
            e.printStackTrace();
        }
        brett3.stellePaketDrauf(paket3);

        // Stützen und Bretter verbinden
        stuetze1.addBrettRechts(brett1);
        stuetze1.addBrettRechts(brett2);
        stuetze1.addBrettRechts(brett3);

        stuetze2.addBrettLinks(brett1);
        stuetze2.addBrettLinks(brett2);
        stuetze2.addBrettLinks(brett3);

        // Regale zusammenbauen

        // Lager zusammenbauen
        lager.addRegal(regal1);
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }

    public void removeBrett(BrettModel b) {
        bretter.remove(b.getId());
    }

    public void removePaket(PaketModel p) {
        pakete.remove(p.getId());
    }

    public void removeStuetze(StuetzeModel s) {
        stuetzen.remove(s.getId());
    }

    public void addPaket(PaketModel paket) {
        pakete.put(paket.getId(), paket);
        support.firePropertyChange("new paket", null, paket);
    }

    public void addStuetze(StuetzeModel stuetze) {
        stuetzen.put(stuetze.getId(), stuetze);
        support.firePropertyChange("new stuetze", null, stuetze);
    }

    public void addBrett(BrettModel brett) {
        bretter.put(brett.getId(), brett);
        support.firePropertyChange("new brett", null, brett);
    }

    public void addRegal(RegalModel regal) {
        lager.addRegal(regal);
    }

    public void removeRegal(RegalModel regal) {
        lager.removeRegal(regal);
    }

    public List<BrettModel> getBretter() {
        return bretter.values().stream().toList();
    }

    public List<StuetzeModel> getStuetzen() {
        return stuetzen.values().stream().toList();
    }

    public List<PaketModel> getPakete() {
        return pakete.values().stream().toList();
    }

    public LagerModel getLager() {
        return lager;
    }

}
