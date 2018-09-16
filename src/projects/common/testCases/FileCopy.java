package projects.common.testCases;
import static core.Logger.log;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import core.failures.ThrowablesWrapper;
import core.testCase.TestCase;

/**
 * Copy file via Java.
 * @author Dan.Rusu
 *
 */
public class FileCopy extends TestCase{


	
	@Override
	public void run(){
		
	
		Path sourceFilePath =  Paths.get(evalAttribute("source"));
		Path destFilePath = Paths.get(evalAttribute("destination"));
		
		String message = "Copy file from "+ sourceFilePath 
		        + " to "+ destFilePath;
		
		log(message);
		
		ThrowablesWrapper.wrapThrowable(
		        
		        "FAILED " + message,
		        
		        () -> Files.copy(
		                sourceFilePath, 
		                destFilePath, 
		                StandardCopyOption.REPLACE_EXISTING));
	}

	
	
	@Override
	public String getTestCaseScenario(){
		return "\nCopy file from source to destination."
				+ "\nTest data: source, destination";
	}


}
