package main.java.projects.common.testCases;

import main.java.base.Assert;
import main.java.base.testCase.TestCase;

/**
 * Asserts two strings equality.
 * 
 * @author Dan.Rusu
 */
public class AreEqual extends TestCase{


    @Override
    public void run(){

    	String expected = evalAttribute("expected");
		String actual = evalAttribute("actual");
		
		Assert.isEqual(
    			expected,
                actual);
    }


    @Override
    public String getTestCaseScenario(){

        return newScenario("Assert strings equality.",
                "Test data: expected, actual.");
    }
}
