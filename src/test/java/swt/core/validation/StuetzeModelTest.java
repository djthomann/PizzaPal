package swt.core.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.hsrm.mi.swt.core.model.RegalModel;
import de.hsrm.mi.swt.core.model.entities.BrettModel;
import de.hsrm.mi.swt.core.model.entities.StuetzeModel;

public class StuetzeModelTest {
    private StuetzeModel stuetze;
    private BrettModel brett1, brett2;
    private Set<BrettModel> bretterRechts;
    private Set<BrettModel> bretterLinks;

    @BeforeEach
    void setUp() {
        stuetze = new StuetzeModel(10, 100, 50);
        brett1 = new BrettModel(120, 30, 50, null, null, 500);
        brett2 = new BrettModel(130, 25, 80, null, null, 450);

    }

    @Test
    public void testKonstruktor() {
        assertNotNull(stuetze);
    }

    @Test
    public void testSetAnGetWidth() {
        stuetze.setWidth(20);
        assertEquals(20, stuetze.getWidth());
    }

    @Test
    public void testSetAndGetHeight() {
        stuetze.setHeight(150);
        assertEquals(150, stuetze.getHeight());
    }

    @Test
    public void testAddBrettRechts() {
        stuetze.addBrettRechts(brett1);
        assertTrue(stuetze.getBretterRechts().contains(brett1));
    }

    @Test
    public void testLoeseBrett() {
        stuetze.addBrettRechts(brett1);
        stuetze.loeseBrett(brett1);
        assertFalse(stuetze.getBretterRechts().contains(brett1));
    }

    @Test
    public void testBrettIterator() {
        stuetze.addBrettRechts(brett1);
        stuetze.addBrettLinks(brett2);
        Iterator<BrettModel> iteratorRechts = stuetze.bretterRechts();
        Iterator<BrettModel> iteratorLinks = stuetze.bretterLinks();
        assertTrue(iteratorRechts.hasNext());
        assertTrue(iteratorLinks.hasNext());
        assertEquals(brett1, iteratorRechts.next());
        assertEquals(brett2, iteratorLinks.next());
    }

    @Test
    public void testNaechstesBrettDrueberRechts() {
        stuetze.addBrettRechts(brett1);
        stuetze.addBrettRechts(brett2);
        brett1.setStuetzeLinks(stuetze);
        brett2.setStuetzeLinks(stuetze);

        assertEquals(brett1, stuetze.naechstesBrettDrueberRechts(stuetze.getPosY() + 50));
    }

    @Test
    public void testNaechstesBrettDrueberLinks() {

    }

    @Test
    public void testNaechstesBrettDrunterRechts() {

    }

    @Test
    public void testNaechstesBrettDrunterLinks() {

    }

    @Test
    public void testGetBretterRechtsImIntervall() {
        stuetze.addBrettRechts(brett1);
        stuetze.addBrettRechts(brett2);
        brett1.setStuetzeLinks(stuetze);
        brett2.setStuetzeLinks(stuetze);

        List<BrettModel> sollBretter = new ArrayList<>();
        sollBretter.add(brett2);
        sollBretter.add(brett1);
        List<BrettModel> istBretter = stuetze.getBretterRechtsImIntervall(stuetze.getPosY(),
                stuetze.getPosY() + stuetze.getHeight());
        assertEquals(sollBretter, istBretter);
    }

    @Test
    public void testGetBretterLinksImIntervall() {

    }

    @Test
    public void testHatBretterRechts() {
        bretterRechts = new HashSet<>();
        bretterLinks = new HashSet<>();
        bretterRechts.add(brett1);
        bretterLinks.add(brett2);
        stuetze.setBretterLinks(bretterLinks);
        stuetze.setBretterRechts(bretterRechts);
        assertTrue(stuetze.hatBretterRechts());
    }

    @Test
    public void testHatBretterLinks() {
        bretterRechts = new HashSet<>();
        bretterLinks = new HashSet<>();
        bretterRechts.add(brett1);
        bretterLinks.add(brett2);
        stuetze.setBretterLinks(bretterLinks);
        stuetze.setBretterRechts(bretterRechts);
        assertTrue(stuetze.hatBretterLinks());
    }

    @Test
    public void testIteratorBretterLinks() {
        bretterRechts = new HashSet<>();
        bretterLinks = new HashSet<>();
        bretterRechts.add(brett1);
        bretterLinks.add(brett2);
        stuetze.setBretterLinks(bretterLinks);
        stuetze.setBretterRechts(bretterRechts);
        assertTrue(stuetze.bretterLinks().hasNext());
    }

    @Test
    public void testIteratorBretterRechts() {
        bretterRechts = new HashSet<>();
        bretterLinks = new HashSet<>();
        bretterRechts.add(brett1);
        bretterLinks.add(brett2);
        stuetze.setBretterLinks(bretterLinks);
        stuetze.setBretterRechts(bretterRechts);
        assertTrue(stuetze.bretterLinks().hasNext());

    }

    @Test
    public void testAnzahlBretterRechts() {
        bretterRechts = new HashSet<>();
        bretterRechts.add(brett1);
        stuetze.setBretterRechts(bretterRechts);
        assertEquals(1, stuetze.anzahlBretterRechts());
    }

    @Test
    public void testAnzahlBretterLinks() {
        bretterLinks = new HashSet<>();
        bretterLinks.add(brett2);
        stuetze.setBretterLinks(bretterLinks);
        assertEquals(1, stuetze.anzahlBretterLinks());

    }

    @Test
    void testMakeInstance() {
        StuetzeModel kopie = (StuetzeModel) stuetze.makeInstance();
        assertEquals(stuetze.getWidth(), kopie.getWidth());
        assertEquals(stuetze.getHeight(), kopie.getHeight());
        assertEquals(stuetze.getPosX(), kopie.getPosX());
    }

    @Test
    public void testGetWidth() {
        assertEquals(10, stuetze.getWidth());
    }

    @Test
    public void testSetWidth() {
        stuetze.setWidth(20);
        assertEquals(20, stuetze.getWidth());
    }

    @Test
    public void testGetHeight() {
        assertEquals(100, stuetze.getHeight());
    }

    @Test
    public void testSetHeight() {
        stuetze.setHeight(270);
        assertEquals(270, stuetze.getHeight());
    }

    @Test
    public void testGetPosX() {
        assertEquals(50, stuetze.getPosX());
    }

    @Test
    public void testSetPosX() {
        stuetze.setPosX(400);
        assertEquals(400, stuetze.getPosX());
    }

    @Test
    public void testGetPosY() {

    }

    @Test
    public void testSetPosY() {

    }

    @Test
    public void testSetAndGetRegal() {

    }

    @Test
    public void testAddBrettLinks() {
        stuetze.addBrettLinks(brett1);
        assertTrue(stuetze.getBretterLinks().contains(brett1));
    }

}
