package main.projects.danrusu.testCases;

import static main.base.Assert.*;
import static main.base.selenium.Driver.*;
import main.base.testCase.WebPageTestCase;
import main.projects.danrusu.pages.HomePage;

public class OpenHomePage extends WebPageTestCase {

	
	@Override
	public void run() {
		
		openUrl(HomePage.getDefaultUrl());
		
		HomePage homePage = new HomePage(getDriver());  
		
		//homePage.waitForPageLoaded(3);
		
		validate(homePage);
		
		// homePage.checkUrlAndTitle();
	}

	
	private void validate(HomePage homePage) {
		
/*		
		String wrongUrl = "http://danrusu.com/";
		String wrongTitle = "Automation in Testing";
		
		// fail test case first 
		Assert.isEqual("Check url", homePage.getUrl(), wrongUrl);
		Assert.isEqual("Check title", homePage.getTitle(), wrongTitle);		
*/		
		String currentUrl = homePage.getCurrentUrl();		
		isEqual(homePage.getUrl(), currentUrl);

		String currentTitle = homePage.getCurrentTitle();		
		isEqual(homePage.getTitle(), currentTitle);	
	}


	@Override
	public String getTestCaseScenario() {
		
		return newScenario("Open danrusu.ro page.",
				"Validate url and title.");
	}

}
