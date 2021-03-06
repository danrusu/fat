package main.java.base.result;

import static main.java.base.Logger.log;

import java.nio.file.Path;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import main.java.base.Assert;
import main.java.base.testCase.TestCaseDocs;
import main.java.base.xmlSuite.XmlTestConfig;


public abstract class Results {
  

    /**
     * Set a TestInfo endDate and result and add it to testsResults list. 
     * 
     * @param testResult - current TestResult
     * @param result - the test result
     */
    public static void addTestResult(
            List<TestResult> testResultInfo,
            TestResult testResult, 
            ResultStatus result, 
            Instant startTestTime){

        //		testResult.setElapsedTestTime( startTestTime );
        //		testResult.setResult( result );
        testResultInfo.add(testResult);
    }


    /**
     * Set a TestCaseInfo endDate and result and add it to testInfo. 
     * 
     * @param testInfo - current TestResult
     * @param result - the test result
     * @param testCaseResult - current TestCaseResult
     * @param startTestTime - test case tart time
     */
    public static void addTestCaseResult(
            TestResult testResult, 
            TestCaseResult testCaseResult, 
            ResultStatus result, 
            Instant startTestCaseTime){

        log("test_" + testResult.getId() + "/" 
                + "testCase_" + testCaseResult.getId() 
                + ": " + result.toString());

        // set testCase results info
        //testCaseResult.setElapsedTestTime( startTestCaseTime );
        //testCaseResult.setResult( result );

        // add test case result to current test result
        //testResult.addTestCaseInfo(testCaseResult.getId(), testCaseResult);
    }

    
    public static String getDetailedResultsHtml(
            List<TestResult> testResultInfo,
            SuiteResult suiteResult
            ){
               
        String suiteResultStatus = (suiteResult.isSucceeded()) ? 
                "<font color=\"green\">Succeeded</font>" : 
                    "<font color=\"red\">Failed</font>";
        
        StringBuilder resultsString = new StringBuilder();

        // HTML start + head
        resultsString.append("<html>\n")
            .append(Html.detailsHead(XmlTestConfig.getSuiteName()))
            .append("<body onload=\"resultsPageSetup();\">\n");


        // Suite result
        resultsString.append("<div id=\"suiteResultsContainer\">");
        resultsString.append("<table class=\"suiteResults\">\n");
        resultsString.append(Html.createTableHeaderRow(

                "Suite",

                "Elapsed",

                "Result",

                "Tests total / failed / skipped",

                "TestCases total / failed / skipped",
                
                "Asserts"));

        resultsString.append(Html.createTableRow(

                XmlTestConfig.getSuiteName(),

                suiteResult.getElapsedTime(),

                suiteResultStatus,

                String.join(
                        " / ", 
                        Integer.toString(suiteResult.getTotalTestsCount()),
                        Integer.toString(suiteResult.getFailedTestsCount()),
                        Integer.toString(suiteResult.getSkippedTestsCount())),

                String.join(
                        " / ",
                        Integer.toString(suiteResult.getTotalTestCasesCount()),
                        Integer.toString(suiteResult.getFailedTestCasesCount()),
                        Integer.toString(suiteResult.getSkippedTestCasesCount())),
                
                Assert.AssertCount.assertCount + ""
        ));


        resultsString.append("</table>\n</div>\n<br/><br/>\n");

        // Tests/TestCaes details
        resultsString.append("<table class=\"results\">\n");

        String expandIcon = "<span title=\"Expand/collapse test cases\">"
                + "<svg class=\"svg24\" id=\"expandTestcases\" xmlns=\"http://www.w3.org/2000/svg\" width=\"24\" height=\"24\" >"
                + "<path fill=\"#00FF00\" fill-rule=\"evenodd\" d=\"M16 11h-3V8c0-.6-.4-1-1-1s-1 .4-1 1v3H8c-.6 0-1 .4-1 1s.4 1 1 "
                + "1h3v3c0 .6.4 1 1 1s1-.4 1-1v-3h3c.6 0 1-.4 1-1s-.4-1-1-1zM12 0C5.4 0 0 5.4 0 12s5.4 12 12 12 12-5.4 12-12S18.6 "
                + "0 12 0zm0 22C6.5 22 2 17.5 2 12S6.5 2 12 2s10 4.5 10 10-4.5 10-10 10z\" clip-rule=\"evenodd\"/>"
                + "</svg></span>&nbsp;"
                + "Test/Testcase";

        String detailsIcon = "<span title=\"Show test cases info\">"
                + "<svg class=\"svg24\" id=\"showDetails\" xmlns=\"http://www.w3.org/2000/svg\" width=\"115\" height=\"24\">"
                + "<path fill=\"#00FF00\" fill-rule=\"evenodd\" d=\"M99 0c-6.6 0-12 5.4-12 12s5.4 12 12 12 12-5.4 12-12-5.4-12-12-12zm.4 "
                + "6c.8 0 1.5.7 1.5 1.5S100.2 9 99.4 9s-1.5-.7-1.5-1.5.6-1.5 1.5-1.5zm1.6 10.3c-.7 1.1-1.5 1.6-2.7 1.6-1 0-1.9-1-1.7-1.6L98 "
                + "12c0-.1 0-.3-.1-.3-.9.1-1.8.6-1.8.6 0-.2 0-.1 0-.3.7-1.1 2.2-2.1 3.4-2 .7.1.8.9.7 1.6L99 16c0 .3.2.5.5.5.5 0 1.5-1.1 1.5-1.1 "
                + "0 .3 0 .8 0 .9z\" clip-rule=\"evenodd\"/>" 
                + "</svg></span>"
                + "Details";

        resultsString.append(Html.createTableHeaderRow(
                expandIcon,
                "Name",
                "Elapsed",
                "Result",
                detailsIcon));



        for (TestResult testResult : testResultInfo){
            
            Map<String, String> testAttr = new TreeMap<>();
            var testResultSatus = testResult.getResultStatus();
            
            testAttr.putAll(testResult.getAttributes());
            testAttr.remove("name");

            // first add test result info
            resultsString.append(Html.createTableRow(
                    
                    testResult.getId(),
                    
                    testResult.getAttributes().get("name"),
                    
                    testResult.getElapsedTime(),
                    
                    Html.tagFromText(
                            "font", 
                            testResultSatus.name(), 
                            Html.attribute(
                                    "color",  
                                    testResultSatus.getHtmlColor())),
                    
                    testAttrToHtml(testAttr)));

            // add test cases result info
            Map<Integer, TestCaseResult> testCasesResults = testResult.getTestCasesResults();
            
            testCasesResults.values().forEach( testCaseResult -> {	    

                        Map<String, String> attr = new TreeMap<>();
                        ResultStatus testCaseResultStatus = testCaseResult.getResultStatus();
                        
                        attr.putAll(testCaseResult.getAttributes());
                        attr.remove("name");
                        
                        // do not show passwords in report
                        attr.remove("password");

                        resultsString.append("\n<tr style=\"display:none;\">");

                        resultsString.append(Html.createTableColumns(
                                
                                testResult.getId() + "/" + testCaseResult.getId(),
                                
                                testCaseResult.getAttributes().get("name"),
                                
                                testCaseResult.getElapsedTime(),
                                
                                Html.tagFromText(
                                        "font", 
                                        testCaseResultStatus.name(), 
                                        Html.attribute(
                                                "color",  
                                                testCaseResultStatus.getHtmlColor())),
                                
                                
                                testCaseAttrToHtml(testCaseResult.getAttributes().get("name"), attr)));

                        resultsString.append("</tr>\n");
            });
        }
        
        resultsString.append("</table>\n"
                +"</body>\n"
                + "</html>\n");

        return resultsString.toString();
    }


