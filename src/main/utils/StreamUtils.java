package main.utils;

import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.function.BinaryOperator;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import main.base.failures.Failure;

public interface StreamUtils {

    
    
    public static Collector<Entry<String, String>, ?, TreeMap<String, String>> 
    
    
        treeMapDefaultCollector() {
        
            return Collectors.toMap(
                
                Entry::getKey,
                
                Entry::getValue,
                   
                mergeStringFunction,
        
                TreeMap<String, String>::new);
    }
    
    

    BinaryOperator<String> mergeStringFunction = (v1, v2) -> { 
        
        throw new Failure(String.format("Duplicate key for: %s : %s", v1, v2));        
    };

}
