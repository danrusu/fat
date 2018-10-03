package baseTest;

import org.junit.jupiter.api.Test;

import base.Assert;
import utils.StringUtils;

class StringUtilsSpec {
    
    
    final String text1 = "This is Azets test automation 1234.";
    final String text2 = "This is Visma test automation 1111.";

    final String textAfterRemovingAllRegex = "testautomation";
    
    final String[] regex = { "^This is", "Azets|Visma", "\\d{4}\\.$", "\\s" }; 
    
    
    
    @Test
    void removeAllRegexTest() {
        
        Assert.equals(                                
                
                textAfterRemovingAllRegex,
                
                StringUtils.removeAllRegex(text1, regex));
    }
    
    
    
    @Test
    void equalsIgnoringTest() {
        
        Assert.assertTrue(
                
                "Validate StringUtils.equalsIgnoring", 
                
                StringUtils.equalsIgnoring(
                        text1, 
                        text2, 
                        regex));
    }
    
}

