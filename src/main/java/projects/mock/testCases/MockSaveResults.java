package main.java.projects.mock.testCases;

import main.java.base.testCase.WebPageTestCase;

/**
 * @author Dan.Rusu
 *
 */
public class MockSaveResults extends WebPageTestCase{

	public String[] orders;

	
	
	@Override
	public void run(){
	    
	    orders = new String[] { "first", "second", "third" };
	    
	    saveResults();
	}

	
	
	@Override
	public String getTestCaseScenario(){
	    
		return "Test case used for unit testing save results functionality!";
	}
}

