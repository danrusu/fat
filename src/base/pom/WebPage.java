package base.pom;
import static base.Logger.log;
import static base.Logger.logSplitByLines;
import static base.pom.ByUtils.byToString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import base.Assert;
import base.Driver;
import base.Driver.DriverType;
import base.failures.Failure;
import utils.DynamicCheck;
import utils.ThreadUtils;


/**
 * Template class for web page mapping (POM template).
 *  
 * Class which extend this will need to add 
 * specific element locators and actions. 
 * 
 * @author Dan Rusu
 *
 */
public class WebPage {

    private String url="";
    private String title="";

    private WebDriver driver;
    private WebDriverWait explicitWait;


    // default timeouts in seconds
    private final long DEFAULT_AJAX_WAIT = 20; 
    private final long DEFAULT_EXPLICIT_WAIT = 10;



    public WebPage(WebDriver driver){
        if (driver==null){
            throw new Failure("No browser driver was created! Driver.driver=null");
        }
        this.driver = driver;
        explicitWait = new WebDriverWait(driver, DEFAULT_EXPLICIT_WAIT);
    }



    public WebPage(WebDriver driver, String defaultUrl, String title){
        this(driver);
        this.url = defaultUrl;
        this.title = title;
    }



    public void checkUrlAndTitle(){
        checkUrlAndTitle(url, title);
    }



    public String getUrl() {
        return url;
    }



    public void setUrl(String url) {
        this.url =url;
    }



    public String getTitle() {
        return title;
    }



    public void setTitle(String title) {
        this.title = title;
    }



    public WebElement findElement(By by){
        log("Find element: " + by);
        return driver.findElement(by);
    }



    public List<WebElement> findElements(By by){
        log("Find elements: " + by);
        return driver.findElements(by);
    }

    
    public static WebElement findUniqElement(WebDriver driver, By by){
        
        log("Find unique WebElement: " + by);

        // this throws org.openqa.selenium.NoSuchElementException if element is not found 
        // implicit wait is applied
        WebElement element = driver.findElement(by); 

        List<WebElement> elements = driver.findElements(by);

        // assert that only one element was found
        Assert.isTrue("Element located by " + by +
                " is unique (found " + elements.size() + ")", 
                elements.size() == 1);

        return element; 
    }


    // Important !!!
    // Use this if you will perform actions against the found web element.
    // If element is found by driver's findElement it asserts that it's unique in the current page.
    public WebElement findUniqElement(By by){
        return findUniqElement(driver, by);       
    }



    //TODO - FIX THIS !!! or find better implementation
    // DO NOT USE THIS YET!
    public List<WebElement> findElements(By... progressiveSelectors){
        log("Find elements by chaining selectors: " + progressiveSelectors.toString());
        List<WebElement> elements = findElements(progressiveSelectors[0]);

        for( int i=1; i<progressiveSelectors.length; i++){

            List<WebElement> currentElements = new ArrayList<>(); 

            for (WebElement element : elements){
                try{
                    currentElements = element.findElements(progressiveSelectors[i]);
                }
                catch(Exception ex){
                    // element not found / stale element ... or other
                }
            }

            elements = currentElements;
        }
        return elements;
    }



    /**
     * Getter for this.driver.
     * 
     * @return WebDriver
     */
    public WebDriver getDriver() {
        return this.driver;
    }



    /**
     * Getter for the explicit wait.
     * 
     * @return WebDriverWait
     */
    public WebDriverWait getExplicitWait() {
        log("Get explicit wait: " + this.explicitWait + "s");
        return this.explicitWait;
    }



    /**
     * Get new WebDriverWait.
     * 
     * @return WebDriverWait
     */
    public WebDriverWait getExplicitWait(long seconds) {
        log("Get explicit wait: " + seconds + "s");
        return new WebDriverWait(driver, seconds);
    }



    /**
     * Click web element by selector.
     * 
     * @param by - web element locator 
     */
    public void click(By by){	      
        log("Click " + by);
        explicitWait.until(ExpectedConditions.elementToBeClickable(by));		
        findUniqElement(by).click();	
    }


