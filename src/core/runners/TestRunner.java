package core.runners;
import static core.Logger.log;
import static core.Logger.logHeader;
import static core.Logger.logLines;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import core.Driver;
import core.failures.TestCaseFailure;
import core.results.ResultStatus;
import core.results.TestCaseResult;
import core.results.TestResult;
import core.runnerConfig.TestConfig;
import core.runnerConfig.TestConfigUtils;
import core.xml.XmlTestConfig;
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
        
        var testCases = testConfig.getTestCases();
        
        var isTestSkipped = testConfig.isTestSkipped(); 


        logHeader(String.format(
                "Running test_%d - attributes: %s", 
                testId, 
                testAttributes.toString()));

        
        if (testConfig.isBrowserNeeded()){
            // this sets Driver.driver
            startDriver(testConfig, XmlTestConfig.getGrid());
        }
        
        
        // run all test cases within the test; retry if test failed and testRetries is set
        var testCasesResults = isTestSkipped ?
                
                TestCaseRunner.skipAll(testId, testCases) :
                
                TestCaseRunner.runAll(testId, testCases);
        
        
        var testResultStatus = isTestSkipped ? 
                
                ResultStatus.Skipped :
                    
                getTestResultStatus(testCasesResults);
                

        // by default, close driver windows after each test
        quitDriver(TestConfigUtils.needToCloseBrowserAtTestEnd(testConfig));
        
        
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
                .anyMatch(result -> result.equals(ResultStatus.Failed));
        
        return isAnyTestCaseFailed ? 
                
                ResultStatus.Failed:
                    
                ResultStatus.Passed;
                    
    }



    public static void startDriver(
            TestConfig testConfig, 
            boolean useGrid) {

        // Reuse previous driver
        // DO NOT USE closeBrowserAtEnd="false" if the next test is in another browser !!!
        String browser = StringUtils.nullToEmptyString(testConfig.getBrowser());

        if ( Driver.driver == null && ( ! browser.isEmpty()) ){

            // if driver start fails, try starting it for 3 times
            for (int i=0; i<3; i++){
                try {
                    log("Start driver, attempt " + i);
                    Driver.driverStart(
                            Driver.getDefaultWait(), 
                            browser,
                            useGrid
                            );
                    break;

                }catch(Exception e){
                    logLines("Failed to launch the browser!\n" + e);
                    testConfig.getTestAttributes().put("failure", "Cannot launch browser: " + e);
                }
            }
        }
        else if ( ! browser.isEmpty() ){
            log("Reuse previous driver.");
        }
        else {
            log("No Seleniun browser driver needed.");
        }
    }

    
    
    public static void quitDriver(boolean isQuitNeeded) {
        
        if ( isQuitNeeded && Driver.driver != null ) {
            
            try{
                log("Test finished; quit current driver."); 
                
                Driver.closeAllWindows();
                Driver.driver.quit();
                Driver.driver = null;
            }

            catch(Throwable th){
                logLines(
                        "Error while quitting driver.\n", 
                        TestCaseFailure.stackToString(th));
                
                Driver.driver = null;
            }
        }
    }
    
}

