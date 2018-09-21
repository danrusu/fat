package projects.common.testCases;

import base.Assert;
import base.testCase.WebPageTestCase;
import utils.PdfUtils;


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

        Assert.equals(

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

