package projects.mock.testCases;

import static base.failures.ThrowablesWrapper.wrapThrowable;

import base.Assert;
import base.testCase.WebPageTestCase;

/**
 * Unit test for an Error test case failure.
 * @author Dan.Rusu
 *
 */
public class MockError extends WebPageTestCase{



    @Override
    public void run(){

       wrapThrowable(

                "Test case error!",

                () -> {
                    Assert.fail("customError");
                    return null;
                });
    }

    
    
    @Override
    public String getTestCaseScenario(){
        return "\nEmpy test case used just for unit testing an Exception test case failure!";
    }


}
