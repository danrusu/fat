package projects.mock.testCases;

import core.Driver;
import core.testCase.WebPageTestCase;

/**
 * Test case that generates a javascript error.
 * @author Dan.Rusu
 *
 */
public class MockJSError extends WebPageTestCase{



	@Override
	public void run(){
			Driver.driver.get("http://danrusu.ro/jsError.html");
	}
	
	
	@Override
	public String getTestCaseScenario(){
		return "\nTest for catching javascript errors"
				+ "\nThis test opens http://danrusu.ro/jsError.html page.";
	}
}
