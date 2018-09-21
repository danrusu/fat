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
	    
	    Assert.assertTrue(
	            "Check that no error message is present.",
	            portalPage.getErrorMessage().isEmpty());	    
	}
	
	

    @Override
    public String getTestCaseScenario() {
        // TODO Auto-generated method stub
        return null;
    }


}
