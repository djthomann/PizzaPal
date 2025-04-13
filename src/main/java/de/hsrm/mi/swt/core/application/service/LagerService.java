package de.hsrm.mi.swt.core.application.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.hsrm.mi.swt.Config;
import de.hsrm.mi.swt.core.application.repository.LagerRepository;
import de.hsrm.mi.swt.core.application.repository.RepositoryRegistry;
import de.hsrm.mi.swt.core.model.LagerModel;
import de.hsrm.mi.swt.core.model.RegalModel;
import de.hsrm.mi.swt.core.model.entities.Ablageflaeche;
import de.hsrm.mi.swt.core.model.entities.BrettModel;
import de.hsrm.mi.swt.core.model.entities.Entity;
import de.hsrm.mi.swt.core.model.entities.PaketModel;
import de.hsrm.mi.swt.core.model.entities.StuetzeModel;
import de.hsrm.mi.swt.core.model.entities.exceptions.StapelException;
import de.hsrm.mi.swt.core.model.zutat.Zutat;
import de.hsrm.mi.swt.core.validation.LagerValidator;
import de.hsrm.mi.swt.core.validation.exceptions.MuessteGestapeltWerdenException;
import de.hsrm.mi.swt.core.validation.exceptions.PaketSchneidetAnderesPaket;
import de.hsrm.mi.swt.core.validation.exceptions.PaketZuBreitFuerBrettException;
import de.hsrm.mi.swt.core.validation.exceptions.PlatzZuLinkerStuetzeException;
import de.hsrm.mi.swt.core.validation.exceptions.PlatzZuOberenBrettException;
import de.hsrm.mi.swt.core.validation.exceptions.PlatzZuRechterStuetzeException;
import de.hsrm.mi.swt.core.validation.exceptions.StuetzeInLagerException;
import de.hsrm.mi.swt.core.validation.exceptions.TragkraftException;
import de.hsrm.mi.swt.core.validation.exceptions.UnvertraeglichkeitsException;
import javafx.scene.paint.Color;

public class LagerService {

    LagerRepository repository = RepositoryRegistry.getInstance().getLagerRepository();

    private LagerModel lager;

    LagerValidator validator;

    public LagerService() {

        lager = repository.getLager();

        validator = new LagerValidator(lager);

    }

    public void reset() {


    }

    public void deleteEntity(Entity e) {

        if (e instanceof PaketModel paket) {

            paket.delete();
            repository.removePaket(paket);

            // System.out.println("Loesche Paket: " + paket);

        } else if (e instanceof StuetzeModel stuetze) {

            // Aufräumen --> Nicht notwendig, nur Stützen ohne Bretter dürfen gelöscht
            // werden
            if (stuetze.hatBretterLinks() || stuetze.hatBretterRechts()) {
                validator.setzeInfo("Stützen an denen Bretter hängen, möchten nicht gelöscht werden!");
                return;
            }

            // Löschen
            stuetze.delete();
            repository.removeStuetze(stuetze);
            // System.out.println("Loesche Stütze: " + stuetze);

        } else if (e instanceof BrettModel brett) {

            StuetzeModel stuetzeLinks = brett.getStuetzeLinks();
            StuetzeModel stuetzeRechts = brett.getStuetzeRechts();

            // Aufräumen --> Wurden Stützen voneinander getrennt?
            if (stuetzeLinks.anzahlBretterRechts() == 1 && stuetzeRechts.anzahlBretterLinks() == 1) {
                // Stützen wurden getrennt

                if (stuetzeLinks.anzahlBretterLinks() == 0 && stuetzeRechts.anzahlBretterRechts() == 0) {
                    // Regal wurde aufgelöst

                    // System.out.println("Regal wurde aufgelöst");

                    RegalModel regal = stuetzeLinks.getRegal();
                    stuetzeLinks.setRegal(null);
                    stuetzeRechts.setRegal(null);
                    repository.removeRegal(regal);
                }

                // Regal wurde nur verkleinert
                if (stuetzeLinks.anzahlBretterLinks() > 0) {
                    // Regal geht links weiter --> Neue Wand
                    // System.out.println("Links geht weiter");
                    stuetzeLinks.getRegal().setStuetzeRechts(stuetzeLinks);
                } else {
                    // Stütze ist jetzt einzeln
                    stuetzeLinks.setRegal(null);
                }

                if (stuetzeRechts.anzahlBretterRechts() > 0) {
                    // Regal geht rechts weiter --> Neue Wand
                    // System.out.println("Rechts geht weiter");
                    stuetzeRechts.getRegal().setStuetzeLinks(stuetzeRechts);
                } else {
                    // Stütze ist jetzt einzeln
                    stuetzeRechts.setRegal(null);
                }
            }

            // Löschen
            brett.delete();
            repository.removeBrett(brett);
            // System.out.println("Loesche Brett: " + brett);

        }
    }

