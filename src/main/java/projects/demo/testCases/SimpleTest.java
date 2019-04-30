package main.java.projects.demo.testCases;

import main.java.base.Assert;
import main.java.base.testCase.TestCase;

public class SimpleTest extends TestCase{

	@Override
	public void run() {
		String text = evalAttribute("text");
		String expected = evalAttribute("expected");
		
		Assert.isEqual(expected, text);
	}

	@Override
	public String getTestCaseScenario() {
		return newScenario("Simple demo testcase.");
	}

}
