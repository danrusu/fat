package projects.mock.testCases;

import static base.failures.ThrowablesWrapper.unchecked;

import java.util.TreeMap;

import base.testCase.WebPageTestCase;

/**
 * Unit test for an RuntimeException test case failure.
 * @author Dan.Rusu
 *
 */
public class MockRuntimeException extends WebPageTestCase{



    @Override
    public void run(){


        unchecked(

                "Test case runtime exception!",

                () -> {
                    new TreeMap<Integer, String>().get(1).equals("Failure");
                    return null;
                });
    }



    @Override
    public String getTestCaseScenario(){
        return "\nEmpy test case used just for unit testing an RuntimeException test case failure!";
    }

}
