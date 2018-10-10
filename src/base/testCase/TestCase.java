package base.testCase;

import static base.Logger.log;
import static base.Logger.logSplitByLines;
import static base.runnerConfig.TestCaseAttribute.expectedFailureRegExp;
import static base.runnerConfig.TestCaseAttribute.failure;
import static base.runnerConfig.TestCaseAttribute.skip;
import static base.xml.XmlDynamicData.evaluateAttributeValue;
import static base.xml.XmlDynamicData.getSavedData;
import static base.xml.XmlDynamicData.saveData;
import static utils.StringUtils.nullToEmptyString;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.openqa.selenium.By;

import base.Assert;
import base.JvmArgs;
import base.failures.Failure;
import base.pom.WebPage;
import base.runnerConfig.TestCaseAttribute;
import base.xml.XmlDynamicData;
import utils.SystemUtils;

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
    
    
    
    public String getTestCaseAttribute(String attributeName) {
        
        return nullToEmptyString(getTestCaseAttributes().get(attributeName));
    }
    
    
    
    public String getTestCaseAttribute(TestCaseAttribute testCaseAttribute) {
        
        return nullToEmptyString(getTestCaseAttributes().get(testCaseAttribute.name()));
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



    public void addAttributeIfValueNotEmpty(String name, String value){
        
        if (!value.isEmpty()){ 
            addAttribute(name, value);
        }
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

                throw new Failure("Saving results failed!", e);
            }
        });

        String saved = getSavedData().toString(); 

        testCaseAttributes.put("save", saved);
    }



    public String evalAttribute(String attribute){
        
        return nullToEmptyString(getTestCaseAttributes().get(attribute));
    }


    
    /* by attributes 
     * 
     * selector="id:idSelector"
     * selector="css:cssSelector"
     * selector="xpath:xpathSelector"
     */
    public By evalByAttribute(String attribute) {

        String attributeValue = evalAttribute(attribute);
        
        String[] selectorTokens = attributeValue.split(":");
        if (selectorTokens.length < 2) {
            throw new Failure("Wrong selector: " + attribute);
        }

        String selectorType = selectorTokens[0];
        String selector = attributeValue.replaceFirst("^" + selectorType + ":", "");

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

    
    
    public File evalFileAttribute(String attribute) {
        
        return Paths.get(evalAttribute(attribute)).toFile();
    }

    
    
    public String evalAttributeNullable(String attribute){
        
        return getTestCaseAttributes() == null ? 
                null : getTestCaseAttributes().get(attribute);
    }



    public boolean attributeExists(String attribute){
        
        return getTestCaseAttributes().get(attribute) != null;
    }



    public Boolean evalBooleanAttribute(String attribute){
        
        return nullToEmptyString(getTestCaseAttributes().get(attribute))
                .equalsIgnoreCase("true");
    }



    public int evalIntAttribute(String attribute){

        try {
            return Integer.parseInt(nullToEmptyString(getTestCaseAttributes().get(attribute)));
        } catch (NumberFormatException e) {
            log(" " + e);
            return 0;
        }		
    }
    
    
    
    public float evalFloatAttribute(String attribute, float defaultValue){

        try {
            return Float.parseFloat(
                    nullToEmptyString(getTestCaseAttributes().get(attribute)));
            
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

        logSplitByLines( Failure.stackToString(th) );
        String failureMessage = th.toString();

        if (th instanceof Failure){

            if(th.getCause() != null){
                failureMessage += " Cause: " + WebPage.getSeleniumExceptionShortMessage(
                        th.getCause().toString());
            }

        }

        addAttribute(failure.name(), failureMessage);

        return failureMessage; 
    }



    public String getFailure() {
        
        return getTestCaseAttribute(failure);
    }



    public String getExpectedFailureRegExp() {
        
        return getTestCaseAttribute(expectedFailureRegExp);
    }


    
    public boolean hasExpectedFailure(String actualFailure) {

        return actualFailure.matches(
                // only accept expected failures wrapped as base.failures.Failure
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
        
        return getTestCaseAttribute(skip.name()).equalsIgnoreCase("true");
    }



    public <T> void setAndCheckIfAttributeExists(

            String attributeName,            
            Function<String, T> attributeEvaluator,

            Consumer<T> setter,
            Supplier<T> getter) {

        if (attributeExists(attributeName)){

            T attributeValue = attributeEvaluator.apply(attributeName);

            setter.accept(attributeValue);

            Assert.equals(
                    "Check " + attributeName,
                    attributeValue.toString(),
                    getter.get().toString());  
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

            Assert.equals(
                    "Check " + attributeName,
                    attributeValue.toString(),
                    getter.get().toString());  
        }
    }

}

