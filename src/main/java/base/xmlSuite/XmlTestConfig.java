package main.java.base.xmlSuite;

import static java.util.stream.Collectors.toMap;
import static main.java.base.Logger.log;
import static main.java.base.failures.ThrowablesWrapper.supplyUnchecked;
import static main.java.base.xmlSuite.XmlConfigTags.isXmlConfigTag;
import static main.java.base.xmlSuite.XmlConfigTags.suite;
import static main.java.base.xmlSuite.XmlConfigTags.test;
import static main.java.base.xmlSuite.XmlConfigTags.valueOf;
import static main.java.utils.StringUtils.toBoolean;
import static main.java.utils.SystemUtils.getPropertyOrDefaultIfNull;
import static main.java.utils.SystemUtils.getPropertyOrEmptyString;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import main.java.base.JvmArgs;
import main.java.base.failures.Failure;
import main.java.base.result.ResultFileType;
import main.java.base.runnerConfig.SuiteAttribute;
import main.java.base.runnerConfig.TestAttribute;
import main.java.base.runnerConfig.TestCaseAttribute;
import main.java.base.runnerConfig.TestConfig;
import main.java.utils.StringUtils;


/**
 * Read test configuration from XML file.
 * 
 * @author Dan Rusu
 */
public class XmlTestConfig {

    private static Element root;

    private static int testIndex;

    private static boolean grid;
    private static String suiteName;
    private static String user;
    private static String project;
    private static String emails;
    private static ResultFileType resultFileType;

    // Map of tests from XML - Map<testName, TestConfig>>
    private static Map<Integer, TestConfig> testMap;


    public XmlTestConfig(){

        testMap = new TreeMap<Integer, TestConfig>();
        testIndex = 1;
        
        // default result file type 
        resultFileType = ResultFileType.HTML;
    }


    /**
     * Initialize configuration from XML file.
     * 
     * @param xmlTestScenario - the test XML file (path relative to user.dir)
     * @return 
     * @throws Exception - invalid test XML exception
     */
    public Map<Integer, TestConfig> setTestMap(String xmlTestScenario){

        XmlDoc configXml = new XmlDoc(new File(xmlTestScenario));

        // validate node tags and get root; this throws for invalid XML
        root = getXmlRoot(configXml);

        XmlDoc.getChildren(root).stream()
            // ignore tags that are not in XmlConfigTags; needed for elementIsTest filter
            .filter(elementIsXmlConfigTag)
            .filter(elementIsTest)
            .forEach(this::addTest);
        
        return testMap;
    }


    private Predicate<Element> elementIsTest = testElement -> 

        valueOf(testElement.getTagName()).equals(test); 


    private Predicate<Element> elementIsXmlConfigTag = testElement -> 

        isXmlConfigTag(testElement.getTagName());


    /**
     * Validate the test XML and get the root element.
     */
    private Element getXmlRoot(XmlDoc configDoc){
        // validate config.xml - only tags defined in ConfigTags are accepted 
        Element root = configDoc.getRoot();                                   

        // validate root                                                      
        if ( ! root.getTagName().equals(suite.name()) ){
            throw new Failure("Wrong xml root tag name; it should be 'suite'");
        }                

        // TODO - change this from static
        setSuiteName(root.getAttribute(SuiteAttribute.name.name()));

        setUser(root.getAttribute(SuiteAttribute.user.name()));
        setProject(root.getAttribute(SuiteAttribute.project.name()));
        setSuiteResultFileType(root.getAttribute(SuiteAttribute.resultType.name()));

        setEmail(root.getAttribute(SuiteAttribute.email.name()));
        setGrid(root.getAttribute(SuiteAttribute.grid.name()));

        // TODO - validate at least one test + one test case 
        return root;
    }


    /**                                                       
     * Add tests and tests' attributes to tests' map.
     * 
     * @param testElement - XML element (tag with attributes)
     */
    private void addTest(Element testElement){

        NamedNodeMap attributes = testElement.getAttributes();

        String browserProperty = getPropertyOrEmptyString(JvmArgs.BROWSER); 
        String browserAttribute = TestAttribute.browser.name();        
                
        Function<Integer, String> getAttributeNameAtIndex = index -> 
        	attributes.item(index).getNodeName();        
        
        Function<Integer, String> getAttributeValueAtIndex = index -> 
        	attributes.item(index).getNodeValue().toString();
        

        Map<String, String> testAttributesMap = IntStream.range(0, attributes.getLength())
                
                .mapToObj(i -> i)
                
                .collect(toMap(getAttributeNameAtIndex, getAttributeValueAtIndex));
        
        Map<String, String> evaluatedTestAttributes = evaluateTestAttributes(testAttributesMap);
        
        
        //TODO - redesign this (no mutation)
        if (! browserProperty.isEmpty() && 
        		evaluatedTestAttributes.containsKey(browserAttribute)){
                
        	evaluatedTestAttributes.put(browserAttribute, browserProperty);                
        }

        String dataProvider = evaluatedTestAttributes.get(TestAttribute.dataProvider.name());
        int dataProviderLength = DataProvider.getDataLength(dataProvider);

        dataProviderTestLoop(
        		testElement, 
        		evaluatedTestAttributes, 
        		dataProviderLength);
    }