    public static String getUniqueResultFileName(String hostname, Path resultFilePath){
        return hostname.replace('.', '_') 
                + "_" 
                + resultFilePath
                .toString()
                .replace(System.getProperty("user.dir"), "")
                .replace('\\', '_')

                .replace("_logs_", "")
                .replace("log_", "");
    }


    private static String testCaseAttrToHtml(String testcaseName, Map<String, String> attr) {		
        String failure = attr.remove("failure");
        String js_errors = attr.remove("js_errors");
        String save = attr.remove("save");
        String saveResults = attr.remove("saveResults");
        String note = attr.remove("note");
        String testCaseHtmlReport = attr.remove("testCaseHtmlReport");

        String htmlAttr = new String();

        // add all attributes but the ones that were removed above
        for (String attribute : attr.keySet()){
            htmlAttr += attribute + "=\"" + attr.get(attribute) + "\"" + "<br/>";
        }


        // add saved attribute
        htmlAttr += Html.divFromText(
                saveResults, 
                x -> "savedResults:{" + x + "}");


        // add saved attribute
        htmlAttr += Html.divFromText(
                save,
                x -> "saved: " + x,
                "class=\"saved\"");	

        // add failure
        htmlAttr += Html.divFromText(
                
                failure,
                
                x -> Html.htmlText("failure: " + x)
                    
                    .replaceAll("Cause:", "<br>Cause:"),
                
                "class=\"failure\"");

        // add js_errors
        htmlAttr += Html.divFromText(
                js_errors, 
                x -> "JS_ERRORS: <br/> " + x.replaceAll("\n", "<br/>"),
                "class=\"js_errors\"");

        // add note
        htmlAttr += Html.tagFromText( 
                "div",
                note,
                x -> "Note:<br/> " + x.replace("\\n", "<br/>"),
                "class=\"note\"");

        // add testCaseHtmlReport attribute
        htmlAttr += Html.divFromText(
                testCaseHtmlReport, 
                x -> "<a href=\"" + testCaseHtmlReport + "\">"
                        + "<img class=\"customIcon\" src=\"../images/icons/html.ico\"/>"
                        + "<br>Report"
                        + "</a>",
                "");


        // add hidden testCaseScenario information, available on click
        if(!testcaseName.isEmpty()){
            String scenario = TestCaseDocs.getTestScenario(testcaseName);
            if(!scenario.isEmpty()){
                htmlAttr += "<div class=\"testCaseDocs\" title=\"Test case info\">"
                        + 
                        scenario.replace("\n","<br>")
                        + "</div>";
            }
        }

        return htmlAttr;
    }


    private static String testAttrToHtml(Map<String, String> attr) {
    
        return testCaseAttrToHtml("", attr);
    }
    
}
