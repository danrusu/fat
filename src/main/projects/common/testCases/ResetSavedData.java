package main.projects.common.testCases;

import main.base.Assert;
import main.base.testCase.TestCase;
import main.base.xml.XmlDynamicData;

/**
 * @author Dan.Rusu
 *
 */
public class ResetSavedData extends TestCase{



	@Override
	public void run(){
		
	    XmlDynamicData.resetSavedData();
	    
	    
		Assert.isTrue(
		        "Saved data was not reset!",
		        XmlDynamicData.getSavedData().isEmpty());
	}

	
	
	@Override
	public String getTestCaseScenario(){
		return newScenario("Reset saved data.");
	}

}


