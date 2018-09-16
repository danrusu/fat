package core.xml;

import java.util.List;

public enum XmlDynamicRegExp{
    
	$afterNumberOfDays ("\\$after(?<daysDelay>(\\d)+)days_(?<dateFormat>(d{1,2})|(M{1,2})|(y){4})$");

    
    private String value; 

    
    
    private XmlDynamicRegExp(String regExpValue) {
        this.value = regExpValue;
    }
    
    
    
    public String getValue() {
        return this.value;
    }
    
    
    public static String getName(String regExp){ 
    
        return List.of(values()).stream()
                .filter(v -> regExp.matches(v.value))
                .map(v -> v.name())
                .findFirst()
                .orElse(null);       
    }    
    
    
     
	public static boolean contains(String name){
	    
	    return List.of(values()).stream()
	            .filter(v -> v.name().equals(name))
	            .map(v -> true)
	            .findAny()
	            .orElse(false);	    
	}
	
	
	
	public static boolean matchesRegExp(String regExp){
        
        return List.of(values()).stream()
                .filter(v -> regExp.matches(v.value))
                .map(v -> true)
                .findAny()
                .orElse(false);     
    }
		
}


