package utils;

import static core.failures.ThrowablesWrapper.wrapAssignment;
import static core.failures.ThrowablesWrapper.wrapThrowable;

import java.util.Base64;
import java.util.List;
import java.util.Optional;



public interface StringUtils {

	
	public static String quote(String s){
	    
		return "\"" + s + "\"";
	}
	
	
	
	public static String encodeBase64(String text, String charSet){
	    
	    return wrapThrowable(
                "encodeBase64 error",
                () -> new String(Base64.getEncoder().encodeToString(text.getBytes()).getBytes(), charSet));	   
	}
	
	
	
	public static String decodeBase64(String encodedText, String charSet){
	    
	    return wrapThrowable(
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
        
        return wrapAssignment(
                () ->  Integer.parseInt(intString), 
                defaultValue);
    }
    
    
    public static int toInt(String intString, int defaultValue, boolean printException) {
        
        return wrapAssignment(
                () ->  Integer.parseInt(intString), 
                defaultValue,
                printException);
    }
 
    
    
    public static String removeZeroPrefixFromIntegers(String integerString) {
        
        return Integer.toString(Integer.parseInt(
            integerString));
    }
    
    
    
    public static List<String> splitBy(String text, String separator) {        
        
        return List.of(text.split(separator));
    }
    
    
    
    public static List<String> splitByEquals(String text){
        return splitBy(text, "=");
    }



    public static String extractTextBeforeRegex(String text, String regex) {
        
        return text.replaceAll(
                "(?s)(?<textBeforePattern>.*)" + regex + ".*", 
                "${textBeforePattern}");
    }
    
}
