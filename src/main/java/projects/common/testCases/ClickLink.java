package main.java.projects.common.testCases;

import static java.lang.String.format;

import org.openqa.selenium.By;

import main.java.base.pom.WebPage;
import main.java.base.testCase.WebPageTestCase;


/**
 * @author Dan.Rusu
 */
public class ClickLink extends WebPageTestCase{


	@Override
	public void run(){
	    	
		String linkText = evalAttribute("linkText");
		String href = evalAttribute("href");
		
		if(href.isBlank()) {
			new WebPage(driver).click(By.linkText(linkText));
		}
		else {
			new WebPage(driver).click(By.cssSelector(
					format("[href=\"%s\"", linkText)));
		}
	}
	
	
	@Override
	public String getTestCaseScenario(){
	    
		return newScenario(
				"Find link by visible text or href and click it.",				
				"Test data: [linkText] [href]");
	}

}
