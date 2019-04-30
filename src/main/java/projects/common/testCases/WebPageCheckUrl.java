package main.java.projects.common.testCases;

import main.java.base.pom.WebPage;
import main.java.base.testCase.WebPageTestCase;


/**
 * @author Dan.Rusu
 */
public class WebPageCheckUrl extends WebPageTestCase{


	@Override
	public void run(){
	    
		String urlRegex = evalAttributeNullable("urlRegex");
        int timeout = evalIntAttribute("timeout");		
       
        new WebPage(driver).checkUrlByRegex(
                urlRegex, 
                Long.parseLong(timeout + ""));
	}
	
	
	@Override
	public String getTestCaseScenario(){
	    
		return newScenario(
				"Validate web page url against regex.",				
				"Test data: urlRegex, [timeout(ms)=0]");
	}

}
