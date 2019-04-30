package main.java.base.runners;

import static java.util.stream.Collectors.toMap;
import static main.java.base.Logger.logSeparator;
import static main.java.base.result.ResultStatus.FAILED;
import static main.java.base.result.ResultStatus.PASSED;
import static main.java.base.result.ResultStatus.SKIPPED;
import static main.java.base.Logger.log;
import static main.java.base.Logger.logAll;
import static main.java.base.runnerConfig.TestCaseAttribute.expectedFailureRegExp;
import static main.java.base.runnerConfig.TestCaseAttribute.getTestCaseRetries;
import static main.java.base.runnerConfig.TestCaseAttribute.getTestCasestopOnFailure;
import static main.java.base.selenium.Driver.saveScreenShotWrapped;
import static main.java.utils.StringUtils.nullToEmptyString;

import java.nio.file.Path;
import java.time.Instant;
import java.util.Map;
import java.util.TreeMap;

import main.java.base.result.ResultStatus;
import main.java.base.result.TestCaseResult;
import main.java.base.selenium.Driver;
import main.java.base.testCase.TestCase;
import main.java.base.testCase.WebPageTestCase;
import main.java.base.xmlSuite.XmlTestConfig;
import main.java.utils.ClassUtils;

public interface TestCaseRunner {


    public static ResultStatus run(TestCase testCase){

        if (testCase.isSkipped()) {
            
            return SKIPPED;
        }

        ResultStatus testCaseResultStatus = PASSED;
        
        // if there was no browser specified in the test tag, evaluate testcase browser attribute
        if (testCase instanceof WebPageTestCase) {
            TestRunner.startDriver(
                    nullToEmptyString(testCase.getBrowser()), 
                    XmlTestConfig.getGrid());
            
            ((WebPageTestCase)testCase).setDriver(Driver.getDriver());
        }

        try {
            testCase.run();
        }

        // handle all problems: Exception, Error, RuntimeException
        catch(Throwable failure){

            String failureMessage = testCase.addFailure(failure);

            testCaseResultStatus = testCase.hasExpectedFailure(failureMessage) ? PASSED : FAILED;
        }


        finally {
            addJsErrors(testCase);
        }

        return testCaseResultStatus;
    }


    /**
     * Run all test cases within a test.java.
     * @param testId - parent test id
     * @param testCases - test cases map
     * @return - true if all test cases passed or false otherwise
     */
    public static Map<Integer, TestCaseResult> runAll(
            int testId, 
            Map<Integer, Map<String, String>> testCases,
            Path dataProviderFile){


        Map<Integer, TestCaseResult> testCasesResults = new TreeMap<>();


        for(int testCaseId : testCases.keySet()){

         // get current test case retries
            int retriesCount = getTestCaseRetries(testCases, testCaseId);
            boolean stopOnFailure = getTestCasestopOnFailure(testCases, testCaseId);


            Map<String, String> testCaseAttributes = testCases.get(testCaseId);


            TestCaseResult testCaseResult = retry(
                    testId, 
                    testCaseId,
                    testCaseAttributes, 
                    dataProviderFile, 
                    retriesCount);

            testCasesResults.put(testCaseId, testCaseResult);

            if(testCaseResult.isFailed() && stopOnFailure) {

                log(String.format(
                        "Test failed! Skip next testcases (%d..%d)", 
                        testCaseId + 1,
                        testCases.size()));
                
                var skippedTestCases = skipAll(testId, testCases, testCaseId + 1); 
                
                testCasesResults.putAll(skippedTestCases);
                
                break;
            }


        } // end test cases loop

        return testCasesResults;
    }



    public static Map<Integer, TestCaseResult> skipAll(
            int testId, 
            Map<Integer, Map<String, String>> testCases,
            int testCasesStartIndex) {

        
        return testCases.keySet().stream()
                
                .filter(testCaseId -> testCaseId >= testCasesStartIndex)
                
                .collect(toMap(
                        
                        testCaseId -> testCaseId,
                        
                        testCaseId -> getSkippedTestCaseResult(testCases, testCaseId)));
    }
    
    
    
    public static Map<Integer, TestCaseResult> skipAll(
            int testId, 
            Map<Integer, Map<String, String>> testCase) {
        
        return skipAll(testId, testCase, 1);
    }



    public static TestCaseResult getSkippedTestCaseResult(
            Map<Integer, Map<String, String>> testCases,
            Integer testCaseId) {
        
        return new TestCaseResult(
        
                "" + testCaseId,
        
                SKIPPED,
                
                Instant.now(),
                
                testCases.get(testCaseId));
    }



    public static TestCaseResult retry(
            int testId,
            int testCaseId,
            Map<String, String>  testCaseAttributes, 
            Path dataProviderFile, 
            int retriesCount) {


        Instant testCaseStartTime = Instant.now();
        ResultStatus testCaseResultStatus = PASSED;
        
        for (int retryIndex = 1; retryIndex <= retriesCount; retryIndex++){

            boolean retry = (retriesCount - retryIndex ) > 0;

            logSeparator();
            logAll(String.format(
                    "Executing testCase_%d/%d (atempt %d/%d) : %s",                                      
                    testId,
                    testCaseId, 
                    retryIndex ,
                    retriesCount,
                    testCaseAttributes.toString()));  

            String testCaseName = testCaseAttributes.get("name");


            try {                
                // get a TestCase instance, set attributes and run it 
                TestCase testCase = (TestCase)ClassUtils.newInstance(testCaseName);
                
                testCase.setTestCaseAttributes(testCaseAttributes, dataProviderFile);
                
                testCaseResultStatus = run(testCase);
                
                // take screenshot if (failure and browser opened)             
                if (! testCaseResultStatus.isPassed() && Driver.getDriver() != null) {
                    
                   saveScreenShotWrapped(String.format(
                            "%d_%d_%s_try%d_failureScreenshot.jpg",
                            testId,
                            testCaseId,
                            testCaseName,
                            retryIndex )); 
                }
                                
                if ( ! retry || testCaseResultStatus.isPassed() ) {
                    break;
                }
            }

            // handle all Throwables
            catch(Throwable failure){
                
                    String failureText = failure.toString();

                    testCaseAttributes.put("failure", failureText);
                    
                    String failureTextMatcher = String.join("",
                            "(?s)",
                            testCaseAttributes.get(expectedFailureRegExp.name()));

                    testCaseResultStatus = failureText.matches(failureTextMatcher) ? PASSED : FAILED;                    
            }
        }

        return new TestCaseResult(
                "" + testCaseId, 
                testCaseResultStatus, 
                testCaseStartTime, 
                testCaseAttributes);
    }



    public static TestCaseResult skip(
            int testId,
            int testCaseId, 
            Map<String, String>  testCaseAttributes) {


        log(String.format("Skipping testCase_%d/%d : %s",                
                testId,
                testCaseId, 
                testCaseAttributes.toString()));  
        
        return new TestCaseResult(
                "" + testCaseId, 
                SKIPPED, 
                Instant.now(), 
                testCaseAttributes);
    }



    public static void addJsErrors(TestCase testCase) {

        if (testCase instanceof WebPageTestCase){
            ((WebPageTestCase)testCase).addJsErrors();
        }
    }



    public static void logAllTestCases(
            String testId, Map<Integer, 
            Map<String, String>> testCases) {

        log("TEST-CASES: ");

        testCases.keySet().stream().forEach(
                
                key -> logAll(
                        "test_", testId, "/", 
                        "testCase_", Integer.toString(key), 
                        " : ", testCases.get(key).toString()));
    }

}
