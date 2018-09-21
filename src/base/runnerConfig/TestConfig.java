package base.runnerConfig;

import java.util.Map;
import java.util.Optional;

import utils.StringUtils;

public class TestConfig {

    
    
    private Map<String, String> testAttributes;

    private Map<Integer, Map<String, String>> testCases;



    public TestConfig(
            Map<String, String> testAttributes,
            Map<Integer, Map<String, String>> testCases
            ) {
        
        this.testAttributes = testAttributes;
        this.testCases = testCases;
    }



    // TESTS
    public Map<String, String> getTestAttributes() {

        return testAttributes;
    }



    public void setTestAtributes(Map<String, String> testAttributes) {
        
        this.testAttributes = testAttributes;
    }

    
    
    public String getName(){
        
        return testAttributes.get("name");
    }



    public String getBrowser(){
        
        return StringUtils.nullToEmptyString(
                testAttributes.get("browser"));
    }

    
    
    public boolean isBrowserNeeded() {
        
        return getBrowser().isEmpty() == false;
    }

    
    
    public boolean isTestSkipped() {
        
        return  Optional.ofNullable(getTestAttributes().get("skip"))
                .orElse("false")
                .equalsIgnoreCase("true");
    }
    
    
    // TESTCASES
    
    public Map<Integer, Map<String, String>> getTestCases() {
        
        return testCases;
    }



    public void setTestCases(Map<Integer, Map<String, String>> testCases) {
        
        this.testCases = testCases;
    }

}

