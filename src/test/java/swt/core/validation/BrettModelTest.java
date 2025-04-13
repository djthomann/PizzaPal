package swt.core.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import de.hsrm.mi.swt.core.model.entities.BrettModel;
import de.hsrm.mi.swt.core.model.entities.PaketModel;
import de.hsrm.mi.swt.core.model.entities.StuetzeModel;

public class BrettModelTest {
    BrettModel brett;
    StuetzeModel stuetzeLinks;
    StuetzeModel stuetzeRechts;
    private PaketModel paket1, paket2;

    @BeforeEach
    void setUp() {
        stuetzeLinks = new StuetzeModel(0, 0, 0);
        stuetzeRechts = new StuetzeModel(0, 0, 0);
        brett = new BrettModel(100, 20, 50, stuetzeLinks, stuetzeRechts, 500);
        paket1 = new PaketModel(1, 2, 3, 4, 5, null);
        paket2 = new PaketModel(6, 7, 8, 9, 10, null);
        brett.setPakete(new ArrayList<>());
    }

    @Test
    public void testKonstruktor() {
        assertNotNull(brett);
    }

    @Test
    public void testSetAndGetTragkraft() {
        brett.setTragkraft(1000);
        assertEquals(1000, brett.getTragkraft());
    }

    @Test
    public void testSetAndGetPaketModel() {
        brett.setPaketModel(paket1);
        assertEquals(paket1, brett.getPaketModel());
    }

    @Test
    public void testStellePaketDrauf() {
        brett.stellePaketDrauf(paket1);
        assertTrue(brett.getPakete().contains(paket1));
        assertEquals(paket1, brett.getPaketModel());
        assertEquals(brett, paket1.getAblageFlaeche());
    }

    @Test
    public void testNehmePaketRunter() {
        brett.stellePaketDrauf(paket1);
        brett.nehmePaketRunter(paket1);
        assertFalse(brett.getPakete().contains(paket1));
    }

    @Test
    public void testSetPakete() {
        ArrayList<PaketModel> neuePakete = new ArrayList<>();
        neuePakete.add(paket1);
        neuePakete.add(paket2);
        brett.setPakete(neuePakete);
        assertEquals(neuePakete, brett.getPakete());
    }

    // @Test
    // public void testSetPos() {
    // brett.setPos(200, 100);

    // int expectedWidth = stuetzeRechts.getPosX() - stuetzeLinks.getPosX() -
    // stuetzeRechts.getWidth();
    // assertEquals(expectedWidth, brett.getWidth());

    // for (PaketModel paket : brett.getPakete()) {
    // assertEquals(30, paket.getPosX());
    // assertEquals(100 - 50, paket.getPosY());
    // }
    // }
}
