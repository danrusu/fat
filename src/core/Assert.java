package core;

import static org.junit.Assert.assertEquals;
import static core.Logger.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import core.failures.Failure;

public final class Assert{


    private Assert(){
        throw new AssertionError("This helper class must not be istantiated!");
    }



    private static void passAssertion(
            String assertionDescription,
            String expected) {

        logLines(
                assertionDescription,
                "expected: \"" + expected + "\"",
                "SUCCEEDED!"
                );
    }



    private static void failAssertion(
            String assertionDescription,
            String expected, 
            String actual){

        String[] failureMessageTokens = new String[] {
                assertionDescription,
                "expected: \"" + expected + "\"",
                "actual: \"" + actual + "\"",
                "FAILED" 
        };

        logLines(failureMessageTokens);

        throw new AssertionError(
                Arrays.asList(failureMessageTokens)
                .stream()
                .collect(Collectors.joining(" | ")));
    }



    public static <T> void isCondition(
            String assertionDescription,
            T expected,
            T actual,
            BiPredicate<T, T> condition) {

        if (condition.test(
                Optional.ofNullable(expected)
                .orElseThrow( () -> new Failure("Expected value is null!") ),
                Optional.ofNullable(actual)
                .orElseThrow( () -> new Failure("Actual value is null!") ))
                ){
            passAssertion(assertionDescription, expected.toString());
        }
        else {
            failAssertion(assertionDescription, expected.toString(), "" + actual);
        }
    }


    public static <T> void isConditionQuiet(
            String assertionDescription,
            T expected,
            T actual,
            BiPredicate<T, T> condition) {

        if (! condition.test(
                Optional.ofNullable(expected)
                .orElseThrow( () -> new Failure("Expected value is null!") ),
                Optional.ofNullable(actual)
                .orElseThrow( () -> new Failure("Actual value is null!") ))
                ){
            
            failAssertion(assertionDescription, expected.toString(), "" + actual);
        }
    }
    

    public static List<AssertionError> assertAll(Runnable ...assertions) {

        List<AssertionError> errors = new ArrayList<>();

        Arrays.asList(assertions).stream()
        .forEach( x -> {

            try {
                x.run();
            }

            catch(AssertionError ae) {
                errors.add(ae);    
            }

        } );

        return errors;
    }


    
    public static List<AssertionError> assertAll(List<Runnable> assertionsList) {

        return assertAll(assertionsList.toArray(new Runnable[0]));
    }

    
    
    public static void assertExceptions(List<AssertionError> assertionErrors) {
        
        if (! assertionErrors.isEmpty()) {            
            throw new Failure(
                    assertionErrors.stream()
                    .map(assertionError -> assertionError.getMessage())
                    .collect(Collectors.joining("<br>"))
                    );
        }
    }



    public static void equals(
            String assertionDescription,
            String expected,            
            String actual){


        isCondition(assertionDescription, 
                expected, 
                actual, 
                (exp, act) -> exp.equals(act)
                );
    }
    
    
    
    public static void equals(
            String assertionDescription,
            boolean expected,            
            boolean actual){


        equals(assertionDescription, expected+"", actual+"");
    }
    
    
    
    public static void equalsQuiet(
            String assertionDescription,
            String expected,            
            String actual){


        isConditionQuiet(assertionDescription, 
                expected, 
                actual, 
                (exp, act) -> exp.equals(act)
                );
    }
    
    
    
    public static void equals(
            String assertionDescription,
            int expected,            
            int actual){


        equals(assertionDescription, "" + expected, "" + actual);
    }
    
    public static void equalsQuiet(
            String assertionDescription,
            int expected,            
            int actual){


        equalsQuiet(assertionDescription, "" + expected, "" + actual);
    }



    public static void equalsIgnoreCase(
            String assertionDescription,
            String expected,
            String actual){

        isCondition(assertionDescription, 
                expected, 
                actual, 
                (exp, act) -> exp.equalsIgnoreCase(act)
                );
    }



    public static void equalsFloatStrings(
            String assertionDescription,
            String expected,
            String actual){

        isCondition(assertionDescription, 
                expected.replace(",", ""), 
                actual, 
                (exp, act) -> Float.parseFloat(exp) == Float.parseFloat(act)
                );
    }



    public static void actualContainsExpected(
            String assertionDescription,
            String expected,
            String actual){

        isCondition(assertionDescription, 
                expected, 
                actual, 
                (exp, act) -> act.contains(exp)
                );
    }



    public static void expectedContainsActual(
            String assertionDescription,
            String expected,
            String actual){

        actualContainsExpected(assertionDescription, 
                actual, 
                expected);
    }



    public static void assertTrue(
            String assertionDescription, 
            boolean isSuccessful){

        if( isSuccessful ){
            logLines("Assertion: \" " + assertionDescription + "\" - SUCCEEDED!");
        }
        else {
            logLines("Assertion: \"" + assertionDescription + "\" - FAILED!");
            throw new AssertionError(assertionDescription);
        }
    }



    public static void fail(String failureMessage){
        logLines("Assertion failed!" + failureMessage);
        throw new AssertionError(failureMessage);
    }

    
    
    public static <T> void assertList(List<T> expectedList, List<T> actualList){
        
       equals("Verify list size", expectedList.size(), actualList.size());
       
       List<Runnable> assertionsList = new ArrayList<>();
       
       IntStream.range(0, expectedList.size()-1)
           .forEach(
                   i -> assertionsList.add(
                           () -> assertEquals(expectedList.get(i), actualList.get(i))
                   )
           );
       
       assertExceptions(assertAll(assertionsList));
    }


}
