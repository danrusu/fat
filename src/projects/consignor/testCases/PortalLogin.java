package projects.consignor.testCases;

import base.Assert;
import base.Driver;
import base.testCase.TestCase;
import projects.consignor.pages.PortalPage;

/**
 * @author Dan.Rusu
 *
 */
public class PortalLogin extends TestCase{


    
	@Override
	public void run(){
	    
	    PortalPage portalPage = new PortalPage(Driver.driver);
	    
	    portalPage.setUsername(evalAttribute("username"));
	    portalPage.setPassword(evalAttribute("password"));
	    portalPage.login();
	    
	    Assert.equals(
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

