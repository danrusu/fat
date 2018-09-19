package core.failures;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ThrowablesWrapper {

    
    public static Function<Runnable, Throwable> runnableToThrowableOrNull = runnable -> {
        
        try {
            runnable.run();
        }
        catch(Throwable throwable) {
            return throwable;
        }
        
        return null;
    };
    
    

    public static List<Throwable> wrapAll(Runnable ...runables){

        return List.of(runables).stream()
        
            .map(runnableToThrowableOrNull)
            
            .filter(throwable -> throwable != null)
            
            .collect(Collectors.toList());            
    }



    public static <T> Optional<T> wrapAssignment(Callable<T> callable, boolean printError){

        T returnedValue = null;

        try {
            returnedValue = callable.call();
        }
        catch(Throwable thrown) {
            if (printError) { 
                System.out.println(thrown); 
            }
        }

        return Optional.ofNullable(returnedValue);
    }

    
    
    public static <T> Optional<T> wrapAssignment(Callable<T> callable){

        return wrapAssignment(callable, true);
    }

    

    public static <T> T wrapAssignment(Callable<T> callable, T defaultValue){

        return wrapAssignment(callable).orElse(defaultValue);
    }
    
    
    
    public static <T> T wrapAssignment(Callable<T> callable, T defaultValue, boolean printError){

        return wrapAssignment(callable, printError).orElse(defaultValue);
    }



    public static <T> T wrapThrowable(
            String testCaseFailureMessage, 
            Callable<T> callable){

        try {
            return callable.call();
        }
        catch (Throwable e) {
            throw new Failure(testCaseFailureMessage, e);
        }
    }



    public static <T> T wrapThrowable(Callable<T> callable){

        try {
            return callable.call();
        }
        catch (Throwable e) {
            throw new Failure(e.toString(), e);
        }
    }



    /* this is not useful
     * compiler will force you to wrap the exception in a try catch 
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
