package projects.consignor.testCases;

import base.Assert;
import base.Driver;
import base.failures.ThrowablesWrapper;
import base.testCase.TestCase;
import projects.consignor.pages.TicketPage;

/**
 * @author Dan.Rusu
 *
 */
public class TicketOpenSubMenu extends TestCase{


	@Override
	public void run(){
	    
	    TicketPage ticketPage = new TicketPage(Driver.driver);
	    
	    String subMenu = evalAttribute("subMenu");
	    
	    
	    ThrowablesWrapper.unchecked(
	            
	            "Fail to open Ticket sub menu \"" + subMenu + "\"", 
	            
	            () -> {
	                ticketPage.openSubMenu(subMenu);
	                return true;
	            });
	    
	    	    
	    Assert.equals(
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

