package main.projects.consignor.testCases;

import static main.base.Assert.isEqual;
import static main.base.selenium.Driver.getDriver;

import main.base.testCase.TestCase;
import main.projects.consignor.pages.PortalPage;

/**
 * @author Dan.Rusu
 *
 */
public class PortalLogin extends TestCase{


	@Override
	public void run(){
	    
	    PortalPage portalPage = new PortalPage(getDriver());
	    
	    portalPage.setUsername(evalAttribute("username"));
	    portalPage.setPassword(evalAttribute("password"));
	    portalPage.login();
	    
	    isEqual(
	    		"Check that no error message is present.",
	            "",
	            portalPage.getErrorMessage());
	}
		

    @Override
    public String getTestCaseScenario() {

        return newScenario("Login to Consignor portal.",
                "Test data: username, password");
    }

}
