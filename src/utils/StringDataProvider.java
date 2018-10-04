package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class StringDataProvider {
        
    
    public static List<List<String>> readDataProviderFile(
            Path filePath, 
            String separatorRegex, 
            int dataLength) throws IOException {
                
        return Files.readAllLines(filePath).stream()
            .map(line -> StringUtils.splitBy(line, separatorRegex, dataLength))
            .collect(Collectors.toList());           
    }

}

