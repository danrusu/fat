package main.java.base.xmlSuite;

import java.util.List;
import static java.util.stream.Collectors.*;

public enum DSLFunction{
	
    // $after10days_ddMMYYYY
	$afterNumberOfDays("^\\$after(?<daysDelay>(\\d)+)days_(?<dateFormat>(d{1,2})|(M{1,2})|(y){4})$"),
    
	// $dataProvider(5)
    $dataProvider("^\\$dataProvider\\((\\d+)\\)$"),
	
	// $property(counter)
	$property("^\\$property\\((\\w+\\d*)\\)$");

    
    private String dslRegexp; 

   
    private DSLFunction(String dslRegexp) {
        this.dslRegexp = dslRegexp;
    }
    
    
    public String getDslRegexp() {
        return this.dslRegexp;
    }
    
    
    public static DSLFunction getDSLFunction(String text){ 
    
        return List.of(values()).stream()
                .filter(v -> text.matches(v.dslRegexp))
                //.map(DSLFunction::name)
                .findFirst()
                .orElse(null);       
    }    
        
     
	public static boolean contains(String name){
	    
	    return List.of(values()).stream()	            
	            .anyMatch(v -> v.name().equals(name));
	}
		
	
	private static List<String> getDslRegexs(){
		return List.of(values()).stream()
				.map(DSLFunction::getDslRegexp)
				.collect(toList());
	}
	
	
	public static boolean isDSLFunction(String text){
          
		return getDslRegexs().stream() 
				.anyMatch(dslRegex -> text.matches(dslRegex));
		 
		/*
		 * List<String> dslRegexs = getDslRegexs();
		 * for(int i=0; i < dslRegexs.size(); i++) { String regex = dslRegexs.get(i);
		 * boolean match = text.matches(regex); if(match) return true;
		 * 
		 * }
		 * 
		 * return false;
		 */
    }
		
}
