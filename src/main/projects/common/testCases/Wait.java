package main.projects.common.testCases;

import main.base.testCase.TestCase;
import main.utils.ThreadUtils;

/**
 * Test case for waiting timeout[seconds].
 * @author Dan.Rusu
 *
 */
public class Wait extends TestCase{



	@Override
	public void run(){
	    
		ThreadUtils.sleep(
		        Long.parseLong(
		                (evalIntAttribute("timeout")*1000)+""));
	}
	
	
	
	@Override
	public String getTestCaseScenario(){
		return "\nTest case for waiting a pecified timeout."
				+ "\nTest data: timeout(seconds)";
	}

}