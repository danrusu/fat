package projects.common.testCases;

import base.testCase.WebPageTestCase;
import utils.ImageUtils;


/**
 * Verify that two images are equal.
 *  
 * @author Dan.Rusu
 *
 */
public class ImagesVerifyEquality extends WebPageTestCase{



    @Override
    public void run(){

        ImageUtils.imagesVerifyEqualityWrapped(
                evalAttribute("expected"), 
                evalAttribute("actual"));

    }



    @Override
    public String getTestCaseScenario(){
        
        return "\nVerify that expected image equals the actaul image."
                + "\nTest data: expected, actual";
    }

}

