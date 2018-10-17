package projects.mock.testCases;

import static base.failures.ThrowablesWrapper.unchecked;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;

import base.Driver;
import base.testCase.WebPageTestCase;

/**
 * Unit test for an Exception test case failure.
 * @author Dan.Rusu
 *
 */
public class MockException extends WebPageTestCase{

	
	
	@Override
	public void run(){
		
	    
       unchecked(

                "Test case exception!",

                () -> {
                    Driver.driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
                    Driver.driver.findElement(By.cssSelector("unknown"));
                    return null;
                });
	}
	
	
	
	@Override
	public String getTestCaseScenario(){
		return "\nEmpy test case used just for unit testing an exception test failure!";
	}


}
