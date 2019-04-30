package main.java.projects.common.testCases;

import main.java.base.testCase.WebPageTestCase;
import main.java.utils.FileUtils;


/**
 * Verify that two images are equal.
 *  
 * @author Dan.Rusu
 *
 */
public class ImagesVerifyEquality extends WebPageTestCase{



    @Override
    public void run(){

        FileUtils.imagesVerifyEquality(
                evalAttribute("expected"), 
                evalAttribute("actual"));

    }



    @Override
    public String getTestCaseScenario(){
        
        return "\nVerify that expected image equals the actaul image."
                + "\nTest data: expected, actual";
    }

}

