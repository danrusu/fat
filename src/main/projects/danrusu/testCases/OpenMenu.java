package main.projects.danrusu.testCases;


import static main.base.selenium.Driver.getDriver;

import main.base.testCase.WebPageTestCase;
import main.projects.danrusu.pages.HomePage;

public class OpenMenu extends WebPageTestCase {

	
	@Override
	public void run() {
		
		String menu = evalAttribute("menu");		
		
		HomePage homePage = new HomePage(getDriver());  
		
		homePage.openMenu(menu);
		
	}
	

	@Override
	public String getTestCaseScenario() {
		
		return newScenario("Open menu in danrusu.ro page.",
				"Data: menu.");
	}
	
}
