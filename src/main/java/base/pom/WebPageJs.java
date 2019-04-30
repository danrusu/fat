package main.java.base.pom;

import static main.java.base.Logger.debug;
import static main.java.base.Logger.log;
import static main.java.base.Logger.logSplitByLines;
import static main.java.base.pom.WebPage.findUniqElement;

import java.nio.file.Path;
import java.util.Optional;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import main.java.base.failures.Failure;
import main.java.utils.FileUtils;
import main.java.utils.http.HttpVerb;

public interface WebPageJs {

	Path COLOR_SNIPPET_PATH = FileUtils.getRelativePath(
			"resources", 
			"webSnippets",
			"color.js");

    
    public static JavascriptExecutor getJsExecutor(WebDriver driver) {
        
        if (driver instanceof JavascriptExecutor) {
            
            return (JavascriptExecutor)driver;
            
        } else {
            
            throw new Failure("This driver cannot run JavaScript.");
        }
    }
    
    
    
    public static Object executeScript(
            WebDriver driver, 
            String script, 
            WebElement webElement) {
        
        return getJsExecutor(driver).executeScript(script, webElement);
    }
    
    
    
    public static Object executeScript(
            WebDriver driver, 
            String script) {
        
        return getJsExecutor(driver).executeScript(script);
    }
    
    
    
    public static void jsClick(WebDriver driver, By by){


        WebElement webElement = findUniqElement(driver, by);
        
        if (webElement.isEnabled()){

            try{
                
                executeScript(driver, "arguments[0].click();", webElement);
                
            }catch(StaleElementReferenceException e){
                
                logSplitByLines(e.toString());
                
                executeScript(driver, "arguments[0].click();", webElement);
            }
        }
        
        else {
            throw new Failure("Element " + by + " is disabled!");
        }
    }
    
    
    
    public static void jsClick(WebDriver driver, WebElement webElement){


        if (webElement.isEnabled()){

            try{
                
                executeScript(driver, "arguments[0].click();", webElement);
                
            }catch(StaleElementReferenceException e){
                
                logSplitByLines(e.toString());
                
                executeScript(driver, "arguments[0].click();", webElement);
            }
        }
        
        else {
            throw new Failure("Element is disabled!");
        }
    }
    
    
    
    // this are usefull when the check box is not clickable
    public static boolean jsCheck(WebDriver driver, By checkboxBy){
        
        log("Check the checkbox via js" + checkboxBy);
        
        WebElement checkbox = findUniqElement(driver, checkboxBy);
        
        if (checkbox.isSelected()){
            log("Already checked! No action needed.");
        }
        else {
            jsClick(driver, checkboxBy);
        }
        
        return checkbox.isSelected();
    }



    public static boolean jsUncheck(WebDriver driver, By checkboxBy){
        
        log("Uncheck the checkbox via js " + checkboxBy);
        
        WebElement checkbox = findUniqElement(driver, checkboxBy);
        
        if (checkbox.isSelected() == false){
            log("Already unchecked! No action needed.");
        }
        else {
            jsClick(driver, checkboxBy);
        }
        return checkbox.isSelected() == false;
    }

    
    
    public static boolean jsSetCheckbox(
            WebDriver driver, 
            By checkboxBy, 
            boolean checked) {
        
        return checked ?
                
            jsCheck(driver, checkboxBy) :
                
            jsUncheck(driver, checkboxBy);
    }
    
    
    
    /**
     * Run AJAX via Selenium JavascriptExecutor.
     * 
     * @param method - HTTP method - GET/POST
     * @param url - HTTP url
     * @param ajaxProperties - properties for the AJAX call - headers, content-type, accepts 
     * @param base64EncodedBody - body for the AJAX call (e.g. for a POST request)
     * 
     */
    public static String jsAjax(
            WebDriver driver,
            HttpVerb method,
            String url, 
            String ajaxProperties, 
            String formDataJs) {


        String postData = "";

        switch(method){
            //TODO add other HTTP request methods
            case POST:
                postData = "\ndata: formData";
                break;

            default:
                break;
        }

        String script= String.join("\n",
                formDataJs,
                "$.ajax({" + ajaxProperties,

                "url: \"" + url + "\", ",

                "type: \"" + method.name() + "\", ",

                postData,

                "})");

        log("***** jsAjax script:\n" + script);

        return Optional.ofNullable(
               executeScript(driver, script))
                .orElse(new String())
                .toString();
    }



