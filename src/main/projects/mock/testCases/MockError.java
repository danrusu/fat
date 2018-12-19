package main.projects.mock.testCases;

import static main.base.failures.ThrowablesWrapper.supplyAndMapThrowableToFailure;

import main.base.Assert;
import main.base.testCase.WebPageTestCase;

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
