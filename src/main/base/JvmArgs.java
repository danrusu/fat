package main.base;


/**
 * List all accepted JVM command line arguments.
 * 
 * @author dan.rusu
 *
 */
public enum JvmArgs {
    
    user,
    browser,
    
    suiteName,
    
    jenkinsJobName,
    jenkinsBuildNr,
    
    resultFileType,
    sendResultsToServer,
    
    threads,
    DEBUG, 
    
    // Custom args: Lighthouse/Performance test cases 
    protocol, 
    url, 
    testName, 
    users
}

