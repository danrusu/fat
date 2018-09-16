package core;


import static core.Logger.getLogDirPath;
import static core.Logger.log;
import static core.Logger.logLines;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import core.driverResources.DriverResource;
import core.failures.ThrowablesWrapper;

public class Driver {


    public static enum DriverType{
        chrome,
        firefox,
        ie,
        safari,
        edge,
        unknown
    }

    public static WebDriver driver;
    private static DriverType driverType;
    private static final Long defaultWait = 15L;  //seconds
    private static String startWindowHandle;


    //private static LoggingPreferences logsPref;
    // default - log java script errors
    // private static Level BROWSER_LOGGING_LEVEL = Level.SEVERE; //TODO set this via XML if needed


    /**
     * Get an instance of an WebDriver.
     * 
     * @param implicitWaitSeconds - set implicit diver wait
     */
    public static void driverStart( long implicitWaitSeconds, String browser, boolean grid ){

        //logs = new LoggingPreferences();
        for (int i=0; i<3; i++){
            log("Instantiate WebDriver (attemnpt " + i + ")");	


            if (grid){
                DesiredCapabilities capability;
                log("Grid !!!");

                switch (browser.toLowerCase()){
                    case "firefox":
                        capability = DesiredCapabilities.firefox();
                        //capability.setCapability("browser.shell.skipDefaultBrowserCheck", true);
                        break;
                    case "chrome":
                        capability = DesiredCapabilities.chrome();
                        break;
                    case "ie":
                        capability = DesiredCapabilities.internetExplorer();
                        break;
                    case "safari":
                        capability = DesiredCapabilities.safari();
                        break;
                    case "edge":
                        capability = DesiredCapabilities.edge();
                        break;	
                    default:
                        capability = null;
                        break;
                }
                try {
                    //TODO - this hangs if no node in hub - must be time outed on separate thread
                    driver = new RemoteWebDriver(new URL("http://10.56.1.57:4444/wd/hub"), capability);
                } catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                log("grid driver instanciated: " + driver);

            }
            else {
                try{
                    switch (browser.toLowerCase()){
                        case "firefox":
                            driver = setFirefoxDriver();

                            // *** Workaround for: https://github.com/mozilla/geckodriver/issues/820
                            // Win 10 , geckodriver 0.18.0, Firefox 54.0
                            // waiting for  FireFox 55.0

                            //driver.manage().window().maximize();
                            driver.manage().window().setSize(new Dimension(1920, 1080));
                            // *** 

                            driverType = DriverType.firefox;
                            break;
                        case "chrome":
                            driver = setChromeDriver();
                            driverType = DriverType.chrome;
                            break;
                        case "ie":
                            driverType = DriverType.ie;
                            driver = setIeDriver();
                            driver.manage().window().maximize();
                            break;
                        case "safari":
                            driverType = DriverType.safari;
                            driver = setSafariDriver();
                            break;
                        case "edge":
                            driverType = DriverType.edge;
                            driver = setEdgeDriver();
                            driver.manage().window().maximize();
                            break;
                        default:
                            driverType = DriverType.unknown;
                            driver = null;
                            break;
                    }
                }
                catch(Exception e){
                    logLines(""+e);
                }

            }

            if (driver != null){
                break;
            }

        }// finish the attempts to start the web driver

        /*		if (driver == null){
			AssertCustom.assertTrue(false, "Failed to start driver!!! Test will stop.");
		}*/

        //else{
        log("WebDriver: " + driver.getClass().getCanonicalName());

        // set driver's implicit wait 
        driver.manage().timeouts().implicitlyWait(implicitWaitSeconds, TimeUnit.SECONDS);
        log("implicit wait: " + implicitWaitSeconds + "s");
        //}
    }



    private static WebDriver setFirefoxDriver(){	
        log("Start Firefox driver");
        //logsPref.enable(LogType.BROWSER, BROWSER_LOGGING_LEVEL);
        DriverResource.exportDriverFromJar("geckodriver.exe",
                "geckodriver.exe");

        Path driverPath = Paths.get(System.getProperty("user.dir") 
                + "/webDrivers"
                + "/geckodriver.exe");

        log("webdriver.gecko.driver = "
                + driverPath.toString());
        System.setProperty("webdriver.gecko.driver", 
                driverPath.toString());


        //driver = new MarionetteDriver();
        /*
		FirefoxProfile myprofile = profile.getProfile("default");
		myprofile.setAcceptUntrustedCertificates(true);
		myprofile.setAssumeUntrustedCertificateIssuer(true);*/


        /*	ProfilesIni profile = new ProfilesIni();
	FirefoxProfile myprofile = profile.getProfile("default");
	myprofile.setPreference("browser.shell.skipDefaultBrowserCheck", true);

	FirefoxDriver dr = new FirefoxDriver(myprofile);*/


        FirefoxDriver dr = new FirefoxDriver();


        log(dr.getCapabilities().toString());
        return dr;
    }



