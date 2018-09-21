package baseTest;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

import base.Assert;

public class AssertAllSpec { 


    @Test
    public void assertAllTest() {


        assertThrows(
                
                AssertionError.class,

                () -> Assert.verifyAll(

                        () -> Assert.equals("Test two strings 1", 
                                "expected", 
                                "actual"),


                        () -> Assert.equals("Test two strings match case 3", 
                                "expected", 
                                "Expected"),


                        () -> Assert.equalsIgnoreCase("Test two strings ignore case 2", 
                                "expected", 
                                "Expected"),


                        () -> Assert.equalsFloatStrings("Test two float strings", 
                                "11.11", 
                                "11.12")));
    }

}
