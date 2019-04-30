package main.java.projects.common;

import java.util.function.BiPredicate;

public enum StringComparison {

    equals,
    
    
    expectedContains,
    
    expectedStartsWith,
    
    expectedEndsWith,
    
    
    actualContains,
    
    actualStartsWith,
    
    actualEndsWith;
    

    public BiPredicate<String, String> getComparisonFunction() {
        
        switch (this) {
            
            case expectedContains:
               return (a,b) -> a.contains(b);
               
            case expectedStartsWith:
                return (a,b) -> a.startsWith(b);
                
            case expectedEndsWith:
                return (a,b) -> a.endsWith(b);
                
                
            case actualContains:
                return (a,b) -> b.contains(a);
                
             case actualStartsWith:
                 return (a,b) -> b.startsWith(a);
                 
             case actualEndsWith:
                 return (a,b) -> b.endsWith(a);   
                
                 
            default:
                return (a, b) -> a.equals(b); 
            
        }

    }
    
}
