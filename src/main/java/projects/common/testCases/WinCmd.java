package main.java.projects.common.testCases;

import static main.java.base.Logger.getFileTimeStamp;
import static main.java.base.Logger.logSplitByLines;

import java.io.IOException;
import java.util.stream.Collectors;

import main.java.base.failures.Failure;
import main.java.base.testCase.TestCase;
import main.java.utils.Exec;

/**
 * Runs command and saves the output.
 * @author Dan.Rusu
 *
 */
public class WinCmd extends TestCase{
    
	@SuppressWarnings("unused")
    private String output = "";

	

	@Override
	public void run(){
		
		String command = evalAttribute("command");
					
		Exec executor = new Exec();
		executor.setCommand( "cmd.exe" );
		
		executor.addArg( "/C" );
		
		String[] commandItems = command.split(" ");
		for (int i=0; i < commandItems.length; i++){
			executor.addArg( commandItems[i]);
		}
		
		
		try {
			executor.run( "WinCmd_"+ getFileTimeStamp() + ".out" );
			executor.join( 5 );
		}
		catch(Exception e){
			throw new Failure(e, "Error for running command: " + command);
		}
		
		finally{
			try {
				output = executor.getOutput(true).stream()
				        .collect(Collectors.joining(" "))
				        .trim();
				
			} catch (IOException ioEx) {
				logSplitByLines(""+ioEx);
			}
		}
		
		saveResults();
	}
	
	@Override
	public String getTestCaseScenario(){
		return "\nRun Windows command and saves the output."
				+ "\nTest data: command";
	}

}

