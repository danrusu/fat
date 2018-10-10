package base.driverResources;


import static base.Logger.log;
import static base.Logger.logSplitByLines;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * Class for extracting drivers from jar to user.dir.
 * 
 * @author Dan.Rusu
 *
 */
public class DriverResource {

	/**
	 *  Copy driver from jar to user.dir
	 * 
	 * @param driverSourceName - driver name within the jar
	 * @param driverDestinationName - driver name form user.dir; this will be used by application
	 * @return true if driver resource was copied or false otherwise.
	 */
	public static boolean exportDriverFromJar(
	        String driverSourceName,
			String driverDestinationName){
	    
		boolean isDriverExported = true;

		// create webDrivers folder if it does not exist
		try {
		    
			Files.createDirectory(
			        Paths.get(System.getProperty("user.dir"), "webDrivers"));
			        
		} catch (FileAlreadyExistsException e){
		    
			// webDrivers folder already exists
		    
		}catch(Exception e){
		    
			log("Error: Coud not create webDrivers folder !\n" + e);
			return false;
		}
		
		// user.dir/webDrivers/driverName.exe
		Path driverDestinationPath = Paths.get(
		        System.getProperty("user.dir"),
		        "webDrivers", 
		        driverDestinationName);

		// get resource to stream
		InputStream resource = DriverResource.class.getResourceAsStream(driverSourceName);			

		if (false == Files.exists(driverDestinationPath)){
		    
			try {
			    
				Files.copy(resource, driverDestinationPath, REPLACE_EXISTING);

			} catch (IOException e) {
			    
				logSplitByLines("Could not copy driver resource from jar! \n" + e);
				isDriverExported = false;

			} finally {
			    
				// close resource 
				if (resource!=null){
				    
					try {
						resource.close();
					} catch (IOException e) {
						e.printStackTrace();
					}			
					resource = null;
				}
			}
		}
		return isDriverExported;		
	}

}

