package utils;
import static base.Logger.log;

/**
 * Hard coded waits using Thread.sleep.
 * 
 * @author dan.rusu
 *
 */
public interface ThreadUtils {
	
	
	
	public static void setMaxThreads(int maxThreadsCount) {
        System.setProperty(
                "java.util.concurent.ForkJoinPool.common.parallelism", 
                Integer.toString(maxThreadsCount));
    }
    
    
    
    public static void setOnlyMainThread() {
       setMaxThreads(0);
    }
    
    
	
	/**
	 * Wait and log log the wait duration. 
	 * 
	 * @param millis - duration of the wait in milliseconds 
	 */
	public static void sleep(long millis){
		try {
			log("Wait " + millis + "ms");
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			log("Thread id="
					+ Thread.currentThread().getId() 
					+ "was interrupted");
		}
	}

	
	
	/**
	 * Do not log the wait :)
	 * 
	 * @param millis - duration of the wait in milliseconds 
	 */
	public static void sleepQuiet(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			log("Thread id="
					+ Thread.currentThread().getId() 
					+ "was interrupted");
		}
		
	}
}
