package projects.consignor.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import base.pom.ByUtils;
import base.pom.WebPage;



/**
 * @author Dan.Rusu
 *
 */
public class TicketPage extends WebPage{

    public static String url = "https://www.consignorportal.com/ticket";
    

	// main menu: Home, History, New (Ticket)
    // ...not needed yet
    // TODO

    
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
