package utils;

import static base.failures.ThrowablesWrapper.*;
import static base.Logger.*;
import static java.util.Collections.emptyList;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;



public class StringDataProvider {
        
    
    public static List<List<String>> readDataProviderFile(
            Path filePath, 
            String separatorRegex, 
            int dataLength){
                
        return wrapAssignment(
                
                () -> Files.readAllLines(filePath).stream()
                    .map(line -> StringUtils.splitBy(line, separatorRegex, dataLength))
                    .collect(Collectors.toList()),
                
                emptyList());           
    }

    
    
    public static int getDataLength(String localFilePath) {
        
        log("Data provider file: " + localFilePath);
        
        int dataLength = wrapAssignment(
                
                countFileLines(localFilePath),
                    
                1);
        
        log("Data provider length: " + dataLength);
        
        return dataLength;
    }



    private static Callable<Integer> countFileLines(String localFilePath) {
        
        return () -> Files.readAllLines(
                Paths.get( System.getProperty("user.dir"), (localFilePath)))
                
        .stream()
        
        .collect(Collectors.counting())
        .intValue();
    }

}

