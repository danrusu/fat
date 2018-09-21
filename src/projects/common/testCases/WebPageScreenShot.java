package projects.common.testCases;

import java.nio.file.Paths;

import base.Driver;
import base.testCase.TestCase;

/**
 * @author Dan.Rusu 
 *
 */
public class WebPageScreenShot extends TestCase{

    

	@Override
	public void run(){
	    
		Driver.saveScreenShot(Paths.get(
		        evalAttribute("file")).toString());		
	}
	
	
	
	@Override
	public String getTestCaseScenario(){
	    
		return newScenario(
				"Save local screenshot of a web page.",				
				"Test data: file.");
	}

}

