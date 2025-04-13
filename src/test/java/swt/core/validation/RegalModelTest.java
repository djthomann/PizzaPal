package swt.core.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import de.hsrm.mi.swt.core.model.RegalModel;
import de.hsrm.mi.swt.core.model.entities.BrettModel;
import de.hsrm.mi.swt.core.model.entities.StuetzeModel;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RegalModelTest {
    StuetzeModel stuetzeLinks;
    StuetzeModel stuetzeRechts;
    BrettModel brett;
    List<BrettModel> bretter;
    RegalModel testRegal;
    BrettModel brettNeu;

    @BeforeEach void setUp(){
        stuetzeLinks = new StuetzeModel(5, 100);
        stuetzeRechts = new StuetzeModel(6, 100);
        brett = new BrettModel(60, 5, 60);
        bretter  = new ArrayList<>();
        bretter.add(brett);
        testRegal = new RegalModel(stuetzeLinks, stuetzeRechts, bretter);
        brettNeu = new BrettModel(40, 3, 100);
    }

    @Test
    public void testKonstruktor(){
        assertNotNull(testRegal, "Regal sollte nach initialiserung nicht null sein");
    }

    @Test
    public void testStuetzenIterator(){
        StuetzeModel stuetzeAusIterator = testRegal.stuetzenIterator().next();
        assertEquals( 5, stuetzeAusIterator.getWidth());
    }

    @Test
    public void testGetStuetzeLinks(){
        StuetzeModel stuetzeLinks = testRegal.getStuetzeLinks();
        assertEquals(5, stuetzeLinks.getWidth());
    }

    @Test
    public void testGetStuetzeRechts(){
        StuetzeModel stuetzeRechts = testRegal.getStuetzeRechts();
        assertEquals(6, stuetzeRechts.getWidth());
    }

    @Test
    public void testRechtsErweitern(){
        StuetzeModel rechtsZuErweiterndeStuetze = new StuetzeModel(10, 100);
        testRegal.rechtsErweitern(rechtsZuErweiterndeStuetze);
        assertEquals(10, testRegal.getStuetzeRechts().getWidth());
    }

    @Test
    public void testLinksErweitern(){
        StuetzeModel linksZuErweiterndeStuetze = new StuetzeModel(11, 100);
        testRegal.linksErweitern(linksZuErweiterndeStuetze);
        assertEquals(11, testRegal.getStuetzeLinks().getWidth());
    }

    @Test
    public void testSetStuetzeLinks(){
        StuetzeModel neueStuetzeLinks = new StuetzeModel(90, 400);
        testRegal.setStuetzeLinks(neueStuetzeLinks);
        assertEquals(neueStuetzeLinks, testRegal.getStuetzeLinks());
    }

    @Test
    public void testSetStuetzeRechts(){
        StuetzeModel neueStuetzeRechts = new StuetzeModel(90, 400);
        testRegal.setStuetzeRechts(neueStuetzeRechts);
        assertEquals(neueStuetzeRechts, testRegal.getStuetzeRechts());
    }

    @Test
    public void testSetBretter(){
        List<BrettModel> neueBretter = new ArrayList<>();
        BrettModel neuesBrett = new BrettModel(1, 2, 555);
        neueBretter.add(neuesBrett);
        testRegal.setBretter(neueBretter);
        BrettModel brettAusList = testRegal.brettIterator().next();
        assertEquals(555, brettAusList.getTragkraft());

    }

    @Test
    public void testGetStuetzen(){
        List<StuetzeModel> stuetzen = testRegal.getStuetzen();
        assertNotNull(stuetzen);

    }

    @Test
    public void testSetStuetzen(){
        List<StuetzeModel> neueStuetzen = new ArrayList<>();
        StuetzeModel neueStuetze = new StuetzeModel(1, 2, 55);
        neueStuetzen.add(neueStuetze);
        testRegal.setStuetzen(neueStuetzen);
        StuetzeModel stuetzeAusList = testRegal.stuetzenIterator().next();
        assertEquals(55, stuetzeAusList.getPosX());
    }

    @Test
    public void testBrettIterator(){
        BrettModel brettAusList = testRegal.brettIterator().next();
        assertEquals(60, brettAusList.getTragkraft());
    }

    @Test
    public void testAddBrett(){
        int startAnzahlBretter = testRegal.getBretter().size();
        assertEquals(1, startAnzahlBretter, "InitialerWert muss 1 sein");
        testRegal.addBrett(brettNeu);
        assertEquals(startAnzahlBretter + 1, testRegal.getBretter().size());
    }

    @Test
    public void testGetBretter(){
        assertEquals(1, testRegal.getBretter().size());
    }

   

   

    







    
}
