package swt.core.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.hsrm.mi.swt.core.model.entities.BrettModel;
import de.hsrm.mi.swt.core.model.entities.PaketModel;
import de.hsrm.mi.swt.core.model.entities.StapelModel;
import de.hsrm.mi.swt.core.model.entities.StuetzeModel;
import de.hsrm.mi.swt.core.model.zutat.Zutat;
import javafx.scene.paint.Color;

public class StapelModelTest {
    List<PaketModel> pakete = new ArrayList<>();
    StuetzeModel stuetzeLinks;
    StuetzeModel stuetzeRechts;
    BrettModel brett;
    Zutat banane;
    PaketModel paket;
    StapelModel testStapel;

    @BeforeEach
    void setUp() {
        stuetzeLinks = new StuetzeModel(0, 0, 0);
        stuetzeRechts = new StuetzeModel(0, 0, 0);
        brett = new BrettModel(0, 0, 0, stuetzeLinks, stuetzeRechts, 30);
        banane = new Zutat("Banane", new Color(1, 1, 0, 1));
        paket = new PaketModel(0, 0, 0, 0, 0, banane, brett);
        pakete.add(paket);
        testStapel = new StapelModel(20, 60, 50, pakete);
    }

    @Test
    public void testKonstruktor() {
        assertNotNull(testStapel);
        assertEquals(50, testStapel.getHeight(), "Höhe muss 50 betragen");
    }

    @Test
    public void testGetGewicht() {
        testStapel.setGewicht(300);
        assertEquals(300, testStapel.getGewicht());
    }

    @Test
    public void testSetGewicht() {
        testStapel.setGewicht(400);
        assertEquals(400, testStapel.getGewicht());
    }

    @Test
    public void testGetWidth() {
        assertEquals(60, testStapel.getWidth());
    }

    @Test
    public void testSetWidth() {
        testStapel.setWidth(200);
        assertEquals(200, testStapel.getWidth());
    }

    @Test
    public void testGetHeight() {
        assertEquals(50, testStapel.getHeight());
    }

    @Test
    public void testSetHeight() {
        testStapel.setHeight(400);
        assertEquals(400, testStapel.getHeight());
    }

    @Test
    public void testGetPakete() {
        assertNotNull(testStapel.getPakete());
    }

    @Test
    public void testSetPakete() {
        paket = new PaketModel(13, 0, 0, 0, 0, banane, brett);
        List<PaketModel> neuePakete = new ArrayList<>();
        neuePakete.add(paket);
        testStapel.setPakete(neuePakete);
        assertNotNull(testStapel.getPakete());

    }

    @Test
    public void testAddPaket() {
        int startAnzahlPakete = testStapel.getPakete().size();
        paket = new PaketModel(14, 0, 0, 0, 0, banane, brett);
        testStapel.addPaket(paket);
        assertEquals(startAnzahlPakete + 1, testStapel.getPakete().size());

    }

    @Test
    public void testRemovePaket() {
        int startAnzahlPakete = testStapel.getPakete().size();
        paket = new PaketModel(14, 0, 0, 0, 0, banane, brett);
        testStapel.addPaket(paket);
        testStapel.removePaket(paket);
        assertEquals(startAnzahlPakete, testStapel.getPakete().size());
    }

    @Test
    public void testPaketIterator() {
        assertNotNull(testStapel.paketIterator().next());
    }

}
