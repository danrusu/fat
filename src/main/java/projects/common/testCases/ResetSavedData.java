package main.java.projects.common.testCases;

import main.java.base.Assert;
import main.java.base.testCase.TestCase;
import main.java.base.xmlSuite.XmlDynamicData;

/**
 * @author Dan.Rusu
 *
 */
public class ResetSavedData extends TestCase{



	@Override
	public void run(){
		
	    XmlDynamicData.resetSavedData();
	    
	    
		Assert.isEqual(
		        true,
		        XmlDynamicData.getSavedData().isEmpty(),
		        "Saved data was not reset!");
	}

	
	
	@Override
	public String getTestCaseScenario(){
		return newScenario("Reset saved data.");
	}

}


