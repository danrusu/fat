package utils;

import static base.failures.ThrowablesWrapper.unchecked;
import static base.failures.ThrowablesWrapper.assignUnchecked;

import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;



public interface StringUtils {

	
	public static String quote(String s){
	    
		return "\"" + s + "\"";
	}
	
	
	
	public static String encodeBase64(String text, String charSet){
	    
	    return unchecked(
                "encodeBase64 error",
                () -> new String(Base64.getEncoder().encodeToString(text.getBytes()).getBytes(), charSet));	   
	}
	
	
	
	public static String decodeBase64(String encodedText, String charSet){
	    
	    return unchecked(
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
                .orElse("");
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
        
        return assignUnchecked(
                () ->  Integer.parseInt(intString), 
                defaultValue);
    }
    
    
    public static int toInt(String intString, int defaultValue, boolean printException) {
        
        return assignUnchecked(
                () ->  Integer.parseInt(intString), 
                defaultValue);
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

    
    
    public static List<String> splitByAndTrim(
            String text, 
            String separatorRegexp, 
            int listLength){
        
          var initialList = List.of(text.split(separatorRegexp)).stream()
                  .map(String::trim)
                  .collect(Collectors.toList());
          
          var listOfEmpyStingsToBeAddedAtTheEnd = IntStream.range(0, listLength - initialList.size())
                  .mapToObj(i -> "")
                  .collect(Collectors.toList());
              
          return listConcat(initialList, listOfEmpyStingsToBeAddedAtTheEnd);          
    }
    
    
    
    @SafeVarargs
    public static <T> List<T> listConcat(List<T> ...lists){
        
        return List.of(lists).stream()                
                .map(List::stream)
                .flatMap(Function.identity())
                .collect(Collectors.toList());
    }
    
    

    public static boolean equalsIgnoring(String text1, String text2, String ...ignoringRegexs) {
        
        return removeAllRegex(text1, ignoringRegexs).equals(
                removeAllRegex(text2, ignoringRegexs));        
        
    }
    
    
    
    public static String removeAllRegex(String text, String ...regexs) {
        
       return List.of(regexs).stream().reduce(
               text,
               (acc,regex) -> acc.replaceAll(regex, ""));
    }
    
}

