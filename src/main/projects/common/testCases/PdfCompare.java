package main.projects.common.testCases;

import static main.base.Logger.logSplitByLines;
import static main.base.failures.ThrowablesWrapper.supplyUnchecked;

import main.base.Assert;
import main.base.failures.Failure;
import main.base.testCase.WebPageTestCase;
import main.projects.common.StringComparison;
import main.utils.PdfUtils;
import main.utils.PdfUtils.PdfCompareMethods;

/**
 * Compare PDF files by text. 
 * @author Dan.Rusu
 *
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
                        + "\n Expected: " + expectedPdfs
                        + "\n Actual: " + actualPdfs);


        String[] expectedPdfFiles = expectedPdfs.split(";");
        String[] actualPdfFiles = actualPdfs.split(";");

        String compareItReport;
        
        switch(method){
            
            case pdfbox:

                		
                Assert.isTrue(
                        
                        "Compare PDF by text contents - comparison: " + comparison,
                        
                        PdfUtils.comparePDFbyText(expectedPdfFiles, actualPdfFiles, comparison));
              
                break;
                

            case compareit:
                
                compareItReport = evalAttribute("report");

                if( (expectedPdfFiles.length>1) || (actualPdfFiles.length>1) ){
                    throw new Failure("Cannot compare multiple files with 'compareit' method.");
                }

                Assert.isTrue(
                        "Check if PDF files' text contents are identical. (Report: " + compareItReport + ")",
                        PdfUtils.compareIt(expectedPdfFiles[0], actualPdfFiles[0], compareItReport));
                
                break;
        }
    }

    

    @Override
    public String getTestCaseScenario(){
        return "\nCompare PDF files."
                + "\nTest data: expected, actual, [method, comparison, report]"
                + "\nMethod: "
                + "\n pdfbox (default) - compares pdfs by text content; it allows multi file compare."
                + "\n compareit - single file compare; generates an html report if files are not identical."
                + "\nComparison: "
                + "\n equals(default), "
                + "\n expectedContains, expectedStartsWith, expectedEndsWith"
                + "\n actualContains, actualStartsWith, actualEndsWith";
    }

}

