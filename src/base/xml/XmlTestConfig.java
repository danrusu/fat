package base.xml;
import static base.Logger.log;
import static base.xml.XmlConfigTags.isXmlConfigTag;
import static base.xml.XmlConfigTags.suite;
import static base.xml.XmlConfigTags.test;
import static base.xml.XmlConfigTags.valueOf;
import static utils.StringUtils.toBoolean;
import static utils.SystemUtils.getPropertyOrDefaultIfNull;
import static utils.SystemUtils.getPropertyOrEmptyString;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import base.JvmArgs;
import base.failures.Failure;
import base.failures.ThrowablesWrapper;
import base.results.ResultFileType;
import base.runnerConfig.SuiteAttribute;
import base.runnerConfig.TestAttribute;
import base.runnerConfig.TestCaseAttribute;
import base.runnerConfig.TestConfig;
import utils.StringDataProvider;
import utils.StringUtils;


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
        resultFileType = ResultFileType.html;
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

        String browserProperty = getPropertyOrEmptyString(JvmArgs.browser); 
        String browserAttribute = TestAttribute.browser.name();

        
                
        Function<Integer, String> getAttributeNameAtIndex = index -> attributes.item(index).getNodeName();        
        Function<Integer, String> getAttributeValueAtIndex = index -> attributes.item(index).getNodeValue().toString();
        

        Map<String, String> testAttributesMap = IntStream.range(0, attributes.getLength())
                
                .mapToObj(i -> i)
                
                .collect(Collectors.toMap(        
                        getAttributeNameAtIndex,
                        getAttributeValueAtIndex));
        
        
        //TODO - redesign this (no mutation)
        if (! browserProperty.isEmpty() && 
                testAttributesMap.containsKey(browserAttribute)){
                
            testAttributesMap.put(browserAttribute, browserProperty);                
        }

        int dataProviderLength = StringDataProvider.getDataLengthWrapped(
                testAttributesMap.get(TestAttribute.dataProvider.name()));

        dataProviderTestLoop(testElement, testAttributesMap, dataProviderLength);
    }



    private void dataProviderTestLoop(
            Element testElement, 
            Map<String, String> testAttributesMap,
            int dataProviderLength) {
        
        String browserAttribute = TestAttribute.browser.name();
        

        for (int dataProviderIndex=0;

                dataProviderIndex <= dataProviderLength;

                dataProviderIndex++) {



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

                .collect(Collectors.toMap(
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
                JvmArgs.suiteName, 
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

        XmlTestConfig.resultFileType = ThrowablesWrapper.unchekedAssignment(

                () -> ResultFileType.valueOf(resultFileType),

                ResultFileType.html);
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

