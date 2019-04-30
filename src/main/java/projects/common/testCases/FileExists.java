package main.java.projects.common.testCases;

import java.nio.file.Files;
import java.nio.file.Paths;

import main.java.base.Assert;
import main.java.base.testCase.TestCase;



/**
 * @author Dan.Rusu
 *
 */
public class FileExists extends TestCase{


	
	@Override
	public void run(){

		Assert.isEqual(    
		        true,
		        Files.exists(Paths.get(evalAttribute("source"))),
		        "File does not exist!");
	}

	
	
	@Override
	public String getTestCaseScenario(){
		
		return newScenario("Fails if file does not exist.",
			"Test data: file");
	}

}

