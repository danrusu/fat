package baseTest;


import java.util.List;

import org.junit.jupiter.api.Test;

import base.Assert;
import base.xml.XmlDynamicData;



class XmlDynamicDataSpec {


    
    @Test
    void testGetDynamicTokens_pairedOpenClosedCurlyBraces() {
        
        String attributeValue = "{This} is a {test} for {dynamic}{$tokens}. Try it{!}";
        List<String> expectedTokens = List.of("{This}", "{test}", "{dynamic}", "{$tokens}", "{!}");
        
        
        System.out.println(XmlDynamicData.getRawTokens(attributeValue));
        
        Assert.assertList(
                
                expectedTokens, 
                
                XmlDynamicData.getRawTokens(attributeValue));
        
    }
    
    
    
    @Test
    void testGetDynamicTokens_unclosedCurlyBraceAtTheEnd() {
        
        String attributeValue = "{This} is a {test} for {dynamic}{$tokens}. Try it{!";
        
        List<String> expectedTokens = List.of("{This}", "{test}", "{dynamic}", "{$tokens}");
        
        
        System.out.println(XmlDynamicData.getRawTokens(attributeValue));
        
        Assert.assertList(
                
                expectedTokens, 
                
                XmlDynamicData.getRawTokens(attributeValue));
    }

    
    
    @Test
    void testGetDynamicTokens_doubleOpenedCurlyBrace() {
        
        String attributeValue = "test {for {dynamic}{$tokens} Try it!";
        List<String> expectedTokens = List.of("{dynamic}", "{$tokens}");
        
        
        System.out.println(XmlDynamicData.getRawTokens(attributeValue));
        
        Assert.assertList(
                
                expectedTokens, 
                
                XmlDynamicData.getRawTokens(attributeValue));
    }
    
}

