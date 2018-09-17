package core.runners;

import static core.Logger.*;
import static core.Logger.log;
import static core.Logger.logLines;
import static utils.SystemUtils.getBooleanPropertyOrDefault;
import static utils.SystemUtils.getPropertyOrDefaultIfNullOrEmpty;
import static utils.SystemUtils.getPropertyOrEmptyString;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import core.JvmArgs;
import core.results.ResultFileType;
import core.results.ResultStatus;
import core.results.Results;
import core.results.SuiteResult;
import core.results.TestCaseResult;
import core.results.TestResult;
import core.runnerConfig.TestConfig;
import core.xml.XmlTestConfig;
import utils.TimeUtils;
import utils.WinUtils;
import utils.server.MainServer;


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
        
        // Test configuration from test XML file
        Map<Integer, TestConfig> tests = new XmlTestConfig().readTestConfig(xmlTestFile);
        

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
        // defaults to false; can be set only from JVM arg -DsendResultsToServer=true
        boolean sendResultsToServer = getBooleanPropertyOrDefault(JvmArgs.sendResultsToServer, false);

        if (sendResultsToServer) {
            Results.insertResultsIntoDatabase(
                    XmlTestConfig.getSuiteName(),
                    project,
                    user,
                    Integer.toString(tests.size()),				
                    "Running",
                    WinUtils.getHostName(),
                    "",
                    jenkinsJobName,
                    jenkinsBuildNr
                    );
        }

        
        
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


        Results.logSuiteResult(testsResultsList, suiteResult);

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
                "Result: " + resultFilePath.toString(),
                MainServer.ip.isEmpty() ? 
                        "" :
                        "Online results can be viewed at: " + MainServer.ip);
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
        return getTestsCountByStatus(testsResultsList, ResultStatus.Failed);
    }

    
    
    public static int getSkippedTestsCount(List<TestResult> testsResultsList) {
        return getTestsCountByStatus(testsResultsList, ResultStatus.Skipped);
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
        
        return getTestCasesCountByStatus(testsResultsList, ResultStatus.Failed);
    }

    
    
    public static int getSkippedTestCasesCount(List<TestResult> testsResultsList) {        
        
        return getTestCasesCountByStatus(testsResultsList, ResultStatus.Skipped);
    }



    public static void updateResultsOnServer(
            String project,
            String user, 
            SuiteResult suiteResult,
            Path resultFilePath,
            String jenkinsJobName,
            String jenkinsBuildNr) {


        // send result TEXT / HTML file to web server
        String uniqResultFileName = Results.getUniqueResultFileName(
                WinUtils.getHostName(),
                resultFilePath);

        MainServer.sendFileToServer(
                resultFilePath.toString(),
                "/var/www/html/logs/", 
                uniqResultFileName);

        Results.updateResultsIntoDatabase(
                XmlTestConfig.getSuiteName(),
                project,
                user,
                suiteResult,
                WinUtils.getHostName(),
                uniqResultFileName,
                jenkinsJobName,
                jenkinsBuildNr);
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
            logLines("Could not write to result file " 
                    + resultFilePath.toString() + "\n" + e);
        }
    }

}

