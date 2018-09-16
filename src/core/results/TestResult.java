package core.results;

import java.time.Instant;
import java.util.Map;


public class TestResult extends Result{
	
	private Map<Integer, TestCaseResult> testCasesResults;

	

	public TestResult(String id,
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

}
