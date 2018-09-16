package core.failures;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;

public class ThrowablesWrapper {


    public static List<Throwable> wrapAll(Runnable ...runables){


        List<Throwable> thrownList = new ArrayList<>();

        Arrays.asList(runables).stream().parallel()
        .unordered() // set the ORDERED bit to 0 to remove statefulness of the stream created from an ArrayList
        .forEach( runnable -> {
            try {
                runnable.run();
            }
            catch(Throwable thrown) {
                thrownList.add(thrown);
            }
        });

        return thrownList;
    }

    
    
    public static <T> Optional<T> wrapAssignment(Callable<T> callable){

        T returnedValue = null;
        
        try {
            returnedValue = callable.call();
        }
        catch(Throwable thrown) {
            // Logger.getLogger().log("" + thrown);
        }

        return Optional.ofNullable(returnedValue);
    }
    
    

    public static <T> T wrapAssignment(Callable<T> callable, T defaultValue){
        
        return wrapAssignment(callable).orElse(defaultValue);
    }



    public static <T> T wrapThrowable(
            String testCaseFailureMessage, 
            Callable<T> callable){
        
        try {
            return callable.call();
        }
        catch (Throwable e) {
            throw new TestCaseFailure(testCaseFailureMessage, e);
        }
    }



    public static <T> T wrapThrowable(Callable<T> callable){
        
        try {
            return callable.call();
        }
        catch (Throwable e) {
            throw new TestCaseFailure(e.toString(), e);
        }
    }
    
    
    
/* this is not useful
 * compiler force you to wrap the exception in a try catch 
 *    
     public static void wrapRunnable(
            String testCaseFailureMessage, 
            Runnable runnable){
        try {
            runnable.run();
        }
        catch (Throwable e) {
            throw new TestCaseFailure(testCaseFailureMessage, e);
        }
    }

    
    
    public static void wrapRunnable(Runnable runnable) {
        wrapRunnable("", runnable);
    }  */
}
