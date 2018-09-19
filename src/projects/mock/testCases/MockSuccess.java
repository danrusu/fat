package projects.mock.testCases;


import static core.Logger.log;

import core.testCase.WebPageTestCase;

/**
 * Unit test for a successful test case run.
 * @author Dan.Rusu
 *
 */
public class MockSuccess extends WebPageTestCase{

	

	@Override
	public void run(){
		log("Empty test case used for unit testing!");
	}
	
	
	@Override
	public String getTestCaseScenario(){
		return "\nEmpy test case used just for unit testing!";
	}
}
