package main.base.xml;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.toList;
import static main.base.Logger.log;
import static main.base.failures.ThrowablesWrapper.supplyUnchecked;
import static main.utils.FileUtils.getRelativePath;
import static main.utils.StringUtils.splitBy;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Predicate;

import main.utils.FileUtils;


public class DataProvider {

    
    public static Predicate<String> lineIsNotEmpty = line -> false == line.trim().isEmpty();
    
    
    public static Predicate<String> lineIsNotCommented = line -> 
    	false == line.trim().startsWith("//"); 

        
    public static List<List<String>> getDataFromProvider(
            Path filePath, 
            String separatorRegex, 
            int dataLength){

        List<String> lines = FileUtils.readAllLines(filePath);
    	
        return lines.stream()
                
                    .filter(lineIsNotEmpty.and(lineIsNotCommented))
                    
                    .map(line -> splitBy(line, separatorRegex, dataLength))
                    
                    .collect(toList());
    }

    
    public static int getDataLength(String localFilePath) {

        if(localFilePath != null) { 
            
            log("Data provider file: " + localFilePath);

            int dataLength = supplyUnchecked(countFileLines(localFilePath), 0);

            log("Data provider length: " + dataLength);

            return dataLength;
        }
        
        return 0;
    }


    private static Callable<Integer> countFileLines(String localFilePath) {
    	
    	List<String> lines = FileUtils.readAllLines(getRelativePath(localFilePath));

        return () -> lines.stream()
                
                .filter(lineIsNotEmpty.and(lineIsNotCommented))
                
                .collect(counting())
                
                .intValue();
    }

    
    public static Path getDataProviderPath(String ...more) {
    
    	return Paths.get(
    			getRelativePath("resources", "dataProviders").toString(),
    			more);
    }
    
}
