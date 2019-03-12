package main.base.testCase;

import static main.base.Logger.log;
import static main.base.Logger.logSplitByLines;
import static main.base.runnerConfig.TestCaseAttribute.expectedFailureRegExp;
import static main.base.runnerConfig.TestCaseAttribute.failure;
import static main.base.runnerConfig.TestCaseAttribute.skip;
import static main.base.xml.XmlDynamicData.evaluateAttributeValue;
import static main.base.xml.XmlDynamicData.getSavedData;
import static main.base.xml.XmlDynamicData.saveData;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.openqa.selenium.By;

import main.base.Assert;
import main.base.AssertCondition;
import main.base.JvmArgs;
import main.base.failures.Failure;
import main.base.pom.WebPage;
import main.base.runnerConfig.TestCaseAttribute;
import main.base.xml.XmlDynamicData;
import main.utils.FileUtils;
import main.utils.StringUtils;
import main.utils.SystemUtils;

/**
 * Generic test Case.
 * 
 * @author Dan Rusu
 *
 */
abstract public class TestCase implements Runnable, TestCaseScenario{	

    protected Map<String, String> testCaseAttributes;
    protected boolean internalTest;



    /**
     * Constructor 
     */
    public TestCase() {		
        this.internalTest = false;
    }


    /**
     * Get the test attributes.
     * 
     * @return - a map of test's attributes
     */
    public Map<String, String> getTestCaseAttributes() {
        return Optional.ofNullable(testCaseAttributes).orElse(new TreeMap<>());
    }


    
    /**
     * Set the test's attributes.
     * 
     * @param testAttributes - test's attributes map 
     */
    public void setTestCaseAttributes(Map<String, String> testCaseAttributes, Path dataProviderFile) {
        
        this.testCaseAttributes = testCaseAttributes;
        
        dynamicEval(testCaseAttributes, dataProviderFile);
                                
    }



    public void addAttribute(String name, String value){
        this.testCaseAttributes.put(name, value);
    }



    public void addNote(String noteText){
        this.testCaseAttributes.put("note", noteText);
    }

    

    public void addAttributeIfValueNotEmpty(String name, String value){

        if (!value.isEmpty()){ 
            addAttribute(name, value);
        }
    }



    public String nullToEmptyString(String s) {

        return StringUtils.nullToEmptyString(s);
    }



    public void dynamicEval(Map<String, String> testCaseAttributes, Path dataProviderFile) {
        
        //Map<String, String> attributes = testCaseAttributes;

        // eval "save" first
        saveAll(evalAttribute("save"), dataProviderFile);

        testCaseAttributes.keySet().forEach(

                key -> {
                    
                    if (! key.equals("save")){

                        String rawValue = nullToEmptyString(testCaseAttributes.get(key));
                        String evaluatedValue = XmlDynamicData.evaluateAttributeValue(
                                dataProviderFile,
                                testCaseAttributes.get("dataProviderIndex"), 
                                rawValue);

                        if ( ! rawValue.equals(evaluatedValue) ){
                            testCaseAttributes.replace(key, evaluatedValue);
                            log("Attribute replaced: " + key + "=" + evaluatedValue);
                        }
                    }
                });
    }



    /**
     * Save String variable into memory so that 
     * they can be used for following test cases. 
     * 
     * @param saveString
     */
    public void saveAll(String saveString, Path dataProviderFile) {

        if (! saveString.isEmpty()){

            List.of(saveString.split(";")).forEach(

                    savedNameAndValue -> {

                        String savedName =  savedNameAndValue.split("=")[0];
                        String savedValue =  savedNameAndValue.split("=")[1];
                        
                        saveData(
                                savedName, 
                                evaluateAttributeValue(dataProviderFile, "", savedValue));

                    });

            String saved = getSavedData().toString(); 
            testCaseAttributes.put("save", saved);
            log("Saved data: save=" + saved);
        }
    }



