package projects.common.testCases;

import base.Driver;
import base.Logger;
import base.testCase.WebPageTestCase;

/**
 * @author Dan.Rusu 
 *
 */
public class WebElementScreenShot extends WebPageTestCase{

    

	@Override
	public void run(){
	    
		Driver.saveElementScreenshotAshot(
		        
		        evalFloatAttribute("scaling", 1),     
		        
		        evalByAttribute("selector"),
		        
		        Logger.getLogDirPath().resolve(evalAttribute("file")));
	}
	
	
	
	@Override
	public String getTestCaseScenario(){
	    
		return newScenario(
				"Save local screenshot of a web element (via Ashot).",				
				"Test data: selector, file.");
	}

}

