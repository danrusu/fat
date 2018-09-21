package projects.common.testCases;

import base.Assert;
import base.testCase.TestCase;

/**
 * Asserts that two strings are equal.
 * @author Dan.Rusu
 *
 */
public class AreEqual extends TestCase{



	@Override
	public void run(){
		
		String string1 = evalAttribute("string1");
		String string2 = evalAttribute("string2");
	
		Assert.assertTrue("string1 is equal to string2", string1.equals(string2));
	}
	
	@Override
	public String getTestCaseScenario(){
		return "\nChecks if two strings are equal."
				+ "\nTest data: string1, string2";
	}

}
