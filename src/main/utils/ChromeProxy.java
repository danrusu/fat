package main.utils;

import java.nio.file.Paths;

import org.openqa.selenium.Proxy;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class ChromeProxy {
	
	
	
	public static void ProxyUsingChromeDriver()
    {
        //Set the location of the ChromeDriver
        System.setProperty("webdriver.chrome.driver", 
        		Paths.get(System.getProperty("user.dir") 
				+ "/webDrivers"
				+ "/chromedriver.exe").toString());
        
        
        //Create a new desired capability
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        
        
        // Create a new proxy object and set the proxy
        Proxy proxy = new Proxy();
        proxy.setHttpProxy("localhost:8888"); // default Fiddler URL
        
        
        //Add the proxy to our capabilities 
        capabilities.setCapability("proxy", proxy);
        
        
        //Start a new ChromeDriver using the capabilities object we created and added the proxy to
        @SuppressWarnings("deprecation")
		ChromeDriver driver = new ChromeDriver(capabilities);

        
        //Navigation to a url and a look at the traffic logged in fiddler
        driver.navigate().to("http://localhost:3000/mirror");
    }


	
	public static void main(String[] args) {

		ProxyUsingChromeDriver();
	}
}
