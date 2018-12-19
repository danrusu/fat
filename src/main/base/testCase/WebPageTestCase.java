package main.base.testCase;

import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Collectors;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import main.base.selenium.Driver;
import main.base.runnerConfig.TestCaseAttribute;
import main.utils.DynamicCheck;
import main.utils.StringUtils;

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
		setDriver(Driver.getDriver());
	}


	/**
	 * Constructor 
	 */
	public WebPageTestCase(WebDriver driver) {
		super();
		setDriver(driver);
	}

	

	public void setDriver(WebDriver driver) {
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



	public void runInternalTestCase(
	        Path dataProviderFile,
	        WebPageTestCase testCase,
			Map<String, String> testAttributes){

		testCase.setTestCaseAttributes(testAttributes, dataProviderFile);
		testCase.run();
	}

	
	public boolean needToCloseDriver(){
		/*return testAttributes.keySet().stream()
				.filter(a -> a.equals("closeDriverAtFinish"))
				.findFirst().map(a -> testAttributes.get(a).equals("true"))
				.orElse(false);
		 */

	    String closeDriverAtFinish = TestCaseAttribute.closeDriverAtFinish.name();
	    
		if (testCaseAttributes.containsKey(closeDriverAtFinish)){
			return testCaseAttributes.get(closeDriverAtFinish).equals("true");
		}
		return false;	
	}


	
	public void setStartWindowHandle(String startWindowHandle) {
		this.startWindowHandle = startWindowHandle;
	}

	
	@Override
	public String getBrowser() {
		return StringUtils.nullToEmptyString(
		        testCaseAttributes.get("browser")).toUpperCase();
	}

	
	
	public void addJsErrors() {
	    
	    String htmlJsErrors = Driver.getJSErrorsFromBrowserLog().values().stream()
	            .collect(Collectors.joining("<br/>"));
	    
	    if (htmlJsErrors.isEmpty() == false) {
	        getTestCaseAttributes().put("js_errors", "" + htmlJsErrors);
	    }
	}
	
}