    private static WebDriver setChromeDriver(){	
        log("Start Chrome driver");
        //logsPref.enable(LogType.BROWSER, BROWSER_LOGGING_LEVEL);
        DriverResource.exportDriverFromJar("chromedriver.exe",
                "chromedriver.exe");
        Path driverPath = Paths.get(System.getProperty("user.dir") 
                + "/webDrivers"
                + "/chromedriver.exe");

        log("webdriver.chrome.driver = "
                + driverPath.toString());
        System.setProperty("webdriver.chrome.driver", 
                driverPath.toString());

        /*DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		capabilities.setCapability("chrome.switches", Arrays.asList("--start-maximized", "--ignore-certificate-errors",
				"--disable-popup-blocking", "--disable-default-apps", "--auto-launch-at-startup", "--always-authorize-plugins")); */

        ChromeOptions options = new ChromeOptions();
        //options.addArguments(Arrays.asList("--start-maximized", "--ignore-certificate-errors"));
        options.addArguments(Arrays.asList("--start-maximized"));



        String downloadFolder = getLogDirPath().toString();
        log("Browser download folder: " + downloadFolder);

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.default_directory", downloadFolder );

        options.setExperimentalOption("prefs", prefs);



        return new ChromeDriver(options);
    }


    private static WebDriver setIeDriver(){	
        log("Start Internet Explorer driver");
        DriverResource.exportDriverFromJar("IEDriverServer.exe",
                "IEDriverServer.exe");

        // set driver's execution path
        Path driverPath = Paths.get(System.getProperty("user.dir") 
                + "/webDrivers"
                + "/IEDriverServer.exe");
        log("webdriver.ie.driver  = "
                + driverPath.toString());
        System.setProperty("webdriver.ie.driver", 
                driverPath.toString());

        // set driver's capabilities
        //		DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
        //		capabilities.setCapability("acceptSslCerts", true);
        //		capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true); 

        InternetExplorerOptions capabilities = new InternetExplorerOptions();
        capabilities.setCapability("ignoreZoomSetting", true);
        capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        capabilities.setCapability("nativeEvents",false);  // element clicks are not working without this setting

        // turn off IE pop up blocker
        // TODO - move this to capabilities if possible
        String cmd = "REG ADD \"HKEY_CURRENT_USER\\Software\\Microsoft\\Internet Explorer\\New Windows\" /F /V \"PopupMgr\" /T REG_SZ /D \"no\"";
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (Exception e) {
            log("Error ocured!");
        }

        return new InternetExplorerDriver(capabilities);
    }


    @SuppressWarnings("deprecation")
    private static WebDriver setEdgeDriver(){	
        log("Start Edge driver");
        DriverResource.exportDriverFromJar("MicrosoftWebDriver.exe",
                "MicrosoftWebDriver.exe");

        // set driver's execution path
        Path driverPath = Paths.get(System.getProperty("user.dir") 
                + "/webDrivers"
                + "/MicrosoftWebDriver.exe");
        log("webdriver.edge.driver  = "
                + driverPath.toString());
        System.setProperty("webdriver.edge.driver", 
                driverPath.toString());

        DesiredCapabilities capabilities = DesiredCapabilities.edge();
        //capability.setBrowserName("MicrosoftEdge");		
        //capability.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        //capability.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);
        //capability.setCapability(CapabilityType.SUPPORTS_JAVASCRIPT , true);  
        capabilities.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
        capabilities.setCapability(CapabilityType.PAGE_LOAD_STRATEGY, "eager");	
        capabilities.setCapability("nativeEvents",false);  // element clicks are not working without this setting
        capabilities.setPlatform(Platform.WIN10);
        capabilities.setBrowserName(BrowserType.EDGE);
        capabilities.setVersion("");
        return new EdgeDriver(capabilities);
    }


    private static WebDriver setSafariDriver(){	


        return new SafariDriver();
    }


    public static void closeAllWindows(){
        log("Closing all browser windows but main one");
        for (String handle : driver.getWindowHandles()){
            // close all windows but start window
            if ( ! handle.equals(startWindowHandle)){
                driver.switchTo().window(handle);
                driver.close();
            }
        }
    }


    // Returns default wait in seconds
    public static Long getDefaultWait() {
        return defaultWait;
    }