    public void addEntity(Entity e, int x, int y) {

        if (e instanceof PaketModel paket) {

            if (bewegeEntity(paket, x, y)) {
                repository.addPaket(paket);
            }

        } else if (e instanceof StuetzeModel stuetze) {

            if (bewegeEntity(stuetze, x, y)) {
                repository.addStuetze(stuetze);
            }

        } else if (e instanceof BrettModel brett) {

            if (bewegeEntity(brett, x, y)) {
                repository.addBrett(brett);
            }

            // if (validator.entityBleibtImLager(brett, x, y)) {

            // // Suche nach Regal
            // RegalModel regal = findeRegalBoundingBox(x, y);

            // if (regal != null) {

            // StuetzeModel stuetzeLinks = findeStuetzeLinksImRegal(regal, x, y);
            // StuetzeModel stuetzeRechts = findeStuetzeRechtsImRegal(regal, x, y);

            // brett.setStuetzeLinks(stuetzeLinks);
            // brett.setStuetzeRechts(stuetzeRechts);
            // stuetzeLinks.addBrettRechts(brett);
            // stuetzeRechts.addBrettLinks(brett);
            // regal.addBrett(brett);
            // brett.setPos(stuetzeLinks.getPosX() + stuetzeLinks.getWidth(), y);
            // repository.addBrett(brett);

            // } else {

            // // Suche nach Stützen links und rechts
            // StuetzeModel stuetzeLinks = findeStuetzeLinks(x, y);
            // StuetzeModel stuetzeRechts = findeStuetzeRechts(x, y);

            // if (stuetzeLinks == null ^ stuetzeRechts == null) {
            // return;
            // }

            // // Kann das Brett hinzugefügt werden?
            // if (stuetzeLinks != null && stuetzeRechts != null) {
            // brett.setStuetzeLinks(stuetzeLinks);
            // brett.setStuetzeRechts(stuetzeRechts);
            // stuetzeLinks.addBrettRechts(brett);
            // stuetzeRechts.addBrettLinks(brett);
            // brett.setPos(stuetzeLinks.getPosX() + stuetzeLinks.getWidth(), y);
            // repository.addBrett(brett);
            // }

            // // Ist ein neues Regal enstanden? oder ein bestehendes vergrößert worden?
            // if (stuetzeLinks.getRegal() == null && stuetzeRechts.getRegal() == null) {
            // // neues Regal

            // System.out.println("Neues Regal");
            // ArrayList<BrettModel> bretter = new ArrayList<>();
            // bretter.add(brett);
            // RegalModel neuesRegal = new RegalModel(stuetzeLinks, stuetzeRechts, bretter);
            // stuetzeLinks.setRegal(neuesRegal);
            // stuetzeRechts.setRegal(neuesRegal);

            // repository.addRegal(neuesRegal);
            // } else if (stuetzeLinks.getRegal() != null && stuetzeRechts.getRegal() ==
            // null) {
            // // bestehendes Regal nach rechts erweitern
            // RegalModel zuErweiterndesRegal = stuetzeLinks.getRegal();
            // zuErweiterndesRegal.rechtsErweitern(stuetzeRechts);
            // stuetzeRechts.setRegal(zuErweiterndesRegal);
            // } else if (stuetzeLinks.getRegal() == null && stuetzeRechts.getRegal() !=
            // null) {
            // // bestehendes Regal nach links erweitern
            // RegalModel zuErweiterndesRegal = stuetzeRechts.getRegal();
            // zuErweiterndesRegal.linksErweitern(stuetzeLinks);
            // stuetzeLinks.setRegal(zuErweiterndesRegal);
            // } else {
            // // Regale zusammengeführt --> Linkes erweitern
            // RegalModel zuErweiterndesRegal = stuetzeLinks.getRegal();
            // RegalModel zuEntfernendesRegal = stuetzeRechts.getRegal();

            // StuetzeModel rechtStuetze = zuEntfernendesRegal.getStuetzeRechts();

            // StuetzeModel stuetzePointer = zuEntfernendesRegal.getStuetzeLinks();

            // zuErweiterndesRegal.rechtsErweitern(rechtStuetze);
            // while (stuetzePointer != rechtStuetze) {

            // stuetzePointer.setRegal(zuErweiterndesRegal);

            // // Stützen dem Regal hinzufügen
            // zuErweiterndesRegal.addStuetze(stuetzePointer);

            // // Bretter dem Regal hinzufügen
            // for (BrettModel brettPointer : stuetzePointer.getBretterRechts()) {
            // zuErweiterndesRegal.addBrett(brettPointer);
            // }

            // // Eine Stütze weiter laufen
            // stuetzePointer = stuetzePointer.getBretterRechts().get(0).getStuetzeRechts();
            // }

            // repository.removeRegal(zuEntfernendesRegal);
            // System.out.println(repository.getLager().anzahlRegale());

            // }
            // }
            // }

        }
    }

