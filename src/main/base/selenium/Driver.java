package main.base.selenium;

import static main.base.Logger.error;
import static main.base.Logger.getLogDirPath;
import static main.base.Logger.log;
import static main.base.Logger.logAll;
import static main.base.driverResources.DriverResource.exportDriverFromJar;
import static main.base.failures.Failure.failureStackToString;
import static main.base.failures.ThrowablesWrapper.supplyAndMapThrowableToFailure;
import static main.base.selenium.Browser.CHROME;
import static main.base.selenium.Browser.FIREFOX;
import static main.base.selenium.Browser.IE;
import static main.utils.FileUtils.copyFile;
import static main.utils.FileUtils.getRelativePath;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.logging.Level;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.remote.CapabilityType;

import main.base.failures.Failure;
import main.base.failures.ThrowablesWrapper;
import main.utils.FileUtils;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.comparison.ImageDiff;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;
import ru.yandex.qatools.ashot.coordinates.WebDriverCoordsProvider;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

public class Driver {

    private static WebDriver driver;
    private static Browser browser;

    private static final String WEB_DRIVERS_DIR = "webDrivers";
    private static String startWindowHandle;

    public static final Long DEFAULT_IMPLICIT_SECOND_WAIT = 15L;
    public static final Long DEFAULT_IMPLICIT_MILISECOND_WAIT = DEFAULT_IMPLICIT_SECOND_WAIT * 1000;


    public static WebDriver getDriver() {

        return driver;
    }


    private static WebDriver getDriverForBrowser(Browser browser){

        switch (browser){

            case FIREFOX: return getFirefoxDriver();

            case CHROME: return getChromeDriver();

            case IE: return getIeDriver();

            default: throw new Failure("Browser not supported: " + browser);
        }
    }


    public static void startNewDriver(Browser browser) {

        driver = getDriverForBrowser(browser);
    }


    public static Browser getBrowser() {

        return browser;
    }


    public static void closeAllWindows(){

        log("Closing all browser windows but start window.");

        driver.getWindowHandles().stream()

            .filter(isNotStartWindow)

            .forEach(closeWindow);                    
    }


    private static String getDriverPath(Browser browser) {

        return getRelativePath(
                WEB_DRIVERS_DIR, 
                browser.getDriverExecutableName()).toString();
    }


    private static WebDriver getFirefoxDriver(){ 

        browser = FIREFOX;

        exportDriverFromJar(FIREFOX);

        System.setProperty("webdriver.gecko.driver", getDriverPath(browser));

        FirefoxDriver firefoxDriver = new FirefoxDriver();

        firefoxDriver.manage().window().setSize(new Dimension(1920, 1080));

        return firefoxDriver;
    }


    private static WebDriver getChromeDriver(){ 

        browser = CHROME;

        exportDriverFromJar(CHROME);

        System.setProperty("webdriver.chrome.driver", getDriverPath(browser));

        ChromeOptions options = new ChromeOptions();

        options.addArguments(Arrays.asList("--start-maximized"));


        String downloadFolder = getLogDirPath().toString();

        options.setExperimentalOption(
                "prefs", 
                Map.of("download.default_directory", downloadFolder));

        log("Browser download folder: " + downloadFolder);       

        return new ChromeDriver(options);
    }


    private static WebDriver getIeDriver(){ 

        browser = IE;

        exportDriverFromJar(IE);

        System.setProperty("webdriver.ie.driver", getDriverPath(browser));


        InternetExplorerOptions capabilities = new InternetExplorerOptions();
        capabilities.setCapability("ignoreZoomSetting", true);
        capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        capabilities.setCapability("nativeEvents", false);  // element clicks are not working without this setting

        // turn off IE pop up blocker
        // TODO - move this to capabilities if possible
        String cmd = "REG ADD \"HKEY_CURRENT_USER\\Software\\Microsoft\\Internet Explorer\\New Windows\" /F /V \"PopupMgr\" /T REG_SZ /D \"no\"";
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (Exception e) {
            log("Error ocured!");
        }

        WebDriver ieDriver = new InternetExplorerDriver(capabilities);

        ieDriver.manage().window().maximize();

        return ieDriver;
    }


