package main.java.projects.mock.testCases;

import static main.java.base.failures.ThrowablesWrapper.supplyAndMapThrowableToFailure;

import main.java.base.Assert;
import main.java.base.testCase.WebPageTestCase;

/**
 * Unit test for an Error test case failure.
 * @author Dan.Rusu
 *
 */
public class MockError extends WebPageTestCase{



    @Override
    public void run(){

        supplyAndMapThrowableToFailure(

                () -> {
                    Assert.fail("customError");
                    return null;
                },

                "Test case error!");
    }

    
    
    @Override
    public String getTestCaseScenario(){
        return "\nEmpy test case used just for unit testing an Exception test case failure!";
    }


}
