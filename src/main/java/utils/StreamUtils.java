package main.java.utils;

import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.function.BinaryOperator;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import main.java.base.failures.Failure;

public interface StreamUtils {
   
    
    static Collector<Entry<String, String>, ?, TreeMap<String, String>> treeMapDefaultCollector() {
        
            return Collectors.toMap(
                
                Entry::getKey,
                
                Entry::getValue,
                   
                mergeStringFunction,
        
                TreeMap::new);
    }
    
    
    static Collector<Entry<String, String>, ?, TreeMap<String, String>> treeMapDefaultCollectorNoThrow() {
    
        return Collectors.toMap(
            
            Entry::getKey,
            
            Entry::getValue,
               
            mergeByKeepingLastStringFunction,
    
            TreeMap::new);
}
    
    

    static BinaryOperator<String> mergeStringFunction = (v1, v2) -> { 
        
        throw new Failure(String.format("Duplicate key for: %s : %s", v1, v2));        
    };
    
    
    static BinaryOperator<String> mergeByKeepingLastStringFunction = (v1, v2) -> { 
          
        return v2;
    };

}
