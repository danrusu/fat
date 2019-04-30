package test.java.base.xml;


import java.util.Map;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.java.base.Assert;
import main.java.base.runnerConfig.TestConfig;
import main.java.base.xmlSuite.XmlTestConfig;

public class XmlConfig_IntegrationTest {
    
    private String xmlTestFile;
    private XmlTestConfig testConfig;
    private Map<Integer, TestConfig> testMap;

    
    
    @BeforeEach
    public void setUp() throws Exception {

        xmlTestFile = "XML/unitTest/UtXmlTestConfig.xml"; 

        testConfig = new XmlTestConfig();
    }

    
    
    @Test
    public void testXmlConfig() {
        
        testMap = testConfig.getTestConfig(xmlTestFile);     
        
        Assert.isEqual(
                10,
                testMap.size(),
                "Check number of tests");

        // check that all first nine tests have one test cases and correct name 
        IntStream.range(1, testMap.size()).forEach(testIndex -> {
            
            Assert.isEqual(
                    1,
                    testMap.get(testIndex).getTestCases().size(),
                    "Check number of test cases for test " + testIndex);

            Assert.isEqual(
                    "Test" + testIndex,
                    testMap.get(testIndex).getName(),
                    "Check test name for test " + testIndex);
        });

        Assert.isEqual(
                "Test10",
                testMap.get(10).getName(),
                "Check test name for the 10th test.java.");
        
        Assert.isEqual(
                10,
                testMap.get(10).getTestCases().size(),
                "Check number of test cases for the 10th test.java.");
    }

}

