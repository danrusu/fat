package main.projects.common.testCases;

import main.base.Assert;
import main.base.testCase.TestCase;

/**
 * Asserts two strings equality.
 * 
 * @author Dan.Rusu
 */
public class AreEqual extends TestCase{


    @Override
    public void run(){

    	Assert.isEqual(
    			evalAttribute("expected"),
                evalAttribute("actual"));
    }


    @Override
    public String getTestCaseScenario(){

        return newScenario("Assert strings equality.",
                "Test data: expected, actual.");
    }
}
