package main.java.base.xmlSuite;

import java.util.List;

/**
 * Accepted tags within testScenario.xml
 *
 */
enum XmlConfigTags{
    
    suite,
    test,
    testcase;
    
    public static boolean isXmlConfigTag(String tagName){
        
       return  List.of(values()).stream()
               .anyMatch(configTag -> configTag.name().equals(tagName));
    }
}
