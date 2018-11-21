package base;

import static base.Logger.logLines;
import static base.Logger.logSplitByLines;
import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public interface Assert{


    private static void passAssertion(
            String assertionDescription,
            String expected) {

        logLines(assertionDescription,
                "expected: \"" + expected + "\"",
                "SUCCEEDED!");
    }



    private static void failAssertion(
            String assertionDescription,
            String expected, 
            String actual){

        String failureMessage = List.of(
                assertionDescription,
                "expected: \"" + expected + "\"",
                "actual: \"" + actual + "\"",
                "FAILED").stream().collect(Collectors.joining(" | ")); 
  
        logSplitByLines(failureMessage);

        throw new AssertionError(failureMessage);
    }



    public static <T> void isCondition(
            String assertionDescription,
            T expected,
            T actual,
            BiPredicate<T, T> condition) {

        if (condition.test(
                
                Optional.ofNullable(expected)
                    .orElseThrow(() -> new AssertionError("Expected value is null!")),
                
                Optional.ofNullable(actual)
                    .orElseThrow(() -> new AssertionError("Actual value is null!")))){
            
            passAssertion(assertionDescription, expected.toString());
        }
        else {
            failAssertion(assertionDescription, expected.toString(), actual.toString());
        }
    }


    public static <T> void isConditionQuiet(
            String assertionDescription,
            T expected,
            T actual,
            BiPredicate<T, T> condition) {

        if (! condition.test(
                
                Optional.ofNullable(expected)
                    .orElseThrow(() -> new AssertionError("Expected value is null!")),
                    
                Optional.ofNullable(actual)
                    .orElseThrow(() -> new AssertionError("Actual value is null!")))){
            
            failAssertion(assertionDescription, expected.toString(), actual.toString());
        }
    }
    
    

    public static Function<Runnable, AssertionError> runnableToAssertionErrorOrNull = runnable -> {
        
        try {
            runnable.run();
        }
        catch(AssertionError ae) {
            return ae;
        }
        
        return null;
    };
    
    
    
    public static List<AssertionError> getAssertionErrors(Runnable ...assertions) {

        return getAssertionErrors(List.of(assertions));
            
    }


    
    public static List<AssertionError> getAssertionErrors(List<Runnable> assertionsList) {

        return assertionsList.stream()
                .map(runnableToAssertionErrorOrNull)
                .filter(assertionError -> assertionError != null)
                .collect(Collectors.toList());
    }

    
    
    public static void verifyAll(List<AssertionError> assertionErrors) {
        
        if (! assertionErrors.isEmpty()) {            
            throw new AssertionError(assertionErrors.stream()
                    .map(assertionError -> assertionError.getMessage())
                    .collect(Collectors.joining("<br>")));
        }
    }
    
    
    
    public static void verifyAll(Runnable ...assertions) {
        verifyAll(getAssertionErrors(assertions));
    }



    public static void isEqual(
            String assertionDescription,
            String expected,            
            String actual){

        isCondition(assertionDescription, 
                expected, 
                actual, 
                (exp, act) -> exp.equals(act));
    }
    

    
    public static void isEqual(
            String expected,            
            String actual){

        isEqual("", expected + "", actual + "");
    }
    
    
    
    public static void isEqual(
            String assertionDescription,
            boolean expected,            
            boolean actual){


        isEqual(assertionDescription, expected + "", actual + "");
    }
    
    
    
    public static void isEqualQuiet(
            String assertionDescription,
            String expected,            
            String actual){


        isConditionQuiet(assertionDescription, 
                expected, 
                actual, 
                (exp, act) -> exp.equals(act));
    }
    
    
    
    public static void isEqual(
            String assertionDescription,
            int expected,            
            int actual){


        isEqual(assertionDescription, "" + expected, "" + actual);
    }
    
    
    
    public static void isEqualQuiet(
            String assertionDescription,
            int expected,            
            int actual){


        isEqualQuiet(assertionDescription, "" + expected, "" + actual);
    }



    public static void isEqualIgnoreCase(
            String assertionDescription,
            String expected,
            String actual){

        isCondition(assertionDescription, 
                expected, 
                actual, 
                (exp, act) -> exp.equalsIgnoreCase(act));
    }



    public static void isEqualAsFloat(
            String assertionDescription,
            String expected,
            String actual){

        isCondition(assertionDescription, 
                expected.replace(",", ""), 
                actual, 
                (exp, act) -> Float.parseFloat(exp) == Float.parseFloat(act));
    }



    public static void actualContainsExpected(
            String assertionDescription,
            String expected,
            String actual){

        isCondition(assertionDescription, 
                expected, 
                actual, 
                (exp, act) -> act.contains(exp));
    }



    public static void expectedContainsActual(
            String assertionDescription,
            String expected,
            String actual){

        actualContainsExpected(assertionDescription, 
                actual, 
                expected);
    }



    public static void isTrue(
            String assertionDescription, 
            boolean isSuccessful){

        if( isSuccessful ){
            logSplitByLines("Assertion: \"" + assertionDescription + "\" - SUCCEEDED!");
        }
        else {
            logSplitByLines("Assertion: \"" + assertionDescription + "\" - FAILED!");
            throw new AssertionError(assertionDescription);
        }
    }



    public static void fail(String failureMessage){
        logSplitByLines("Assertion failed!" + failureMessage);
        throw new AssertionError(failureMessage);
    }

    
    
    public static <T> void assertList(List<T> expectedList, List<T> actualList){
        
        isEqual("Verify lists size", expectedList.size(), actualList.size());
        
        List<Runnable> assertionBlocks = IntStream.range(0, expectedList.size()-1)
            .mapToObj(i -> (Runnable)() -> assertEquals(expectedList.get(i), actualList.get(i)))
            .collect(Collectors.toList());
                
        verifyAll(getAssertionErrors(assertionBlocks));
     }
    
    
    
    public static <T, U> void assertMap(Map<T, U> expectedMap, Map<T, U> actualMap){
        
        isEqual("Verify maps size", expectedMap.size(), actualMap.size());
        
        List<Runnable> assertionBlocks = expectedMap.keySet().stream()
                
            .map(key ->
            
                (Runnable)() -> assertEquals(
                    key + ":" + expectedMap.get(key), 
                    key + ":" + actualMap.get(key)))
            
            .collect(Collectors.toList());
                
        verifyAll(getAssertionErrors(assertionBlocks));
     }

}