    public void instantClick(By by){	
        log("instant click " + by);
        getExplicitWait(0).until(ExpectedConditions.elementToBeClickable(by)).click();
    }



    /**
     * Click web element by selector.
     * 5 retries, 2 seconds delayed if it does not work.
     * 
     * @param by - web element locator 
     */
    public void mClick(By by){	
        String failure="";

        log("Click " + by);
        explicitWait.until(ExpectedConditions.elementToBeClickable(by));	

        for (int i=0; i<5; i++){
            failure = "";
            log("Click " + by + " atempt: " +  i);
            try {
                findUniqElement(by).click();
                break;
            }
            catch(Exception e){
                failure = e.getMessage();
                logSplitByLines(failure);
            }
            ThreadUtils.sleep(2000);
        }

        Assert.isTrue(failure, failure.isEmpty());
    }



    /**
     * Click web element; 
     * 5 retries, 2 seconds delayed if it does not work.
     * 
     * @param by - web element  
     */
    public void mClick(WebElement we){	
        String failure="";

        log("Click " + we);
        explicitWait.until(ExpectedConditions.elementToBeClickable(we));	

        for (int i=0; i<5; i++){
            failure = "";
            log("Click " + we + " atempt: " +  i);
            try {
                we.click();
                break;
            }
            catch(Exception e){
                failure = e.getMessage();
                logSplitByLines(failure);
            }
            ThreadUtils.sleep(2000);
        }

        Assert.isTrue(failure, failure.isEmpty());
    }







    public void jqClick(String cssSelector){
        log("Click by selector (jquery): " + cssSelector);
        try{
            String script = "$('" + cssSelector + "').eq(0).click();";
            log("Executing js:" + script);
            executeScript(script);

        }catch(Exception e){
            logSplitByLines(e.toString());
            throw new Failure("Cannot click via js by selector: " + cssSelector);

        }

    }



    /**
     * Click web element via jQuery.
     * 
     * @param by - web element locator
     */
    public void jqClick(By by){
        log("Click (jQuery): " + by);
        WebElement we = driver.findElement(by);
        if (we.isEnabled()){
            try{
                executeScript(
                        "$(arguments[0]).click();", we);

            }catch(StaleElementReferenceException e){
                logSplitByLines(e.toString());
                executeScript(
                        "arguments[0].click();", driver.findElement(by));
            }
        }
        else {
            Assert.fail("Element " + we + "is disabled!");
        }
    }



    /**
     * Click web element via jQuery.
     * 
     * @param WebElement
     */
    public void jqClick(WebElement element){
        log("Click (jQuery): " + element);
        if (element.isEnabled()){
            try{
                executeScript(
                        "$(arguments[0]).click();", element);

            }catch(StaleElementReferenceException e){
                logSplitByLines(e.toString());
                executeScript(
                        "arguments[0].click();", element);
            }
        }
        else {
            Assert.fail("Element " + element + "is disabled!");
        }
    }



    public String jqGetText(By by){
        log("Get text (jQuery): " + by);
        WebElement we = driver.findElement(by);
        return executeScript(
                "$(arguments[0]).text();", we).toString();
    }



    /**
     * Send strings to text input elements and clear.
     * 
     * @param str - String to send
     * @param by - web element locator
     */
    public void clearAndSendString(By inputBy, String str){
        explicitWait.until(ExpectedConditions.visibilityOfElementLocated(inputBy)).click();	
        //wait.until(ExpectedConditions.presenceOfElementLocated(by));
        //		wait.until(ExpectedConditions.elementToBeClickable(by));
        // TODO - investigate why the above doesn't work in some cases

        log("Clear " + inputBy);
        clearInput(inputBy);
        log("Send text to " + inputBy + " text = " + str);
        driver.findElement(inputBy).sendKeys(new String[]{ str });
    }


