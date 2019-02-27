package main.base;

import static main.base.Assert.AssertCount.assertCount;
import static main.base.Logger.log;
import static main.base.Logger.logSplitByLines;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;



public interface Assert{
    
    
    public static class AssertCount{
        public static int assertCount = 0; 
    }



    private static void passAssertion(
            String assertionDescription,
            String expected) {

        assertCount++;
        
        log(assertionDescription
                + " expected: \"" + expected + "\" "
                + "SUCCEEDED!");
    }



    private static void failAssertion(
            String assertionDescription,
            String expected, 
            String actual){
        
        assertCount++;
        
        String failureMessage = String.join(" | ",
                assertionDescription,
                "expected: \"" + expected + "\"",
                "actual: \"" + actual + "\"",
                "FAILED"); 
  
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
            
            String allAssertionErrors = assertionErrors.stream()
                    
                    .map(AssertionError::getMessage)
                    
                    .collect(Collectors.joining("\n"));
            
            throw new AssertionError(allAssertionErrors);
        }
    }
    
    
    
    public static void verifyAllAsserts(Runnable ...assertions) {
        
        verifyAll(getAssertionErrors(assertions));
    }


    
    public static <T> void isEqual(
            T expected,            
            T actual){

        isEqual("", expected, actual);
    }
    
    
    public static <T> void isEqual(
            String assertionDescription,
            T expected,            
            T actual){

        isCondition(assertionDescription, 
                expected, 
                actual, 
                (exp, act) -> exp.equals(act));
    }
    
    
    public static <T> void isEqualQuiet(
            String assertionDescription,
            T expected,            
            T actual){

        isConditionQuiet(assertionDescription, 
                expected, 
                actual, 
                (exp, act) -> exp.equals(act));
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
    
    
    public static void isEqualIgnoreCase(
            String expected,
            String actual){

        isEqualIgnoreCase("", 
                expected, 
                actual);
    }
    
    
    
    public static void matchesRegex(
            String assertionDescription,
            String expectedRegex,
            String actual){

        isCondition(assertionDescription, 
                expectedRegex, 
                actual, 
                (exp, act) -> act.matches(expectedRegex));
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

        actualContainsExpected(
                assertionDescription, 
                actual, 
                expected);
    }



    public static void fail(String failureMessage){
        
        logSplitByLines("Assertion failed!" + failureMessage);
        throw new AssertionError(failureMessage);
    }

    
    
    public static <T> void assertList(List<T> expectedList, List<T> actualList){
        
        isEqual("Verify list size", expectedList.size(), actualList.size());
        
        List<Runnable> assertionsList = IntStream.range(0, expectedList.size()-1)
                
            .mapToObj(i -> (Runnable)() -> isEqual(
                    
                    expectedList.get(i).toString(),
                    
                    actualList.get(i).toString()))
            
            .collect(Collectors.toList());
                
        verifyAll(getAssertionErrors(assertionsList));
     }

    
    
    public static <T, U> void assertMap(Map<T, U> expectedMap, Map<T, U> actualMap){
        
        isEqual("Verify maps size", expectedMap.size(), actualMap.size());
        
        List<Runnable> assertionsList = expectedMap.keySet().stream()
                
            .map(key ->
            
                (Runnable)() -> isEqual(
                        
                    key + ":" + expectedMap.get(key),
                    
                    key + ":" + actualMap.get(key)))
            
            .collect(Collectors.toList());
                
        verifyAll(getAssertionErrors(assertionsList));
     }



    static void customAssertString(
            String assertionDescription, 
            String expected, 
            String actual, 
            AssertCondition assertCondition) {
        
        isCondition(assertionDescription, 
                expected, 
                actual, 
                assertCondition.getCondition());
        
    }
    
   /* public static <T> void conditionalAssert(       
            
            Predicate<T> condition,
            
            String message,
            T expected,
            T actual,
            
            BiFunction<String, T, T> assertionIfCondition,
            BiFunction<String, T, T> assertionIfNotCondition) {
            
        if (condition.test(actual)){
            
            assertionIfCondition.apply(message, expected, actual);
        }
        else {
            
            assertionIfNotCondition.apply(message, expected, actual);            
        }        
    }*/
}

