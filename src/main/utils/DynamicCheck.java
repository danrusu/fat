package main.utils;

import static main.base.Logger.log;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import main.base.pom.WebPage;



/**
 * Dynamic timeouts for custom conditions via high order functions.
 * 
 * @author dan.rusu
 *
 */
public interface DynamicCheck {



    public static <T, U, X, Y> boolean waitUntilOneFunctionReturnsExpectedValue(
            long totalMilisTimeout,
            long stepTimeout,

            Function< T, U> function1,
            T funtion1Argument,
            U expectedObject1,

            Function< X, Y> function2,
            X funtion2Argument,
            Y expectedObject2){

        U returnedObject1;
        Y returnedObject2;

        for(int i=0; i<totalMilisTimeout/stepTimeout; i++){

            returnedObject1 = function1.apply(funtion1Argument);

            returnedObject2 = function2.apply(funtion2Argument);

            if ( returnedObject1.equals(expectedObject1) 
                    || returnedObject2.equals(expectedObject2) ){

                log(
                        "condition reached after " + i*stepTimeout + "ms");
                return true;
            }
            ThreadUtils.sleepQuiet(stepTimeout);
        }

        log(
                "condition not reached after " + totalMilisTimeout + "ms");
        return false;
    }


    public static <T,U> boolean waitUntilFunctionReturnsExpectedValue(
            long totalMilisTimeout,
            long stepTimeout,
            Function<T, U> function,
            T funtionArgument,
            U expectedObject
            ){

        return waitUntilFunctionReturnsValueThatMachesCondition(
                totalMilisTimeout,
                stepTimeout,
                function,
                funtionArgument,
                x -> x.equals(expectedObject));
    }


    public static <T,U> boolean waitUntilFunctionReturnsExpectedValue(
            long totalMilisTimeout,
            long stepTimeout,
            Supplier<U> function,
            U expectedObject
            ){

        return waitUntilFunctionReturnsValueThatMachesCondition(
                totalMilisTimeout,
                stepTimeout,
                function,
                x -> x.equals(expectedObject));
    }





    public static <T,U> boolean waitUntilFunctionReturnsDifferentValue(
            long totalMilisTimeout,
            long stepTimeout,
            Function< T, U> function,
            T funtionArgument,
            U expectedObject
            ){

        return waitUntilFunctionReturnsValueThatMachesCondition(
                totalMilisTimeout,
                stepTimeout,
                function,
                funtionArgument,
                x -> ! x.equals(expectedObject));
    }



    public static <T,U> boolean waitUntilFunctionReturnsValueThatMachesCondition(
            long totalMilisTimeout,
            long stepTimeout,
            Function< T, U> function,
            T funtionArgument,
            Predicate<U> expectedCondition
            ){


        for(int i=0; i<totalMilisTimeout/stepTimeout; i++){

            U resultObject = function.apply(funtionArgument);

            if ( expectedCondition.test(resultObject) ){
                
                log("Dynamic wait - funtion result: " + resultObject.toString());
                log("condition reached after " + i*stepTimeout + "ms");
                return true;
            }

            ThreadUtils.sleepQuiet(stepTimeout);
        }

        log("Dynamic wait - funtion result: " + 
        		function.apply(funtionArgument).toString());
        log("condition not reached after " + totalMilisTimeout + "ms");
        
        return false;
    }



    public static <T,U> boolean waitUntilFunctionReturnsValueThatMachesCondition(
            long totalMilisTimeout,
            long stepTimeout,
            Supplier<U> supplier,
            Predicate<U> expectedCondition
            ){


        for(int i=0; i<totalMilisTimeout/stepTimeout; i++){

            U resultObject = supplier.get();

            if ( expectedCondition.test(resultObject) ){
                log(
                        "Dynamic wait - funtion result: " + resultObject.toString());
                log(
                        "condition reached after " + i*stepTimeout + "ms");
                return true;
            }

            ThreadUtils.sleepQuiet(stepTimeout);
        }

        log(
                "Dynamic wait - funtion result: " + 
                        supplier.get().toString());
        log(
                "condition not reached after " + totalMilisTimeout + "ms");
        return false;
    }



    public static <T> boolean waitUntilWebPageActionMatchesCondition(
            long totalMilisTimeout,
            long stepTimeout,
            Function< WebPage, T> function1,
            WebPage page,
            Predicate<T> expectedCondition
            ){

        page.resetImplicitWait();
        boolean success=false;

        for(int i=0; i<totalMilisTimeout/stepTimeout; i++){
            if ( expectedCondition.test(function1.apply(page)) ){
                log(
                        "condition reached after " + i*stepTimeout + "ms");

                success = true;
                break;
            }
            ThreadUtils.sleepQuiet(stepTimeout);
        }

        page.setDefaultImplicitWait();

        if (! success){
            log(
                    "condition not reached after " + totalMilisTimeout + "ms");
        }
        return success;
    }

}