    public void clearAndSendString(WebElement element, String str){
        //explicitWait.until(ExpectedConditions.visibilityOfAllElements(element)); 
        // doesn't work in some cases

        clearInput(element);
        ThreadUtils.sleepQuiet(500);
        element.sendKeys(new String[]{ str });
    }


    /**
     *  Clear input method - multiple cases
     *   
     *  @param element - WebElement
     **/	
    private void clearInput(WebElement element) {
        try
        {
            element.clear();
        }
        catch (Exception e)
        {
            logSplitByLines("ClearInput " + element.toString() + "\n" + e);
        }


        if( ! element.getText().isEmpty()) {
            log("Input clear is not working. Try to clear field using \"Back space\" key");  
            int len = element.getText().length();
            for (int i=0;i<len;i++)
            {
                element.sendKeys(Keys.BACK_SPACE);
            }
        }
    }



    /**
     *  Clear input method - multiple cases
     *   
     *   @param inputBy - By
     **/	
    public void clearInput(By inputBy) {
        WebElement input = driver.findElement(inputBy);
        try
        {
            input.clear();
        }
        catch (Exception e)
        {
            logSplitByLines("ClearInput " + inputBy.toString() + "\n" + e);
        }


        if( ! getValueJs(inputBy).isEmpty()) {
            log("Input clear is not working. Try to clear field using \"Back space\" key"); 
            int len=getValueJs(inputBy).length();
            for (int i=0;i<len;i++)
            {
                input.sendKeys(Keys.BACK_SPACE);
            }
        }


    }


    /**
     * Click by element, send strings to text input elements and clear, press Enter
     * 
     * @param str - String to send
     * @param by - web element locator
     */
    public void clearSendStringAndEnter(By by, String str) {
        explicitWait.until(ExpectedConditions.visibilityOfElementLocated(by)).click();	
        ThreadUtils.sleepQuiet(500);
        clearAndSendString(by, str);
        ThreadUtils.sleepQuiet(1000);
        findElement(by).sendKeys(Keys.ENTER);
        waitForAjaxFinish();
    }



    /**
     * Send strings to text input elements and clear, press Enter
     * 
     * @param str - String to send
     * @param by - web element locator
     */
    public void clearSendStringAndEnter(WebElement element, String str) {
        //	explicitWait.until(ExpectedConditions.visibilityOf(element)).click();	// click is not working all the time
        //	ThreadUtils.sleepQuiet(500);
        clearAndSendString(element, str);
        ThreadUtils.sleepQuiet(1000);
        element.sendKeys(Keys.ENTER);
        waitForAjaxFinish();
    }



    public void clearAndSendKeys( By by, CharSequence chr){
        explicitWait.until(ExpectedConditions.visibilityOfElementLocated(by)).click();	
        //wait.until(ExpectedConditions.presenceOfElementLocated(by));
        //		wait.until(ExpectedConditions.elementToBeClickable(by));
        // TODO - investigate why the above doesn't work in some cases

        log("Clear " + by);
        driver.findElement(by).clear();
        log("Send text to " + by + " text = " + chr);
        driver.findElement(by).sendKeys(chr);
    }


    /**
     * Get text from web element.
     * 
     * @param by - web element locator
     * @param waitForVisibility - wait for an element to be visible ("true") or not ("false")
     * @param explicitWait - time to wait for an element to be visible
     * 
     * @return - the web element innerHTML
     */ 
    public String getText(
            By by, 
            boolean waitForVisibility,
            WebDriverWait explicitWait){

        log("Get text from " + by);
        if (waitForVisibility) {
            return explicitWait.until(ExpectedConditions.visibilityOfElementLocated(by))
                    .getText()
                    .trim();
        }
        return findUniqElement(by).getText().trim();
    }


    /**
     * Get text from web element.
     * 
     * @param by - web element locator
     * @param waitForVisibility - wait for an element to be visible ("true") or not ("false") 
     *                         with the default explicitWait                                                             
     *      
     * @return - the web element innerHTML
     */    
    public String getText(By by, boolean waitForVisibility){
        return getText( by, waitForVisibility, getExplicitWait());        
    }


