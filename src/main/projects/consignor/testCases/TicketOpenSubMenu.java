package main.projects.consignor.testCases;

import static main.base.Assert.isEqual;
import static main.base.failures.ThrowablesWrapper.supplyAndMapThrowableToFailure;
import static main.base.selenium.Driver.getDriver;

import main.base.testCase.TestCase;
import main.projects.consignor.pages.TicketPage;

/**
 * @author Dan.Rusu
 *
 */
public class TicketOpenSubMenu extends TestCase{


	@Override
	public void run(){
	    
	    TicketPage ticketPage = new TicketPage(getDriver());
	    
	    String subMenu = evalAttribute("subMenu");
	    
	    
	    supplyAndMapThrowableToFailure(	    	 
	            
	            () -> {
	                ticketPage.openSubMenu(subMenu);
	                return true;
	            },
	    
	            "Fail to open Ticket sub menu \"" + subMenu + "\"");
	    
	    	    
	    isEqual(
	            "Verify active sub menu.",
	            subMenu,
	            ticketPage.getActiveSubMenu());	    
	}	
	

    @Override
    public String getTestCaseScenario() {
    	
        return newScenario(
                "Open sub menu in the New (Ticket) page.",
                "Verify that the opened sub menu is active.",
                "Test data: subMenu",
                "Available sub menus: "
                        + "New, Inbox, Draft, Autoprint, Outbox, "
                        + "Batches, Print boxes, Contacts, Settings");
    }

}
