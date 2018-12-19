package test.base.xml;


import java.util.Map;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.base.Assert;
import main.base.runnerConfig.TestConfig;
import main.base.xml.XmlTestConfig;

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
                "Check number of tests",
                10,
                testMap.size());

        // check that all first nine tests have one test cases and correct name 
        IntStream.range(1, testMap.size()).forEach(testIndex -> {
            
            Assert.isEqual(
                    "Check number of test cases for test " + testIndex,
                    1,
                    testMap.get(testIndex).getTestCases().size());

            Assert.isEqual(
                    "Check test name for test " + testIndex,
                    "Test" + testIndex,
                    testMap.get(testIndex).getName());
        });

        Assert.isEqual(
                "Check test name for the 10th test.",
                "Test10",
                testMap.get(10).getName());
        
        Assert.isEqual(
                "Check number of test cases for the 10th test.",
                10,
                testMap.get(10).getTestCases().size());
    }

}

