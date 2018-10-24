package base.runners;

import static base.Logger.log;
import static base.Logger.logHeader;
import static base.Logger.logLines;
import static base.failures.ThrowablesWrapper.unchecked;
import static base.failures.ThrowablesWrapper.assignUnchecked;
import static base.results.ResultStatus.Failed;
import static base.results.ResultStatus.Passed;
import static base.results.ResultStatus.Skipped;
import static base.results.ResultStatus.Started;
import static base.runnerConfig.TestUtils.needToCloseBrowserAtEnd;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import base.Driver;
import base.failures.Failure;
import base.pom.WebPage;
import base.results.ResultStatus;
import base.results.TestCaseResult;
import base.results.TestResult;
import base.runnerConfig.TestAttribute;
import base.runnerConfig.TestConfig;
import base.xml.XmlTestConfig;
import utils.StringUtils;

public interface TestRunner {



    public static List<TestResult> runAll(Map<Integer, TestConfig> tests) {

        return tests.keySet().stream()

                .map(testId -> run(testId, tests.get(testId)))

                .collect(Collectors.toCollection(ArrayList::new));       
    }



    public static TestResult run(
            Integer testId, 
            TestConfig testConfig) {


        Instant testStartTime = Instant.now();

        var testAttributes = testConfig.getTestAttributes();
        
        Path dataProviderFile = assignUnchecked(()->
                Paths.get(
                        System.getProperty("user.dir"), 
                        testAttributes.get(TestAttribute.dataProvider.name())),                
                null);

        var testCases = testConfig.getTestCases();

        logHeader(String.format(
                "Started test_%d - attributes: %s", 
                testId, 
                testAttributes.toString()));

        
        var testResultStatus = testConfig.isTestSkipped() ?

                Skipped :

                Started;


        Map<Integer, TestCaseResult> testCasesResults =  null;
        // if test started
        if (testResultStatus == Started) {

            try {
                if (testConfig.isBrowserNeeded()){
                    
                    startDriver(testConfig.getBrowser(), XmlTestConfig.getGrid());
                }
            }
            catch(Failure driverFailure) {
                
                testResultStatus = Failed;
                
                testAttributes.put(
                        TestAttribute.failure.name(), 
                        driverFailure + " Cause: "+ WebPage.getSeleniumExceptionShortMessage(driverFailure.getCause()));
               
                testCasesResults = TestCaseRunner.skipAll(testId, testCases);
            }


            // run all test cases within the test
            if (testResultStatus == Started) {

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
        quitDriver(needToCloseBrowserAtEnd(testConfig));


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
                .anyMatch(result -> result.equals(Failed));

        return isAnyTestCaseFailed ? 

                Failed :

               Passed;

    }



    public static void startDriver(
            String browserName, 
            boolean useGrid) {

        // Reuse previous driver
        // DO NOT USE closeBrowserAtEnd="false" if the next test is in another browser !!!
        String browser = StringUtils.nullToEmptyString(browserName);



        if (Driver.driver == null && (browser.isEmpty() == false)){

            unchecked(
                    
                    "Failed to launch the browser! Test will stop.", 
                    
                    () -> {
                        Driver.driverStart(                    
                            Driver.getDefaultWait(), 
                            browser);
                        
                        return null;
                    });
        }
        
        
        else if (browser.isEmpty() == false){
          log("Reuse previous driver.");
        }
        
        
        else {
          log("No Seleniun browser driver needed.");
        }
    }



    public static void quitDriver(boolean isQuitNeeded) {

        if (isQuitNeeded && Driver.driver != null) {

            try{
              log("Test finished; quit current driver."); 

                Driver.closeAllWindows();
                Driver.driver.quit();
                Driver.driver = null;
            }

            catch(Throwable th){
              logLines("Error while quitting driver.\n", 
                        Failure.stackToString(th));

                Driver.driver = null;
            }
        }
    }

}

