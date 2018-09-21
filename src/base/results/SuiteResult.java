package base.results;

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

}

