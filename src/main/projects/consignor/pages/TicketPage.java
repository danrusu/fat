package main.projects.consignor.pages;

import java.io.File;
import java.nio.file.Path;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static main.base.selenium.Driver.*;
import main.base.pom.ByUtils;
import main.base.pom.WebPage;



/**
 * @author Dan.Rusu
 *
 */
public class TicketPage extends WebPage{

    public static String url = "https://www.consignorportal.com/ticket";
    
    // primary menu: Home, History, New (Ticket)
    // ...not needed yet
    // TODO
    
    private By userDropdown = By.id("user-dropdown");
    private By userDropdownMenu = By.cssSelector("#user-dropdown + [class*=\"dropdown-menu\"]");
    
    
    
    // Page Sections Containers - user for element screenshots
    private By primaryNavigator = By.cssSelector("nav[class*=\"primary\"]");
    private By secondaryNavigator = By.cssSelector("nav[class*=\"secondary\"]");
    
    
    
    public File getPrimaryNavivatorScreenshot(Path outputFile) {
        
        return saveElementScreenshot(primaryNavigator, outputFile);
    }

    
    
    public File getSecondaryNavivatorScreenshot(Path outputFile) {
            
        return saveElementScreenshot(secondaryNavigator, outputFile);
    }


    public File getUserDropdownScreenshot(Path outputFile) {
        
        click(userDropdown);
        File screenshotFile =  saveElementScreenshot(userDropdownMenu, outputFile);
        click(userDropdown);
        
        return screenshotFile;         
    }


    
    // sub menu: New, Inbox, Draft, Autoprint, Outbox, Batches, Print boxes, Contacts, Settings 
    private By subMenuTemplate = By.xpath("//*[@id=\"js-nav-sub-menu\"]//a[text()=\"@linkText\"]/ancestor::li"); 
    private By activeSubMenu = By.xpath("//*[@id=\"js-nav-sub-menu\"]//li[contains(@class, \"active\")]/a");


    
	public TicketPage(WebDriver driver) {
	    super(driver);
	}

	
	
	public void openSubMenu(String subMenuName) {
	    
	    findUniqElement(ByUtils.replace(
	            subMenuTemplate, 
	            "@linkText", 
	            subMenuName)).click();
	    
	    waitForAjaxFinish();	    	   
	}
	
	
	
	public String getActiveSubMenu() {
	    
	    return getText(activeSubMenu);
	}
		
}
