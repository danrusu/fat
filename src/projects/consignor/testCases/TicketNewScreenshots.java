package projects.consignor.testCases;

import static base.Logger.log;

import java.io.File;
import java.util.List;

import base.Driver;
import base.testCase.TestCase;
import projects.consignor.pages.TicketNewPage;

/**
 * @author Dan.Rusu
 *
 */
public class TicketNewScreenshots extends TestCase{


	@Override
	public void run(){
	    
	    TicketNewPage ticketNewPage = new TicketNewPage(Driver.driver);
	    
	    
	    File[] screenshots = ticketNewPage.getSectionsScreenshots();
    
	    List.of(screenshots).forEach(
	            
	            screenshot -> log(screenshot.getAbsolutePath()));
	}
	
	

    @Override
    public String getTestCaseScenario() {
        return newScenario(
                "");
    }

}

