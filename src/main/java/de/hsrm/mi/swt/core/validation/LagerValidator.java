package de.hsrm.mi.swt.core.validation;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

import de.hsrm.mi.swt.core.model.LagerModel;
import de.hsrm.mi.swt.core.model.entities.BrettModel;
import de.hsrm.mi.swt.core.model.entities.Entity;
import de.hsrm.mi.swt.core.model.entities.PaketModel;
import de.hsrm.mi.swt.core.model.entities.StapelModel;
import de.hsrm.mi.swt.core.model.entities.StuetzeModel;
import de.hsrm.mi.swt.core.model.zutat.Zutat;
import de.hsrm.mi.swt.core.validation.exceptions.BrettInRegalException;
import de.hsrm.mi.swt.core.validation.exceptions.BrettKonfigurierbarkeitException;
import de.hsrm.mi.swt.core.validation.exceptions.CollisionsException;
import de.hsrm.mi.swt.core.validation.exceptions.MuessteGestapeltWerdenException;
import de.hsrm.mi.swt.core.validation.exceptions.PaketSchneidetAnderesPaket;
import de.hsrm.mi.swt.core.validation.exceptions.PaketZuBreitFuerBrettException;
import de.hsrm.mi.swt.core.validation.exceptions.PlatzZuLinkerStuetzeException;
import de.hsrm.mi.swt.core.validation.exceptions.PlatzZuOberenBrettException;
import de.hsrm.mi.swt.core.validation.exceptions.PlatzZuRechterStuetzeException;
import de.hsrm.mi.swt.core.validation.exceptions.StuetzeInLagerException;
import de.hsrm.mi.swt.core.validation.exceptions.TragkraftException;
import de.hsrm.mi.swt.core.validation.exceptions.UnvertraeglichkeitsException;

public class LagerValidator {
    // Tragkraft
    // Unverträglichkeit
    // Platz zu Oberen Brett
    // Platz zu linken Stütze
    // Platz zu rechten Stütze
    // Kollisionen mit Paketen/Stapeln

    LagerModel lager;

    String info;

    private PropertyChangeSupport support;

    public LagerValidator(LagerModel lager) {
        this.lager = lager;
        support = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }

    public void setzeInfo(String msg) {
        info = msg;
        support.firePropertyChange("new info", null, msg);
    }

    public String getInfo() {
        return info;
    }

    public boolean positionIstImLager(int posX, int posY) {
        if (posX > 0 && posY > 0 && posX < lager.getWidth() && posY < lager.getHeight()) {
            return true;
        } else {
            setzeInfo("Position ist außerhalb des Lagers");
            return false;
        }
    }

    public boolean entityBleibtImLager(Entity e, int posX, int posY) {
        if (posX > 0 && posY > 0 && posX + e.getWidth() < lager.getWidth()
                && posY + e.getHeight() < lager.getHeight()) {
            return true;
        } else {
            // setzeInfo("Position ist außerhalb des Lagers");
            return false;
        }
    }

    public int bindX(Entity e, int posX) {
        if (posX + e.getWidth() > lager.getWidth()) {
            return lager.getWidth() - e.getWidth();
        } else {
            return posX;
        }
    }

    public int bindY(Entity e, int posY) {
        if (posY + e.getHeight() > lager.getHeight()) {
            return lager.getHeight() - e.getHeight();
        } else {
            return posY;
        }
    }

    public void pruefeTragkraft(BrettModel vorhandenesBrett, PaketModel paket)
            throws TragkraftException {

        if (vorhandenesBrett.getPakete().contains(paket)) {
            // Paket steht schon auf Brett --> kein Problem
            return;
        }

        int gesamtgewicht = paket.getWeight();
        // for (StapelModel vorhandenerStapel : vorhandenesBrett.getStapel()) {
        // for (PaketModel paketAusStapel : vorhandenerStapel.getPakete()) {
        // gesamtgewicht += paketAusStapel.getWeight();
        // }
        // }

        for (PaketModel p : vorhandenesBrett.getPakete()) {
            gesamtgewicht += p.getWeight();
        }

        if (vorhandenesBrett.getTragkraft() - gesamtgewicht < 0) {
            throw new TragkraftException("Tragkraft des Brettes reicht nicht aus");
        }
    }

    public void pruefePlatzZwischenBretternFuerPakete(BrettModel unteresBrett, int neuesY, BrettModel oberesBrett,
            boolean up)
            throws PlatzZuOberenBrettException {

        if (oberesBrett == null || unteresBrett == oberesBrett || unteresBrett == null) {
            return;
        }

        int maxHeight = 0;
        for (PaketModel boden : unteresBrett.getPakete()) {
            // if (paket.getHeight() > maxHeight) {
            // maxHeight = paket.getHeight();
            // }
            int stapelhoehe = boden.getStapelHoehe();
            if (stapelhoehe > maxHeight) {
                maxHeight = stapelhoehe;
            }
        }

        int platzZwischenBretter = 0;
        if (up) { // unteresBrett wird verschoben
            platzZwischenBretter = neuesY - oberesBrett.getHeight() - oberesBrett.getPosY();
        } else { // oberesBrett wird verschoben
            platzZwischenBretter = unteresBrett.getPosY() - oberesBrett.getHeight() - neuesY;
        }

        if (platzZwischenBretter < maxHeight) {
            throw new PlatzZuOberenBrettException("Platz zwischen Brettern reicht nicht");
        }

    }

