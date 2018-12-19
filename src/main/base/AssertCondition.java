package main.base;

import java.util.function.BiPredicate;


public enum AssertCondition {

    ACTUAL_CONTAINS_EXPECTED_PREDICATE(
            (String expected, String actual) -> actual.contains(expected)),
    
    EXPECTED_CONTAINS_ACTUAL_PREDICATE(
            (String expected, String actual) -> expected.contains(actual));
    
    
    private BiPredicate<String, String> condition; 
   
    
    private AssertCondition(BiPredicate<String, String>  condition) {
        this.condition =  condition;
    }    
    
    
    public BiPredicate<String, String> getCondition() {
        
        return condition;
    }
}
