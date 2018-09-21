package base.results;

public enum ResultStatus {
        
          Started,
    
          Skipped,
          
	      Passed, 
	      
	      Failed;
	      
    
    
    
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

