package main.projects.common.testCases;

import static main.base.Logger.log;
import static main.base.selenium.Driver.getJsExecutor;

import main.base.testCase.TestCase;

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
		getJsExecutor().executeScript(script);
		log("finished");
	}
	
	
	
	@Override
	public String getTestCaseScenario(){
		return "\nExecute javascript code via Selenium driver";
	}


}
