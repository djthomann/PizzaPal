package swt.core.validation;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.hsrm.mi.swt.core.model.zutat.Zutat;
import javafx.scene.paint.Color;

public class ZutatTest {
    Color color;
    Set<Zutat> unverstraeglichkeiten;


    @BeforeEach void setUp(){
        color = new Color(0, 1, 0.5, 1);
        Zutat ersteUnvertraeglichkeit = new Zutat("Käse", color);
        unverstraeglichkeiten = new HashSet<>();
        unverstraeglichkeiten.add(ersteUnvertraeglichkeit);
    }

    @Test
    public void zutatErstellen(){
        Zutat testZutat = new Zutat("Tomaten", color, unverstraeglichkeiten);
        assertNotNull(testZutat);
        
    }
    

    
}
