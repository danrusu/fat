package main.base.failures;

import static java.lang.String.join;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Wrapper for any kind of test case failure: Exception, Error, RuntimeException.
 * 
 * TestCaseFailure extends Error so there is no need to add Throws 
 * to the method that will throw this.
 * 
 * @author Dan.Rusu
 *
 */
public class Failure extends Error {


    private static final long serialVersionUID = 1L;


    public Failure(Throwable cause, String ...messageTokens) {

        super(join("", messageTokens), 
                cause);
    }


    public Failure(String ...messageTokens) {

        super(join("", messageTokens));
    }


    public Failure(Throwable cause) { 
   
        super(cause); 
    }


    public static String failureStackToString(Throwable cause){

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        cause.printStackTrace(pw);
    
        return sw.toString();
    }

}
