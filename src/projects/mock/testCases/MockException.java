package projects.mock.testCases;

import static core.failures.ThrowablesWrapper.wrapThrowable;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;

import core.Driver;
import core.testCase.WebPageTestCase;

/**
 * Unit test for an Exception test case failure.
 * @author Dan.Rusu
 *
 */
public class MockException extends WebPageTestCase{

	
	
	@Override
	public void run(){
		
	    
       wrapThrowable(

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
