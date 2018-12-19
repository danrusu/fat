package main.projects.consignor.testCases;

import static main.base.Logger.log;

import java.io.File;
import java.util.List;

import static main.base.selenium.Driver.*;
import main.base.testCase.TestCase;
import main.projects.consignor.pages.TicketNewPage;

/**
 * @author Dan.Rusu
 *
 */
public class TicketNewScreenshots extends TestCase{


	@Override
	public void run(){
	    
	    TicketNewPage ticketNewPage = new TicketNewPage(getDriver());
	    	    
	    File[] screenshots = ticketNewPage.getSectionsScreenshots();
    
	    List.of(screenshots).forEach(
	            
	            screenshot -> log(screenshot.getAbsolutePath()));
	}
	
	

    @Override
    public String getTestCaseScenario() {
    	
        return newScenario("auto generated");
    }

}

