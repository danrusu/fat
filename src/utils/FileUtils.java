package utils;
import static base.Logger.log;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
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

import javax.imageio.ImageIO;

import base.Assert;
import base.failures.ThrowablesWrapper;


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
        try (
                FileOutputStream fos = new FileOutputStream(file);
                ObjectOutputStream oos = new ObjectOutputStream(fos)
                )
        {					
            oos.writeObject(object);
            return true;
        }

        catch (Exception e){
            log("Failed writing to file! \n" + e);
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



    public static String fileToString(String filePath, Charset charset){

        return ThrowablesWrapper.wrapThrowable(

                () -> new String(Files.readAllBytes(Paths.get(filePath)), charset));
    }



    public static String fileToString(String filePath){

        return ThrowablesWrapper.wrapThrowable(

                () -> new String(Files.readAllBytes(Paths.get(filePath))));
    }



    public static byte[] getBytes(String filePath){

        return ThrowablesWrapper.wrapThrowable(

                () -> Files.readAllBytes(Paths.get(filePath)));
    }



    public static void createDir(Path newFolderPath) {

        if (! Files.exists(newFolderPath)) {

            ThrowablesWrapper.wrapThrowable(

                    () -> Files.createDirectory(newFolderPath));          
        }

    }
    
    
    
    public static void createDirs(Path ...newFolderPath) {

        List.of(newFolderPath).forEach(FileUtils::createDir);

    }


    
    public static void imagesVerifyEquality(String expectedImage, String actualImage) {      

        ThrowablesWrapper.wrapThrowable(

                () -> {

                    // take buffer data from botm image files //
                    BufferedImage expectedImageBuffer = ImageIO.read(Paths.get(expectedImage).toFile());
                    DataBuffer expectedImageDataBuffer = expectedImageBuffer.getData().getDataBuffer();

                    BufferedImage actualImageBuffer = ImageIO.read(Paths.get(actualImage).toFile());
                    DataBuffer actualImageDataBuffer = actualImageBuffer.getData().getDataBuffer();


                    // compare data-buffer objects //
                    // 1. assert sizes           
                    Assert.equals(
                            "Images have the same size", 
                            expectedImageDataBuffer.getSize(), 
                            actualImageDataBuffer.getSize());


                    // 2. assert content
                    for(int i=0; i < expectedImageDataBuffer.getSize(); i++) { 
                        
                        Assert.equalsQuiet(
                                "",
                                expectedImageDataBuffer.getElem(i),
                                actualImageDataBuffer.getElem(i));                    
                    }
                    
                    return true;
                });
    }
    
}


  