	private Map<String, String> evaluateTestAttributes(Map<String, String> testAttributesMap) {
		
		return testAttributesMap.entrySet().stream()
	    		.collect(toMap(
	    				Entry::getKey, 
	    				entry -> XmlDynamicData.evaluateAttributeValue(entry.getValue())));
	}


    private void dataProviderTestLoop(
            Element testElement, 
            Map<String, String> testAttributesMap,
            int dataProviderLength) {
        
        String browserAttribute = TestAttribute.browser.name();  


        for (int dataProviderIndex=0;

                dataProviderIndex <= dataProviderLength;

                dataProviderIndex++) {
        	
            //updateTestName(testAttributesMap, dataProviderIndex);        	

            // get test cases info
            List<Element> testCasesList = XmlDoc.getChildren(testElement);
            
            Map<Integer, Map<String, String>> testCasesMap = new TreeMap<>();
            Map<String, String> testCaseAttributes = new TreeMap<>();
            
            int k=0;
            for(Element tc : testCasesList){

                testCaseAttributes = getAttributes(tc.getAttributes());

                if(dataProviderLength > 0) {
                        testCaseAttributes.put(
                                TestCaseAttribute.dataProviderIndex.name(), 
                                dataProviderIndex + "");
                }


                if (! StringUtils.nullToEmptyString(testAttributesMap.get(browserAttribute)).isEmpty()){  
                    
                    testCaseAttributes.put(browserAttribute, testAttributesMap.get(browserAttribute));
                }

                testCasesMap.put(++k, testCaseAttributes);
            }


            TestConfig testConfig = new TestConfig(testAttributesMap, testCasesMap);

            // add test with attributes to tests' map
            testMap.put(testIndex++, testConfig);
            
            // TODO - redesign this
            if (dataProviderLength > 0 && (dataProviderIndex == (dataProviderLength-1))){
                break;
            }
        }
    }



    /**
     * Get tests map from XML.
     * 	
     * @param testXml - the test XML file (path relative to user.dir)
     * @return - the tests' map if succeeded or empty map if failed.
     */
    public Map<Integer, TestConfig> getTestConfig(String testXml){

        // read & validate configuration config.xml file
        log("Read configuration: " 
                + System.getProperty("user.dir")
                + "/" +  testXml);		

        try {
            
            setTestMap(System.getProperty("user.dir") + "/" + testXml);

        } catch (Exception e) {
            
            log("Wrong configuration!!!");
            e.printStackTrace();
            return new TreeMap<>();
        }

        logTestConfig(testMap);

        return testMap;
    }


    public Map<String, String> getAttributes(NamedNodeMap attributesMap){

        return IntStream.range(0, attributesMap.getLength())

                .mapToObj(attributesMap::item)

                .collect(toMap(
                        Node::getNodeName,
                        Node::getNodeValue));
    }


    private void logTestConfig(Map<Integer, TestConfig> tests) {

        tests.entrySet().forEach(testEntry -> {
                    
            log("Test_" + testEntry.getKey() + ": " + testEntry.getValue().getTestAttributes());

            testEntry.getValue().getTestCases().entrySet().forEach(testCaseEntry -> 
            
            log("TestCase " + testEntry.getKey() 
                    + "_" + testCaseEntry.getKey() 
                    + ": " + testCaseEntry.getValue()));
        });
    }

    
    // *** Setters
    public static void setSuiteName(String suiteName) {

        XmlTestConfig.suiteName = getPropertyOrDefaultIfNull(
                JvmArgs.SUITE_NAME, 
                suiteName);
    }
    
    
    private static void setUser(String user) {

        XmlTestConfig.user = user;
    }


    private static void setProject(String project) {

        XmlTestConfig.project = project;
    }
    
      
    private static void setEmail(String email) {

        XmlTestConfig.emails = email;
    }
    
      
    private void setGrid(String attribute) {
        XmlTestConfig.grid = toBoolean(attribute);
    }

    
    public static void setSuiteResultFileType(String resultFileType) {

        XmlTestConfig.resultFileType = supplyUnchecked(

                () -> ResultFileType.valueOf(resultFileType),

                ResultFileType.HTML);
    }
    
    
    // *** Getters
    public static String getSuiteName() {

        return suiteName;
    }
    
    
    public static String getUser() {

        return XmlTestConfig.user;
    }

    
    public static String getProject() {
        
        return XmlTestConfig.project;
    }

    
    public static boolean getGrid() {

        return XmlTestConfig.grid;
    }

    
    public static List<String> getEmails() {

        return StringUtils.splitBy(emails, ";");
    }
    
    
    public static ResultFileType getSuiteResultFileType() {

        return resultFileType;
    }

}
