package main.java.projects.common.testCases;

import main.java.base.selenium.Driver;
import main.java.base.testCase.WebPageTestCase;


/**
 * @author Dan.Rusu
 */
public class WebPageGoBack extends WebPageTestCase{


	@Override
	public void run(){	    
		Driver.back();
	}
	
	
	@Override
	public String getTestCaseScenario(){	    
		return newScenario("Press Back in browser window.");
	}

}
