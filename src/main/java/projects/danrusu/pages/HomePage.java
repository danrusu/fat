package main.java.projects.danrusu.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import main.java.base.failures.Failure;
import main.java.base.pom.WebPage;

public class HomePage extends WebPage {

	
	private static final String defaultUrl = "http://danrusu.ro/";
	private final String defaultTitle = "Automation";
	

	public HomePage(WebDriver driver) {

		super(driver);
		
		setTitle(defaultTitle);
		setUrl(defaultUrl);		
	}
	
	
	public static String getDefaultUrl() {
		
		return defaultUrl;
	}
		
	
	// locators
	private By menuGitHub = By.cssSelector("*[data-qa-hook=\"menu_openSource\"]");
	private By menuTips = By.cssSelector("*[data-qa-hook=\"menu_tips\"]");
	private By menuUiTest = By.cssSelector("*[data-qa-hook=\"menu_uiTest\"]");
	private By menuCustom = By.cssSelector("*[data-qa-hook=\"menu_custom\"]");
	private By menuContact = By.cssSelector("*[data-qa-hook=\"menu_contact\"]");
	
	
	public void openMenu(String menu) {
				
		switch(menu.toLowerCase()) {
		
		case "github": 
			click(menuGitHub);			
			break;	
			
		case "tips": 
			click(menuTips);
			break;		
			
		case "ui test": 
			click(menuUiTest);
			break;
			
		case "custom": 
			click(menuCustom);
			break;	
			
		case "contact": 
			click(menuContact);
			break;				
		
		default:
			throw new Failure("wrong menu!");
		
		}
	}
}
