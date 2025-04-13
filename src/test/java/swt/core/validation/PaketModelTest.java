package swt.core.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.hsrm.mi.swt.core.model.entities.BrettModel;
import de.hsrm.mi.swt.core.model.entities.PaketModel;
import de.hsrm.mi.swt.core.model.entities.exceptions.StapelException;
import de.hsrm.mi.swt.core.model.zutat.Zutat;

public class PaketModelTest {

    private PaketModel paket;
    private Zutat zutat;
    private BrettModel brett;

    @BeforeEach
    void setUp() {
        zutat = new Zutat("Tomate", null);
        brett = new BrettModel(100, 20, 50, null, null, 500);
        paket = new PaketModel(50, 20, 30, 40, 25, 100, zutat, brett);
    }

    @Test
    void testKonstruktorMitBrett() {
        // assertEquals(30, paket.getPosX());
        // assertEquals(-20, paket.getPosY());
        assertEquals(brett, paket.getAblageFlaeche());
    }

    @Test
    void testKonstruktorOhneBrett() {
        PaketModel paketOhneBrett = new PaketModel(50, 20, 30, 40, 25, zutat);
        assertEquals(30, paketOhneBrett.getPosX());
        assertEquals(40, paketOhneBrett.getPosY());
        assertNull(paketOhneBrett.getAblageFlaeche());
    }

    @Test
    void testSetAndGetWeight() {
        paket.setWeight(30);
        assertEquals(30, paket.getWeight());
    }

    @Test
    void testSetAndGetHeight() {
        paket.setHeight(35);
        assertEquals(35, paket.getHeight());
    }

    @Test
    void testSetAndGetInhalt() {
        Zutat neueZutat = new Zutat("Kartoffel", null);
        paket.setInhalt(neueZutat);
        assertEquals(neueZutat, paket.getInhalt());
    }

    @Test
    void testSetAndGetAblageFlaeche() {
        BrettModel neuesBrett = new BrettModel(120, 25, 60, null, null, 600);
        paket.setAblageFlaeche(neuesBrett);
        assertEquals(neuesBrett, paket.getAblageFlaeche());
    }

    @Test
    void testSetAndGetDimensions() {
        paket.setWidth(55);
        paket.setHeight(25);
        assertEquals(55, paket.getWidth());
        assertEquals(25, paket.getHeight());
    }

    @Test
    void testMakeInstance() {
        PaketModel neuesPaket = (PaketModel) paket.makeInstance();
        assertNotNull(neuesPaket);
        assertEquals(paket.getWidth(), neuesPaket.getWidth());
        assertEquals(paket.getHeight(), neuesPaket.getHeight());
        assertEquals(paket.getWeight(), neuesPaket.getWeight());
        assertEquals(paket.getInhalt(), neuesPaket.getInhalt());
    }

    @Test
    void testStapelbarKeitEinfach() {
        PaketModel paketUnten = new PaketModel(50, 20, 10, 10, 20, 100, zutat);
        PaketModel paketOben = new PaketModel(20, 10, 0, 0, 10, zutat);

        try {
            paketUnten.stellePaketDrauf(paketOben);
        } catch (StapelException e) {
            e.printStackTrace();
        }

        assertEquals(paketUnten, paketOben.getAblageFlaeche());
        assertEquals(paketOben, paketUnten.getPakete().get(0));
    }

    @Test
    void testStapelbarKeitMehrere() {
        PaketModel paketUnten = new PaketModel(50, 20, 10, 10, 20, 100, zutat);
        PaketModel paketOben1 = new PaketModel(20, 10, 0, 0, 10, zutat);
        PaketModel paketOben2 = new PaketModel(20, 10, 0, 0, 0, zutat);

        try {
            paketUnten.stellePaketDrauf(paketOben1);
            paketUnten.stellePaketDrauf(paketOben2);
        } catch (StapelException e) {
            e.printStackTrace();
        }

        List<PaketModel> paketeOben = new ArrayList<>();
        paketeOben.add(paketOben2);
        paketeOben.add(paketOben1);

        assertEquals(paketUnten, paketOben1.getAblageFlaeche());
        assertEquals(paketUnten, paketOben2.getAblageFlaeche());
        assertEquals(paketeOben, paketUnten.getPakete());
    }

    @Test
    void testStapelbarKeitZuBreit() {
        PaketModel paketUnten = new PaketModel(50, 20, 10, 10, 20, 100, zutat);
        PaketModel paketOben1 = new PaketModel(20, 10, 0, 0, 10, zutat);
        PaketModel paketOben2 = new PaketModel(20, 10, 0, 0, 0, zutat);
        PaketModel paketOben3 = new PaketModel(20, 10, 0, 0, 0, zutat);

        try {
            paketUnten.stellePaketDrauf(paketOben1);
            paketUnten.stellePaketDrauf(paketOben2);
            paketUnten.stellePaketDrauf(paketOben3);
        } catch (StapelException e) {
            e.printStackTrace();
        }

        List<PaketModel> paketeOben = new ArrayList<>();
        paketeOben.add(paketOben2);
        paketeOben.add(paketOben1);

        assertEquals(paketUnten, paketOben1.getAblageFlaeche());
        assertEquals(paketUnten, paketOben2.getAblageFlaeche());
        assertEquals(null, paketOben3.getAblageFlaeche());
        assertEquals(paketeOben, paketUnten.getPakete());
    }

    @Test
    void testStapelbarKeitZuSchwer() {
        PaketModel paketUnten = new PaketModel(50, 20, 10, 10, 20, 100, zutat);
        PaketModel paketOben1 = new PaketModel(10, 10, 0, 0, 50, zutat);
        PaketModel paketOben2 = new PaketModel(10, 10, 0, 0, 50, zutat);
        PaketModel paketOben3 = new PaketModel(10, 10, 0, 0, 50, zutat);

        try {
            paketUnten.stellePaketDrauf(paketOben1, 0);
            paketUnten.stellePaketDrauf(paketOben2, 0);
            paketUnten.stellePaketDrauf(paketOben3, 0);
        } catch (StapelException e) {
            e.printStackTrace();
        }

        List<PaketModel> paketeOben = new ArrayList<>();
        paketeOben.add(paketOben1);
        paketeOben.add(paketOben2);

        assertEquals(paketUnten, paketOben1.getAblageFlaeche());
        assertEquals(paketUnten, paketOben2.getAblageFlaeche());
        assertEquals(null, paketOben3.getAblageFlaeche());
        assertEquals(paketeOben, paketUnten.getPakete());
    }

    @Test
    void testBreitePaketeDrauf() {

    }
}
