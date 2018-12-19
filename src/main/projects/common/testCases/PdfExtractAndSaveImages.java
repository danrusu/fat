package main.projects.common.testCases;

import main.base.testCase.WebPageTestCase;
import main.utils.PdfUtils;


/**
 * Save images (png) from PDF. 
 *  
 * @author Dan.Rusu
 *
 */
public class PdfExtractAndSaveImages extends WebPageTestCase{



    @Override
    public void run(){

         PdfUtils.saveImages(
                 
                 evalAttribute("pdf"),
                 
                 evalAttribute("destinationDir"));

    }



    @Override
    public String getTestCaseScenario(){
        
        return "\nSave images from PDF file in destinationDir."
                + "\nTest data: pdf, destinationDir";
    }

}

