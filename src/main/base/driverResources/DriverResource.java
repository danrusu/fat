package main.base.driverResources;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static main.utils.FileUtils.createDir;
import static main.utils.FileUtils.getRelativePath;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import main.base.failures.Failure;
import main.base.selenium.Browser;


/**
 * Class for extracting drivers from jar to user.dir/webDrivers.
 * 
 * @author Dan.Rusu
 *
 */
public class DriverResource {
    
    
    private final static String WEB_DRIVERS_DIR = "webDrivers";
    private final static Path WEB_DRIVERS_PATH = getRelativePath(WEB_DRIVERS_DIR);
    

	public static void exportDriverFromJar(Browser browser){	    
		
	    createDir(WEB_DRIVERS_PATH);
		
	    String driverExecutableResource = browser.getDriverExecutableName();
	    
	    // driver destination path: user.dir/webDrivers/driverName.exe	    
		Path driverDestinationPath = WEB_DRIVERS_PATH.resolve(driverExecutableResource);					
		
		if (Files.notExists(driverDestinationPath)) {
		    	
		    try (InputStream resource = DriverResource.class.getResourceAsStream(
		            driverExecutableResource);){
		        		    
		        if (resource == null) {
		            
		            throw new Failure("Driver resource is null!");
		        }
		        
		        else {
		            Files.copy(resource,
                        driverDestinationPath,                  
                        REPLACE_EXISTING);
		        }

			} catch (IOException e) {
			    
				throw new Failure(e, "Could not get/copy driver resource from jar!");
			}
		}
	}

}