    private static Predicate<String> isNotStartWindow = handle ->

        false == handle.equals(startWindowHandle);


    private static Consumer<String> closeWindow = handle -> {        

        driver.switchTo().window(handle);
        driver.close();
    };


    public boolean switchToWindowByUrl(String windowUrl){

        String currentHandle = driver.getWindowHandle();
        Set<String> allHandles = driver.getWindowHandles();

        for (String handle : allHandles){

            driver.switchTo().window(handle);
            if (driver.getTitle().contains(windowUrl)){

                return true;
            }
        }

        driver.switchTo().window(currentHandle);
        logAll("Window with URL containig \"", 
                windowUrl,  
                "\" was not found; driver was not switched."); 

        return false;
    }


    public static boolean switchToWindowByTitle(String windowTitle){

        String currentHandle = driver.getWindowHandle();
        Set<String> handles = driver.getWindowHandles();      

        for (String handle : handles){

            driver.switchTo().window(handle);
            if (driver.getTitle().contains(windowTitle)){

                return true;
            }
        }

        driver.switchTo().window(currentHandle);
        logAll("Window with title containig \"", 
                windowTitle,  
                "\" was not found; driver was not switched."); 

        return false;
    }


    public static void quitDriver() {               

        if (driver != null) {

            try{
                log("Quit current driver."); 

                closeAllWindows();
                driver.quit();
                
                driver = null;
                browser = null;
            }

            catch(Throwable th){

                error("Error while quitting driver.\n" 
                        + failureStackToString(th));

                driver = null;
                browser = null;
            }
        }
    }


    public static File saveScreenShotWrapped(String fileName){

        return supplyAndMapThrowableToFailure(

                saveScreenshot(fileName),

                "Failed to save screenshot!");    
    }



