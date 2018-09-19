package core.runnerConfig;

import java.util.Map;

import utils.StringUtils;

public interface TestCaseConfigUtils {

    
    
    // testCaseRetries defaults to 1
    public static int getTestCaseRetries(
            Map<Integer, Map<String, String>> testCases, 
            int testCaseId) {
        
        return StringUtils.toInt(
                testCases.get(testCaseId).get(TestConfigAttributes.retries.name()), 
                1, 
                false);
    }

    
    
    // stopOnFailure defaults to true (if false is not explicitly not set)
    public static boolean getTestCasestopOnFailure(
            Map<Integer, Map<String, String>> testCases,
            int testCaseId) {
    
        String stopOnFailure = StringUtils.nullToEmptyString(
                testCases.get(testCaseId).get(TestConfigAttributes.stopOnFailure.name())
        );
    
        return stopOnFailure.equalsIgnoreCase(Boolean.toString(false)) ?
                false : true;
    }

    
    
    // expectFailure defaults to false
    public static String getTestCaseExpectedFailure(
            Map<Integer, Map<String, String>> testCases,
            int testCaseId
            ) {
    
        return StringUtils.nullToEmptyString(
                testCases.get(testCaseId).get((TestConfigAttributes.expectedFailure.name()))
        );
    }

}
