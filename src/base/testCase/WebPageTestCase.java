package base.testCase;

import java.util.Map;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import base.Driver;
import utils.DynamicCheck;

/**
 * Generic web page test.
 * 
 * @author Dan Rusu
 *
 */
abstract public class WebPageTestCase extends TestCase{	

    public WebDriver driver = null;
	public static WebDriver currentDriver = null;
	
	private String startWindowHandle;
	private DynamicCheck dc;



	/**
	 * Constructor 
	 */
	public WebPageTestCase() {		
		super();
		this.driver = Driver.driver;
	}


	/**
	 * Constructor 
	 */
	public WebPageTestCase(WebDriver driver) {
		super();
		this.driver = driver;
	}

	

	/**
	 * Open URL in browser.
	 * 
	 * @param url - web page URL
	 */
	public void openUrl(String url){
		driver.get(url);
		startWindowHandle = driver.getWindowHandle();
		Driver.setStartWindowHandle(startWindowHandle);
	}



	// Get an instance for dynamic waits 
	public DynamicCheck getDynamicCheck() {
		return dc;
	}

	

	public boolean checkPageTitle(String title, 
			long totalMilisTimeout,
			long stepTimeout){

		return DynamicCheck.waitUntilFunctionReturnsExpectedValue(
				totalMilisTimeout, 
				stepTimeout, 
				object -> ((WebDriver)object).getTitle(), 
				driver, 
				title);
	}



	public boolean checkPageUrl(String url, 
			long totalMilisTimeout,
			long stepTimeout){

		return DynamicCheck.waitUntilFunctionReturnsExpectedValue(
				totalMilisTimeout, 
				stepTimeout, 
				object -> ((WebDriver)object).getCurrentUrl(), 
				driver, 
				url);
	}


	
	public boolean isAlertPresent(long timeout){
		boolean foundAlert = false;
		WebDriverWait wait = new WebDriverWait(driver, timeout/*timeout in seconds*/);
		try {
			wait.until(ExpectedConditions.alertIsPresent());
			foundAlert = true;
		} catch (TimeoutException eTO) {
			foundAlert = false;
		}
		return foundAlert;
	}


	
	public String getStartWindowHandle() {
		return this.startWindowHandle;
	}


	
	public boolean isInternalTest() {
		return internalTest;
	}



	public void runInternalTestCase(WebPageTestCase test,
			Map<String, String> testAttributes){

		test.setTestCaseAttributes(testAttributes);
		test.run();
	}

	
	public boolean needToCloseDriver(){
		/*return testAttributes.keySet().stream()
				.filter(a -> a.equals("closeDriverAtFinish"))
				.findFirst().map(a -> testAttributes.get(a).equals("true"))
				.orElse(false);
		 */

		if (testCaseAttributes.containsKey("closeDriverAtFinish")){
			return testCaseAttributes.get("closeDriverAtFinish").equals("true");
		}
		return false;	
	}


	
	public void setStartWindowHandle(String startWindowHandle) {
		this.startWindowHandle = startWindowHandle;
	}

	
	public String getBrowser() {
		return testCaseAttributes.get("browser");
	}

	
	
	public void addJsErrors() {
		Map<String, String> jsErrors = Driver.getBrowserLogJsErrors();
		String values="";
		
		if (jsErrors.size()>0){			
			for (Map.Entry<String, String> entry : jsErrors.entrySet())
			{
				 values+=entry.getValue()+"<br/>";
			}
			getTestCaseAttributes().put("js_errors", "" + values);
		}
	}

	
}
