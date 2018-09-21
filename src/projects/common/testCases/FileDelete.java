package projects.common.testCases;

import static base.Logger.log;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import base.failures.Failure;
import base.testCase.TestCase;

/**
 * Delete File from disk.
 * @author Dan.Rusu
 *
 */
public class FileDelete extends TestCase{

	

	@Override
	public void run(){
		
		
		String source = evalAttribute("source");
		
		Path sourceFilePath =  Paths.get(source);
		
		log("Delete file: " + sourceFilePath);
		
		try{		
			Files.delete(sourceFilePath);
			log("Delete file - succeeded.");
		}
		catch(IOException e){
			throw new Failure("Could not delete file: " + source, e);
		}
							
			
		
	}
	
	@Override
	public String getTestCaseScenario(){
		return "\nDelete file from disk."
				+ "\nTest data: source.";
	}


}