    /**
     * Save test case results (field values) into memory 
     * at the end of a test case so that they can be reported 
     * or used by the following test cases.
     * 
     * Fields that can be saved will be listed in the 
     * getTestScenario method, Results section.
     * 
     * Use the "saveResults" attribute for this.
     * 
     */
    public void saveResults(){

        List<String> saveList = evalListAttribute("saveResults", ";");

        saveList.forEach(saveNameAndFieldName -> {

            String saveName =  saveNameAndFieldName.split("=")[0];
            String fieldName =  saveNameAndFieldName.split("=")[1];

            try {
                Field field = this.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);					

                Object objectValue = field.get(this);

                //TODO - check that field is String or String[]
                if (objectValue != null){

                    if (objectValue.getClass().isArray()) {

                        saveData(
                                saveName,

                                List.of((String[])objectValue).stream()                                    
                                    .collect(Collectors.joining("||")));
                    }
                    else {
                        saveData(saveName, objectValue.toString());

                    }
                }

                else {
                    throw new Failure(String.join("",
                            "Saving results failed! Field ", 
                            this.getClass().getSimpleName(),
                            ".",
                            fieldName,
                            " is null!"));
                }


            }catch (NoSuchFieldException
                    |SecurityException
                    |IllegalArgumentException
                    |IllegalAccessException e) {

                throw new Failure(e, "Saving results failed!");
            }
        });

        String saved = getSavedData().toString(); 