    public static File saveScreenShot(String fileName){


        return ThrowablesWrapper.wrapThrowable(

                "Failed to save screenshot!",

                () -> {

                    File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);

                    Path destPath = getLogDirPath().resolve(fileName);

                    FileUtils.copyFile(scrFile, destPath.toFile());

                    log("Saved screenshot: " + destPath.toString());

                    return scrFile;

                });    
    }



    public static void setStartWindowHandle(String startWindowHandle){
        Driver.startWindowHandle = startWindowHandle;
        log("Set test start window handle: " + startWindowHandle);
    }



    public static String getStartWindowHandle() {
        return Driver.startWindowHandle;
    }


    public void injectjQueryIfNeeded(){
        if (!jQueryLoaded()){
            injectjQuery();
        }
    }



    public Boolean jQueryLoaded(){
        Boolean loaded;
        try {
            loaded = (Boolean) (getJsExecutor().executeScript("return jQuery()!=null"));
        }
        catch(WebDriverException e){
            loaded = false;
        }
        return loaded;
    }



    public void injectjQuery(){


        getJsExecutor().executeScript(
                "var head = document.getElementsByTagName(\"head\")[0];"
                        + "var newScript = document.createElement('script');"
                        + "newScript.type = 'text/javascript';"
                        + "newScript.src = 'http://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js';"
                        + "head.appendChild(newScript);");
    }



    public static JavascriptExecutor getJsExecutor() {
        if (driver instanceof JavascriptExecutor) {
            return (JavascriptExecutor)driver;
        } else {
            throw new IllegalStateException("This driver cannot run JavaScript.");
        }
    }


    public static Object executeScript(String script, Object... arguments){
        return getJsExecutor().executeScript(script, arguments);
    }


    public boolean switchToWindowByUrl(String windowUrl){
        String currentHandle = driver.getWindowHandle();
        Set<String> handles = driver.getWindowHandles();
        boolean found = false;

        for (String handle : handles){
            driver.switchTo().window(handle);
            if (driver.getTitle().contains(windowUrl)){
                found = true;
                break;
            }
        }

        if(!found){
            driver.switchTo().window(currentHandle);
            log("Window with URL containig \"" 
                    + windowUrl  
                    + "\" was not found; driver was not switched."); 

            return false;
        }
        return true;
    }



    public static boolean switchToWindowByTitle(String windowTitle){
        String currentHandle = driver.getWindowHandle();
        Set<String> handles = driver.getWindowHandles();
        boolean found = false;

        for (String handle : handles){
            driver.switchTo().window(handle);
            if (driver.getTitle().contains(windowTitle)){
                found = true;
                break;
            }
        }

        if(!found){
            driver.switchTo().window(currentHandle);
            log("Window with title containig \"" 
                    + windowTitle  
                    + "\" was not found; driver was not switched."); 

            return false;
        }

        log("Switched to window whith title containing \"" 
                + windowTitle + "\".");
        return true;
    }


    /**
     * Get Java script generated errors from Chrome log
     * and return a map of (timeStamp, error) entries. 
     * 
     * Limitation: it only works for Chrome
     */
    public static Map<String, String> getBrowserLogJsErrors(){
        Map<String, String> jsErrors = new TreeMap<>();
        if (Driver.driver != null){

            if (driverType == DriverType.chrome){
                LogEntries logEntries = driver.manage().logs().get(LogType.BROWSER);
                if(logEntries!=null){
                    List<LogEntry> allEntries = logEntries.filter(Level.SEVERE);
                    if (allEntries.size()==0){
                        log("BROWSER_JS_ERROR: No java script errors found.");
                    }

                    else {
                        for (int i=0; i<allEntries.size(); i++){
                            LogEntry logEntry = allEntries.get(i);
                            jsErrors.put(Long.valueOf(logEntry.getTimestamp()).toString(), 
                                    logEntry.getMessage() + " | " + logEntry.getLevel());
                            log("BROWSER_JS_ERROR !!! :" 
                                    + logEntry.getMessage() + " | " 
                                    + logEntry.getLevel());
                        }
                    }
                }
                else {
                    log("BROWSER_JS_ERROR: Could not get the LogEntries.");
                }
            }
            else {
                log("BROWSER_JS_ERROR: Java script errors are not checked for this browser.");
            }
        }
        else {
            log("No browser defined for this test; do not check the java script errors.");
        }
        return jsErrors;
    }



    public static DriverType getDriverType() {
        return driverType;
    }



    /**
     * Get only the first (relevant) info from a Selenium Exception.
     * This is used to add only failure relevant info in the final report.
     * 
     * @param message full Selenium Exception message
     * @return
     */
    public static String getSeleniumExceptionShortMessage(String message) {
        String[] m = message.split("\\(Session info:");
        if (m.length > 0) {
            return m[0].trim();
        }
        
        return "";
    }



    public static File saveElementScreenshot(By locator, File outputFile) {

        return ThrowablesWrapper.wrapThrowable(

                "Failed to save elements screenshot!",

                () -> {

                    WebElement element = driver.findElement(locator);

                    // Get entire page screenshot
                    File fullPageScreenshotFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
                    BufferedImage fullPageImage = ImageIO.read(fullPageScreenshotFile);

                    // Get the location of element on the page
                    Point point = element.getLocation();

                    // Get width and height of the element
                    int elementWidth = element.getSize().getWidth();
                    int elementHeight = element.getSize().getHeight();

                    // Crop the entire page screenshot to get only element screenshot
                    BufferedImage elementScreenshot = fullPageImage.getSubimage(
                            point.getX(), 
                            point.getY(),
                            elementWidth, 
                            elementHeight);

                    File elementScreenshotFile = fullPageScreenshotFile;
                    ImageIO.write(
                            elementScreenshot, 
                            "png", 
                            elementScreenshotFile);


                    // Save the element screenshot to disk
                    FileUtils.copyFile(elementScreenshotFile, outputFile);

                    return elementScreenshotFile;
                });
    }

}

