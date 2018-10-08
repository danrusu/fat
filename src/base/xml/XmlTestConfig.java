package base.xml;
import static base.Logger.log;
import static base.xml.XmlConfigTags.isXmlConfigTag;
import static base.xml.XmlConfigTags.suite;
import static base.xml.XmlConfigTags.test;
import static base.xml.XmlConfigTags.valueOf;
import static utils.StringUtils.nullToEmptyString;
import static utils.StringUtils.toBoolean;
import static utils.SystemUtils.getPropertyOrDefaultIfNull;
import static utils.SystemUtils.getPropertyOrEmptyString;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import base.JvmArgs;
import base.failures.ThrowablesWrapper;
import base.results.ResultFileType;
import base.runnerConfig.SuiteAttribute;
import base.runnerConfig.TestAttribute;
import base.runnerConfig.TestConfig;
import utils.StringDataProvider;
import utils.StringUtils;


/**
 * Read test configuration from XML file.
 * 
 * @author Dan Rusu
 *
 */
public class XmlTestConfig {

    private static Element root;

    int testIndex;

    private static boolean grid;
    private static String suiteName;
    private static String user;
    private static String project;
    private static String emails;
    private static ResultFileType resultFileType;

    // Map of tests from XML - Map< testName, TestConfig> >
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
     * @return - true if no Exception
     * @throws Exception - invalid test XML exception
     */
    public void init(String xmlTestScenario) throws Exception{

        XmlDoc configDoc = new XmlDoc(new File(xmlTestScenario));

        // validate node tags and get root; this throws for invalid XML
        root = validateTestXml(configDoc);

        List<Element> rootChildren = XmlDoc.getChildren(root);
        
        rootChildren.stream()
            // ignore tags that are not in XmlConfigTags; needed for next filter
            .filter(elementIsXmlConfigTag())
            .filter(elementIsTest())
            .forEach(this::addTest);
    }



    private Predicate<? super Element> elementIsTest() {
        
        return testElement -> valueOf(testElement.getTagName()).equals(test);
    }



    private Predicate<? super Element> elementIsXmlConfigTag() {
        
        return testElement -> isXmlConfigTag(testElement.getTagName());
    }



    /**
     * Validate the test XML and get the root element.
     * 
     * @param configDoc 
     * @return  - the root element of the XML
     * @throws Exception - in case of an invalid tag name; 
     *                     accepted tag names are enumerated in ConfigTags 
     */
    private Element validateTestXml(XmlDoc configDoc) throws Exception{
        // validate config.xml - only tags defined in ConfigTags are accepted 
        Element root = configDoc.getRoot();                                   

        // validate root                                                      
        if ( ! root.getTagName().equals(suite.name()) ){
            throw new Exception("wrong xml root tag name; it should be 'suite' ");
        }                

        // TODO - change this from static
        setSuiteName(root.getAttribute(SuiteAttribute.name.name()));

        setUser(root.getAttribute(SuiteAttribute.user.name()));
        setProject(root.getAttribute(SuiteAttribute.project.name()));
        setSuiteResultFileType(root.getAttribute(SuiteAttribute.resultType.name()));

        setEmail(root.getAttribute(SuiteAttribute.email.name()));
        setGrid(root.getAttribute(SuiteAttribute.grid.name()));

        return root;
    }



    private void setGrid(String attribute) {
        XmlTestConfig.grid = toBoolean(attribute);
    }



    /**                                                       
     * Add tests and tests' attributes to tests' map.
     * 
     * @param e - XML element (tag with attributes)
     */
    private void addTest(Element e){

        NamedNodeMap attributes;
        Map <String, String> testAttributes = new TreeMap<>();	
        attributes = e.getAttributes();

        String browserProperty = getPropertyOrEmptyString(JvmArgs.browser); 
        String browser = TestAttribute.browser.name();


        for ( int j=0; j < attributes.getLength(); j++){

            if ( ! browserProperty.isEmpty() ){

                if ((attributes.item(j).getNodeName().equals(browser)))
                {
                    testAttributes.put(
                            attributes.item(j).getNodeName(), 
                            browserProperty);	
                    continue;
                }
            }  

            testAttributes.put(attributes.item(j).getNodeName(), 
                    attributes.item(j).getNodeValue());			
        }

        int dataProviderLength = StringDataProvider.getDataLength(
                testAttributes.get("dataProvider"));
                
        
        for (int dataProviderIndex=0;
                
                dataProviderIndex < dataProviderLength;
                
                dataProviderIndex++) {



            // get test cases info
            List<Element> testCasesList = XmlDoc.getChildren(e);
            Map<Integer, Map<String, String>> testCases = new TreeMap<>();
            Map<String, String> testCaseAttributes = new TreeMap<>();


            int k=0;
            for(Element tc : testCasesList){


                testCaseAttributes = getAttributes(tc.getAttributes());

                testCaseAttributes.put("dataProviderIndex", dataProviderIndex + "");


                if (! nullToEmptyString(testAttributes.get(browser)).isEmpty()){						
                    testCaseAttributes.put(browser, testAttributes.get(browser));
                }

                testCases.put(++k, testCaseAttributes);
            }


            TestConfig testConfig = new TestConfig(testAttributes, testCases);

            // add test with attributes to tests' map
            testMap.put(testIndex++, testConfig);
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
            init(System.getProperty("user.dir") + "/" + testXml);

        } catch (Exception e) {
            log("Wrong configuration!!!");
            e.printStackTrace();
            return new TreeMap<Integer, TestConfig>();
        }

        logTestConfig(testMap);

        return testMap;
    }



    public static Map<Integer, TestConfig> getTestsMap(){
        return testMap;
    }



    public Map<String, String> getAttributes(NamedNodeMap attributesMap){
       
        return IntStream.range(0, attributesMap.getLength())
                
                .mapToObj(attributesMap::item)
                
                .collect(Collectors.toMap(
                        Node::getNodeName,
                        Node::getNodeValue));
        }



    public static String getSuiteName() {
        
        return suiteName;
    }

    
    
    public static ResultFileType getSuiteResultFileType() {
        
        return resultFileType;
    }



    public static void setSuiteName(String suiteName) {
        
        XmlTestConfig.suiteName = getPropertyOrDefaultIfNull(
                JvmArgs.suiteName, 
                suiteName);
    }


    
    public static void setSuiteResultFileType(String resultFileType) {
    
            XmlTestConfig.resultFileType = ThrowablesWrapper.wrapAssignment(
                    
                    () -> ResultFileType.valueOf(resultFileType),
                    
                    ResultFileType.html);
    }

    
    
    public static String getUser() {
    
        return XmlTestConfig.user;
    }


    
    private static void setUser(String user) {
        
        XmlTestConfig.user = user;
    }

    

    public static String getProject() {
        
        return XmlTestConfig.project;
    }



    private static void setProject(String project) {
        
        XmlTestConfig.project = project;
    }



    private static void setEmail(String email) {
        
        XmlTestConfig.emails = email;
    }


    private void logTestConfig(Map<Integer, TestConfig> tests) {
        
        tests.keySet().forEach(
        
                k -> {
                    log("Test_" + k + ": " + tests.get(k).getTestAttributes());
                    
                    tests.get(k).getTestCases().entrySet().forEach(
                            
                            e -> log("TestCase " + k + "_" + e.getKey() + ": " + e.getValue()));
                });
    }



    public static boolean getGrid() {

        return XmlTestConfig.grid;
    }



    public static List<String> getEmails() {

        return StringUtils.splitBy(emails, ";");
    }

}

