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

                        () -> Assert.isEqual("Test two strings 1", 
                                "expected", 
                                "actual"),


                        () -> Assert.isEqual("Test two strings match case 3", 
                                "expected", 
                                "Expected"),


                        () -> Assert.isEqualIgnoreCase("Test two strings ignore case 2", 
                                "expected", 
                                "Expected"),


                        () -> Assert.isEqualAsFloat("Test two float strings", 
                                "11.11", 
                                "11.12")));
    }

}
