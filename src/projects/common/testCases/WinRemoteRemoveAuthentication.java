package projects.common.testCases;
import static core.Logger.logLines;

import java.io.IOException;
import java.util.Vector;

import core.Logger;
import core.failures.Failure;
import core.testCase.TestCase;
import utils.Exec;
import utils.ThreadUtils;

/**
 * Remove authentication to another Windows computer via network (net use).
 * @author Dan.Rusu
 *
 */
public class WinRemoteRemoveAuthentication extends TestCase{

	private String output="";
	private final String NET_USE_REMOVE_SUCCESS_MESSAGE = "was deleted successfully.";
	private final String NETWORK_CONNECTION_NOT_FOUND="The network connection could not be found.";



	@Override
	public void run(){

		String remotePath = evalAttribute("remotePath");

		Exec executor = new Exec();

		executor.setCommand( "cmd.exe" );
		executor.addArg( "/C" );

		executor.addArg( "net use" );

		executor.addArg( remotePath );

		// remove authentication
		executor.addArg( "/delete" );



		try {
			executor.run( "WinRemoteRemoveAuthentication_"+ Logger.getFileTimeStamp() + ".out" );
			executor.join( 5 );
		}
		catch(Exception e){
			throw new Failure("WinRemoteRemoveAuthentication error:", e);
		}

		finally{
			try {
				Vector<String> out = executor.getOutput(true);

				out.stream().forEach(
						x -> output = output.concat(x+" "));

			} catch (IOException ioEx) {
				logLines(""+ioEx);
			}
			output = output.trim();			
		}

		// verify command output
		if (!(output.contains(remotePath + " " + NET_USE_REMOVE_SUCCESS_MESSAGE)
				// do not fail if there is nothing to be removed
				|| output.contains(NETWORK_CONNECTION_NOT_FOUND))){
			throw new Failure("WinRemoteRemoveAuthentication error: " + output);
		}
		
		ThreadUtils.sleepQuiet(2000);
	}

	@Override
	public String getTestCaseScenario(){
		return "\nRemove authentication to another Windows computer via network (net use)."
				+ "\nTest data: remotePath";
	}

}
