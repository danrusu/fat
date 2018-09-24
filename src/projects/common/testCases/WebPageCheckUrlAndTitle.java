package projects.common.testCases;

import base.Driver;
import base.pom.WebPage;
import base.testCase.TestCase;

/**
 * Test case for verifying web page URL and title.
 * @author Dan.Rusu
 *
 */
public class WebPageCheckUrlAndTitle extends TestCase{


	@Override
	public void run(){
	    
		String url = evalAttributeNullable("url");
		String title = evalAttributeNullable("title");
        int timeout = evalIntAttribute("timeout");
		
       
        new WebPage(Driver.driver).checkUrlAndTitle(
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