    /**
     * Get text from web element.
     * 
     * @param by - web element locator
     * @param WebDriverWait - use an explicit wait 
     * 
     * @return - the web element innerHTML
     */    
    public String getText(By by, WebDriverWait explicitWait){
        return getText(by, true, explicitWait);
    }

    /**
     * Get text from web element.
     * 
     * @param by - web element locator
     * @return - the web element innerHTML
     */
    public String getText(By by){
        return getText(by, true);
    }


    /**
     * Check the web page title.
     * 
     * @param title - string for matching the page title  
     * @return - true if page title matches the parameter, false otherwise
     */
    public boolean checkPageTitle(String title){
        log("Check page title: " +  title);
        explicitWait.until(ExpectedConditions.titleContains(title));
        return true;
    }



    /**
     * Set web element attribute via java script.
     * 
     * @param driver - current web driver 
     * @param e - web element for setting the attribute
     * @param attributeName - name of the attribute to set
     * @param value - value to set to the attributeName
     */
    public void setAttribute(By by, 
            String attributeName, 
            String value){
        WebElement e = driver.findElement(by);
        String scriptSetAttr = "arguments[0].setAttribute(arguments[1], arguments[2]);";
        log("Set attribute (javascript): name=" + attributeName
                + ", value=" + value 
                + ", locator: " + by);
        executeScript(scriptSetAttr, e, attributeName, value);
    }



    public Object executeScript(String scriptJS, Object... arguments){
        return getJsExecutor().executeScript(scriptJS, arguments);
    }


    public String getInnerHTML(By by){
        return getAttribute(by, "innerHTML");
    }


    // getAttribute() is used when the element in not visible but exists in the DOM
    public String getAttribute(By by, 
            String attributeName){
        return findUniqElement(by).getAttribute(attributeName);
    }


    public String getAttributeJs(By by, 
            String attributeName){
        WebElement e = driver.findElement(by);
        String scriptSetAttr = "return arguments[0].getAttribute(arguments[1]);";
        log("Get attribute (javascript): name=" + attributeName
                + ", locator: " + by);
        return Optional.ofNullable(
                executeScript(scriptSetAttr, e, attributeName))
                .orElse("Attribute " + attributeName + " not found!")
                .toString();
    }

    
    
    public void setValueJs(By by, 
            String value){

        log("Set value (javascript):  value=" + value
                + ", locator=" + by);
        
        WebElement e = driver.findElement(by);
        
        String scriptSetAttr = "arguments[0].value=arguments[1];";
        
        executeScript(scriptSetAttr, e, value);

    }



    public void setValueJs(WebElement we, 
            String value){

        String script = "$( arguments[0] )" 
                + ".val(\"" + value + "\");";

        log("Set value (javascript):  value=" + value
                + ", element=" + we);
        executeScript(script, we);
    }



    public void setNumberValueJs(By by, 
            String value){
        log("Set number value (javascript):  value=" + value
                + ", locator=" + by);
        WebElement e = driver.findElement(by);

        String scriptSetAttr = "arguments[0].value=Number(arguments[1]);";

        executeScript(scriptSetAttr, e, value);

    }



    public void setValueAndUpdate(WebElement we, 
            String value){
        //wait.until(ExpectedConditions.visibilityOf(we));

        String script = "$( arguments[0] )" 
                + ".val('" + value + "')"
                + ".keydown().change();";

        log("Set value + update (jQuery):  value=" + value
                + ", element=" + we);
        executeScript(script, we);
        waitForAjaxFinish();
    }



    public void setValueAndUpdate(By byCssSelector, 
            String value){

        explicitWait.until(ExpectedConditions.visibilityOfElementLocated(byCssSelector));

        String script = "$(\"" + byCssSelector.toString().substring(16) + "\")" 
                + ".val(\"" + value + "\")"
                + ".keydown().change();";

        log("Set value + update (jQuery):  value=" + value
                + ", locator=" + byCssSelector);

        executeScript(script);		
        waitForAjaxFinish();
    }



    
   

