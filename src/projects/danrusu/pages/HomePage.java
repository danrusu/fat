package projects.danrusu.pages;

import org.openqa.selenium.WebDriver;

import base.pom.WebPage;

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
		
}
