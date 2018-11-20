package projects.common.testCases;

import base.Driver;
import base.pom.WebPage;
import base.testCase.TestCase;

/**
 * Test case for verifying web page URL and title.
 * 
 * @author Dan.Rusu
 *
 */
public class WebPageCheckUrlAndTitle extends TestCase{


	@Override
	public void run(){	    
		
       
        new WebPage(Driver.driver).checkUrlAndTitle(
        		
        		evalAttribute("url"), 
        		
        		evalAttribute("title"), 
        		
        		evalLongAttribute("timeout", 0));
	}
	
	
	
	@Override
	public String getTestCaseScenario(){
	    
		return newScenario(
				"Verify web page url and title.",				
				"Test data: url, title, [timeout(ms)=0]");
	}

}

