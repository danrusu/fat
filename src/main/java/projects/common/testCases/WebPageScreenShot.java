package main.java.projects.common.testCases;

import static main.java.base.selenium.Driver.saveScreenShotWrapped;

import main.java.base.testCase.TestCase;

/**
 * @author Dan.Rusu 
 *
 */
public class WebPageScreenShot extends TestCase{
 

	@Override
	public void run(){
	    
		saveScreenShotWrapped(evalPathAttribute("file").toString());		
	}
	
	
	@Override
	public String getTestCaseScenario(){
	    
		return newScenario(
				"Save local screenshot of a web page.",				
				"Test data: file.");
	}

}
