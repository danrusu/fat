package main.java.base.runnerConfig;

import static main.java.base.runnerConfig.TestAttribute.browser;
import static main.java.base.runnerConfig.TestAttribute.name;
import static main.java.base.runnerConfig.TestAttribute.skip;
import static main.java.utils.StringUtils.nullToEmptyString;

import java.util.Map;
import java.util.Optional;


public class TestConfig {

        
    private Map<String, String> testAttributes;
    
    private Map<Integer, Map<String, String>> testCases;



    public TestConfig(
            Map<String, String> testAttributes,
            Map<Integer, Map<String, String>> testCases) {
    	
        this.testAttributes = testAttributes;
        this.testCases = testCases;
    }


    //*** TESTS ***
    public Map<String, String> getTestAttributes() {

        return testAttributes;
    }
    
    
    public String getTestAttribute(String attributeName) {

        return getTestAttributes().get(attributeName);
    }
    
    
    public String getTestAttribute(TestAttribute attributeName) {

        return getTestAttributes().get(attributeName.name());
    }
       
    
    public String getName(){
        
        return testAttributes.get(name.name());
    }


    public String getBrowser(){
        
        return nullToEmptyString(testAttributes.get(browser.name()));
    }
   
    
    public boolean isBrowserNeeded() {
        
        return getBrowser().isEmpty() == false;
    }

        
    public boolean isTestSkipped() {
        
        return  Optional.ofNullable(testAttributes.get(skip.name()))
                .orElse("false")
                .equalsIgnoreCase("true");
    }
        
    
    //*** TESTCASES ***  
    public Map<Integer, Map<String, String>> getTestCases() {
        
        return testCases;
    }


    public void setTestCases(Map<Integer, Map<String, String>> testCases) {
        
        this.testCases = testCases;
    }

}
