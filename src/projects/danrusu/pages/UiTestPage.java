package projects.danrusu.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import base.pom.WebPage;
import projects.danrusu.testCases.Operation;

public class UiTestPage extends WebPage {

	
	private static final String defaultUrl = "http://danrusu.ro/uiTest/uiTest.html";
	private final String defaultTitle = "UI tests";
	

	public UiTestPage(WebDriver driver) {

		super(driver);
		setTitle(defaultTitle);
		setUrl(defaultUrl);		
	}
	
	
		
	
	// Calculate API locators
	private By firstNumber = By.id("firstNumber");
	private By secondNumber = By.id("secondNumber");
	
	private By operationDropdown = By.id("operation");
	private String operationOption =  "#operation option[value=\"@operation\"]";
	
	private By calculate =  By.id("calculate");
	
	private By result = By.id("calculationResult"); 
	
	
	
	public void calculate(String firstNumber, String secondNumber, Operation operation) {
		
		setValueJs(this.firstNumber, firstNumber);
		
		setValueJs(this.secondNumber, secondNumber);
		
		click(operationDropdown);
		
		By operationOption = By.cssSelector(
				this.operationOption.replace("@operation", "" + operation.getValue()));
		
		click(operationOption);
		
		click(calculate);
		
	}
	
	
	public String getResult() {
		
		return getText(result);
	}
}
