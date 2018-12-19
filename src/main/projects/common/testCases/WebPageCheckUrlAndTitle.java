package main.projects.common.testCases;

import main.base.selenium.Driver;
import main.base.pom.WebPage;
import main.base.testCase.TestCase;

/**
 * Test case for verifying web page URL and title.
 * 
 * @author Dan.Rusu
 *
 */
public class WebPageCheckUrlAndTitle extends TestCase{


	@Override
	public void run(){
	    
		String url = evalAttributeNullable("url");
		String title = evalAttributeNullable("title");
        int timeout = evalIntAttribute("timeout");
		
       
        new WebPage(Driver.getDriver()).checkUrlAndTitle(
                url, 
                title, 
                Long.parseLong(timeout + ""));
	}
	
	
	
	@Override
	public String getTestCaseScenario(){
	    
		return newScenario(
				"Verify web page url and title.",				
				"Test data: url, title, [timeout(ms)=0]");
	}

}

