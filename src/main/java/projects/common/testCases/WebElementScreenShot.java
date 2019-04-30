package main.java.projects.common.testCases;

import static main.java.base.Logger.getLogDirPath;
import static main.java.base.selenium.Driver.saveElementScreenshotAshotWrapped;

import main.java.base.testCase.WebPageTestCase;

/**
 * @author Dan.Rusu 
 *
 */
public class WebElementScreenShot extends WebPageTestCase{

    
	@Override
	public void run(){
	    
		saveElementScreenshotAshotWrapped(
		        
		        evalFloatAttribute("scaling", 1),     
		        
		        evalByAttribute("selector"),
		        
		        getLogDirPath().resolve(evalAttribute("file")));
	}
		
	
	@Override
	public String getTestCaseScenario(){
	    
		return newScenario(
				"Save local screenshot of a web element (via Ashot).",				
				"Test data: [scaling=1] selector, file.");
	}

}