    public String getValueJs(By by){
        
        WebElement e = findUniqElement(by);
        String scriptSetAttr = "return arguments[0].value;";
        
        String value = executeScript(scriptSetAttr, e).toString();
        log("GetValueJs " + by + " : " + value);
        return value;
    }

    
    
    public String getValue(By by){    
   
        return getExplicitWait(5)
                .until(ExpectedConditions.visibilityOfElementLocated(by))
                .getAttribute("value");        
    }


    
    
    public JavascriptExecutor getJsExecutor(){
        return Driver.getJsExecutor();
    }



    /**
     * @return - page URL
     */
    public String getCurrentUrl(){
        return driver.getCurrentUrl();
    }



    public String getCurrentTitle() {
        return getDriver().getTitle();
    }




    /**
     * Wait secondsWait seconds for page to load - check document.readyState.
     * 
     * @return true when page is loaded in less then secondsWait; false otherwise.
     */
    public void waitForPageLoaded(long seconds) {
    	        
    	WebDriverWait wait = new WebDriverWait(this.driver, seconds);
    	
        Function<WebDriver, Boolean> isDocumentReady = new Function<WebDriver, Boolean>() {
        
        	@Override
            public Boolean apply(WebDriver d) {
                return ((JavascriptExecutor)d)
                        .executeScript("return document.readyState")
                        .equals("complete");
            } 
        };		

        wait.until(isDocumentReady);
    }



    public void setImplicitWait(long miliSeconds) {
        driver.manage().timeouts().implicitlyWait(miliSeconds, TimeUnit.MILLISECONDS);
    }



    /**
     * Resets driver's implicit wait.
     */
    public void resetImplicitWait() {
        setImplicitWait(0);
    }



    /**
     * Sets driver's implicit wait to the default value.
     */
    public void setDefaultImplicitWait() {
        setImplicitWait(Driver.getDefaultWait()*1000);
    }



    /**
     * Wait until all AJAX requests are finished; timeouts after DEFAULT_AJAX_WAIT. 
     * 
     */ 
    public void waitForAjaxFinish(){
        waitForAjaxFinish(this.DEFAULT_AJAX_WAIT);
    }



    /**
     * Wait until all AJAX requests are finished; timeouts after secondsWait.
     * 
     * @param secondsWait - timeout for all AJAX requests to finish
     */
    public static void waitForAjaxFinish(WebDriver driver, long secondsWait){
        log("Wait " + secondsWait + " seconds for all AJAX requests to finish.");
        // wait 1/2 sec for AJAX to start
        // TODO - move this on another thread that starts before triggering AJAX
        ThreadUtils.sleepQuiet(500);
        try {
            new WebDriverWait(driver, secondsWait).until(
                    (WebDriver d) -> ((JavascriptExecutor)d).executeScript("return jQuery.active==0")
                    );
            log("All AJAX requests finished.");
        }
        catch (TimeoutException te){
            // throw assertion error if failed to set the message from the report
            Assert.isTrue("AJAX requests were not finished after " + secondsWait + "s.",
                    false);
        }
        // generic 1s wait for Elastic search to react on AJAX request
        // however this is not needed for requests calls that do not change the Elastic database
        ThreadUtils.sleepQuiet(1000);
    }



    /**
     * Wait until all AJAX requests are finished; timeouts after secondsWait.
     * 
     * @param secondsWait - timeout for all AJAX requests to finish
     */
    public void waitForAjaxFinish(long secondsWait){
        log(this.getClass().getName() + 
                ": Wait " + secondsWait + " seconds for all AJAX requests to finish.");
        // wait 1/2 sec for AJAX to start
        // TODO - move this on another thread that starts before triggering AJAX
        ThreadUtils.sleepQuiet(500);
        try {
            new WebDriverWait(driver, secondsWait).until(
                    (WebDriver d) -> ((JavascriptExecutor)d).executeScript("return jQuery.active==0")
                    );
            log("All AJAX requests finished.");
        }
        catch (TimeoutException te){
            // throw assertion error if failed to set the message from the report
            Assert.isTrue("AJAX requests were not finished after " + secondsWait + "s.",
                    false);
        }
        // generic 1s wait for Elastic search to react on AJAX request
        // however this is not needed for requests calls that do not change the Elastic database
        ThreadUtils.sleepQuiet(1000);
    }




