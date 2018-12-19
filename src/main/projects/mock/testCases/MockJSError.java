package main.projects.mock.testCases;

import main.base.testCase.WebPageTestCase;

/**
 * Test case that generates a javascript error.
 * @author Dan.Rusu
 *
 */
public class MockJSError extends WebPageTestCase{


    @Override
    public void run(){

        //getDriver().get("not developed");
    }


    @Override
    public String getTestCaseScenario(){

        return newScenario("Test for catching javascript errors",
                "This test opens http://danrusu.ro/jsError.html page.");
    }

}
