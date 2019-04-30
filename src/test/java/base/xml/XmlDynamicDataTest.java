package test.java.base.xml;


import java.util.List;

import org.junit.jupiter.api.Test;

import main.java.base.Assert;
import main.java.base.xmlSuite.XmlDynamicData;



public class XmlDynamicDataTest {

    
    @Test
    void pairedOpenClosedCurlyBraces_test() {
        
        String attributeValue = "{This} is a {test} for {dynamic}{$tokens}. Try it{!}";
        List<String> expectedTokens = List.of("{This}", "{test}", "{dynamic}", "{$tokens}", "{!}");
        
        
        System.out.println(XmlDynamicData.getRawTokens(attributeValue));
        
        Assert.assertList(
                
                expectedTokens, 
                
                XmlDynamicData.getRawTokens(attributeValue));
        
    }
    
    
    
    @Test
    void unclosedCurlyBraceAtTheEnd_test() {
        
        String attributeValue = "{This} is a {test} for {dynamic}{$tokens}. Try it{!";
        
        List<String> expectedTokens = List.of("{This}", "{test}", "{dynamic}", "{$tokens}");
        
        
        System.out.println(XmlDynamicData.getRawTokens(attributeValue));
        
        Assert.assertList(
                
                expectedTokens, 
                
                XmlDynamicData.getRawTokens(attributeValue));
    }

    
    
    @Test
    void doubleOpenedCurlyBrace_test() {
        
        String attributeValue = "test {for {dynamic}{$tokens} Try it!";
        List<String> expectedTokens = List.of("{dynamic}", "{$tokens}");
        
        
        System.out.println(XmlDynamicData.getRawTokens(attributeValue));
        
        Assert.assertList(
                
                expectedTokens, 
                
                XmlDynamicData.getRawTokens(attributeValue));
    }
    
}