    public LagerValidator getLagerValidator() {
        return validator;
    }

    public LagerModel getLager() {
        return lager;
    }

    public Iterator<RegalModel> holeRegale() {
        return lager.regalIterator();
    }

    public Iterator<BrettModel> holeBretter() {
        return repository.getBretter().iterator();
    }

    public Iterator<StuetzeModel> holeStuetzen() {
        return repository.getStuetzen().iterator();
    }

    public Iterator<PaketModel> holePakete() {
        return repository.getPakete().iterator();
    }

    public boolean daStehtEinRegal(int x) {

        for (Iterator<RegalModel> regalIterator = lager.regalIterator(); regalIterator.hasNext();) {
            RegalModel regal = regalIterator.next();

            StuetzeModel stuetzeLinks = regal.getStuetzeLinks();
            StuetzeModel stuetzeRechts = regal.getStuetzeRechts();

            int links = stuetzeLinks.getPosX();
            int rechts = stuetzeRechts.getPosX() + stuetzeRechts.getWidth();

            if (links < x && rechts > x) {
                return true;
            }
        }
        return false;
    }

    public RegalModel findeRegalMitPosition(int x) {

        for (Iterator<RegalModel> regalIterator = lager.regalIterator(); regalIterator.hasNext();) {
            RegalModel regal = regalIterator.next();

            StuetzeModel stuetzeLinks = regal.getStuetzeLinks();
            StuetzeModel stuetzeRechts = regal.getStuetzeRechts();

            int links = stuetzeLinks.getPosX();
            int rechts = stuetzeRechts.getPosX() + stuetzeRechts.getWidth();

            if (links < x && rechts > x) {
                return regal;
            }

        }
        return null;
    }

    public RegalModel findeRegalBoundingBox(int x, int y) {

        for (Iterator<RegalModel> regalIterator = lager.regalIterator(); regalIterator.hasNext();) {
            RegalModel regal = regalIterator.next();

            StuetzeModel stuetzeLinks = regal.getStuetzeLinks();
            StuetzeModel stuetzeRechts = regal.getStuetzeRechts();

            int oben = Math.min(stuetzeLinks.getPosY(), stuetzeRechts.getPosY());
            int hoehe = Math.max(stuetzeLinks.getHeight(), stuetzeRechts.getHeight());
            int unten = oben + hoehe;
            int links = stuetzeLinks.getPosX();
            int rechts = stuetzeRechts.getPosX() + stuetzeRechts.getWidth();

            if (links <= x && rechts >= x && oben <= y && unten >= y) {
                return regal;
            }

        }
        return null;
    }

    public StuetzeModel findeStuetzeLinksImRegal(RegalModel regal, int x, int y) {

        StuetzeModel stuetzeLinks = null;

        Iterator<StuetzeModel> iterator = regal.stuetzenIterator();

        while (iterator.hasNext()) {
            StuetzeModel stuetze = iterator.next();
            if (stuetze.getPosX() < x) {

                if (stuetzeLinks == null) {
                    stuetzeLinks = stuetze;
                } else if (stuetze.getPosX() > stuetzeLinks.getPosX()) {
                    stuetzeLinks = stuetze;
                }

            }
        }
        return stuetzeLinks;
    }

    public StuetzeModel findeStuetzeRechtsImRegal(RegalModel regal, int x, int y) {

        StuetzeModel stuetzeRechts = null;

        Iterator<StuetzeModel> iterator = regal.stuetzenIterator();

        while (iterator.hasNext()) {
            StuetzeModel stuetze = iterator.next();
            if (stuetze.getPosX() > x) {

                if (stuetzeRechts == null) {
                    stuetzeRechts = stuetze;
                } else if (stuetze.getPosX() < stuetzeRechts.getPosX()) {
                    stuetzeRechts = stuetze;
                }

            }
        }
        return stuetzeRechts;
    }

