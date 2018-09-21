package base.results;

import java.time.Instant;
import java.util.Map;
import java.util.TreeMap;

import base.results.ResultStatus;
import utils.TimeUtils;


public class Result {
    
	private String id;  // id = "testIndex/testCaseIndex"

	private ResultStatus resultStatus;
	private String elapsedTime;
	private long elapsedTimeInMillis;

	private Map<String, String> attributes;
	
	

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

	

	/**
	 * Get a header string for displaying test info.
	 * 
	 * @return - formatted test info header string
	 */
	public static String getHeader(Integer defaultColWidth){
		String colWidth = Integer.toString(defaultColWidth);
		String format = "%-"+ colWidth + "s";
		return String.join(" | ",
		        String.format(format, "Test[/TestCase]" ), 
				String.format(format.replace(colWidth, "20"), "Elapsed"),
				String.format(format.replace(colWidth, "40"), "Name"),
				String.format(format.replace(colWidth, "15"), "Result"),
				String.format(format, "Details"));
	}
	
	
	
	/**
	 * Get formatted test information.
	 * 
	 * @return - test information string
	 */
	public String getInfo(Integer defaultColWidth){
	    
		String colWidth = Integer.toString(defaultColWidth);
		String format = "%-"+ colWidth + "s";
		
		// details: all attributes but name
		Map<String, String> details = new TreeMap<>();
		details.putAll(attributes);
		details.remove("name");

		return String.join(" | ",
		        String.format(format, id),
		        String.format(format.replace(colWidth, "20"), elapsedTime),
		        String.format(format.replace(colWidth, "40"), attributes.get("name")),
		        String.format(format.replace(colWidth, "15"), resultStatus),
		        String.format(format, details));
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
	    return getResultStatus().equals(ResultStatus.Failed);
	}
	
}

