package projects.danrusu.testCases;

import base.Assert;
import base.Driver;
import base.testCase.WebPageTestCase;
import projects.danrusu.pages.UiTestPage;

public class Calculate extends WebPageTestCase {

	
	@Override
	public void run() {
		
		String firstNumber = evalAttribute("firstNumber");

		String secondNumber = evalAttribute("secondNumber");

		Operation operation = Operation.valueOf(evalAttribute("operation").toUpperCase());
		
		String expectedResult = evalAttribute("expectedResult");
		
		
		UiTestPage uiTestPage = new UiTestPage(Driver.driver);  
		
		uiTestPage.waitForPageLoaded(3);
		
		
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
