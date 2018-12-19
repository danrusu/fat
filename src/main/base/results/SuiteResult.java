package main.base.results;


import static java.util.stream.Collectors.joining;
import static main.utils.StringUtils.joinObjectsToString;

import java.util.List;
import java.util.stream.Collectors;

import main.base.Assert;

public class SuiteResult {
	

    private int totalTestsCount;	
	private int failedTestsCount;
	private int skippedTestsCount;
	
	private int totalTestCasesCount;
	private int failedTestCasesCount;	
    private int skippedTestCasesCount;    
    
    private String elapsedTime;
	
	
	public SuiteResult(
	        int totalTestsCount, 
	        int failedTestsCount, 
	        int skippedTestsCount, 
	        
	        int totalTestCasesCount,
	        int failedTestCasesCount, 
	        int skippedTestCasesCount, 
	        
	        String elapsedTime) {
	    
	    this.totalTestsCount = totalTestsCount;
	    this.failedTestsCount = failedTestsCount;
	    this.skippedTestsCount = skippedTestsCount;
	    this.totalTestCasesCount = totalTestCasesCount;
	    this.failedTestCasesCount = failedTestCasesCount;
	    this.skippedTestCasesCount = skippedTestCasesCount;
	    this.elapsedTime = elapsedTime;
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
}

