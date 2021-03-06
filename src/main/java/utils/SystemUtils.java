package main.java.utils;

import static main.java.base.failures.ThrowablesWrapper.supplyUnchecked;

import java.util.Optional;

import main.java.base.JvmArgs;



public interface SystemUtils {

    
    
    // String JVM args
    public static String getPropertyOrDefaultIfNull(JvmArgs arg, String defaultValue) {
        
        return Optional.ofNullable(System.getProperty(arg.toString()))
                .orElse(defaultValue);
    }


    
    public static String getPropertyOrDefaultIfNullOrEmpty(JvmArgs arg, String defaultValue) {
        
        Optional<String> optionalProperty = Optional.ofNullable(System.getProperty(arg.toString()));
        
        return (optionalProperty.isPresent() &&  optionalProperty.get().isEmpty() == false) ? 
                
                optionalProperty.get() 
                
                : defaultValue; 
     }
           

    
    public static String getPropertyOrEmptyString(JvmArgs arg){
        
        return getPropertyOrDefaultIfNull(arg, "");
    }



    // Boolean JVM args
    public static boolean getBooleanPropertyOrDefault(JvmArgs arg, boolean defaultValue) {
        
        return getPropertyOrDefaultIfNull(arg, Boolean.toString(defaultValue))
                .equalsIgnoreCase("true");
    }
    
    

    // Integer JVM args
    public static int getIntPropertyOrDefault(JvmArgs arg, int defaultValue) {
        
        return supplyUnchecked(
                
                () -> Optional.ofNullable(Integer.parseInt(System.getProperty(arg.name())))
                    .orElse(defaultValue),
                 
                defaultValue);
    }
}