    public void pruefeUnvertraeglichkeiten(BrettModel brett, PaketModel neuesPaket, StapelModel neuerStapel)
            throws UnvertraeglichkeitsException {
        if (brett.getPakete().contains(neuesPaket)) {
            // Paket steht schon auf Brett --> kein Problem
            return;
        }
        for (PaketModel paket : brett.getPakete()) {
            Zutat zutatInPaket = paket.getInhalt();
            Zutat neueZutat = neuesPaket.getInhalt();
            if (zutatInPaket == neueZutat) {
                continue;
            }

            if (zutatInPaket.getUnvertraeglichkeiten().contains(neueZutat)
                    || neueZutat.getUnvertraeglichkeiten().contains(
                            zutatInPaket)) {
                throw new UnvertraeglichkeitsException(
                        "Paket abstellen nicht möglich, Zutaten dürfen nicht nebeneinander stehen");
            }
        }
    }

    public void pruefeBreiteVonBrettUndPaket(BrettModel brett, PaketModel paket) throws PaketZuBreitFuerBrettException {
        if (brett.getWidth() < paket.getWidth()) {
            throw new PaketZuBreitFuerBrettException("Das Paket ist zu breit für dieses Brett");
        }
    }

    public void pruefePlatzZuOberenBrett(PaketModel paket, StapelModel stapel, BrettModel unteresBrett,
            BrettModel oberesBrett) throws PlatzZuOberenBrettException {

        // Gibt kein oberes Brett --> Kein Problem oder immer ein Problem
        if (oberesBrett == null) {
            return;
        }

        int platzZwischenBretter = unteresBrett.getPosY() - oberesBrett.getHeight() - oberesBrett.getPosY();

        if (platzZwischenBretter < paket.getStapelHoehe()) {
            throw new PlatzZuOberenBrettException("Platz zu oberem Brett reicht nicht");
        }
    }

    public void pruefePlatzZuLinkerStuetze(int posX, PaketModel neuesPaket, StuetzeModel stuetzeLinks)
            throws PlatzZuLinkerStuetzeException {
        if (posX < stuetzeLinks.getPosX() + stuetzeLinks.getWidth()) {
            throw new PlatzZuLinkerStuetzeException("Platz zu linker Stuetze reicht nicht aus");
        }
    }

    public void pruefePlatzZuRechterStuetze(int posX, PaketModel neuesPaket, StuetzeModel stuetzeRechts)
            throws PlatzZuRechterStuetzeException {
        if ((posX + neuesPaket.getWidth()) > stuetzeRechts.getPosX()) {
            throw new PlatzZuRechterStuetzeException("Platz zu rechter Stütze reicht nicht aus");
        }
    }

    public void pruefeKollisionMitAnderenBrettern(BrettModel bewegtesBrett) {

    }

    public void pruefeBrettInRegal(StuetzeModel stuetzeLinks, StuetzeModel stuetzeRechts, BrettModel brett)
            throws BrettInRegalException {
        if (brett.getPosX() >= stuetzeRechts.getPosX() && brett.getPosX() <= stuetzeRechts.getPosX()) {
            return;
        }
        throw new BrettInRegalException("Brett muss innherhalb des Regals Platziert werden");
    }

    public void pruefeStuetzeInLager(StuetzeModel stuetze, LagerModel lager) throws StuetzeInLagerException {
        if (stuetze.getPosX() >= 0 || stuetze.getPosX() + stuetze.getWidth() <= lager.getWidth()) {
        }
        throw new StuetzeInLagerException("Stütze muss innerhalb des Regals platzert werden");
    }

    public void pruefeStapelung(BrettModel brett, int posX, PaketModel neuesPaket)
            throws PaketSchneidetAnderesPaket, MuessteGestapeltWerdenException {

        // System.out.println("Pakete auf Brett: " + brett.getPakete().size());

        // Kollision mit allen Pakete prüfen
        for (PaketModel paket : brett.getPakete()) {

            if (paket == neuesPaket) {
                continue;
            }

            int neuesPaketAnfang = posX;
            int neuesPaketEnde = posX + neuesPaket.getWidth();
            int paketStart = paket.getPosX();
            int paketEnde = paket.getPosX() + paket.getWidth();

            // Check ob Paket genau in den Bounds des anderen Paketsliegt
            if (neuesPaketAnfang > paketStart && neuesPaketEnde < paketEnde) {
                throw new MuessteGestapeltWerdenException("Das Paket muss gestapelt werden!");
            }

            int overlapLeft = paket.getPosX() - posX;
            int overlapRight = neuesPaketEnde - paketEnde;
            // System.out.println(overlapLeft + "|" + overlapRight);
            // Check for overlap
            if (!(neuesPaketEnde <= paketStart || posX >= paketEnde)) {
                throw new PaketSchneidetAnderesPaket("Das Brett ist an dieser Stelle nicht vakant!", paket, overlapLeft,
                        overlapRight);
            }
        }
    }

    public void pruefeBrettTrittKeinAnderesBrett(BrettModel brettModel, StuetzeModel stuetzeLinks,
            StuetzeModel stuetzeRechts, int posX, int posY) {

    }

}