    public static String jsAjaxGet(
            WebDriver driver,
            String url, 
            String ajaxProperties){
        
        return jsAjax(driver, 
                HttpVerb.GET, 
                url, 
                ajaxProperties, 
                "");
    }



    public static String jsAjaxPost(
            WebDriver driver,
            String url, 
            String ajaxProperties,
            String formDataJs){
        
        return jsAjax(driver, 
                HttpVerb.POST, 
                url, 
                ajaxProperties, 
                formDataJs);
    }

    public static String getAjaxPostScript(
            WebDriver driver,
            String url, 
            String ajaxProperties){
        
        String script = String.join("\n",
                
                "$.ajax({" + ajaxProperties,

                "url: \"" + url + "\", ",

                "type: \"" + HttpVerb.POST.name() + "\", ",

                "data: formData",

                "})");

        debug("*** getAjaxPostScript:\n" + script);

        return script;
    }
    
    
    
    /**
     * Add javas cript to Web Page's DOM
     * 
     * @param jsScript - java script code as string
     */
    public static void jsAddScriptToDOM(WebDriver driver, String jsScript){
        
        log("Adding script to curent DOM ...");

        String jsScriptAdder = "var script = document.createElement(\"script\");"
                + "codeText= \"" + jsScript + "\";"
                + "code = document.createTextNode(codeText);"
                + "script.appendChild(code);"
                + "document.head.appendChild(script);";

        executeScript(driver, jsScriptAdder);
    }



    /**
     * Evaluate xPath selector via java script.
     * This is only for usage within Developer Tools.
     * 
     * @return the first web element found or null if no element was found.
     */
    public static void jsAddEvalXpathToDOM(WebDriver driver){
        
        log("Add eval(xpath) to DOM"); 
        jsAddScriptToDOM(driver, getEvalXpathJavascript());
    }
    
    
    
    private static String getEvalXpathJavascript(){
        
        return String.join(
                
                "\n",
                
                "function eval(xpath){ ",
                "var elements =  document.evaluate(xpath,document,null,XPathResult.ORDERED_NODE_SNAPSHOT_TYPE,null);",
                "console.log(\"elements found: \" + elements.snapshotLength);",
                "for (var i=0;i<elements.snapshotLength; i++) {",
                "console.log(\"element\"+i+\": \");",
                "console.log(elements.snapshotItem(i));}",
                "if ( elements.snapshotLength > 0 ) { return elements.snapshotItem(0);}",
                "else {return null;}}");
    }
    
    
    
    public static void jsTriggerEvents(
            WebDriver driver,
            WebElement element, 
            String ...domEvents) {
        
        String script =  String.join("\n", 

                "var trigger = function(){",
                "var [element, ...events] = [].slice.call(arguments);",          
                "events.forEach(function(event){",
                "if ('createEvent' in document) {",
                "var evt = document.createEvent('HTMLEvents');",
                "evt.initEvent(event, false, true);",
                "element.dispatchEvent(evt);",
                "}",
                "else{",
                "element.fireEvent('on' + event); // IE",
                "}",
                "});",
                "};",

                "trigger(element, domEvents);");

        getJsExecutor(driver).executeScript(script, element, domEvents);
    }



    public static void jsTriggerChange(WebDriver driver, WebElement element){
        
        jsTriggerEvents(driver, element, "change");
    }

    

    public static void jqTriggerEnter(WebDriver driver, WebElement element) {
        
        executeScript(
                driver,
                "var e = $.Event( \"keypress\", { which: 13 } ); $(arguments[0]).trigger(e)", 
                element);
    }





    
    public static String getBgColor(WebDriver driver, WebElement element) {
    	injectSnippet(driver, COLOR_SNIPPET_PATH);
    	
    	String color = getJsExecutor(driver).executeScript(
    			"return window.color.getBgColorHex(arguments[0]);", 
    			element) + "";
    	
    	return color;
    }



	public static void injectSnippet(WebDriver driver, Path snippetFilePath) {
		String script = FileUtils.fileToString(snippetFilePath);
		getJsExecutor(driver).executeScript(script);	
	}


}

