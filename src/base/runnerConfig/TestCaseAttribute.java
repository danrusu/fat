package base.runnerConfig;

import static utils.StringUtils.toBoolean;
import static utils.StringUtils.toInt;

import java.util.Map;

public enum TestCaseAttribute {
    
    name,
    
    browser,
    
    retries,
    skip,
    
    failure,    
    stopOnFailure,
    expectedFailureRegExp,
    
    note;

    // stopOnFailure defaults to true
    public static boolean getTestCasestopOnFailure(
            Map<Integer, Map<String, String>> testCases,
            int testCaseId) {
    
        return toBoolean(testCases.get(testCaseId).get(stopOnFailure.name()), true);
    }


    // testCaseRetries defaults to 1
    public static int getTestCaseRetries(
            Map<Integer, Map<String, String>> testCases, 
            int testCaseId) {
        
        return toInt(testCases.get(testCaseId).get(retries.name()), 1, false);
    }    
}
