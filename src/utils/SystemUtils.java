package utils;

import java.util.Optional;

import core.JvmArgs;
import core.failures.ThrowablesWrapper;



public final class SystemUtils {

    
    
    private SystemUtils(){
        throw new AssertionError("This helper class must not be istantiated!");
    }

    
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
        
        return ThrowablesWrapper.wrapAssignment(
                
                () -> Optional.ofNullable(Integer.parseInt(System.getProperty(arg.name())))
                    .orElse(defaultValue),
                 
                defaultValue);
    }
}
