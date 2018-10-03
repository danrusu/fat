package base;

import static base.failures.ThrowablesWrapper.wrapThrowable;
import static utils.FileUtils.createDirs;
import static utils.TimeUtils.formatCurrentDate;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;



/**
 * Logger class (static usage; intialized once).
 * 
 * @author Dan Rusu
 *
 */
public class Logger {

    private static boolean isInitialized = false;

    private static final String logFileName = "log.txt";

    private static Path logsDirPath;

    private static Path logDirPath;
    private static Path logFilePath;
    private static Path tempDirPath;


    private static final String timeStampFormat = "yyyy/MM/dd HH:mm:ss";
    private static final String fileTimeStampFormat = "yyyy_MM_dd_HH_mm_ss";

    private static final String separatorChar = "#";
    private static final int separatorLength = 25;
    
    private static final String separator = IntStream.range(0, separatorLength)
            .mapToObj(i -> separatorChar)
            .collect(Collectors.joining());	

    private static boolean DEBUG;



    private static void init(){

        if (isInitialized == false) {
            
            isInitialized = true;


            // Create directories: logs, temp, log_unique_name 
            logsDirPath = Paths.get(System.getProperty("user.dir"), "logs");

            tempDirPath = Paths.get(System.getProperty("user.dir"), "temp");

            logDirPath = Paths.get(
                    System.getProperty("user.dir"), 
                    "logs/log__" + getTimeStamp(fileTimeStampFormat));        

            createDirs(logsDirPath, tempDirPath, logDirPath);

            // create log file 
            logFilePath = logDirPath.resolve(logFileName);
            createLogFile();
        }
    }



    private static void createLogFile() {

        wrapThrowable(

                "Coud not create log !",

                () -> Files.createFile(logFilePath));
    }



    public static void writeTextToFileInLogDir(String text, String fileName){

        wrapThrowable(

                "Could not create file " + fileName, 

                () -> {
                    
                    Path filePath= getLogDirPath().resolve(fileName);
                    Files.createFile(filePath);
                    Files.write(filePath, text.getBytes());
                    
                    return true;
                });
    }


    /**
     * Method to write formatted log messages to the log file.
     * 	
     * Log format:
     * 
     * timeStamp              | thread id | message
     * 2016/07/18 09:03:02    |    1      | Instantiate driver 
     * 
     * @param message - String to be logged 
     */
    public static void log(String message){

        init();

        String formattedMessage = new StringBuilder()
                .append(getTimeStamp(timeStampFormat))
                .append(" | t=" + Thread.currentThread().getId() + " | ")
                .append(message) 
                .toString();

        System.out.println(formattedMessage);

        wrapThrowable(

                "Could not write to log!",

                () -> Files.write(logFilePath, 
                        ("\n" + formattedMessage).getBytes(), 
                        StandardOpenOption.APPEND));
    }



    public static void logAll(String ...strings) {  

        log(List.of(strings)
                .stream()
                .collect(Collectors.joining())); 
    }



    /**
     * Method to write additional debug info.
     * 
     * Enabled by "-DDEBUG=true" JVM argument
     */
    public static void debug(String message){	
        
        if(DEBUG){
            log("DEBUG INFO: " +  message);
        }
    }



    /**
     * Method to get formatted current date.
     * 
     * @return formatted current date
     */
    private static String getTimeStamp(String format){
        
        return formatCurrentDate(format);	
    }



    public static String getTimeStamp(){

        return formatCurrentDate(timeStampFormat);	
    }



    public static String getFileTimeStamp(){

        return formatCurrentDate(fileTimeStampFormat);	
    }



    /**
     * Log multiple line string.
     * 
     * @param linesString
     */
    public static void logLines(String linesString) {

        List.of(linesString.split("\n")).forEach(Logger::log); 
    }



    public static void logLines(String ...lines) {

        List.of(lines).forEach(Logger::log);
    }



    /**
     * Log First line from string.
     * 
     * @param lines
     */
    public static void logFirstLine(String lines) {

        List.of(lines.split("\n")).stream()
        .limit(1)
        .forEach(Logger::log);
    }



    /**
     * Logs the message in a header style
     * between two lines of #
     * 
     * @param message - message to be logged
     */
    public static void logHeader(String message){
        
        logLines(separator, message, separator);
    }



    /**
     * Returns a line of #
     * 
     * @return - the log.separator string
     */
    public static String getSeparator(){
        return separator;
    }



    /**
     * Getter for log directory.
     * 
     * @return - log directory path
     */
    public static Path getLogDirPath() {
        
        init();
        return logDirPath;
    }


    public static Path getPathInLogDir(String fileName) {
        
        init();
        return logDirPath.resolve(fileName);
    }



    /**
     * Getter for log file path.
     * 
     * @return - log file path (../log.txt)
     */
    public static Path getLogFilePath() {
        
        init();
        return logFilePath;
    }



    public static Path getTempDirPath(){
        
        init();
        return tempDirPath;
    }

}

