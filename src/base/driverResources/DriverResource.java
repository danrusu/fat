package base.driverResources;


import static base.Logger.log;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


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
	public static boolean exportDriverFromJar(String driverSourceName,
			String driverDestinationName){
		boolean rc = true;
		
		// create webDrivers folder if it does not exist
		try {
			Files.createDirectory(Paths.get(
					System.getProperty("user.dir") 
					+ "/webDrivers"));
		} catch (FileAlreadyExistsException e){
			// webDrivers folder already exists 
		}catch(Exception e){
			log("Error: Coud not create webDrivers folder !\n" + e);
			return false;
		}
		
		// driver destination path: user.dir/webDrivers/driverName.exe
		Path resourcePathDest = Paths.get(System.getProperty("user.dir") 
				+ "/webDrivers/" + driverDestinationName);

		// get resource to stream
		InputStream resource = DriverResource.class.getResourceAsStream(driverSourceName);			

		if ( false == Files.exists( resourcePathDest ) ){
			try {
				Files.copy(resource,
						resourcePathDest, 					
						StandardCopyOption.REPLACE_EXISTING);

			} catch (IOException e) {
				log("" + e);
				log("Could not copy driver resource from jar!");
				rc = false;

			} finally {
				// close resource 
				if (resource!=null){
					try {
						resource.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}			
					resource = null;
				}
			}
		}
		return rc;		
	}

}
