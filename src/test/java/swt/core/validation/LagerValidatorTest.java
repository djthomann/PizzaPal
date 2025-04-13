package swt.core.validation;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.hsrm.mi.swt.core.model.LagerModel;
import de.hsrm.mi.swt.core.validation.LagerValidator;

public class LagerValidatorTest {

    LagerModel testLager;

    @BeforeEach void setUp(){
        testLager = new LagerModel(1000, 800);
    }

    @Test
    public void LagerValidatorErstellen(){
        LagerValidator tesLagerValidator = new LagerValidator(testLager);
        assertNotNull(tesLagerValidator);
    }

    
    
}
