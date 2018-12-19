package main.projects.consignor.pages;

import static main.base.Logger.getLogDirPath;

import java.io.File;

import org.openqa.selenium.WebDriver;



/**
 * @author Dan.Rusu
 *
 */
public class TicketNewPage extends TicketPage{

    
    public static String url = "https://www.consignorportal.com/ticket/Production";
    

    
	public TicketNewPage(WebDriver driver) {
	    super(driver);
	}

	

	public File[] getSectionsScreenshots() {
	    
	    File primaryNavigatorScreenShot = getPrimaryNavivatorScreenshot(
	            getLogDirPath().resolve("primaryNavigatorScreenShot.png")); 
	    
	    File secondaryNavigatorScreenShot = getSecondaryNavivatorScreenshot(
                getLogDirPath().resolve("secondaryNavigatorScreenShot.png"));
	    
	    File userDropdownScreenshot = getUserDropdownScreenshot(
                getLogDirPath().resolve("primaryNavigatorScreenShot.png"));
	    
	    return new File[]{
	            primaryNavigatorScreenShot, 
	            secondaryNavigatorScreenShot, 
	            userDropdownScreenshot};
	}
	
		
}
