package main.base.results;

import java.time.Instant;
import java.util.Map;

public class TestCaseResult extends Result{

	public TestCaseResult(
	        String id,
            ResultStatus resultStatus,
            Instant startTime,
            Map<String, String> attributes) {
        
        super(id, resultStatus, startTime, attributes);
	}

}
