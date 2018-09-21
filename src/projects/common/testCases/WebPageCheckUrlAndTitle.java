package projects.common.testCases;

import base.Driver;
import base.pom.WebPage;
import base.testCase.TestCase;

/**
 * Test case for verifying web page URL and title
 * @author Dan.Rusu
 *
 */
public class WebPageCheckUrlAndTitle extends TestCase{


	@Override
	public void run(){
		String url = evalAttributeNullable("url");
		String title = evalAttributeNullable("title");
        String timeout = evalAttribute("timeout");
		
        if (! timeout.isEmpty()) {
        	new WebPage(Driver.driver).checkUrlAndTitle(url, title, Long.parseLong(timeout) );
        }
        else {
        	new WebPage(Driver.driver).checkUrlAndTitle(url, title);
        }
        		
	}
	
	@Override
	public String getTestCaseScenario(){
		return new StringBuilder()
				.append("\nVerify web page url and title.")				
				.toString();
	}


}
