package projects.mock.testCases;

import base.testCase.WebPageTestCase;

/**
 * Unit test for saving the result of adding two numbers.
 * @author Dan.Rusu
 *
 */
public class AddNumbers extends WebPageTestCase{

	String result;
	


	@Override
	public void run(){
		result = Long.toString(Long.parseLong(evalAttribute("number1")) 
				+ Long.parseLong(evalAttribute("number2")));
		
		saveResults();
	}
	
	
	@Override
	public String getTestCaseScenario(){
		return "\nTest case used just for unit testing of the 'Save results' funtionality"
				+ "\nData: number1, number2"
				+ "\nResults: result";
	}
}
