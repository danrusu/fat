package main.base.results;

import static main.base.results.ResultStatus.FAILED;

import java.time.Instant;
import java.util.Map;
import java.util.TreeMap;

import main.utils.TimeUtils;


public class Result {

    private String id;  // id = "testIndex/testCaseIndex"

    private ResultStatus resultStatus;
    private String elapsedTime;
    private long elapsedTimeInMillis;

    private Map<String, String> attributes;

    private static final String COLUMN_WIDTH_PATTERN = "@colWidth@";
    private static final String OUTPUT_FORMAT = "%-" + COLUMN_WIDTH_PATTERN + "s";

    private static final String TESTCASE_NAME_PREFIX = "main\\.projects\\.";


    public Result(
            String id,
            ResultStatus resultStatus,
            Instant startTime,
            Map<String, String> attributes) {

        this.id = id;
        this.resultStatus = resultStatus;
        this.elapsedTimeInMillis = TimeUtils.getElapsedTimeInMillis(startTime);
        this.elapsedTime = TimeUtils.getElapsedTime(startTime);
        this.attributes = attributes;
    }


    public static String getTestInfoHeader(){	

        return String.join(" | ",                   
                getFormattedColumn("TestId", 10),
                getFormattedColumn("Result", 10),
                getFormattedColumn("Name", 50),
                //getFormattedColumn("Elapsed", 20),
                getFormattedColumn("Details", 10));
    }


    public String getInfo(){

        Map<String, String> details = new TreeMap<>();       
        details.putAll(attributes);

        // do not display the 'name' attribute in the details section
        String name = details.remove("name")
                .replaceAll(TESTCASE_NAME_PREFIX, "");
        
        return String.join(" | ",		        		        
                getFormattedColumn(id, 10),		        
                getFormattedColumn(resultStatus.name(), 10),		        
                getFormattedColumn(name, 50),		        
                //getFormattedColumn(elapsedTime, 20),		        
                getFormattedColumn(details.toString(), 10));		       		      
    }


    private static String getFormattedColumn(String columnData, int width) {       
        
        return String.format(
                OUTPUT_FORMAT.replace(COLUMN_WIDTH_PATTERN, "" + width), 
                columnData);
    }


    public String getId() {
        return id;
    }


    public ResultStatus getResultStatus() {

        return resultStatus;
    }


    public String getElapsedTime() {

        return elapsedTime;
    }


    public long getElapsedTimeInMillis() {

        return elapsedTimeInMillis;
    }	


    public Map<String, String> getAttributes(){

        return attributes;
    }


    public boolean isFailed() {

        return getResultStatus().equals(FAILED);
    }

}
