package main.projects.mock.testCases;

import static main.base.Logger.log;

import main.base.testCase.WebPageTestCase;

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
		return newScenario("Empy test case used just for unit testing!");
	}
}
