package main.projects.common.testCases;

import main.base.Assert;
import main.base.testCase.WebPageTestCase;
import main.utils.PdfUtils;


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

                "Verify pages count for " + pdf,

                evalIntAttribute("expectedPageCount"),

                PdfUtils.getPagesCount(pdf));

    }



    @Override
    public String getTestCaseScenario(){
        
        return "\nVerify pages count for PDF file."
                + "\nTest data: pdf, expectedPageCount";
    }

}

