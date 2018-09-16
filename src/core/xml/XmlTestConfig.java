package core.xml;
import static core.Logger.log;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import core.JvmArgs;
import core.results.ResultFileType;
import core.runnerConfig.SuiteConfigAttributes;
import core.runnerConfig.TestConfig;
import core.runnerConfig.TestConfigAttributes;
import core.testCase.TestCasesPackages;
import utils.StringUtils;
import utils.SystemUtils;


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

    private static List<String> testCasesPackages;

    // Map of tests from XML - Map< testName, TestConfig> >
    private static Map<Integer, TestConfig> testsMap;



    /**
     * Accepted tags within config.xml
     *
     */
    private enum ConfigTags{
        suite,
        test;

        public static boolean contains(String s){
            for( ConfigTags tag : values() ){
                if ( tag.name().equals(s) ) 
                    return true;
            }
            return false;
        } 
    }



    /**
     * Constructor.
     */
    public XmlTestConfig(){
        
        testsMap = new TreeMap<Integer, TestConfig>();
        testIndex = 1;
        // default result file type 
        resultFileType = ResultFileType.html;
    }



    /**
     * Initialize configuration from XML file.
     * 
     * @param testXml - the test XML file (path relative to user.dir)
     * @return - true if no Exception
     * @throws Exception - invalid test XML exception
     */
    public boolean init(String testXml) throws Exception{

        File input = new File( testXml );					
        XmlDoc configDoc = new XmlDoc(input);

        // validate node tags and get root; this throws for invalid XML
        root = validateTestXml(configDoc);
        List<Element> testsList = XmlDoc.getChildren(root);

        testCasesPackages = initTestCasesPackages();

        Iterator<Element> iter = testsList.iterator();
        while ( iter.hasNext() ) {
            
            Element e = iter.next();
            
            switch ( ConfigTags.valueOf(e.getTagName()) ){
                
                case test:
                    addTest(e);
                    break;	

                default:
                    break;
            }		
        }

        return true;
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
        if ( ! root.getTagName().equals(ConfigTags.suite.name()) ){
            throw new Exception("wrong xml root tag name; it should be 'suite' ");
        }                

        // TODO - change this from static
        setSuiteName(root.getAttribute(SuiteConfigAttributes.name.name()));

        setUser(root.getAttribute(SuiteConfigAttributes.user.name()));
        setProject(root.getAttribute(SuiteConfigAttributes.project.name()));
        setSuiteResultFileType(root.getAttribute(SuiteConfigAttributes.resultType.name()));
        
        setEmail(root.getAttribute(SuiteConfigAttributes.email.name()));
        setGrid(root.getAttribute(SuiteConfigAttributes.grid.name()));


        List<Element> testsList = XmlDoc.getChildren(root);
        String testName;
        for (Element e : testsList) {
            testName = e.getTagName();
            // validate test tags         
            if ( ! ConfigTags.contains( testName ) ){                                                                            
                throw new Exception("wrong tag: " + e.getTagName());
            }
        }         

        return root;
    }



    private static List<String> initTestCasesPackages() {

        String testCasesPackages = StringUtils.nullToEmptyString(
                root.getAttribute(SuiteConfigAttributes.packages.name()));

        if (! testCasesPackages.isEmpty()){
            return Arrays.asList(testCasesPackages.split(";"))
                    .stream()
                    .unordered().parallel()
                    .map(packageName -> packageName + ".testCases")
                    .collect(Collectors.toList());
        }

        return new ArrayList<>();
    }



    private void setGrid(String attribute) {
        XmlTestConfig.grid = StringUtils.toBoolean(attribute);
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

        String browserProperty = SystemUtils.getPropertyOrEmptyString(JvmArgs.browser); 
        String browser = TestConfigAttributes.browser.name();


        for (  int j=0; j<attributes.getLength(); j++ ){

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

        // get test cases info
        List<Element> testCasesList = XmlDoc.getChildren(e);
        Map<Integer, Map<String, String>> testCases = new TreeMap<>();
        Map<String, String> testCaseAttributes = new TreeMap<>();


        int k=0;
        for(Element tc : testCasesList){
            testCaseAttributes = getAttributes(tc.getAttributes());

            //dynamicEval(testCaseAttributes); - moved to WebPageTestCase


            if (! StringUtils.nullToEmptyString(testAttributes.get(browser)).isEmpty()){						
                testCaseAttributes.put(browser, testAttributes.get(browser));
            }

            testCases.put(++k, testCaseAttributes);
        }


        TestConfig testConfig = new TestConfig(testAttributes, testCases);
        
        // add test with attributes to tests' map
        testsMap.put(testIndex++, testConfig);

    }





    /**
     * Get tests map from XML.
     * 	
     * @param testXML - the test XML file (path relative to user.dir)
     * @return - the tests' map if succeeded or empty map if failed.
     */
    public Map<Integer, TestConfig> readTestConfig(String testXML){

        // read & validate configuration config.xml file
        log("Read configuration: " 
                + System.getProperty("user.dir")
                + "/" +  testXML);							
        try {
            init(System.getProperty("user.dir") + "/" + testXML);

        } catch (Exception e) {
            log("Wrong configuration!!!");
            e.printStackTrace();
            return new TreeMap<Integer, TestConfig>();
        }

        list(testsMap);

        return testsMap;
    }



    public static Map<Integer, TestConfig> getTestsMap(){
        return testsMap;
    }



    public Map<String, String> getAttributes(NamedNodeMap attributes){
        Map<String, String > attrMap = new TreeMap<>();
        for (  int j=0; j<attributes.getLength(); j++ ){
            attrMap.put(attributes.item(j).getNodeName(), 
                    attributes.item(j).getNodeValue());
        }
        return attrMap;
    }



    public static String getSuiteName() {
        return suiteName;
    }

    public static ResultFileType getSuiteResultFileType() {
        return resultFileType;
    }



    public static void setSuiteName(String suiteName) {
        XmlTestConfig.suiteName = utils.SystemUtils.getPropertyOrDefaultIfNull(JvmArgs.suiteName, suiteName);
    }


    public static void setSuiteResultFileType(String resultFileType) {
        if ( ! resultFileType.isEmpty()){
            XmlTestConfig.resultFileType = ResultFileType.valueOf(resultFileType);
        }
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


    private void list(Map<Integer, TestConfig> tests) {
        tests.keySet().forEach(
                k -> {
                    log("Test_" + k + ": " + tests.get(k).getTestAttributes());
                    tests.get(k).getTestCases().entrySet().forEach(
                            e -> log("TestCase " + k + "_" + e.getKey() + ": " + e.getValue())
                            );
                }
                );

    }



    public static boolean getGrid() {

        return XmlTestConfig.grid;
    }



    public static List<String> getEmails() {

        return Optional.of(
                Arrays.asList(XmlTestConfig.emails.split(";"))
                .stream().collect(Collectors.toList())
                )
                .orElse(new ArrayList<>());
    }



    public static List<String> getTestCasesPackages() {

        return testCasesPackages.isEmpty() ? TestCasesPackages.getAll() : testCasesPackages;               
    }

}

