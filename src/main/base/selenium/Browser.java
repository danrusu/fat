package main.base.selenium;


public enum Browser{
    
    CHROME("chromedriver.exe"),
    
    FIREFOX("geckodriver.exe"),
    
    IE("IEDriverServer.exe");
    
    private Browser(String driverExecutable) {
        
        this.driverExecutable = driverExecutable;            
    }
    
    
    private String driverExecutable; 
    
    
    public String getDriverExecutableName() {
        
        return driverExecutable;
    }
}