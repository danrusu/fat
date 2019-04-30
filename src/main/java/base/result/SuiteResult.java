package main.java.base.result;


import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.joining;
import static main.java.utils.StringUtils.joinObjectsToString;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import main.java.base.Assert;
import main.java.utils.TimeUtils;


public class SuiteResult {
	
    private List<TestResult> testsResultsList;
    
    private int totalTestsCount;	
	private int failedTestsCount;
	private int skippedTestsCount;
	
	private int totalTestCasesCount;
	private int failedTestCasesCount;	
    private int skippedTestCasesCount;    
    
    private String elapsedTime;
	
	
	public SuiteResult(List<TestResult> testsResultsList, Instant startSuiteTime) {
	    
	    this.testsResultsList = testsResultsList;
	    
	    // Tests info
	    totalTestsCount = testsResultsList.size();
	    setFailedTestsCount();
	    setSkippedTestsCount();
        
        // TestCases info
	    setTotalTestCasesCount();
	    setFailedTestCasesCount();
	    setSkippedTestCasesCount();                         
	    elapsedTime = TimeUtils.getElapsedTime(startSuiteTime);
	}
	
	
    private void setFailedTestsCount() {
        failedTestsCount = getTestsCountByStatus(ResultStatus.FAILED);
    }
	
    
    private void setSkippedTestsCount() {
        skippedTestsCount = getTestsCountByStatus(ResultStatus.SKIPPED);
    }
    
	
    private int getTestsCountByStatus(ResultStatus testResultStatus) {
        
        return testsResultsList.stream()
                .map(TestResult::getResultStatus)
                .filter(resultStatus -> resultStatus.equals(testResultStatus))
                .collect(counting())
                .intValue();
    }
    
    
    private void setTotalTestCasesCount() {
        
        totalTestCasesCount = testsResultsList.stream()
                .mapToInt(TestResult::getTestCasesCount)
                .sum();              
    }
	
    
    private void setFailedTestCasesCount() {        
        
        failedTestCasesCount = getTestCasesCountByStatus(ResultStatus.FAILED);
    }
  
    
    private void setSkippedTestCasesCount() {        
        
        skippedTestCasesCount = getTestCasesCountByStatus(ResultStatus.SKIPPED);
    }
    
    
    private int getTestCasesCountByStatus(ResultStatus testCaseResultStatus) {
        
        return testsResultsList.stream()
                
                .map(TestResult::getTestCasesResults)
                .map(Map::values)
                
                .map(Collection::stream)
                .flatMap(x -> x)
                
                .map(TestCaseResult::getResultStatus)
                
                .filter(status -> status.equals(testCaseResultStatus))
                
                .collect(counting())
                .intValue();
    }
    
	
	public int getSkippedTestsCount() {
        return skippedTestsCount;
    }


    public int getFailedTestCasesCount() {
        return failedTestCasesCount;
    }


    public int getSkippedTestCasesCount() {
        return skippedTestCasesCount;
    }

	
	public int getTotalTestsCount() {
		return totalTestsCount;
	}
		
	
	public int getTotalTestCasesCount() {
	    return totalTestCasesCount;
	}
	
	
	public int getFailedTestsCount() {
        return failedTestsCount;
    }
	
	
	public int getPassedTestsCount() {
        return totalTestsCount - failedTestsCount;
    }
	
	
	public String getElapsedTime() {
        return elapsedTime;
    }
	

	public boolean isSucceeded() {
		return failedTestsCount == 0;
	}


    public String getStatusText() {
        
       return isSucceeded() ? "Succeeded" : "Failed";
    }

    
    public static String getInfo(SuiteResult suiteResult){

        String colWidth = "35";
        String format = "%-"+ colWidth + "s";

        
        String header = getInfoHeader(format);
                
        
        String result = List.of(
                
                suiteResult.getStatusText(),
                
                suiteResult.getElapsedTime(),
                
                getTestStatistics(suiteResult),
                
                getTestCaseStatistics(suiteResult),
                
                Assert.AssertCount.assertCount)
                
                .stream()

                .map(text -> String.format(format, text))

                .collect(Collectors.joining(" | "));;
                

        return String.join("\n", header, result);               
    }


    private static String getTestCaseStatistics(SuiteResult suiteResult) {
        
        return joinObjectsToString("/", 
                suiteResult.getTotalTestCasesCount(),
                suiteResult.getFailedTestCasesCount(),
                suiteResult.getSkippedTestCasesCount());
    }   


    private static String getTestStatistics(SuiteResult suiteResult) {
        
        return joinObjectsToString("/", 
                suiteResult.getPassedTestsCount(),
                suiteResult.getFailedTestsCount(),
                suiteResult.getSkippedTestsCount());
    }

    
    private static String getInfoHeader(String format) {
        
        return List.of(
                "Result",
                "Elapsed", 
                "Test [passed/failed/skipped]", 
                "Testcase [passed/failed/skipped]",
                "Asserts")
                
                .stream()
                .map(text -> String.format(format, text))
                .collect(joining(" | "));
    }


    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }
}
