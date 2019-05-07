package main.java.projects.common.testCases;

import static main.java.base.Logger.logSplitByLines;
import static main.java.base.failures.ThrowablesWrapper.supplyUnchecked;

import main.java.base.Assert;
import main.java.base.failures.Failure;
import main.java.base.testCase.WebPageTestCase;
import main.java.projects.common.StringComparison;
import main.java.utils.PdfUtils;
import main.java.utils.PdfUtils.PdfCompareMethods;

/**
 * Compare PDF files by text. 
 * @author dan.rusu
 */
public class PdfCompare extends WebPageTestCase{

    private PdfCompareMethods method;
    private StringComparison comparison;
    

    @Override
    public void run(){

        method = supplyUnchecked(

                () -> PdfCompareMethods.valueOf(evalAttribute("method")),

                PdfCompareMethods.pdfbox);


        comparison = supplyUnchecked(

                () -> StringComparison.valueOf(evalAttribute("comparison")),

                StringComparison.equals);


        String expectedPdfs = evalAttribute("expected");
        String actualPdfs = evalAttribute("actual");


        logSplitByLines(
                "Compare PDF files by text | method: " + method.name()
                        + " | comparison: " + comparison.name()
                        + " Expected: " + expectedPdfs
                        + " Actual: " + actualPdfs);


        String[] expectedPdfFiles = expectedPdfs.split(";");
        String[] actualPdfFiles = actualPdfs.split(";");

        String compareItReport;
        
        switch(method){
            
            case pdfbox:

                Assert.isEqual(
                        true,
                        PdfUtils.comparePDFbyText(expectedPdfFiles, actualPdfFiles, comparison),
                        "Compare PDF by text contents - comparison: " + comparison);
              
                break;              

            case compareit:
                
                compareItReport = evalAttribute("report");

                if( (expectedPdfFiles.length>1) || (actualPdfFiles.length>1) ){
                    throw new Failure("Cannot compare multiple files with 'compareit' method.");
                }

                Assert.isEqual(
                        true,
                        PdfUtils.compareIt(
                        		expectedPdfFiles[0], 
                        		actualPdfFiles[0], 
                        		compareItReport),
                        "Check if PDF files' text contents are identical. (Report: " 
                        		+ compareItReport + ")");
                
                break;
        }
    }


    @Override
    public String getTestCaseScenario(){
    	
        return newScenario("Compare PDF files.",
                "Test data: expected, actual, [method, comparison, report]",
                "Method: ",
                " pdfbox (default) - compares pdfs by text content; it allows multi file compare.",
                " compareit - single file compare; generates an html report if files are not identical.",
                "Comparison: ",
                " equals(default), ",
                " expectedContains, expectedStartsWith, expectedEndsWith",
                " actualContains, actualStartsWith, actualEndsWith");
    }

}
