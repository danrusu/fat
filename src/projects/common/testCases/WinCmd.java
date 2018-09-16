package projects.common.testCases;
import static core.Logger.*;
import java.io.IOException;
import java.util.Vector;

import core.Logger;
import core.failures.TestCaseFailure;
import core.testCase.TestCase;
import utils.Exec;

/**
 * Runs command and saves the output.
 * @author Dan.Rusu
 *
 */
public class WinCmd extends TestCase{
	private String output="";

	

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
			executor.run( "WinCmd_"+ Logger.getFileTimeStamp() + ".out" );
			executor.join( 5 );
		}
		catch(Exception e){
			throw new TestCaseFailure("Error for running command: " + command, e);
		}
		
		finally{
			try {
				Vector<String> out = executor.getOutput(true);
						
				out.stream().forEach(
						x -> output = output.concat(x+" "));
				
			} catch (IOException ioEx) {
				logLines(""+ioEx);
			}
		}
		
		output = output.trim();
		
		saveResults();
	}
	
	@Override
	public String getTestCaseScenario(){
		return "\nRun Windows command and saves the output."
				+ "\nTest data: command";
	}


}