    public void dragAndDrop(By sourceBy, By targetBy){
        WebElement source = driver.findElement(sourceBy);
        WebElement target = driver.findElement(targetBy);
        (new Actions(driver)).dragAndDrop(source, target).perform();
    }



    public void jsScrollDown(WebElement scrollableElement){
        executeScript("arguments[0].scrollDown += 100", scrollableElement);
    }



    public void jsScrollUp(WebElement scrollableElement){
        executeScript("arguments[0].scrollDown -= 100", scrollableElement);
    }



    // TO DO - redesign this ...???
    public void jsScrollIntoviewVertical( By locator, String scrollableParentCssLocator) {	
        
        log("Scroll element " + locator + " into view.");
        String script = "pos=arguments[0].offsetTop;"
                + "console.log('position='+pos);"
                + "$(arguments[0]).parents(arguments[1]).eq(0).scrollTop(pos);";

        executeScript(script, 
                driver.findElement(locator),
                scrollableParentCssLocator);
    }


    public void jsScrollIntoview(By locator) {      

        WebElement el = findUniqElement(locator);       

        log("Scroll into view (js) for element " + locator);
        String script =  "arguments[0].scrollIntoView();";
        executeScript(script, el);
    }    


    /**
     * Moves the mouse pointer via java script over an web element.
     * This is sensitive to any real mouse movement !!!
     * Avoid using this if possible !!!
     * 
     * Does not work for Firefox !!!!!
     * 
     * @param locator - web element locator
     */
    public void moveMouseOverElement(By locator){
        log("Move mouse to " + locator);
        WebElement we = driver.findElement(locator);

        // TODO - find workaround ... really difficult !!!
        if (Driver.getDriverType().equals(DriverType.firefox)){
            Assert.fail("Mouse move over element does not work in Firefox due to a Selenium issue: "
                    + "https://github.com/SeleniumHQ/selenium/issues/2285");
        }

        /*Locatable hoverItem = (Locatable) we;
		Mouse mouse = ((HasInputDevices) driver).getMouse();

		Coordinates coordinates = hoverItem.getCoordinates();
		log("Move mouse to (" + coordinates.onPage().getX() 
				+ "," +  coordinates.onPage().getX() + ")" );
		mouse.mouseMove(coordinates);*/

        Actions action = new Actions(driver);
        action.moveToElement(we).clickAndHold().release().build().perform();
    }




    // Tables functionality *******************************************************
    public String getTableHeaderIndexOld(
            By xPathTableLocator, 
            String attributeName, 
            String attributeValue){

        By xPathTableHeader = By.xpath(byToString(xPathTableLocator)
                + "//th[@" + attributeName + "=\"" + attributeValue + "\"]");

        // first find the wanted th table cell, before counting preceding th cells
        driver.findElement(xPathTableHeader);
        log("Table th found: " + xPathTableHeader);

        By precedingThsLocator = By.xpath(
                byToString(xPathTableHeader)
                + "/preceding-sibling::th");
        log("Counting elements: " + precedingThsLocator);

        resetImplicitWait();
        int index = driver.findElements(precedingThsLocator).size() + 1;
        setDefaultImplicitWait();

        return Integer.toString(index);
    }


    public String getTableHeaderIndex(
            By tableLocator, 
            String thAttributeName, 
            String thAttributeValue){

        return getElementIndexInSiblingsList(
                tableLocator, 
                By.cssSelector("th[" + thAttributeName + "=\"" + thAttributeValue + "\"]"),
                "th");
    }



