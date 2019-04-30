package main.java.base.result;

import static java.util.stream.Collectors.joining;

import java.time.Instant;
import java.util.List;
import java.util.Map;


public class TestResult extends Result{
	
	private Map<Integer, TestCaseResult> testCasesResults;
	

	public TestResult(
	        String id,
            ResultStatus resultStatus,
            Instant startTime,
            Map<String, String> attributes,
            
            Map<Integer, TestCaseResult> testCasesResults) {
	    
		super(id, resultStatus, startTime, attributes);
		
		this.testCasesResults = testCasesResults;
	}



	public Map<Integer, TestCaseResult> getTestCasesResults() {
	    
		return testCasesResults;
	}

	
	public static String getInfo(List<TestResult> testResults) {       
        
	    String testInfoHeader = TestResult.getTestInfoHeader();
        String testInfo =  testResults.stream()
                
            .map(testResult -> testResult.getInfo() + "\n"                    
                    + testResult.getTestCasesInfo(testResult.getId()))
            
            .collect(joining("\n"));
        
        return String.join("\n", 
                testInfoHeader,
                testInfo);
    }
	
	
	private String getTestCasesInfo(String testId) {
	    
	    return getTestCasesResults().values().stream()
	        .map(testCaseResult -> testId + "/" + testCaseResult.getInfo() + "\n")
            .collect(joining());                                
	}


    public int getTestCasesCount() {
        return getTestCasesResults().size();
    }


    public long getTestCasesFailuresCount() {
        
        return getTestCasesResults().values().stream()
                .filter(TestCaseResult::isFailed)
                .count();
    }
		
}
