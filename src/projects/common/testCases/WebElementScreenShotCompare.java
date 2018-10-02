package projects.common.testCases;

import static base.Assert.assertTrue;
import static base.Driver.areScreenshotsDifferent;

import java.nio.file.Path;
import java.nio.file.Paths;

import base.testCase.WebPageTestCase;



/**
 * @author Dan.Rusu 
 *
 */
public class WebElementScreenShotCompare extends WebPageTestCase{



    @Override
    public void run(){

        Path diffFile = Paths.get(evalAttribute("diffFile"));
        Path file1 = Paths.get(evalAttribute("file1"));
        Path file2 = Paths.get(evalAttribute("file2"));


        assertTrue(

                String.join(" | ",                        
                        "Screenshots are different",
                        "file1=" + file1,
                        "file2=" + file2,
                        "diffFile="+ diffFile),        

                areScreenshotsDifferent(

                        evalFloatAttribute("scaling", 1),     

                        evalByAttribute("selector1"),

                        evalByAttribute("selector2"),


                        file1,

                        file2,

                        diffFile));
    }



    @Override
    public String getTestCaseScenario(){

        return newScenario(
                "Save local screenshot of a web element (via Ashot).",				
                "Test data: [scaling=1] selector1, selector2, file1, file2, diffFile.");
    }

}

