package projects.danrusu.testCases;

import base.Driver;
import base.testCase.WebPageTestCase;
import projects.danrusu.pages.HomePage;

public class OpenMenu extends WebPageTestCase {

	
	@Override
	public void run() {
		
		String menu = evalAttribute("menu");
		
		
		HomePage homePage = new HomePage(Driver.driver);  
		
		homePage.openMenu(menu);
		
	}
	

	@Override
	public String getTestCaseScenario() {
		
		return newScenario("Open menu in danrusu.ro page.",
				"Data: menu.");
	}
	
}