    public StuetzeModel findeStuetzeLinks(int x, int y) {

        StuetzeModel stuetzeLinks = null;

        for (StuetzeModel stuetze : repository.getStuetzen()) {
            if (stuetze.getPosX() < x) {

                if (stuetzeLinks == null) {
                    stuetzeLinks = stuetze;
                } else if (stuetze.getPosX() > stuetzeLinks.getPosX()) {
                    stuetzeLinks = stuetze;
                }

            }
        }
        return stuetzeLinks;

    }

    public StuetzeModel findeStuetzeRechts(int x, int y) {

        StuetzeModel stuetzeRechts = null;

        for (StuetzeModel stuetze : repository.getStuetzen()) {
            if (stuetze.getPosX() > x) {

                if (stuetzeRechts == null) {
                    stuetzeRechts = stuetze;
                } else if (stuetze.getPosX() < stuetzeRechts.getPosX()) {
                    stuetzeRechts = stuetze;
                }

            }
        }
        return stuetzeRechts;

    }

    public BrettModel findeUnteresBrett(RegalModel regal, int x, int y) {

        BrettModel brettDrunter = null;

        for (Iterator<BrettModel> brettIterator = regal.brettIterator(); brettIterator.hasNext();) {
            BrettModel brett = brettIterator.next();
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

    public BrettModel findeOberesBrett(RegalModel regal, int x, int y) {

        BrettModel brettDrueber = null;

        for (Iterator<BrettModel> brettIterator = regal.brettIterator(); brettIterator.hasNext();) {
            BrettModel brett = brettIterator.next();
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

    public boolean stuetzenSindHochGenug(RegalModel regal, int x, int y) {

        StuetzeModel stuetzeLinks = findeStuetzeLinksImRegal(regal, x, y);
        StuetzeModel stuetzeRechts = findeStuetzeRechtsImRegal(regal, x, y);

        if (stuetzeRechts.getPosY() > y || stuetzeLinks.getPosY() > y) {
            return false;
        } else {
            return true;
        }

    }

    public boolean bewegeEntity(Entity e, double mouseX, double mouseY) {

        int posX = (int) mouseX;
        int posY = (int) mouseY;

        if (!validator.positionIstImLager(posX, posY)) {
            validator.setzeInfo("Nicht im Lager");
            return false;
        }

        if (!validator.entityBleibtImLager(e, posX, posY)) {
            posX = validator.bindX(e, posX);
            posY = validator.bindY(e, posY);
        }

        if (e instanceof PaketModel paket) {

            return bewegePaket(paket, posX, posY);

        } else if (e instanceof BrettModel brett) {

            return bewegeBrett(brett, posX, posY);

        } else if (e instanceof StuetzeModel stuetze) {

            return bewegeStuetze(stuetze, posX, posY);

        }
        return false;

    }

    public boolean bewegePaket(PaketModel paket, int posX, int posY) {

        // Logik um ein Paket zu bewegen

        // Finde Regal
        RegalModel regal = findeRegalMitPosition(posX);
        if (regal != null) {

            // Finde Stützen
            StuetzeModel stuetzeLinks = findeStuetzeLinksImRegal(regal, posX, posY);
            StuetzeModel stuetzeRechts = findeStuetzeRechtsImRegal(regal, posX, posY);

            // Finde Bretter
            BrettModel unteresBrett = stuetzeRechts.naechstesBrettDrunterLinks(posY);
            BrettModel oberesBrett = stuetzeRechts.naechstesBrettDrueberLinks(posY);

            // System.out.println(unteresBrett + "|" + oberesBrett);

            if (unteresBrett != null) {

                boolean mussGestapeltWerden = false;

                // Prüfe Unverträglichkeiten
                try {
                    validator.pruefeUnvertraeglichkeiten(unteresBrett, paket, null);
                } catch (UnvertraeglichkeitsException exception) {
                    validator.setzeInfo(exception.getMessage());
                    return false;
                }

                // Checke Breite des Pakets
                try {
                    validator.pruefeBreiteVonBrettUndPaket(unteresBrett, paket);
                } catch (PaketZuBreitFuerBrettException e1) {
                    validator.setzeInfo(e1.getMessage());
                    return false;
                }

                // Prüfe Platz nach oben
                try {
                    validator.pruefePlatzZuOberenBrett(paket, null, unteresBrett, oberesBrett);
                } catch (PlatzZuOberenBrettException e1) {
                    validator.setzeInfo(e1.getMessage());
                    return false;
                }

                // Checke Platz nach rechts und links
                try {
                    validator.pruefePlatzZuRechterStuetze(posX, paket, unteresBrett.getStuetzeRechts());
                    validator.pruefePlatzZuLinkerStuetze(posX, paket, unteresBrett.getStuetzeLinks());
                } catch (PlatzZuRechterStuetzeException e1) {
                    // Wird nicht abgebrochen, sondern maximal nach rechts verschieben
                    posX = unteresBrett.getStuetzeRechts().getPosX() - paket.getWidth();
                } catch (PlatzZuLinkerStuetzeException e1) {
                    // Wird nicht abgebrochen, sondern maximal nach links verschieben
                    posX = unteresBrett.getStuetzeLinks().getPosX() + unteresBrett.getStuetzeLinks().getWidth();
                }

                // Prüfe Tragkraft
                try {
                    validator.pruefeTragkraft(unteresBrett, paket);
                } catch (TragkraftException exception) {
                    validator.setzeInfo(exception.getMessage());
                    return false;
                }

                PaketModel paketUnten = null;

                // Checke Kollision mit anderen Paketen
                try {
                    validator.pruefeStapelung(unteresBrett, posX, paket);
                } catch (PaketSchneidetAnderesPaket e1) {
                    if (e1.getOverlapLeft() > 0) {
                        // System.out.println(posX);
                        posX = posX + e1.getOverlapLeft();
                        // System.out.println(posX);
                    } else if (e1.getOverlaptRight() > 0) {
                        // System.out.println(posX);
                        posX = posX - e1.getOverlaptRight();
                        // System.out.println(posX);
                    }
                    paketUnten = e1.getGeschnittenesPaket();
                    mussGestapeltWerden = true;
                } catch (MuessteGestapeltWerdenException e1) {

                    mussGestapeltWerden = true;
                }

                if (mussGestapeltWerden) {
                    // Das Paket muss gestapelt werden
                    Ablageflaeche alteFlaeche = paket.getAblageFlaeche();
                    if (paketUnten == null) {
                        paketUnten = unteresBrett.getPaketAnPosition(posX);
                    }
                    try {
                        paketUnten.stellePaketDrauf(paket, posX);
                        return true;
                    } catch (StapelException exception) {
                        validator.setzeInfo(exception.getMessage());
                        return false;
                    }
                    // if (paketUnten.stellePaketDrauf(paket, posX)) {
                    // if (alteFlaeche != paket.getAblageFlaeche() && alteFlaeche != null) {
                    // alteFlaeche.nehmePaketRunter(paket);
                    // }
                    // return true;
                    // } else {
                    // validator.setzeInfo("Stapeln hat nicht geklappt");
                    // return false;
                    // }
                } else {
                    Ablageflaeche ablageflaeche = paket.getAblageFlaeche();
                    if (ablageflaeche != null) {
                        paket.getAblageFlaeche().nehmePaketRunter(paket);
                    }
                    unteresBrett.stellePaketDrauf(paket);
                    paket.setPos(posX, unteresBrett.getPosY() - paket.getHeight());
                    return true;
                }

            } else {
                validator.setzeInfo("Unterhalb der Bretter! ");
                return false;
            }

        } else {
            validator.setzeInfo("Nicht im Regal! ");
            return false;
        }

    }

    public boolean bewegeStuetze(StuetzeModel stuetze, int posX, int posY) {

        RegalModel regal = findeRegalMitPosition(posX);
        if (regal == null || regal == stuetze.getRegal()) {
            // An der Position ist kein Regal

            // Prüfen ob Regal durch Bewegung nicht "negativ" breit wird
            if (posX < stuetze.getPosX()) {
                // Wird nach links verschoben
                if (stuetze.hatBretterLinks()) {
                    // Regal wird verkleinert |--| --> |-|

                    // Mehr als Bretter lang sind?
                    if (stuetze.bretterLinks().next().getWidth() < (stuetze.getPosX() - posX)) {
                        validator.setzeInfo("Regal wäre negativ breit");
                        return false;
                    }

                    // Würde ein Paket im nichts stehen?!
                    Iterator<BrettModel> bretterLinks = stuetze.bretterLinks();
                    while (bretterLinks.hasNext()) {
                        BrettModel brett = bretterLinks.next();

                        for (PaketModel paket : brett.getPakete()) {
                            if (paket.getPosX() + paket.getWidth() > posX) {
                                validator.setzeInfo("Ein Paket verhindert die Umstützung!");
                                return false;
                            }
                        }
                    }

                } else {
                    // validator.setzeInfo("Regal wird größer");
                }
            } else if (posX > stuetze.getPosX()) {
                // Wird nach rechts verschoben
                if (stuetze.hatBretterRechts()) {
                    // Regal wird verkleinert |--| --> |-|

                    // Mehr als Bretter lang sind?
                    if (stuetze.bretterRechts().next().getWidth() < (posX - stuetze.getPosX())) {
                        validator.setzeInfo("Regal wäre negativ breit");
                        return false;
                    }

                    // Würde ein Paket im nichts stehen?!
                    Iterator<BrettModel> bretterRechts = stuetze.bretterRechts();
                    while (bretterRechts.hasNext()) {
                        BrettModel brett = bretterRechts.next();

                        for (PaketModel paket : brett.getPakete()) {
                            if (paket.getPosX() < posX + stuetze.getWidth()) {
                                validator.setzeInfo("Ein Paket verhindert die Umstützung!");
                                return false;
                            }
                        }
                    }

                } else {
                    // validator.setzeInfo("Regal wird größer");
                }
            } else {
                // Wird nicht verschoben
                return false;
            }

            // Prüfen ob zwischen aktueller Position und neuer Position ein Regal steht

            stuetze.setPos(posX, 0);
            return true;

        } else {
            validator.setzeInfo("Da ist ein Regal! ");
            return false;
        }

    }

    public boolean bewegeBrett(BrettModel brett, int posX, int posY) {

        // Logik um Bretter zu bewegen

        RegalModel regal = findeRegalMitPosition(posX);

        StuetzeModel alteStuetzeLinks = brett.getStuetzeLinks();
        StuetzeModel alteStuetzeRechts = brett.getStuetzeRechts();

        StuetzeModel stuetzeLinks;
        StuetzeModel stuetzeRechts;

        boolean gesetzt = false;

        if (regal != null) {
            // Da ist schon mal ein Regal

            stuetzeLinks = findeStuetzeLinksImRegal(regal, posX, posY);
            stuetzeRechts = findeStuetzeRechtsImRegal(regal, posX, posY);

            // Sind die Stuetzen auf beiden Seiten hoch genug?
            if (!stuetzenSindHochGenug(regal, posX, posY)) {
                posY = Math.max(stuetzeLinks.getPosY(), stuetzeRechts.getPosY());
            }

            // Kollidiert das Brett mit einem anderen Brett?
            List<BrettModel> bretterAnDieserStelle = stuetzeLinks.getBretterRechtsImIntervall(posY,
                    posY + brett.getHeight());
            if (bretterAnDieserStelle.size() > 0) {

                // validator.setzeInfo("Bretter gefunden: " + bretterAnDieserStelle.size());

                if (bretterAnDieserStelle.size() == 1) {
                    if (bretterAnDieserStelle.contains(brett)) {
                        // Mit sich selbst kollidiert man nicht
                    } else {
                        validator.setzeInfo("Da ist schon ein Brett");
                        return false;
                    }
                } else {
                    validator.setzeInfo("Da ist schon ein Brett");
                    return false;
                }
            }

            // Reicht der Platz zum nächsten Brett noch für meine Pakete?
            if (posY > brett.getPosY()) {
                // Wird nach unten verschoben
                // System.out.println("Nach unten");
                BrettModel brettDrunter = stuetzeLinks.naechstesBrettDrunterRechts(posY);
                if (brettDrunter != null) {

                    try {
                        validator.pruefePlatzZwischenBretternFuerPakete(
                                brettDrunter, posY,
                                brett, false);
                    } catch (PlatzZuOberenBrettException e1) {
                        validator.setzeInfo(e1.getMessage());
                        return false;
                    }
                }

            } else {
                // Wird nach oben verschoben
                // System.out.println("Nach oben");
                BrettModel brettDrueber = stuetzeLinks.naechstesBrettDrueberRechts(posY);
                if (brettDrueber != null) {
                    try {
                        validator.pruefePlatzZwischenBretternFuerPakete(brett, posY,
                                brettDrueber, true);
                    } catch (PlatzZuOberenBrettException e1) {
                        validator.setzeInfo(e1.getMessage());
                        return false;
                    }
                }
            }

            RegalModel aktuellesRegal = null;
            if (brett.getStuetzeLinks() != null) {
                aktuellesRegal = brett.getStuetzeLinks().getRegal();
            }

            if (aktuellesRegal != regal) {
                if (regal.hasBrett(brett)) {
                    brett.getStuetzeLinks().getRegal().removeBrett(brett);
                } else {
                    regal.addBrett(brett);
                }
            }

            if (alteStuetzeLinks != stuetzeLinks && alteStuetzeLinks != null) {
                alteStuetzeLinks.removeBrettRechts(brett);
                brett.setStuetzeLinks(stuetzeLinks);
                stuetzeLinks.addBrettRechts(brett);
            }

            if (alteStuetzeRechts != stuetzeRechts && alteStuetzeRechts != null) {
                alteStuetzeRechts.removeBrettLinks(brett);
                brett.setStuetzeRechts(stuetzeRechts);
                stuetzeRechts.addBrettLinks(brett);
            }

            brett.setStuetzeLinks(stuetzeLinks);
            brett.setStuetzeRechts(stuetzeRechts);
            stuetzeLinks.addBrettRechts(brett);
            stuetzeRechts.addBrettLinks(brett);
            brett.setPos(stuetzeLinks.getPosX() + stuetzeLinks.getWidth(), posY);
            gesetzt = true;
        } else {

            // Suche nach Stützen links und rechts
            stuetzeLinks = findeStuetzeLinks(posX, posY);
            stuetzeRechts = findeStuetzeRechts(posX, posY);

            // if (stuetzeLinks == null ^ stuetzeRechts == null) {
            // return false;
            // }

            // Kann das Brett hinzugefügt werden?
            if (stuetzeLinks != null && stuetzeRechts != null) {
                brett.setStuetzeLinks(stuetzeLinks);
                brett.setStuetzeRechts(stuetzeRechts);
                stuetzeLinks.addBrettRechts(brett);
                stuetzeRechts.addBrettLinks(brett);
                brett.setPos(stuetzeLinks.getPosX() + stuetzeLinks.getWidth(), posY);
                gesetzt = true;
            } else {
                validator.setzeInfo("Keine Stützen gefunden werden");
                return false;
            }
        }

        if (gesetzt) {

            // Ist ein neues Regal enstanden? oder ein bestehendes vergrößert worden? Oder
            // ein aufgelöst worden?
            if (stuetzeLinks.getRegal() == null && stuetzeRechts.getRegal() == null) {
                // neues Regal

                // System.out.println("Neues Regal");
                ArrayList<BrettModel> bretter = new ArrayList<>();
                bretter.add(brett);
                RegalModel neuesRegal = new RegalModel(stuetzeLinks, stuetzeRechts, bretter);
                stuetzeLinks.setRegal(neuesRegal);
                stuetzeRechts.setRegal(neuesRegal);

                repository.addRegal(neuesRegal);
            }

            if (stuetzeLinks.getRegal() != null && stuetzeRechts.getRegal() == null) {
                // bestehendes Regal nach rechts erweitern
                RegalModel zuErweiterndesRegal = stuetzeLinks.getRegal();
                zuErweiterndesRegal.rechtsErweitern(stuetzeRechts);
                // System.out.println("Nach Rechts Erweitern");
                stuetzeRechts.setRegal(zuErweiterndesRegal);
            }

            if (stuetzeLinks.getRegal() == null && stuetzeRechts.getRegal() != null) {
                // bestehendes Regal nach links erweitern
                RegalModel zuErweiterndesRegal = stuetzeRechts.getRegal();
                zuErweiterndesRegal.linksErweitern(stuetzeLinks);
                // System.out.println("Nach Links Erweitern");
                stuetzeLinks.setRegal(zuErweiterndesRegal);
            }

            if (stuetzeLinks.getRegal() != null && stuetzeRechts.getRegal() != null) {

                if (stuetzeLinks.getRegal() != stuetzeRechts.getRegal()) {

                    // System.out.println("Regale zusammengeführt Erweitern");

                    // Regale zusammengeführt --> Linkes erweitern
                    RegalModel zuErweiterndesRegal = stuetzeLinks.getRegal();
                    RegalModel zuEntfernendesRegal = stuetzeRechts.getRegal();

                    StuetzeModel rechtStuetze = zuEntfernendesRegal.getStuetzeRechts();

                    StuetzeModel stuetzePointer = zuEntfernendesRegal.getStuetzeLinks();

                    zuErweiterndesRegal.rechtsErweitern(rechtStuetze);
                    while (stuetzePointer != rechtStuetze) {

                        stuetzePointer.setRegal(zuErweiterndesRegal);

                        // Stützen dem Regal hinzufügen
                        zuErweiterndesRegal.addStuetze(stuetzePointer);

                        // Bretter dem Regal hinzufügen
                        for (BrettModel brettPointer : stuetzePointer.getBretterRechts()) {
                            zuErweiterndesRegal.addBrett(brettPointer);
                        }

                        // Eine Stütze weiter laufen
                        stuetzePointer = stuetzePointer.getBretterRechts().iterator().next().getStuetzeRechts();
                    }

                    repository.removeRegal(zuEntfernendesRegal);
                    // System.out.println(repository.getLager().anzahlRegale());

                } else {

                    // Schauen ob alte Stützen jetzt einsam sind
                    // System.out.println("Alte Stütze ist einsam");

                    if (alteStuetzeLinks != null && alteStuetzeRechts != null) {

                        boolean einsameStuetzeLinks = false;
                        boolean einsameStuetzeRechts = false;

                        if (!alteStuetzeLinks.hatBretterRechts() && alteStuetzeLinks != brett.getStuetzeLinks()
                                && alteStuetzeLinks != brett.getStuetzeRechts()) {

                            if (alteStuetzeLinks.hatBretterLinks()) {
                                // System.out.println("Neues Regal Links");
                                // Nicht ganz einsam --> neues Regal rechts
                                StuetzeModel stuetzePointer = alteStuetzeLinks.getBretterLinks().iterator().next()
                                        .getStuetzeLinks();
                                RegalModel neuesRegal = new RegalModel(stuetzePointer, alteStuetzeLinks, null);
                                while (stuetzePointer.hatBretterRechts()) {

                                    stuetzePointer = stuetzePointer.getBretterLinks().iterator().next()
                                            .getStuetzeLinks();
                                    neuesRegal.linksErweitern(stuetzePointer);
                                }
                                repository.addRegal(neuesRegal);
                            } else {
                                einsameStuetzeLinks = true;
                            }

                        }

                        if (!alteStuetzeRechts.hatBretterLinks() && alteStuetzeRechts != brett.getStuetzeRechts()
                                && alteStuetzeRechts != brett.getStuetzeLinks()) {

                            if (alteStuetzeRechts.hatBretterRechts()) {
                                // Nicht ganz einsam --> neues Regal rechts
                                // System.out.println("Neues Regal Rechts");
                                StuetzeModel stuetzePointer = alteStuetzeRechts.getBretterRechts().iterator().next()
                                        .getStuetzeRechts();
                                RegalModel neuesRegal = new RegalModel(alteStuetzeRechts, stuetzePointer, null);
                                while (stuetzePointer.hatBretterRechts()) {

                                    stuetzePointer = stuetzePointer.getBretterRechts().iterator().next()
                                            .getStuetzeRechts();
                                    neuesRegal.rechtsErweitern(stuetzePointer);
                                }

                                repository.addRegal(neuesRegal);
                            } else {
                                einsameStuetzeRechts = true;
                            }

                        }

                        boolean regalAufgeloest = einsameStuetzeLinks && einsameStuetzeRechts;
                        if (regalAufgeloest) {

                            // System.out.println("Regal wurde aufgelöst");
                            // Regal wurde aufgelöst
                            RegalModel zuEntfRegal = alteStuetzeLinks.getRegal();
                            alteStuetzeLinks.setRegal(null);
                            alteStuetzeRechts.setRegal(null);
                            repository.removeRegal(zuEntfRegal);

                        } else {
                            if (einsameStuetzeLinks) {

                                // System.out.println("Einsame Stütze Links");
                                RegalModel aktRegal = alteStuetzeLinks.getRegal();
                                aktRegal.removeStuetze(alteStuetzeLinks);
                                aktRegal.setStuetzeLinks(brett.getStuetzeLinks());
                                alteStuetzeLinks.setRegal(null);
                            }
                            if (einsameStuetzeRechts) {

                                // System.out.println("Einsame Stütze Rechts");

                                RegalModel aktRegal = alteStuetzeRechts.getRegal();
                                aktRegal.removeStuetze(alteStuetzeRechts);
                                aktRegal.setStuetzeRechts(brett.getStuetzeRechts());
                                alteStuetzeRechts.setRegal(null);
                            }
                        }

                    }

                }

            }

            return true;
        } else {
            return false;
        }

    }

}
