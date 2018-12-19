package main.utils;

import static main.base.failures.ThrowablesWrapper.supplyAndMapThrowableToFailure;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Supplier;

import javax.imageio.ImageIO;

import main.base.Assert;
import main.base.failures.Failure;


/**
 * File IO helper.
 * 
 * @author Dan Rusu
 *
 */
public interface FileUtils {
    
    
    static Path getRelativePath(String... morePaths) {

        return Paths.get(
                System.getProperty("user.dir"),
                morePaths);
    }
    
    
    static <T> void writeObject(Path outFilePath, T objectToWrite) {
        
        try( 
                FileOutputStream fos = new FileOutputStream(outFilePath.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos);                
                ){
            oos.writeObject(objectToWrite);
        }
        catch (IOException e){        
            throw new Failure(e, "Could not write to file " + outFilePath);
        }
        
        
    }
    
    
    
    @SuppressWarnings("unchecked")
    static <T> T readObject(Path inFilePath, Supplier<T> readObjectFactory) {        
        
        try( 
                FileInputStream fis = new FileInputStream(inFilePath.toFile());
                ObjectInputStream ois = new ObjectInputStream(fis);
                
                ){       
            
            return (T) ois.readObject(); 
        }
        
        catch (IOException | ClassNotFoundException e){        
            throw new Failure(e, "Could not read from file " + inFilePath);
        }
    }


    /*	
     *  Usage example:

        Map<String, String> map = new HashMap<>();
		map.put("1","aaa");
		map.put("2","bbb");
		map.put("3","ccc");


		final Path outFilePath = getLocalPath("Resources/x.map");
		
	    FileUtils.write(map, outFilePath);

		Map<String, String> readMap = FilesUtils.read(outFilePath, new TreeMap<String, String>());

		// display results
		System.out.println(readMap);
     */


    
    static String fileToString(String filePath, Charset charset){
      
        return supplyAndMapThrowableToFailure(

                () -> new String(Files.readAllBytes(Paths.get(filePath)), charset));
    }



    static String fileToString(String filePath){

        return supplyAndMapThrowableToFailure(

                () -> new String(Files.readAllBytes(Paths.get(filePath))));
    }



    static byte[] getBytes(String filePath){

        return supplyAndMapThrowableToFailure(

                () -> Files.readAllBytes(Paths.get(filePath)));
    }



    static void createDir(Path newFolderPath) {

        if (! Files.exists(newFolderPath)) {

            supplyAndMapThrowableToFailure(

                    () -> Files.createDirectory(newFolderPath));          
        }

    }
    
    
    
    static void createDirs(Path ...newFolderPath) {

        List.of(newFolderPath).forEach(FileUtils::createDir);

    }
    
    
    static void copyFile(File source, File destination) {
        
        supplyAndMapThrowableToFailure(
                
                () -> {
                    org.apache.commons.io.FileUtils.copyFile(source, destination);
                    return true;
                },
                
                "Could not copy files");
    }


    
    static void imagesVerifyEquality(String expectedImage, String actualImage) {      

        supplyAndMapThrowableToFailure(

                () -> {

                    // take buffer data from botm image files //
                    BufferedImage expectedImageBuffer = ImageIO.read(Paths.get(expectedImage).toFile());
                    DataBuffer expectedImageDataBuffer = expectedImageBuffer.getData().getDataBuffer();

                    BufferedImage actualImageBuffer = ImageIO.read(Paths.get(actualImage).toFile());
                    DataBuffer actualImageDataBuffer = actualImageBuffer.getData().getDataBuffer();


                    // compare data-buffer objects //
                    // 1. assert sizes           
                    Assert.isEqual(
                            "Images have the same size", 
                            expectedImageDataBuffer.getSize(), 
                            actualImageDataBuffer.getSize());


                    // 2. assert content
                    for(int i=0; i < expectedImageDataBuffer.getSize(); i++) { 
                        
                        Assert.isEqualsQuiet(
                                "",
                                expectedImageDataBuffer.getElem(i),
                                actualImageDataBuffer.getElem(i));                    
                    }
                    
                    return true;
                });
    }


    
    static boolean exists(String file) {
        
        return Files.exists(Paths.get(file));
    }

    

    static void delete(String file) {
        
        supplyAndMapThrowableToFailure( 
                () -> { 
                    Files.delete(Paths.get(file));
                    return true;
                });        
    }


    static String restFilePath(String bodyFile) {
        
        return Paths.get(
                System.getProperty("user.dir"),
                "resources",
                "rest", 
                bodyFile).toString();
    }



    
}