    public String getElementIndexInSiblingsList(
            By containerLocator, 
            By elementLocatorWithinContainer,
            String siblingTagName){


        String precedingElementsXpathLocator = "./preceding-sibling::" + siblingTagName ;

        // first find the wanted element
        WebElement table = findUniqElement(containerLocator);

        String index = "-1"; // not found 

        try {
            resetImplicitWait();

            WebElement element = table.findElement(elementLocatorWithinContainer);

            // count preceding sibling
            log("Counting preceding elements: " + precedingElementsXpathLocator);

            index = Integer.toString(
                    element.findElements(By.xpath(precedingElementsXpathLocator)).size() + 1);
        }

        catch(Exception e) {
            log("Element " + elementLocatorWithinContainer 
                    + " was not found  in " + containerLocator 
                    + "\n" + e);
        }

        finally{
            setDefaultImplicitWait();
        }

        log("Index: " + index);
        return index;		
    }



    public WebElement getCellByIndex(By tableLocator, String rowIndex, String columnIndex) {

        By cellSelector = By.cssSelector("tr:nth-of-type("+rowIndex+") td:nth-of-type("+columnIndex+")");

        log(byToString(tableLocator) + " " + byToString(cellSelector));

        return findElement(tableLocator).findElement(cellSelector);
    }



    public String getCellTextByIndex(By tableLocator, String rowIndex, String columnIndex) {
        return getCellByIndex(tableLocator, rowIndex, columnIndex).getText().trim();
    }
    // end tables utilities



    // Inputs *******************************************************
    // Check box
    public boolean setCheckBoxWithScrollIntoView(By checkboxLocator, boolean checked) {
        
        return checked ? 
                
            checkWithScrollIntoView(checkboxLocator) :
            
            uncheckWithScrollIntoView(checkboxLocator);
    }
    
    
    
    public boolean setCheckBox(By checkboxLocator, boolean checked) {
        
        return checked ? 
                
                check(checkboxLocator) :
                    
                uncheck(checkboxLocator);
    }




    public boolean checkWithScrollIntoView(By checkboxLocator){
        
        jsScrollIntoview(checkboxLocator);
        
        return check(checkboxLocator);
    }



    public boolean check(By checkboxLocator) {
        
        log("Check the checkbox " + checkboxLocator);
        WebElement checkbox = driver.findElement(checkboxLocator);
        
        if (checkbox.isSelected()){
            log("Already checked! No action needed.");
        }
        else {
            checkbox.click();
        }
        
        return checkbox.isSelected();
    }



    public boolean uncheckWithScrollIntoView(By checkboxLocator){
        
        jsScrollIntoview(checkboxLocator);
        
        return uncheck(checkboxLocator);
    }



    public boolean uncheck(By checkboxLocator) {
        
        log("Uncheck the checkbox " + checkboxLocator);
        WebElement checkbox = driver.findElement(checkboxLocator);
        
        if (!checkbox.isSelected()){
            log("Already unchecked! No action needed.");
        }
        else {
            checkbox.click();
        }
        
        return checkbox.isSelected() == false;
    }



    public boolean isCheckboxSelected(By by){
        
        return findUniqElement(by).isSelected();
    }

    /**
     * Find elements in the page and validate that they are unique.
     * Throws NoSuchElementException if an element is not found.
     * Throws AssertionError if an element is not unique.
     * 
     * @param elementsSelectors - web selectors
     */
    public void validateUniqElements(By ...elementsSelectors){
        log("Validate WebElements:  " + elementsSelectors);
        Arrays.asList(elementsSelectors).forEach(
                this::findUniqElement);
    }



    public void validateUniqElements(Enum<?> ...ids){
        
        log("Validate WebElements:  " + ids);
        
        Arrays.asList(ids).stream()
            .map( x -> By.id(x.name()) )
            .collect(Collectors.toList())
            .forEach(this::findUniqElement);
    }



