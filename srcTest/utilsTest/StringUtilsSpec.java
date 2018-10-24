package utilsTest;

import java.util.List;

import org.junit.jupiter.api.Test;

import base.Assert;
import utils.StringUtils;

class StringUtilsSpec {
    
    
    final String text1 = "This is Azets test automation 1234.";
    final String text2 = "This is Visma test automation 1111.";

    final String textAfterRemovingAllRegex = "testautomation";
    
    final String[] regex = { "^This is", "Azets|Visma", "\\d{4}\\.$", "\\s" }; 
    
        
    
    
    @Test
    void splitBy_sameFinalListSize_test() {
        
        Assert.assertList(                                
                
                List.of("users", "password", "age"),
                
                StringUtils.splitBy(
                        "users, password, age",
                        ", ",
                        3));
    }

    
    @Test
    void splitBy_biggerFinalListSize_test() {
        
        Assert.assertList(                                
                
                List.of("users", "password", "age", "", ""),
                
                StringUtils.splitBy(
                        "users, password, age",
                        ", ",
                        5));
    }
    
    
    
    @Test
    void splitBy_smallerFinalListSize_test() {
        
        Assert.assertList(                                
                
                List.of("users", "password", "age"),
                
                StringUtils.splitBy(
                        "users, password, age",
                        ", ",
                        1));
    }
    
    
    
    @Test
    void removeAllRegexTest() {
        
        Assert.isEqual(                                
                
                textAfterRemovingAllRegex,
                
                StringUtils.removeAllRegex(text1, regex));
    }
    
    
    
    @Test
    void equalsIgnoringTest() {
        
        Assert.isTrue(
                
                "Validate StringUtils.equalsIgnoring", 
                
                StringUtils.equalsIgnoring(
                        text1, 
                        text2, 
                        regex));
    }
    
}

