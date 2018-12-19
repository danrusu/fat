package main.base.runners;

import static main.base.Logger.log;
import static main.base.Logger.logSeparator;
import static main.base.failures.ThrowablesWrapper.supplyAndMapThrowableToFailure;
import static main.base.failures.ThrowablesWrapper.supplyUnchecked;
import static main.base.pom.WebPage.getSeleniumExceptionShortMessage;
import static main.base.results.ResultStatus.FAILED;
import static main.base.results.ResultStatus.PASSED;
import static main.base.results.ResultStatus.SKIPPED;
import static main.base.results.ResultStatus.STARTED;
import static main.base.runnerConfig.TestUtils.needToCloseBrowserAtEnd;
import static main.base.selenium.Driver.quitDriver;
import static main.base.selenium.Driver.startNewDriver;
import static main.utils.FileUtils.getRelativePath;

import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import main.base.failures.Failure;
import main.base.results.ResultStatus;
import main.base.results.TestCaseResult;
import main.base.results.TestResult;
import main.base.runnerConfig.TestAttribute;
import main.base.runnerConfig.TestConfig;
import main.base.selenium.Browser;
import main.base.selenium.Driver;
import main.base.xml.XmlTestConfig;

public interface TestRunner {



    public static List<TestResult> runAll(Map<Integer, TestConfig> tests) {

        return tests.keySet().stream()

                .map(testId -> {
                  
                    TestResult result = run(testId, tests.get(testId));
                    
                    return result;
                    
                })

                .collect(Collectors.toCollection(ArrayList::new));       
    }



    public static TestResult run(
            Integer testId, 
            TestConfig testConfig) {


        Instant testStartTime = Instant.now();

        var testAttributes = testConfig.getTestAttributes();
        
        Path dataProviderFile = supplyUnchecked(()->
        
                getRelativePath(testAttributes.get(
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
        // if test started
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

        // if test failed to start then skipp all test-cases within
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


    public static void startDriver(
            String browserName, 
            boolean useGrid) {

        // Reuse previous driver
        // DO NOT USE closeBrowserAtEnd="false" if the next test is in another browser !!!
        String browser = browserName.toUpperCase();


        if (Driver.getDriver() == null && (browser.isEmpty() == false)){

            supplyAndMapThrowableToFailure(
                    
                    () -> {
                        
                        startNewDriver(Browser.valueOf(browser.toUpperCase()));
                        
                        return null;
                    }, 
                    
                    "Failed to launch the browser! Test will stop.");
        }
        
        
        else if (browser.isEmpty() == false){
            log("Reuse previous driver.");
        }
        
        
        else {
            log("No Seleniun browser driver needed.");
        }
    }





}

