package main.java.base.runners;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static main.java.base.Logger.getLogDirPath;
import static main.java.base.Logger.getLogFolderTimeStamp;
import static main.java.base.Logger.getPathInLogDir;
import static main.java.base.Logger.log;
import static main.java.base.Logger.logFormat;
import static main.java.base.Logger.logLines;
import static main.java.base.Logger.logSplitByLines;
import static main.java.utils.FileUtils.getRelativePath;
import static main.java.utils.SystemUtils.getPropertyOrDefaultIfNullOrEmpty;
import static main.java.utils.SystemUtils.getPropertyOrEmptyString;
import static main.java.utils.ThreadUtils.parralelismProperty;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import main.java.base.JvmArgs;
import main.java.base.result.ResultFileType;
import main.java.base.result.Results;
import main.java.base.result.SuiteResult;
import main.java.base.result.TestCaseResult;
import main.java.base.result.TestResult;
import main.java.base.result.junitXml.FailureTag;
import main.java.base.result.junitXml.FailureTag.FailureTagBuilder;
import main.java.base.result.junitXml.TestCaseTag;
import main.java.base.result.junitXml.TestSuiteTag;
import main.java.base.result.junitXml.TestSuiteTag.TestSuiteTagBuilder;
import main.java.base.result.junitXml.TestSuitesTag;
import main.java.base.result.junitXml.TestSuitesTag.TestSuitesTagBuilder;
import main.java.base.runnerConfig.TestConfig;
import main.java.base.xmlSuite.XmlTestConfig;
import main.java.utils.FileUtils;
import main.java.utils.Ssh;
import main.java.utils.SystemUtils;


/**
 * Class for running tests from an XML test configuration file.
 * 
 * @author Dan.Rusu
 */
public class SuiteRunner {
    
    private Map<Integer, TestConfig> tests;
    
    @SuppressWarnings("unused")
    private String user;
    @SuppressWarnings("unused")
    private String project;
    private Instant startSuiteTime;
    
    @SuppressWarnings("unused")
    private String jenkinsJobName;
    private String jenkinsBuildNr;
    
    private Path htmlReportPath;
    private Path junitXmlReportPath;
    
    List<TestResult> testsResultsList;
    SuiteResult suiteResult;
    
    
    public SuiteRunner(String xmlTestFile) {
        
        log(parralelismProperty + " : " 
                + System.getProperty(parralelismProperty));
        
        // Get test configuration from test XML file
        tests = new XmlTestConfig().getTestConfig(xmlTestFile);       
        
        jenkinsJobName = getPropertyOrEmptyString(JvmArgs.JENKINS_JOB_NAME);
        jenkinsBuildNr = getPropertyOrEmptyString(JvmArgs.JENKINS_BUILD_NR);                
        logFormat("Starting TestRunner(\"%s\", \"%s\")", xmlTestFile, jenkinsBuildNr);

        startSuiteTime = Instant.now();
        log("Starting Suite: " + XmlTestConfig.getSuiteName());
        

        // JVM arg -Duser=userName overrides user from XML
        user = getPropertyOrDefaultIfNullOrEmpty(JvmArgs.USER, XmlTestConfig.getUser());

        project = XmlTestConfig.getProject();
        
        htmlReportPath = getPathInLogDir("result" + "." + ResultFileType.HTML.toString());
        junitXmlReportPath = getPathInLogDir("result" + "." + ResultFileType.XML.toString());
    }
    

    /**
     * Runs all tests from the tests' map. 
     */
    public void run() {
                        
        testsResultsList = TestRunner.runAll(tests);
            
        suiteResult = new SuiteResult(testsResultsList, startSuiteTime);

        logAllResults();
        
        // reports: (logDirPath/) result.html, result.xml     
        saveJunitXmlReport();
        saveHtmlReport();               

        logSummary();

        boolean sendResultsToWeb = SystemUtils.getBooleanPropertyOrDefault(JvmArgs.DANRUSU_REPORTING, false);
        if (sendResultsToWeb) {
        	log("Send report to web: http://danrusu.ro/fat/all.php?auth=token");
        	sendResultsToWeb();
        }
    }


