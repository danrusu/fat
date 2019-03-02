package main.base;

import static java.util.stream.Collectors.joining;
import static main.base.failures.ThrowablesWrapper.supplyAndMapThrowableToFailure;
import static main.utils.FileUtils.createDirs;
import static main.utils.FileUtils.getRelativePath;
import static main.utils.SystemUtils.getBooleanPropertyOrDefault;
import static main.utils.TimeUtils.formatCurrentDate;

import java.nio.file.Files;
import java.nio.file.Path;
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
    

    public static Logger LOGGER = new Logger();

    private static final String LOG_FILE_NAME = "log.txt";

    private static Path logsRootPath;
    private static Path logDirPath;
    private static Path logFilePath;
    private static Path tempDirPath;

    private static final String TIMESTAMP_FORMAT = "yyyy/MM/dd HH:mm:ss";
    private static final String FILE_NAME_TIMESTAMP_FORMAT = "yyyy_MM_dd_HH_mm_ss";
    
    private static final String LOG_SEPARATOR = IntStream
            .range(0, 50)
            .mapToObj(i -> "*")
            .collect(joining()); 
   
    private static boolean isDebuggingMode;


    private Logger(){

            // Create directories: logs, temp, log_unique_name 
            logsRootPath = getRelativePath("logs");

            tempDirPath = getRelativePath("temp");

            logDirPath = logsRootPath.resolve("log__" + getTimeStamp(FILE_NAME_TIMESTAMP_FORMAT));        

            createDirs(logsRootPath, tempDirPath, logDirPath);

            // create log file 
            logFilePath = logDirPath.resolve(LOG_FILE_NAME);
            createLogFile();

            isDebuggingMode = getBooleanPropertyOrDefault(JvmArgs.DEBUG, false);
        
    }


    private static void createLogFile() {

        supplyAndMapThrowableToFailure(                

                () -> Files.createFile(logFilePath),

                "Coud not create log !");
    }


    public static void writeTextToFileInLogDir(String text, String fileName){

        supplyAndMapThrowableToFailure(                

                () -> {

                    Path filePath= getLogDirPath().resolve(fileName);
                    Files.createFile(filePath);
                    Files.write(filePath, text.getBytes());

                    return true;
                },

                "Could not create file " + fileName);
    }


    public static void log(String message){

        String formattedMessage = String.join(" | ",
                
                getTimeStamp(TIMESTAMP_FORMAT),
                
                //.append(" | t=" + Thread.currentThread().getId() + " | ")
                
                message); 

        System.out.println(formattedMessage);

        supplyAndMapThrowableToFailure(                

                () -> Files.write(
                        logFilePath, 
                        ("\n" + formattedMessage).getBytes(), 
                        StandardOpenOption.APPEND),
                
                "Could not write to log!");
    }


    public static void logAll(String ...strings) {  

        log(List.of(strings)
                .stream()
                .collect(Collectors.joining())); 
    }


    public static void debug(String message){   

        if(isDebuggingMode){
            log("DEBUG | " +  message);
        }
    }
    
    
    public static void runOnlyInDebugMode(Runnable runnable) {
  
        if (isDebuggingMode) {            
            runnable.run();
        };
    }


    public static void error(String message){   

        log("ERROR | " +  message);
    }


    private static String getTimeStamp(String format){

        return formatCurrentDate(format);   
    }


    public static String getTimeStamp(){

        return formatCurrentDate(TIMESTAMP_FORMAT);  
    }


    public static String getFileTimeStamp(){

        return formatCurrentDate(FILE_NAME_TIMESTAMP_FORMAT);  
    }


    public static void logSplitByLines(String linesString) {

        List.of(linesString.split("\n")).forEach(Logger::log); 
    }


    public static void logLines(String ...lines) {

        List.of(lines).forEach(Logger::log);
    }


    public static void logFirstLine(String lines) {

        List.of(lines.split("\n")).stream()
            .limit(1)
            .forEach(Logger::log);
    }


    public static Path getLogDirPath() {
       
        return logDirPath;
    }


    public static Path getPathInLogDir(String fileName) {
        
        return logDirPath.resolve(fileName);
    }


    public static Path getLogFilePath() {

        return logFilePath;
    }


    public static Path getTempDirPath(){
        
        return tempDirPath;
    }


    public static void logSeparator() {
        
        log(LOG_SEPARATOR);
    }

}
