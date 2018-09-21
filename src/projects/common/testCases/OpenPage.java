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
		
		Assert.assertTrue("Check that url is not empty.", !url.isEmpty());
		
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
		return new StringBuilder()
				.append("\n Open Web page in browser by URL.")
				.append("\nData: url [expectedUrl] [expectedTitle]")
				.toString();
	}
}