    private void sendResultsToWeb() {
    	
    	String sourcePath = getLogDirPath().resolve("result.html").toString();
    	
		
    	String destPath = "upload/reports/" 
    	
    			+ String.join("_",
				
    					getLogFolderTimeStamp() + "_",
				
    					user,
    					
    					XmlTestConfig.getSuiteName(),
				
    					"report.html");
		
    	String privateKeyFile = getRelativePath("resources/ssh/danrusu/id_rsa").toString();
		
		new Ssh("danrusur", "danrusu.ro", 22).sendFileWithPrivateKey(
				sourcePath,				
				destPath,				
				privateKeyFile);
		
	}


	private void logSummary() {
        logLines("Test Suite Finished!",
                "Log: " + getLogDirPath().toString() + "\\log.txt",
                "Result: " + htmlReportPath.toString(),
                "Junit XML: " + junitXmlReportPath.toString());
    }
    
    
    private void logAllResults(){
        
        logSplitByLines(getDetailedResults());
    }
    
    
    private String getDetailedResults(){

        return String.join("\n", 
        
                SuiteResult.getInfo(suiteResult),               
        
                TestResult.getInfo(testsResultsList));
    }
    
    
    private void saveJunitXmlReport() {
        
        log("Save JunitXmlReport to: " + junitXmlReportPath.toString());
        
        TestSuitesTag testSuites = getTestSuitesTag();
        
        FileUtils.write(
                junitXmlReportPath, 
                testSuites.toString().getBytes());
        
    }

    
    public void saveHtmlReport() {

        log("Save HtmlReport to: " + htmlReportPath.toString());

        String content = Results.getDetailedResultsHtml(
                        testsResultsList, 
                        suiteResult);

        try {
            Files.write(
                    htmlReportPath, 
                    content.getBytes(), 
                    StandardOpenOption.CREATE_NEW);

        } catch (Exception e) {
            logSplitByLines("Could not write to result file " 
                    + htmlReportPath.toString() + "\n" + e);
        }
    }
    

    private TestSuitesTag getTestSuitesTag() {
        
        return new TestSuitesTagBuilder()
                .id(getLogFolderTimeStamp() + "_" + XmlTestConfig.getSuiteName())
                .name(XmlTestConfig.getSuiteName())
                .tests(testsResultsList.size() + "")
                .failures(suiteResult.getFailedTestsCount() + "")
                .time(suiteResult.getElapsedTime())
                
                .testSuites(getTestSuiteTags())                
                
                .build();        
    }
    
    
    private List<TestSuiteTag> getTestSuiteTags() {
        
        return testsResultsList.stream()
            .map(this::testResultToJunitXmlTestSuiteTag)
            .collect(toList());
    }


    private TestSuiteTag testResultToJunitXmlTestSuiteTag(TestResult testResult) {
        
        Collection<TestCaseResult> testCasesResults = testResult.getTestCasesResults().values();
        
        return new TestSuiteTagBuilder()
                .id(testResult.getId())
                .name(testResult.getName())
                .tests(testResult.getTestCasesCount() + "")
                .failures(testResult.getTestCasesFailuresCount() + "")
                .time(testResult.getElapsedTime())
                
                .testCaseTags(getTestCasesTags(testCasesResults))
                
                .build();
    }


    private List<TestCaseTag> getTestCasesTags(Collection<TestCaseResult> testCasesResults) {
        
        return testCasesResults.stream()
                .map(this::testCasesResultToJunitXmlTestCase)
                .collect(toList());
    }


    private TestCaseTag testCasesResultToJunitXmlTestCase(TestCaseResult testCasesResult) {
        return new TestCaseTag.TestCaseTagBuilder()
                
                .id(testCasesResult.getId())
                .name(testCasesResult.getName())
                .time(testCasesResult.getElapsedTime())
                
                .failures(getFailureTags(testCasesResult))
                
                .build();
    }


    public List<FailureTag> getFailureTags(TestCaseResult testCasesResult) {

        String failureAttribute = testCasesResult.getAttributes().get("failure");
        List<String> failureList = failureAttribute == null ? emptyList() : List.of(failureAttribute);  
        
        return List.of(
                new FailureTagBuilder()
                    .message("FAILURE")
                    .type("ERROR")
                    .text(failureList)
                    .build());
    }             

}
