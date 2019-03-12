package main.projects.common.testCases;

import main.base.Assert;
import main.base.pom.WebPage;
import main.base.testCase.WebPageTestCase;

/**
 * Open Web page in browser by URL.
 * 
 * @author Dan.Rusu
 */
public class OpenPage extends WebPageTestCase{

	
	@Override
	public void run(){
	    
		String url = evalAttribute("url");
		
		String expectedUrl = evalAttribute("expectedUrl");
		String expectedTitle = evalAttribute("expectedTitle");
		
		Assert.isEqual(
				false,
				url.isEmpty(),
				"Check that url is not empty.");
		
		openUrl(url);
		
		if(!expectedUrl.isEmpty()) {
			new WebPage(driver).checkUrl(expectedUrl, 500);		
		}
				
		if(!expectedTitle.isEmpty()) {
			new WebPage(driver).checkTitle(expectedTitle, 500);		
		}		
	}


	@Override
	public String getTestCaseScenario(){
		return newScenario(
				"Open Web page in browser by URL.",
				"Test data: url [expectedUrl] [expectedTitle]");
	}
}
