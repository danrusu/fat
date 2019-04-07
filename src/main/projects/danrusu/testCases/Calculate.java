package main.projects.danrusu.testCases;

import static main.base.Assert.isEqual;
import static main.base.selenium.Driver.getDriver;

import main.base.testCase.WebPageTestCase;
import main.projects.danrusu.pages.UiTestPage;
import main.utils.ThreadUtils;

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
		ThreadUtils.sleep(1000);
		
		
		String result = uiTestPage.getResult();
		
		
		isEqual(expectedResult, result);
	}
	

	@Override
	public String getTestCaseScenario() {
		
		return newScenario("Validate Calculate functionality.",
				"Data: [firstNumber], [secondNumber], operation, expectedResult.");
	}

}
