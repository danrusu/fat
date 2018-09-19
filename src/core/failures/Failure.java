package core.failures;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Wrapper for any kind of failure: Exception, Error, RuntimeException.
 * 
 * TestCaseFailure extends Error so there is no need to add Throws 
 * to the method that will throw this.
 * 
 * @author Dan.Rusu
 *
 */
public class Failure extends Error {

    private static final long serialVersionUID = 1L;



    public Failure(String message, Throwable cause) {
        super(message, cause);
    }



    public Failure(String message) {
        super(message);
    }



    public Failure(Throwable cause) {
        super(cause);
    }



    public static String stackToString(Throwable cause){
        
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        cause.printStackTrace(pw);
        return sw.toString();
    }

}

