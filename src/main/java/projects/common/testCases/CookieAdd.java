package main.java.projects.common.testCases;

import java.util.Date;
import java.util.Set;

import org.openqa.selenium.Cookie;

import main.java.base.selenium.Driver;
import main.java.base.pom.WebPage;
import main.java.base.testCase.WebPageTestCase;

/**
 * Add cookie to the current web page.
 * 
 * @author Dan.Rusu 
 * 
 */
public class CookieAdd extends WebPageTestCase{

	@Override
	public void run(){

		@SuppressWarnings("deprecation")
        Cookie cookie = new Cookie.Builder(
				evalAttribute("cookieName"),
				evalAttribute("cookieValue")
				)

				.domain(evalAttribute("cookieValue"))
				.expiresOn(new Date(2020, 10, 28))
				.isHttpOnly(evalBooleanAttribute("cookieIsHttpOnly"))
				//.isSecure(evalBooleanAttribute("cookieIsSecure"))
				.path(evalAttribute("cookiePath")
						)

				.build();

		WebPage page = new WebPage(Driver.getDriver());
		page.addCookie(cookie);

		Set<Cookie> c = page.getAllCookies();
		
		System.out.println(c.toString()); 
        
	}

	@Override
	public String getTestCaseScenario(){
		return new StringBuilder()
				.append("\nAdd cookie to the current web page.")				
				.toString();
	}


}
