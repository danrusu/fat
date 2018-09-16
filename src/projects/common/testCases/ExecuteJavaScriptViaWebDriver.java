package projects.common.testCases;

import static core.Logger.log;

import core.Driver;
import core.testCase.TestCase;

/**
 * ???
 * @author Dan.Rusu
 *
 */
public class ExecuteJavaScriptViaWebDriver extends TestCase{
	private String script="return document.getElementsByTagName(\"body\").item(0)";


	@Override
	public void run(){
		log(script);
		Driver.getJsExecutor().executeScript(script);
		log("finished");
	}
	
	
	
	@Override
	public String getTestCaseScenario(){
		return "\nExecute javascript code via Selenium driver";
	}


}