        testCaseAttributes.put("save", saved);
    }



    public String evalAttribute(String attribute){

        return nullToEmptyString(getTestCaseAttributes().get(attribute));
    }


    public float evalFloatAttribute(String attribute, int defaultValue){

        try {

            return Float.parseFloat(
                    nullToEmptyString(getTestCaseAttributes().get(attribute)));

        } catch (NumberFormatException e) {
            log(" " + e);
            return defaultValue;
        }       
    }



    public String evalAttribute(String attribute, String defaultValue){

        return Optional.ofNullable(getTestCaseAttributes().get(attribute)).orElse(defaultValue);
    }



    public Map<Integer, Map<String, String>> evalMssqlQueryResultAttribute(String attribute){

        return FileUtils.readObject(
                evalPathAttribute(attribute), 
                () -> new TreeMap<Integer, Map<String, String>>());
    }
    
    
    
    public Map<String, String> evalMapAttribute(String mapFileName){
        
       return FileUtils.readObject(
                evalPathAttribute(mapFileName), 
                () -> new TreeMap<String, String>());        
    }



    /* by attributes 
     * 
     * selector="id:idSelector"
     * selector="css:cssSelector"
     * selector="xpath:xpathSelector"
     */
    public By evalByAttribute(String attribute) {

        String[] selectorTokens = attribute.split(":");
        if (selectorTokens.length < 2) {
            throw new Failure("Wrong selector: " + attribute);
        }

        String selectorType = selectorTokens[0];
        String selector = attribute.replaceFirst("^" + selectorType, "");

        switch(selectorType.toLowerCase()) {

            case "id":
                return By.id(selector);

            case "cssselector":
                return By.cssSelector(selector);

            case "xpath":
                return By.xpath(selector);

            default:
                throw new Failure("Wrong selector: " + attribute);
        } 
    }



    public File evalFileAttribute(String filePath) {

        return Paths.get(evalAttribute(filePath)).toFile();
    }
    
    
    public Path evalPathAttribute(String filePath) {

        return Paths.get(evalAttribute(filePath));
    }



    public String evalAttributeNullable(String attribute){

        return getTestCaseAttributes() == null ? 
                null : getTestCaseAttributes().get(attribute);
    }



    public boolean attributeExists(String attribute){

        return getTestCaseAttributes().get(attribute) != null;
    }



    public Boolean evalBooleanAttribute(String attribute){

        return nullToEmptyString(getTestCaseAttributes().get(attribute)).equalsIgnoreCase("true");
    }



    public int evalIntAttribute(String attribute){

        try {

            return Integer.parseInt(nullToEmptyString(getTestCaseAttributes().get(attribute)));

        } catch (NumberFormatException e) {
            log(" " + e);
            return 0;
        }		
    }


    public int evalIntAttribute(String attribute, int defaultValue){

        try {

            return Integer.parseInt(nullToEmptyString(getTestCaseAttributes().get(attribute)));

        } catch (NumberFormatException e) {
            log(" " + e);
            return defaultValue;
        }       
    }


    public List<String> evalListAttribute(String attribute, String separator){

        return List.of(evalAttribute(attribute).split(separator)).stream()

                .filter( element -> ! element.isEmpty())

                .collect(Collectors.toList());
    }



    public String addFailure(Throwable th) {

        logSplitByLines(Failure.failureStackToString(th));
        String failureMessage = th.toString();

        if (th instanceof Failure){

            if(th.getCause() != null){
                failureMessage += " Cause: " + WebPage.getSeleniumExceptionShortMessage(
                        th.getCause().toString());
            }

        }

        addAttribute("failure", failureMessage);

        return failureMessage; 
    }



    public String getFailure() {

        return nullToEmptyString(getTestCaseAttributes().get(
                failure.name()));
    }



    public String getExpectedFailureRegExp() {

        return nullToEmptyString(getTestCaseAttributes().get(
                expectedFailureRegExp.name()));
    }



    public boolean hasExpectedFailure(String actualFailure) {

        return actualFailure.matches(
                "(?s)" + getExpectedFailureRegExp());                
    }



    public String getJvmProperty(JvmArgs property) {
        return SystemUtils.getPropertyOrEmptyString(property);
    }



    public void removeAttr(String attribute) {
        getTestCaseAttributes().remove(attribute);
    }



    public void removeFailure() {
        removeAttr(failure.name());
    }



    public boolean isSkipped() {

        return Optional.ofNullable(getTestCaseAttributes().get(skip.name()))
                .orElse("")
                .equalsIgnoreCase("true");
    }



    public <T> void setAndCheckIfAttributeExists(

            String attributeName,            
            Function<String, T> attributeEvaluator,

            Consumer<T> setter,
            Supplier<T> getter) {

        setAndCheckIfAttributeExists(
                attributeName,
                attributeEvaluator,
                setter,
                getter,
                (expected, actual) -> expected.equals(actual));
    }

    //TODO --needs generic assertion via enum
    public <T> void setAndCheckIfAttributeExists(

            String attributeName,            
            Function<String, T> attributeEvaluator,

            Consumer<T> setter,
            Supplier<T> getter,

            BiPredicate<T, T> assertionCondition) {

        if (attributeExists(attributeName)){

            T attributeValue = attributeEvaluator.apply(attributeName);

            setter.accept(attributeValue);

            T actual = getter.get();

            Assert.isEqual(
                    true,
                    assertionCondition.test(attributeValue, actual),
                    "Check " + attributeName 
                         + " expected=" + attributeValue  
                         + " actual=" + actual);  
        }
    }

    
    public void setAndCheckStringIfAttributeExists(

            String attributeName,            
            Function<String, String> attributeEvaluator,

            Consumer<String> setter,
            Supplier<String> getter,

            AssertCondition assertCondition) {

        if (attributeExists(attributeName)){

            String attributeValue = attributeEvaluator.apply(attributeName);

            setter.accept(attributeValue);

            String actual = getter.get();

            Assert.customAssertString(
                    "Check attribute " + attributeName 
                        + "(" + assertCondition.name() +  ")", 
                    attributeValue, 
                    actual, 
                    assertCondition);
        }
    }


    
    public <T> void setIfAttributeExists(

            String attributeName,            
            Function<String, T> attributeEvaluator,

            Consumer<T> setter) {

        if (attributeExists(attributeName)){

            T attributeValue = attributeEvaluator.apply(attributeName);

            setter.accept(attributeValue);
        }
    }



    public <T> void verifyValueIfAttributeExists(

            String attributeName,            
            Function<String, T> attributeEvaluator,

            Supplier<T> getter) {

        if (attributeExists(attributeName)){

            T attributeValue = attributeEvaluator.apply(attributeName);

            Assert.isEqual(
                    attributeValue.toString(),
                    getter.get().toString(),
                    "Check " + attributeName);  
        }
    }

    
    
    public String getBrowser() {
        
        return getTestCaseAttributes().get(TestCaseAttribute.browser.name());
    }
    
}

