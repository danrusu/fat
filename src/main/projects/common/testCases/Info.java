package main.projects.common.testCases;

import main.base.testCase.WebPageTestCase;

/**
 * Simple info module for adding meta-data in a test report.
 * Used especially in data-driven tests.
 * 
 * @author Dan.Rusu
 *
 */
public class Info extends WebPageTestCase{

	

	@Override
	public void run(){
		// empty by design
	}
	
	
	
	@Override
	public String getTestCaseScenario(){
	    
		return newScenario("Simple info module for adding metadata in a test report", 
		        "Used especially in data-driven tests");
	}
	
}

