package projects.common.testCases;

import base.Assert;
import base.pom.WebPage;
import base.testCase.WebPageTestCase;

/**
 * Open Web page in browser by URL.
 * @author Dan.Rusu
 *
 */
public class OpenPage extends WebPageTestCase{

	
	
	@Override
	public void run(){
		
		String url = evalAttribute("url");
		
		String expectedUrl = evalAttribute("expectedUrl");
		String expectedTitle = evalAttribute("expectedTitle");
		
		Assert.isTrue("Check that url is not empty.", false == url.isEmpty());
		
		openUrl(url);
		
		if(!expectedUrl.isEmpty()) {
			new WebPage(driver).checkUrl(expectedUrl, 1000);		
		}
				
		if(!expectedTitle.isEmpty()) {
			new WebPage(driver).checkTitle(expectedTitle, 1000);		
		}
		
	}


	
	@Override
	public String getTestCaseScenario(){
		
		return newScenario("Open Web page in browser by URL.",
				"Data: url [expectedUrl] [expectedTitle]");
	}
}
