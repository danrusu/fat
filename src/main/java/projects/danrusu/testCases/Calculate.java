package main.java.projects.danrusu.testCases;

import static main.java.base.selenium.Driver.getDriver;

import main.java.base.Assert;
import main.java.base.testCase.WebPageTestCase;
import main.java.projects.danrusu.pages.UiTestPage;

public class Calculate extends WebPageTestCase {

	
	@Override
	public void run() {	
		
		// from XML
		String firstNumber = evalAttribute("firstNumber");

		String secondNumber = evalAttribute("secondNumber");

		Operation operation = Operation.valueOf(evalAttribute("operation").toUpperCase());
		
		String expectedResult = evalAttribute("expectedResult");
		
		// actions
		UiTestPage uiTestPage = new UiTestPage(getDriver());  
		
		//uiTestPage.waitForPageLoaded(3);		
		
		uiTestPage.calculate(firstNumber, secondNumber, operation);
		
		uiTestPage.waitForAjaxFinish();
		
		
		String result = uiTestPage.getResult();
				
		Assert.isEqual(expectedResult, result);
	}
	

	@Override
	public String getTestCaseScenario() {
		
		return newScenario("Validate Calculate functionality.",
				"Data: [firstNumber], [secondNumber], operation, expectedResult.");
	}

}
