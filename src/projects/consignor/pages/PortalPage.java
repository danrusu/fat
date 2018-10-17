package projects.consignor.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import base.failures.ThrowablesWrapper;
import base.pom.WebPage;



/**
 * @author Dan.Rusu
 *
 */
public class PortalPage extends WebPage{

    //public static String url = "https://www.consignorportal.com/idsrv/Account/login";
    

	By username = By.id("username");
	By password = By.id("password");
	By login = By.cssSelector("#reg-login button[type=\"submit\"]"); // TODO use less brittle selector (dev)
	By loginErrorMessage = By.id("errormessage");


	public PortalPage(WebDriver driver) {
	    super(driver);
	}

	
	
	public void setUsername(String username) {
	    clearAndSendString(this.username, username);  
	}
	
	
	
	public void setPassword(String password) {
        clearAndSendString(this.password, password);  
    }
	
	
	
	public void login() {
	    click(login);
	}
	
	
	
	// empty string if cannot get the text 
	public String getErrorMessage() {
	    
	    resetImplicitWait();
	    
	    String errorMessage = ThrowablesWrapper.unchekedAssignment(
	            
	            () -> getExplicitWait(2)
	                    .until(ExpectedConditions.visibilityOfElementLocated(loginErrorMessage))
	                    .getText()
	                    .trim(),
	            
	            new String());
	    
       setDefaultImplicitWait();
       
       return errorMessage;
	}
	
}

