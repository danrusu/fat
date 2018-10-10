package projects.common.testCases;
import static base.Logger.logSplitByLines;

import java.io.IOException;
import java.util.Vector;

import base.Logger;
import base.failures.Failure;
import base.testCase.TestCase;
import utils.Exec;
import utils.ThreadUtils;

/**
 * Add authentication to another Windows computer via network (net use).
 * @author Dan.Rusu
 *
 */
public class WinRemoteAddAuthentication extends TestCase{


	private String output="";
	private final String NET_USE_ADD_SUCCESS_MESSAGE = "The command completed successfully.";


	@Override
	public void run(){

		String remotePath = evalAttribute("remotePath");
		String user = evalAttribute("user");
		String password = evalAttribute("password");

		Exec executor = new Exec();

		executor.setCommand( "cmd.exe" );
		executor.addArg( "/C" );

		executor.addArg( "net use" );

		executor.addArg( remotePath );

		// add authentication
		executor.addArg( password );
		executor.addArg( "/user:" + user );

		// net use hangs sometimes for unknown reason
		// try to authenticate for 5 times
		for (int i=0; i<5; i++){
			try {
				executor.run( "WinRemoteAddAuthentication_"+ Logger.getFileTimeStamp() + ".out" );
				executor.join( 15 );
			}
			catch(Exception e){
				//throw new TestCaseFailure("WinRemoteAddAuthentication error:", e);
				continue;
			}

			finally{
				try {
					Vector<String> out = executor.getOutput(true);

					out.stream().forEach(
							x -> output = output.concat(x+" "));

				} catch (IOException ioEx) {
					logSplitByLines(""+ioEx);
				}
				output = output.trim();			
			}

			if ((output.contains(NET_USE_ADD_SUCCESS_MESSAGE))){
				break;	
			}
			ThreadUtils.sleep(2000);
		}//end retries

		// verify last command output
		if (!(output.contains(NET_USE_ADD_SUCCESS_MESSAGE) )){
			throw new Failure("WinRemoteAddAuthentication error: " + output);
		}

	}

	@Override
	public String getTestCaseScenario(){
		return "\nAdd authentication to another Windows computer via network (net use)."
				+ "\nTest data: remotePath, user, password";
	}


}
