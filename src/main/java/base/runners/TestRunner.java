package main.java.base.runners;

import static main.java.base.Logger.log;
import static main.java.base.Logger.logSeparator;
import static java.util.stream.Collectors.*;
import static main.java.base.failures.ThrowablesWrapper.executeUnchecked;
import static main.java.base.failures.ThrowablesWrapper.supplyUnchecked;
import static main.java.base.pom.WebPage.getSeleniumExceptionShortMessage;
import static main.java.base.result.ResultStatus.FAILED;
import static main.java.base.result.ResultStatus.PASSED;
import static main.java.base.result.ResultStatus.SKIPPED;
import static main.java.base.result.ResultStatus.STARTED;
import static main.java.base.runnerConfig.TestUtils.needToCloseBrowserAtEnd;
import static main.java.base.selenium.Driver.quitDriver;
import static main.java.base.selenium.Driver.startNewDriver;
import static main.java.utils.FileUtils.getRelativePath;

import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import main.java.base.failures.Failure;
import main.java.base.result.ResultStatus;
import main.java.base.result.TestCaseResult;
import main.java.base.result.TestResult;
import main.java.base.runnerConfig.TestAttribute;
import main.java.base.runnerConfig.TestConfig;
import main.java.base.selenium.Browser;
import main.java.base.selenium.Driver;
import main.java.base.xmlSuite.XmlTestConfig;

public class TestRunner {
		
	//TODO - parallel ???
    public static List<TestResult> runAll(Map<Integer, TestConfig> tests) {

        return tests.keySet().stream()

                .map(testId -> run(testId, tests.get(testId)))
                    
                .collect(toCollection(ArrayList::new));       
    }


    public static TestResult run(Integer testId, TestConfig testConfig) {

        Instant testStartTime = Instant.now();

        var testAttributes = testConfig.getTestAttributes();
        
        Path dataProviderFile = supplyUnchecked(
        		() -> getRelativePath(testAttributes.get(
                        TestAttribute.dataProvider.name())),                
                null);

        var testCases = testConfig.getTestCases();

        logSeparator();
        log(String.format(
                "Started test_%d - attributes: %s", 
                testId, 
                testAttributes.toString()));

        
        var testResultStatus = testConfig.isTestSkipped() ?

                SKIPPED :

                STARTED;


        Map<Integer, TestCaseResult> testCasesResults =  null;

        if (testResultStatus == STARTED) {

            try {
                if (testConfig.isBrowserNeeded()){
                    
                    startDriver(testConfig.getBrowser(), XmlTestConfig.getGrid());
                }
            }
            
            catch(Failure driverFailure) {
                
                testResultStatus = FAILED;
                
                testAttributes.put(
                        TestAttribute.failure.name(), 
                        driverFailure + " Cause: "+ getSeleniumExceptionShortMessage(
                                driverFailure.getCause()));
               
                testCasesResults = TestCaseRunner.skipAll(testId, testCases);
            }


            // run all test cases within the test
            if (testResultStatus == STARTED) {

                testCasesResults = TestCaseRunner.runAll(
                        testId, 
                        testCases, 
                        dataProviderFile);

                testResultStatus = getTestResultStatus(testCasesResults);
            }
        }     

        // if test failed to start then skip all test-cases within
        else {
            testCasesResults = TestCaseRunner.skipAll(testId, testCases);
        }


        // by default, close driver windows after each test
        if (needToCloseBrowserAtEnd(testConfig)) {
            quitDriver();
        }

        return new TestResult(
                Integer.toString(testId), 
                testResultStatus, 
                testStartTime, 
                testAttributes, 
                testCasesResults);        
    }


    public static ResultStatus getTestResultStatus(Map<Integer, TestCaseResult> testCasesResults) {

        boolean isAnyTestCaseFailed = testCasesResults.values().stream()
                .map(TestCaseResult::getResultStatus)
                .anyMatch(result -> result.equals(FAILED));

        return isAnyTestCaseFailed ? FAILED : PASSED;
    }


    public static void startDriver(String browserName, boolean useGrid) {
    	
        String browser = browserName.toUpperCase();

        if (Driver.getDriver() == null && (browser.isEmpty() == false)){

            startNewDriverUnchecked(browser);
        }
        
        
        else if (browser.isEmpty() == false){ log("Reuse previous driver."); }
        
        
        else { log("No Selenium browser driver needed."); }
    }


	private static void startNewDriverUnchecked(String browser) {
		
		executeUnchecked(
		        
		        () -> startNewDriver(Browser.valueOf(browser)),
		            
		        "Failed to launch the browser! Test will stop.");
	}

}
