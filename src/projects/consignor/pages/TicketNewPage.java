package projects.consignor.pages;

import java.io.File;

import org.openqa.selenium.WebDriver;

import base.Logger;



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
	            Logger.getLogDirPath().resolve("primaryNavigatorScreenShot.png")); 
	    
	    File secondaryNavigatorScreenShot = getSecondaryNavivatorScreenshot(
                Logger.getLogDirPath().resolve("secondaryNavigatorScreenShot.png"));
	    
	    File userDropdownScreenshot = getUserDropdownScreenshot(
                Logger.getLogDirPath().resolve("primaryNavigatorScreenShot.png"));
	    
	    return new File[]{
	            primaryNavigatorScreenShot, 
	            secondaryNavigatorScreenShot, 
	            userDropdownScreenshot};
	}
	
		
}
