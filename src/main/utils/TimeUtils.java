package main.utils;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Calendar;
import java.util.Map;
import java.util.TreeMap;

public interface TimeUtils {
	
	
	public static long getElapsedTimeInMillis(Instant startTime){	    	

		return Duration.between(startTime, Instant.now())
		        .toMillis();
	}
	

	public static Map<String, String> millisToTime(long inputMillis){
	    
		Map<String, String> timeMap = new TreeMap<>();

		long totalSeconds = inputMillis / 1000;
		long millis = inputMillis % 1000;
		timeMap.put("millis", Long.toString(millis));

		long totalMinutes = totalSeconds / 60;
		long seconds = totalSeconds % 60;
		timeMap.put("seconds", Long.toString(seconds));

		long totalHours = totalMinutes / 60;
		long minutes = totalMinutes % 60;
		timeMap.put("minutes", Long.toString(minutes));
		timeMap.put("hours", Long.toString(totalHours));

		return timeMap;
	}


	public static String getElapsedTime(Instant startTime){

		Map<String, String> timeMap = TimeUtils.millisToTime(
		        TimeUtils.getElapsedTimeInMillis(startTime));

		return new StringBuilder()
				.append(timeMap.get("hours"))
				.append("h:")
				.append(timeMap.get("minutes"))
				.append("m:")
				.append(timeMap.get("seconds"))
				.append("s:")
				.append(timeMap.get("millis"))
				.append("ms")
				.toString();
	}
	
	
	public static String formatCurrentDate(String format){
	    
		return new SimpleDateFormat(format).format(
		        Calendar.getInstance().getTime());		
	}
	
	
	public static String formatNextDate(String format, String addDays){
	    
	    Calendar calendar = Calendar.getInstance();
	    calendar.add(Calendar.DATE, Integer.parseInt(addDays));
	        
	        return new SimpleDateFormat(format).format
	                (calendar.getTime());      
	}
	
}
