package utils;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

import core.failures.ThrowablesWrapper;

public final class StringUtils {
	
	private StringUtils(){
		throw new AssertionError("This helper class must not be istantiated!");
	}
	

	
	public static String quote(String s){
	    
		return "\"" + s + "\"";
	}
	
	
	
	public static String encodeBase64(String text, String charSet){
	    
	    return ThrowablesWrapper.wrapThrowable(
                "encodeBase64 error",
                () -> new String(Base64.getEncoder().encodeToString(text.getBytes()).getBytes(), charSet));	   
	}
	
	
	
	public static String decodeBase64(String encodedText, String charSet){
	    
	    return ThrowablesWrapper.wrapThrowable(
	            "decodeBase64 error",
	            () -> new String(Base64.getDecoder().decode(encodedText), charSet));
	}
	
	
	
	public static String encodeBase64(String text){
	    
		return new String(Base64.getEncoder().encodeToString(text.getBytes()));
	}
	
	
	
	public static String decodeBase64(String encodedText){
	    
		return new String(Base64.getDecoder().decode(encodedText));
	}



    public static String nullToEmptyString(String s){
        
        return Optional.ofNullable(s)
                .orElse(new String());
    }



    public static boolean toBoolean(String booleanString, boolean defaultValue) {    
        
        if (defaultValue) {
            return ! nullToEmptyString(booleanString)
                    .equalsIgnoreCase(Boolean.toString(false));
        }
        else {
            return nullToEmptyString(booleanString)
                    .equalsIgnoreCase(Boolean.toString(true));                     
        }
    }
    
    
    
    public static boolean toBoolean(String booleanString) {
        
        return toBoolean(booleanString, false);
    }



    public static int toInt(String intString, int defaultValue) {
        
        return ThrowablesWrapper.wrapAssignment(
                () ->  Integer.parseInt(intString), 
                defaultValue);
    }
 
    
    
    public static String removeZeroPrefixFromIntegers(String integerString) {
        
        return Integer.toString(Integer.parseInt(
            integerString));
    }
    
    
    
    public static List<String> splitByEquals(String string) {
        return List.of(string.split("="));
    }
    
}
