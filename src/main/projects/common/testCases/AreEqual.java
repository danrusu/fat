package main.projects.common.testCases;

import main.base.Assert;
import main.base.testCase.TestCase;

/**
 * Asserts two strings.
 * 
 * @author Dan.Rusu
 *
 */
public class AreEqual extends TestCase{



    @Override
    public void run(){

        run(
                evalAttribute("expected"),
                evalAttribute("actual"));
    }


    private void run(String expected, String actual){

        Assert.isEqual(expected, actual);
    }


    @Override
    public String getTestCaseScenario(){

        return newScenario("Assert strings.",
                "Test data: expected, actual");
    }


}

