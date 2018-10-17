package utils;

import static base.failures.ThrowablesWrapper.unchekedAssignment;

import java.util.Optional;

import base.JvmArgs;



public interface SystemUtils {

    
    
    // String JVM args
    public static String getPropertyOrDefaultIfNull(JvmArgs arg, String defaultValue) {
        
        return Optional.ofNullable(System.getProperty(arg.name()))
                .orElse(defaultValue);
    }


    
    public static String getPropertyOrDefaultIfNullOrEmpty(JvmArgs arg, String defaultValue) {
        
        Optional<String> optionalProperty = Optional.ofNullable(System.getProperty(arg.name()));
        
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
        
        return unchekedAssignment(
                
                () -> Optional.ofNullable(Integer.parseInt(System.getProperty(arg.name())))
                    .orElse(defaultValue),
                 
                defaultValue);
    }
}
