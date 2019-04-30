package main.java.base;


/**
 * List all accepted JVM command line arguments.
 * 
 * @author dan.rusu
 *
 */
public enum JvmArgs {
    
    USER("user"),
    BROWSER("browser"),
    SUITE_NAME("suiteName"),
    
    JENKINS_JOB_NAME("jenkinsJobName"),
    JENKINS_BUILD_NR("jenkinsBuildNr"),
    
    RESULT_FILE_TYPE("resultFileType"),
    SEND_RESULTS_TO_SERVER("sendResultsToServer"),
    
    THREAD_COUNT_LIMIT("threads"),
    DEBUG("DEBUG"), 
    
    LIGHTHOUSE_PROTOCOL("lighthouseProtocol"), 
    LIGHTHOUSE_URL("lighthouseUrl"),
    
    PERFORMANCE_TEST_NAME("performanceTestName"), 
    PERFORMANCE_URL("performanceUrl"),
    PERFORMANCE_PROTOCOL("performanceProtocol"),
    PERFORMANCE_USERS("performanceUsers"),
	
	DANRUSU_REPORTING("danrusuReporting");

    private String value;
    
    
    private JvmArgs(String value) {
        this.value = value; 
    }

    
    @Override
    public String toString() {
        return value;
    }
      
}
