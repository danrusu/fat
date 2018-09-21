package projects.common.testCases;

import base.Driver;
import base.testCase.TestCase;

/**
 * @author Dan.Rusu 
 *
 */
public class WebElementScreenShot extends TestCase{

    

	@Override
	public void run(){
	    
		Driver.saveElementScreenshot(
		        
		        evalByAttribute("selector"),
		        
		        evalFileAttribute("file"));		
	}
	
	
	
	@Override
	public String getTestCaseScenario(){
	    
		return newScenario(
				"Save local screenshot of a web element.",				
				"Test data: selector, file.");
	}

}

