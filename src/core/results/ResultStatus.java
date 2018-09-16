package core.results;

public enum ResultStatus {
    
	      Passed, 
	      
	      Failed,
	      
	      Skipped;	  
    
    
    
    public String getHtmlColor() {
        
        return this.equals(Passed) ? 
                
                "green" : this.equals(Failed) ? 
                        
                "red" :
                        
                "blue";
    }
    
    
    
    public boolean isPassed() {
        return this.equals(Passed);
    }
    
    
    
    public boolean isFailed() {
        return this.equals(Failed);
    }
    
    
    
    public boolean isSkiped() {
        return this.equals(Skipped);
    }
	      	     
}

