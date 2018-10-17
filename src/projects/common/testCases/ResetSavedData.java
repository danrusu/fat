package projects.common.testCases;

import base.Assert;
import base.testCase.TestCase;
import base.xml.XmlDynamicData;

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


