package utils;

import static base.failures.ThrowablesWrapper.wrapThrowable;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.nio.file.Paths;
import java.util.concurrent.Callable;
import java.util.stream.IntStream;

import javax.imageio.ImageIO;

import base.Assert;



public class ImageUtils {

    
    
    
    public static void imagesVerifyEqualityWrapped(String expectedImage, String actualImage) {      
    
        wrapThrowable(imagesVerifyEquality(expectedImage, actualImage));
    }

    
    
    
    public static Callable<Boolean> imagesVerifyEquality(String expectedImage, String actualImage) {
        
        return () -> {
    
            // take buffer data from both image files //
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
            IntStream.range(0, expectedImageDataBuffer.getSize()).forEach(
                    
                    i -> Assert.equalsQuiet(
                        "",
                        expectedImageDataBuffer.getElem(i),
                        actualImageDataBuffer.getElem(i)));                    
            
            return true;
        };
    }

}

