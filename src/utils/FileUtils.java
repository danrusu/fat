package utils;

import static base.Logger.log;
import static base.Logger.logSplitByLines;
import static base.failures.ThrowablesWrapper.wrapThrowable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


/**
 * File IO helper.
 * 
 * @author Dan Rusu
 *
 */
public interface FileUtils {


    /**
     * Write object to file.
     * 
     * @param object - object to be written.
     * @param file - output file.
     * @return - true if success of false if failure
     */
    public static boolean write(Object object, File file){
        
        try (FileOutputStream fos = new FileOutputStream(file);
                ObjectOutputStream oos = new ObjectOutputStream(fos)){
            
            oos.writeObject(object);
            return true;
        }

        catch (Exception e){
            
            logSplitByLines("Failed writing to file! \n" + e);
            return false;
        }
    }



    /**
     * Reads object from file.
     * 
     * @param file - input file
     * @return an Object if read succeeded or null otherwise
     */
    public static Object read(File file){
        Object object = null;
        try( 
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis) 
                )
        {		
            object = ois.readObject();	
        }
        catch (Exception e){		
            log("Failed reading from file \n" + e);
        }

        return object;
    }

    
    
    /*	
     *  Usage example:

Map<String, String> map = new HashMap<>();
		map.put("1","aaa");
		map.put("2","bbb");
		map.put("3","ccc");


		File f = new File(System.getProperty("user.dir") + "/x.data");

		Object objectMap = map;
		FilesIO.write(objectMap, f);

		Object object = FilesIO.read(new File(System.getProperty("user.dir") + "/x.data"));

		// display results
		((Map<String, String>)object).entrySet().forEach(System.out::println);
     */



    public static String fileToStringWrapped(String filePath, Charset charset){

        return wrapThrowable(

                () -> new String(Files.readAllBytes(Paths.get(filePath)), charset));
    }



    public static String fileToStringWrapped(String filePath){

        return wrapThrowable(

                () -> new String(Files.readAllBytes(Paths.get(filePath))));
    }



    public static byte[] getBytesWrapped(String filePath){

        return wrapThrowable(() -> Files.readAllBytes(Paths.get(filePath)));
    }



    public static void createDirWrapped(Path newFolderPath) {

        if (! Files.exists(newFolderPath)) {

           wrapThrowable(() -> Files.createDirectory(newFolderPath));          
        }

    }
    
    
    
    public static void createDirsWrapped(Path ...newFolderPath) {

        List.of(newFolderPath).forEach(FileUtils::createDirWrapped);

    }
    
}

  