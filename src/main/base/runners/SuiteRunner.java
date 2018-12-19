package main.base.runners;

import static main.base.Logger.getLogDirPath;
import static main.base.Logger.log;
import static main.base.Logger.logLines;
import static main.base.Logger.logSplitByLines;
import static main.utils.SystemUtils.getBooleanPropertyOrDefault;
import static main.utils.SystemUtils.getPropertyOrDefaultIfNullOrEmpty;
import static main.utils.SystemUtils.getPropertyOrEmptyString;
import static main.utils.ThreadUtils.parralelismProperty;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import main.base.JvmArgs;
import main.base.results.ResultFileType;
import main.base.results.ResultStatus;
import main.base.results.Results;
import main.base.results.SuiteResult;
import main.base.results.TestCaseResult;
import main.base.results.TestResult;
import main.base.runnerConfig.TestConfig;
import main.base.xml.XmlTestConfig;
import main.utils.TimeUtils;


/**
 * Class for running tests from an XML test configuration file.
 * 
 * @author Dan.Rusu
 */
public interface SuiteRunner {	


    /**
     * Main method for running tests.
     * It runs all tests from the tests' map. 
     */
    public static void run(String xmlTestFile) {
        
       log(parralelismProperty + " : " 
                + System.getProperty(parralelismProperty));
        
        // Test configuration from test XML file
        Map<Integer, TestConfig> tests = new XmlTestConfig().getTestConfig(xmlTestFile);
        

        // use HTML result file type
        // resultFileType = XmlTestConfig.getSuiteResultFileType();
        Path resultFilePath = getLogDirPath()
                .resolve("result" + "." + ResultFileType.html);
        
        String jenkinsJobName = getPropertyOrEmptyString(JvmArgs.jenkinsJobName);
        String jenkinsBuildNr = getPropertyOrEmptyString(JvmArgs.jenkinsBuildNr); 
               
        log(String.format(
                "Starting TestRunner(\"%s\", \"%s\")", 
                xmlTestFile, 
                jenkinsBuildNr));

        Instant startSuiteTime = Instant.now();
        log("Starting Suite: " + XmlTestConfig.getSuiteName());
        

        // JVM arg -Duser=userName overrides user from XML
        String user = getPropertyOrDefaultIfNullOrEmpty(JvmArgs.user, XmlTestConfig.getUser());

        String project = XmlTestConfig.getProject();

        // JVM arg -DsendResultsToServer
        // defaults to false; set it to false only from JVM arg -DsendResultsToServer=false
        boolean sendResultsToServer = getBooleanPropertyOrDefault(JvmArgs.sendResultsToServer, false);
        
        
        // ***** RUN Tests *****
        List<TestResult> testsResultsList = TestRunner.runAll(tests);

        
        
        SuiteResult suiteResult = new SuiteResult(
                // Tests info
                tests.size(),
                getFailedTestsCount(testsResultsList),
                getSkippedTestsCount(testsResultsList),
                
                // TestCases info
                getTotalTestCasesCount(tests),
                getFailedTestCasesCount(testsResultsList),
                getSkippedTestCasesCount(testsResultsList),
                                
                TimeUtils.getElapsedTime(startSuiteTime));


        Results.logAllResults(testsResultsList, suiteResult);

        // save report: results.html
        saveLocalResult(resultFilePath, suiteResult, testsResultsList);
        

        if (sendResultsToServer) {
            updateResultsOnServer(
                    project, 
                    user, 
                    suiteResult,
                    resultFilePath,
                    jenkinsJobName,
                    jenkinsBuildNr);
        }


        logLines(
                "Test Suite Finished!",
                "Log: " + getLogDirPath().toString() + "\\log.txt",
                "Result: " + resultFilePath.toString());
    }
    
    
    
    public static int getTestsCountByStatus(
            List<TestResult> testsResultsList,
            ResultStatus testResultStatus) {
        
        return testsResultsList.stream()
                .map(TestResult::getResultStatus)
                .filter(resultStatus -> resultStatus.equals(testResultStatus))
                .collect(Collectors.counting())
                .intValue();
    }
    
    
    
    public static int getFailedTestsCount(List<TestResult> testsResultsList) {
        return getTestsCountByStatus(testsResultsList, ResultStatus.FAILED);
    }

    
    
    public static int getSkippedTestsCount(List<TestResult> testsResultsList) {
        return getTestsCountByStatus(testsResultsList, ResultStatus.SKIPPED);
    }
    

    
    public static int getTotalTestCasesCount(Map<Integer, TestConfig> tests) {
        
        return tests.values().stream()
                .mapToInt(testConfig -> testConfig.getTestCases().size())
                .sum();              
    }
    
    
    
    public static int getTestCasesCountByStatus(
            List<TestResult> testsResultsList,
            ResultStatus testCaseResultStatus) {
        
        return testsResultsList.stream()
                
                .map(TestResult::getTestCasesResults)
                .map(Map::values)
                
                .map(Collection::stream)
                .flatMap(x -> x)
                
                .map(TestCaseResult::getResultStatus)
                
                .filter(status -> status.equals(testCaseResultStatus))
                
                .collect(Collectors.counting())
                .intValue();
    }
    
    
    
    public static int getFailedTestCasesCount(List<TestResult> testsResultsList) {        
        
        return getTestCasesCountByStatus(testsResultsList, ResultStatus.FAILED);
    }

    
    
    public static int getSkippedTestCasesCount(List<TestResult> testsResultsList) {        
        
        return getTestCasesCountByStatus(testsResultsList, ResultStatus.SKIPPED);
    }



    public static void updateResultsOnServer(
            String project,
            String user, 
            SuiteResult suiteResult,
            Path resultFilePath,
            String jenkinsJobName,
            String jenkinsBuildNr) {


        // send result TEXT / HTML file to web server
/*        String uniqResultFileName = Results.getUniqueResultFileName(
                WinUtils.getHostName(),
                resultFilePath);*/
    }



    /**
     * Save test result details in logFolder/results.(fileType)
     * @param resultFileName - final result file name (text or HTML)
     * @param suiteResult - test suite result
     */
    public static void saveLocalResult( Path resultFilePath,
            SuiteResult suiteResult, 
            List<TestResult>testsResultsList) {

        log("Save result to: " + resultFilePath.toString());

        String content = Results.getDetailedResultsHtml(
                        testsResultsList, 
                        suiteResult);

        try {
            Files.write(
                    resultFilePath, 
                    content.getBytes(), 
                    StandardOpenOption.CREATE_NEW);

        } catch (Exception e) {
            logSplitByLines("Could not write to result file " 
                    + resultFilePath.toString() + "\n" + e);
        }
    }

}

