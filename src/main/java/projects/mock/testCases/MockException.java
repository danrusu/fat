package main.java.projects.mock.testCases;

import static main.java.base.failures.ThrowablesWrapper.supplyAndMapThrowableToFailure;
import static main.java.base.selenium.Driver.getDriver;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;

import main.java.base.testCase.WebPageTestCase;

/**
 * Unit test for an Exception test case failure.
 * @author Dan.Rusu
 *
 */
public class MockException extends WebPageTestCase{


    @Override
    public void run(){


        supplyAndMapThrowableToFailure(

                () -> {
                    getDriver().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
                    getDriver().findElement(By.cssSelector("unknown"));
                    return null;
                },

                "Test case exception!");
    }


    @Override
    public String getTestCaseScenario(){
        return "\nEmpy test case used just for unit testing an exception test failure!";
    }

}