    public void validateUniqElementsWithIdsFromEnum(Enum<?>[] enumValues){
        By[] selectors = new By[enumValues.length];
        for (int i=0; i < enumValues.length; i++){
            selectors[i] = (By.id(enumValues[i].name()));
        }
        validateUniqElements(selectors);
    }



    public String getColumnHeaderIndex(By xPathLocator){
        
        resetImplicitWait();
        By countingLocator = By.xpath(byToString(xPathLocator) + "/preceding-sibling::th");

        log("Find elements: " +  countingLocator);
        List<WebElement> precedingSiblings = driver.findElements(countingLocator);
        setDefaultImplicitWait();
        return Integer.toString(precedingSiblings.size()+1);
    }



    // containerXpath - to distinguish between different pages with similar content
    
    public String getColumnHeaderIndex(By containerXpath, String dataField) {
        return getColumnHeaderIndex(
                By.xpath(byToString(containerXpath) 
                        + "//th[@data-field=\"" + dataField +"\"]"));
    }



    public void checkUrlAndTitle(String url, String title, long timeoutInMiliSeconds) {
        
        if (url.isEmpty() == false){
            
            Assert.isTrue(
                    
                    "Check URL: "
                            + "expected: \"" + url 
                            + "\"; actual: \"" + getCurrentUrl() + "\"",
                            
                     DynamicCheck.waitUntilFunctionReturnsExpectedValue(
                             timeoutInMiliSeconds,
                             500, 
                             (WebPage x) -> x.getCurrentUrl(), 
                             this, 
                             url));
        }
        
        if (title.isEmpty() == false){
            
            Assert.isTrue(
                    
                    "Check title: "
                            + "expected: \"" + title
                            + "\"; actual: \"" + getCurrentTitle() + "\"",
                            
                     DynamicCheck.waitUntilFunctionReturnsExpectedValue(
                             timeoutInMiliSeconds,
                             500, 
                             (WebPage x) -> x.getCurrentTitle(), 
                             this, 
                             title));
        }
    }



    public void checkUrlAndTitle(String url, String title) {
        checkUrlAndTitle(url, title, 3000);
    }

    
    
    public void checkUrl(String url, long timeoutInMiliSeconds) {
        checkUrlAndTitle(url, "", timeoutInMiliSeconds);
    }

    
    
    public void checkTitle(String title, long timeoutInMiliSeconds) {
        checkUrlAndTitle("", title, timeoutInMiliSeconds);
    }




    public void clickAndHandleStaleElementReference(By byLocator, long timeOutInSeconds){

        getExplicitWait(timeOutInSeconds)

            .ignoring(StaleElementReferenceException.class)
            
            .until(new Function<WebDriver, Boolean>() {
                @Override
                public Boolean apply(WebDriver driver) {
                    driver.findElement(byLocator).click();
                    return true;
                }
            });
    }




    // COOKIES
    public Set<Cookie> getAllCookies() {
        return driver.manage().getCookies();
    }



    public void deleteAllCookies() {
        driver.manage().deleteAllCookies();
    }



    public Cookie getCookie(String cookieName) {
        return driver.manage().getCookieNamed(cookieName);
    }



    public void deleteCookie(String cookieName) {
        driver.manage().deleteCookieNamed(cookieName);
    }



    public void addCookie(Cookie cookie) {
        driver.manage().addCookie(cookie);
    }



    /**
     * Get only the first (relevant) info from a Selenium Exception.
     * This is used to add only failure relevant info in the final report.
     * 
     * @param message full Selenium Exception message
     * @return
     */
    public static String getSeleniumExceptionShortMessage(String message) {
        
        return getFirstFromArrayOrDefault(message.split("\n"), "");              
    }
    
    
    
    public static String getSeleniumExceptionShortMessage(Throwable throwable) {
        
        return getFirstFromArrayOrDefault(
                throwable.toString().split("\n"),
                "");
    }
    
    
    
    public static <T> T getFirstFromArrayOrDefault(T[] array, T defaultValue) {
        
        return List.of(array).stream()
                .findFirst()
                .orElse(defaultValue);
    }
    
}

