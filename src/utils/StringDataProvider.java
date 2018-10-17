package utils;

import static base.failures.ThrowablesWrapper.*;
import static base.Logger.*;
import static java.util.Collections.emptyList;
import static utils.StringUtils.*;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;



public class StringDataProvider {


    public static List<List<String>> getDataWrapped(
            Path filePath, 
            String separatorRegex, 
            int dataLength){

        return unchekedAssignment(

                () -> Files.readAllLines(filePath).stream()
                    .map(line -> splitBy(line, separatorRegex, dataLength))
                    .collect(Collectors.toList()),

                emptyList());           
    }



    public static int getDataLengthWrapped(String localFilePath) {

        if(localFilePath != null) { 
            
            log("Data provider file: " + localFilePath);

            int dataLength = unchekedAssignment(

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

                .collect(Collectors.counting())
                .intValue();
    }

}

