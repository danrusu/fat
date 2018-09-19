package projects.mock.testCases;

import static core.failures.ThrowablesWrapper.wrapThrowable;

import java.util.TreeMap;

import core.testCase.WebPageTestCase;

/**
 * Unit test for an RuntimeException test case failure.
 * @author Dan.Rusu
 *
 */
public class MockRuntimeException extends WebPageTestCase{



    @Override
    public void run(){


        wrapThrowable(

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
