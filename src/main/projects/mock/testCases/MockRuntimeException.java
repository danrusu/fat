package main.projects.mock.testCases;

import static main.base.failures.ThrowablesWrapper.supplyAndMapThrowableToFailure;

import java.util.TreeMap;

import main.base.testCase.WebPageTestCase;

/**
 * Unit test for an RuntimeException test case failure.
 * @author Dan.Rusu
 *
 */
public class MockRuntimeException extends WebPageTestCase{



    @Override
    public void run(){


        supplyAndMapThrowableToFailure(

                () -> {
                    new TreeMap<Integer, String>().get(1).equals("Failure");
                    return null;
                },

                "Test case runtime exception!");
    }



    @Override
    public String getTestCaseScenario(){
        return "\nEmpy test case used just for unit testing an RuntimeException test case failure!";
    }

}
