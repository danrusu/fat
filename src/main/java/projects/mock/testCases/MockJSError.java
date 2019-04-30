package main.java.projects.mock.testCases;

import static main.java.base.selenium.Driver.getDriver;

import main.java.base.testCase.WebPageTestCase;

/**
 * Test case that generates a javascript error.
 * @author Dan.Rusu
 *
 */
public class MockJSError extends WebPageTestCase{


    @Override
    public void run(){

        getDriver().get("http://danrusu.ro/jsError.html");
    }


    @Override
    public String getTestCaseScenario(){

        return newScenario("Test for catching javascript errors",
                "This test opens http://danrusu.ro/jsError.html page.");
    }

}
