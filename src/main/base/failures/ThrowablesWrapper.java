package main.base.failures;

import static java.util.stream.Collectors.toList;
import static main.base.Logger.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Function;


public class ThrowablesWrapper {


    private static Function<Runnable, Optional<Throwable>> runnableToThrowable = runnable -> {
        
        Throwable caughtThrowable = null;

        try { runnable.run(); }

        catch(Throwable thrown) { caughtThrowable = thrown; }

        return Optional.ofNullable(caughtThrowable);
    };


    public static List<Throwable> runAllAndGetTrowables(Runnable ...runables){

        return List.of(runables).stream()

                .map(runnableToThrowable)

                .filter(Optional::isPresent)

                .map(Optional::get)

                .collect(toList());            
    }


    public static <T> T supplyUnchecked(Callable<T> valueSupplier, T defaultValue){

        T suppliedValue = null;

        try { suppliedValue = valueSupplier.call(); }
        
        catch(Throwable thrown) { debug("Wrapped throwable [" + thrown + "]"); }         

        return Optional.ofNullable(suppliedValue)

                .orElse(defaultValue);
    }    


    public static <T> T supplyAndMapThrowableToFailure(
            Callable<T> valueSupplier, 
            String testCaseFailureMessage){

        try { return valueSupplier.call(); }
        
        catch (Throwable e) { throw new Failure(testCaseFailureMessage); }       
    }


    public static <T> T supplyAndMapThrowableToFailure(Callable<T> valueSupplier){

        try { return valueSupplier.call(); }
        
        catch (Throwable thrown) { throw new Failure(thrown); }
    }

}
