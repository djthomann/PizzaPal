package swt.core.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.hsrm.mi.swt.core.model.LagerModel;
import de.hsrm.mi.swt.core.model.RegalModel;
import de.hsrm.mi.swt.core.model.entities.BrettModel;
import de.hsrm.mi.swt.core.model.entities.StuetzeModel;

public class LagerModelTest {
    StuetzeModel stuetzeLinks;
    StuetzeModel stuetzeRechts;
    BrettModel brett;
    List<BrettModel> bretter;
    LagerModel testLager;
    RegalModel testRegal;


    @BeforeEach void setUp(){
        testLager = new LagerModel(500, 200);
        stuetzeLinks = new StuetzeModel(5, 100);
        stuetzeRechts = new StuetzeModel(6, 100);
        brett = new BrettModel(60, 5, 60);
        bretter = new ArrayList<>();
        bretter.add(brett);
        testRegal = new RegalModel(stuetzeLinks, stuetzeRechts, bretter);
        testLager.addRegal(testRegal);
    }


    @Test
    public void testKonstruktor(){
        assertNotNull(testLager);
    }

    @Test
    public void testRegalIterator(){
        RegalModel regalAusList = testLager.regalIterator().next();
        assertNotNull(regalAusList);
    }

    @Test
    public void testAnzahlRegale(){
        assertEquals(1, testLager.anzahlRegale());
    }

    @Test
    public void addRegal(){
        StuetzeModel stuetzeLinks = new StuetzeModel(5, 100);
        StuetzeModel stuetzeRechts = new StuetzeModel(6, 100);
        BrettModel brett = new BrettModel(60, 5, 60);
        List<BrettModel> bretter = new ArrayList<>();
        bretter.add(brett);
        RegalModel testRegal2 = new RegalModel(stuetzeLinks, stuetzeRechts, bretter);
        testLager.addRegal(testRegal2);
        assertEquals(2, testLager.anzahlRegale());
    }

    @Test
    public void testGetWidth(){
        assertEquals(500, testLager.getWidth());
    }

    @Test
    public void testSetWidth(){
        testLager.setWidth(1000);
        assertEquals(1000, testLager.getWidth());
    }

    @Test
    public void testGetHeight(){
        assertEquals(200, testLager.getHeight());
    }

    @Test
    public void testSetHeight(){
        testLager.setHeight(300);
        assertEquals(300, testLager.getHeight());
    }

    @Test
    public void testGetRegale(){
        List<RegalModel> regale = testLager.getRegale();
        assertNotNull(regale);
    }

    @Test
    public void testSetRegale(){
        StuetzeModel stuetzeLinks = new StuetzeModel(5, 100);
        StuetzeModel stuetzeRechts = new StuetzeModel(6, 100);
        BrettModel brett = new BrettModel(60, 5, 60);
        List<BrettModel> bretter = new ArrayList<>();
        bretter.add(brett);
        RegalModel testRegal3 = new RegalModel(stuetzeLinks, stuetzeRechts, bretter);
        List<RegalModel> regale = new ArrayList<>();
        regale.add(testRegal3);
        testLager.setRegale(regale);
        assertNotNull(testLager.getRegale());
        

    }

    

    
}