    private static Callable<File> saveScreenshot(String fileName){ 

        return () -> {

            File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);

            Path destPath = getLogDirPath().resolve(fileName);

            copyFile(scrFile, destPath.toFile());

            log("Saved screenshot: " + destPath.toString());

            return destPath.toFile();
        };
    }


    public static void setStartWindowHandle(String handle){

        startWindowHandle = handle;
        log("Set test start window handle: " + startWindowHandle);
    }


    public static String getStartWindowHandle() {

        return startWindowHandle;
    }


    public static JavascriptExecutor getJsExecutor() {

        return supplyAndMapThrowableToFailure(

                () -> (JavascriptExecutor)driver,

                "This driver cannot run JavaScript.") ;            
    }


    public static Object executeScript(String script, Object... arguments){

        return getJsExecutor().executeScript(script, arguments);
    }



    /**
     * Get Java script generated errors from Chrome log
     * and return a map of (timeStamp, error) entries. 
     * 
     * Limitation: it only works for Chrome
     */
    //TODO - clean this
    public static Map<Long, String> getJSErrorsFromBrowserLog(){

        Map<Long, String> jsErrors = new TreeMap<>();

        if (isChromeDriver()){

            LogEntries logEntries = driver.manage().logs().get(LogType.BROWSER);

            if(logEntries!=null){

                List<LogEntry> severeLogEntries = logEntries.filter(Level.SEVERE);

                for (LogEntry logEntry : severeLogEntries){                            

                    jsErrors.put(
                            logEntry.getTimestamp(), 
                            logEntry.getMessage() + " | " + logEntry.getLevel());

                    logAll("BROWSER_JS_ERROR !!! :", 
                           logEntry.getMessage(), " | ", 
                           logEntry.getLevel().getName());
                }
            }
            else {
                log("BROWSER_JS_ERROR: Could not get the LogEntries.");
            }
        }

        else {
            log("BROWSER_JS_ERROR: Java script errors were not checked.");
        }    

        return jsErrors;
    }


    private static boolean isChromeDriver() {
        
        return ThrowablesWrapper.supplyUnchecked(
        
                () -> browser.equals(CHROME),
                
                false);
    }


    public static boolean areScreenshotsDifferentWrapped(
            Float scaling, 
            By locator1, 
            By locator2,   

            Path outputFile1,
            Path outputFile2,
            Path outputFileDiff) {

        return ThrowablesWrapper.supplyAndMapThrowableToFailure(

                areScreenshotsDifferent(
                        scaling, 
                        locator1, 
                        locator2, 
                        outputFile1, 
                        outputFile2, 
                        outputFileDiff),

                "Failed to compare element screenshot");
    }



    private static Callable<Boolean> areScreenshotsDifferent(
            Float scaling, 
            By locator1, 
            By locator2, 
            Path outputFile1,
            Path outputFile2, 
            Path outputFileDiff) {

        return () -> { 

            var masterScreenshot = saveScreenshot( 

                    getElementAshotScreenshot(scaling, locator1),

                    outputFile1);


            var currentScreenshot = saveScreenshot( 

                    getElementAshotScreenshot(scaling, locator2),

                    outputFile2);


            ImageDiff diff = new ImageDiffer().makeDiff(masterScreenshot, currentScreenshot);

            BufferedImage diffImage = diff.getMarkedImage(); 

            ImageIO.write(diffImage, "png", outputFileDiff.toFile());

            return diff.hasDiff() == false;
        };
    }



    public static boolean areScreenshotsDifferentWrapped(

            Path screenshotFile1,
            Path screenshotFile2,
            Path diffFile) {

        return ThrowablesWrapper.supplyAndMapThrowableToFailure(

                areScreenshotsDifferent(screenshotFile1, screenshotFile2, diffFile),

                "Failed to compare element screenshot");
    }



    private static Callable<Boolean> areScreenshotsDifferent(
            Path screenshotFile1, 
            Path screenshotFile2,
            Path diffFile) {

        return () -> { 


            BufferedImage image1 = ImageIO.read(screenshotFile1.toFile());
            BufferedImage image2 = ImageIO.read(screenshotFile2.toFile());


            ImageDiff diff = new ImageDiffer().makeDiff(
                    new Screenshot(image1),
                    new Screenshot(image2));

            BufferedImage diffImage = diff.getMarkedImage();
            ImageIO.write(diffImage, "png", diffFile.toFile());

            return diff.hasDiff() == false;
        };
    }



    public static Screenshot saveElementScreenshotAshotWrapped(
            Float scaling, 
            By locator, 
            Path outputFile) {

        return ThrowablesWrapper.supplyAndMapThrowableToFailure(

                saveElementScreenshotAshot(scaling, locator, outputFile),

                "Failed to save element screenshot for " + locator);                            
    }



    private static Callable<Screenshot> saveElementScreenshotAshot(
            Float scaling, 
            By locator, 
            Path outputFile) {

        return () -> saveScreenshot( 

                getElementAshotScreenshot(scaling, locator),

                outputFile);
    }



    private static Screenshot saveScreenshot(Screenshot screenshot, Path outputFile) throws IOException{

        BufferedImage screenshotImage = screenshot.getImage();

        ImageIO.write(screenshotImage, "png", outputFile.toFile());

        log("Screenshot saved to: " + outputFile.toString());

        return screenshot;
    }



    private static Screenshot getElementAshotScreenshot(Float scaling, By locator) {

        return new AShot()

                .coordsProvider(new WebDriverCoordsProvider())

                .shootingStrategy(ShootingStrategies.scaling(scaling))

                .takeScreenshot(

                        Driver.driver, 

                        Driver.driver.findElement(locator));
    }



    // this does not work with scaling other than 100%
    // TODO - rethink
    public static File saveElementScreenshot(By locator, Path outputFile) {

        return ThrowablesWrapper.supplyAndMapThrowableToFailure(

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

                    log("Element image: Origin( X=" + point.getX()
                    + ", Y=" + point.getY() + " ) "
                    + "--- width=" + elementWidth
                    + ", height=" + elementHeight);


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
                    FileUtils.copyFile(elementScreenshotFile, outputFile.toFile());

                    return elementScreenshotFile;
                },

                "Failed to save elements screenshot!");
    }

}
