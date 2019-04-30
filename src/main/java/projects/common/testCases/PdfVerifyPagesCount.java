package main.java.projects.common.testCases;

import main.java.base.Assert;
import main.java.base.testCase.WebPageTestCase;
import main.java.utils.PdfUtils;


/**
 * Verify pages count for PDF file.
 *  
 * @author Dan.Rusu
 *
 */
public class PdfVerifyPagesCount extends WebPageTestCase{



    @Override
    public void run(){

        String pdf = evalAttribute("pdf");

        Assert.isEqual(

                evalIntAttribute("expectedPageCount"),

                PdfUtils.getPagesCount(pdf),

                "Verify pages count for " + pdf);

    }



    @Override
    public String getTestCaseScenario(){
        
        return "\nVerify pages count for PDF file."
                + "\nTest data: pdf, expectedPageCount";
    }

}

