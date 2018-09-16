package projects.common.testCases;

import org.openqa.selenium.Cookie;

import core.Driver;
import core.failures.TestCaseFailure;
import core.pom.WebPage;
import core.testCase.WebPageTestCase;

/**
 * Get cookie by name from the current web page.
 * 
 * @author Dan.Rusu 
 * 
 */
public class CookieGet extends WebPageTestCase{
	public String cookieName;
	public String cookieValue;
	public String cookieDomain;
	
	//public String cookieExpiresOn;
	public String cookieIsHttpOnly;
	public String cookieIsSecure;
	
	public String cookiePath;
	

	@Override
	public void run(){
		
		String configCookieName = evalAttribute("cookieName"); 
		
		Cookie cookie = new WebPage(Driver.driver)
				.getCookie(configCookieName);
		
		if (cookie == null) {
			throw new TestCaseFailure("Cookie named\"" + configCookieName + "\" does not exist!");
		}
		
		cookieName = cookie.getName();
		cookieValue = cookie.getValue();
		cookieDomain = cookie.getDomain();
		//Date cookieExpiresOn = cookie.getExpiry();  // null ???
		cookieIsHttpOnly = "" + cookie.isHttpOnly();
		cookieIsSecure = ""+ cookie.isSecure();
		cookiePath = cookie.getPath();
		
		
		saveResults();
	
	}
	
	@Override
	public String getTestCaseScenario(){
		return new StringBuilder()
				.append("\nGet cookie by name from the current web page.")				
				.toString();
	}


}
