package main.java.projects.common.testCases;

import static main.java.utils.ThreadUtils.sleep;

import main.java.base.testCase.TestCase;


/**
 * Test case for waiting secondsTimeout[seconds].
 * @author Dan.Rusu
 */
public class Wait extends TestCase{


	@Override
	public void run(){
	    
	    float milliSecondsTimeout = evalFloatAttribute("timeout"); 
	    float secondsTimeoutInMilli = evalFloatAttribute("secondsTimeout") * 1000; 
	    float minutesTimeoutInMilli = evalFloatAttribute("minutesTimeout") * 60 * 1000;
	    
	    Float totalTimeout = milliSecondsTimeout + secondsTimeoutInMilli + minutesTimeoutInMilli;
	    
		sleep(totalTimeout.intValue());
	}
		
	
	@Override
	public String getTestCaseScenario(){
		return newScenario("Test case for waiting a specified timeout.",
				"Test data: [timeout], [secondsTimeout], [minutesTimeout]");
	}

}
