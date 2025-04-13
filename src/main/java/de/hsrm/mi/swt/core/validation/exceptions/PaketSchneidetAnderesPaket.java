package de.hsrm.mi.swt.core.validation.exceptions;

import de.hsrm.mi.swt.core.model.entities.PaketModel;

public class PaketSchneidetAnderesPaket extends Throwable {

    private PaketModel geschnittenesPaket;
    private int overlapLeft;
    private int overlaptRight;

    public static int OVERLAP_TOLERANCE = 5;

    public PaketSchneidetAnderesPaket(String message, PaketModel geschnittenesPaket, int overlapLeft,
            int overlaptRight) {
        super(message);
        this.geschnittenesPaket = geschnittenesPaket;
        this.overlapLeft = overlapLeft;
        this.overlaptRight = overlaptRight;
    }

    public PaketModel getGeschnittenesPaket() {
        return geschnittenesPaket;
    }

    public int getOverlapLeft() {
        return overlapLeft;
    }

    public int getOverlaptRight() {
        return overlaptRight;
    }

}
