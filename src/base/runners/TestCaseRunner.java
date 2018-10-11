package base.runners;
import static base.Logger.log;
import static base.Logger.logAll;
import static base.Logger.logHeader;

import java.nio.file.Path;
import java.time.Instant;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import base.Driver;
import base.results.ResultStatus;
import base.results.TestCaseResult;
import base.runnerConfig.TestCaseAttribute;
import base.testCase.TestCase;
import base.testCase.WebPageTestCase;
import base.xml.XmlTestConfig;
import utils.ClassUtils;

public interface TestCaseRunner {



    public static ResultStatus run(TestCase testCase){

        if (testCase.isSkipped()) {
            return ResultStatus.Skipped;
        }

        ResultStatus testCaseResultStatus = ResultStatus.Passed;
        
        //startDriver(testConfig, XmlTestConfig.getGrid());
        

        try {
            testCase.run();
        }

        // handle all problems: Exception, Error, RuntimeException
        catch(Throwable failure){

            String failureMessage = testCase.addFailure(failure);

            testCaseResultStatus = testCase.hasExpectedFailure(failureMessage) ?

                    ResultStatus.Passed : ResultStatus.Failed;
        }


        finally {
            addJsErrors(testCase);
        }

        return testCaseResultStatus;
    }



    /**
     * Run all test cases within a test.
     * @param testCases - test cases map
     * @param testId - parent test id
     * 
     * @return - true if all test cases passed or false otherwise
     */
    public static Map<Integer, TestCaseResult> runAll(
            int testId, 
            Path dataProviderFile,
            Map<Integer, Map<String, String>> testCases){


        Map<Integer, TestCaseResult> testCasesResults = new TreeMap<>();


        for(int testCaseId : testCases.keySet()){

         // get current test case retries
            int retriesCount = TestCaseAttribute.getTestCaseRetries(testCases, testCaseId);
            boolean stopOnFailure = TestCaseAttribute.getTestCasestopOnFailure(testCases, testCaseId);


            Map<String, String> testCaseAttributes = testCases.get(testCaseId);


            TestCaseResult testCaseResult = retry(
                    testId, 
                    dataProviderFile,
                    testCaseId, 
                    testCaseAttributes, 
                    retriesCount);

            testCasesResults.put(testCaseId, testCaseResult);

            if(testCaseResult.isFailed() && stopOnFailure) {

                log("Test failed! Skip next testcases (" 
                        + (testCaseId + 1) + ".." + testCases.size() + ").");
                
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
                
                .collect(Collectors.toMap(
                        
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
        
                ResultStatus.Skipped,
                
                Instant.now(),
                
                testCases.get(testCaseId));
    }



    public static TestCaseResult retry(
            int testId,
            Path dataProviderFile,
            int testCaseId, 
            Map<String, String>  testCaseAttributes, 
            int retriesCount) {


        Instant testCaseStartTime = Instant.now();
        ResultStatus testCaseResultStatus = ResultStatus.Passed;
        
        for (int i=1; i<=retriesCount; i++){

            boolean retry = (retriesCount - i) > 0;

            logHeader("Executing"
                    + " testCase_" + testId + "/" + testCaseId 
                    + " (attempt " + i + "/" + retriesCount + ")"
                    + " : " + testCaseAttributes);  

            String testCaseName = testCaseAttributes.get("name");


            try {
                // get a TestCase instance, set attributes and run it 
                TestCase testCase = (TestCase)ClassUtils.newInstance(
                        testCaseName);
                
                testCase.setTestCaseAttributes(testCaseAttributes, dataProviderFile);
                
                testCaseResultStatus = run(testCase);
                
                // take screenshot if (failure and browser opened)             
                if (! testCaseResultStatus.isPassed() && Driver.driver != null) {
                    
                    Driver.saveScreenShotWrapped(String.join("_",
                            "" + testId,
                            "" + testCaseId,
                            testCaseName,
                            "try" + i, 
                            "failureScreenshot.jpg")); 
                }
                
                
                if ( ! retry || testCaseResultStatus.isPassed() ) {
                    break;
                }
            }

            // handle all problems: Exception, Error, RuntimeException
            catch(Throwable failure){

                    testCaseAttributes.put("failure", failure.toString());

                    testCaseResultStatus = failure.toString()
                            
                            .matches("(?s)" + testCaseAttributes.get(TestCaseAttribute.expectedFailureRegExp.name())) ?

                            ResultStatus.Passed : ResultStatus.Failed;
                    
            }

        }

        return new TestCaseResult(
                ""+testCaseId, 
                testCaseResultStatus, 
                testCaseStartTime, 
                testCaseAttributes);
    }



    public static TestCaseResult skip(
            int testId,
            int testCaseId, 
            Map<String, String>  testCaseAttributes) {


        logHeader(String.join("",
                "Skipping",
                " testCase_", testId + "", "/", testCaseId + "", 
                " : ", testCaseAttributes.toString()));  
        
        return new TestCaseResult(
                ""+testCaseId, 
                ResultStatus.Skipped, 
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

        log("TestCases: ");

        testCases.keySet().stream()
        .forEach(
                key -> logAll(
                        "test_", testId, "/", 
                        "testCase_", Integer.toString(key), 
                        " : ", testCases.get(key).toString()));
    }

}

