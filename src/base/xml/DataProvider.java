package base.xml;

import static base.failures.ThrowablesWrapper.*;
import static base.Logger.*;
import static java.util.Collections.emptyList;
import static utils.StringUtils.*;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Predicate;
import java.util.stream.Collectors;



public class DataProvider {
    
    
    
    public static Predicate<String> lineIsNotEmpty = line -> line.isEmpty() == false;
    
    
    
    public static Predicate<String> lineIsNotCommented = line -> line.startsWith("//") == false; 
    


    public static List<List<String>> getData(
            Path filePath, 
            String separatorRegex, 
            int dataLength){

        return assignUnchecked(

                () -> Files.readAllLines(filePath).stream()
                
                    .filter(lineIsNotEmpty)
                
                    .filter(lineIsNotCommented)
                    
                    .map(line -> splitByAndTrim(line, separatorRegex, dataLength))
                    
                    .collect(Collectors.toList()),                    

                emptyList());           
    }



    public static int getDataLength(String localFilePath) {

        if(localFilePath != null) { 
            
            log("Data provider file: " + localFilePath);

            int dataLength = assignUnchecked(

                    countFileLines(localFilePath),

                    0);

            log("Data provider length: " + dataLength);

            return dataLength;

        }
        
        return 0;
    }



    private static Callable<Integer> countFileLines(String localFilePath) {

        return () -> Files.readAllLines(Paths.get( 
                    System.getProperty("user.dir"), 
                    (localFilePath)))

                .stream()

                .filter(lineIsNotEmpty)
                
                .filter(lineIsNotCommented)

                .collect(Collectors.counting())
                
                .intValue();
    }

}

