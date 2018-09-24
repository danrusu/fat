package base.testCase;
import static base.Logger.log;
import static base.Logger.logLines;

import java.io.File;
import java.lang.reflect.Field;
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
import base.xml.XmlDynamicData;
import utils.StringUtils;
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


    /**
     * Set the test's attributes.
     * 
     * @param testAttributes - test's attributes map 
     */
    public void setTestCaseAttributes(Map<String, String> testAttributes) {
        this.testCaseAttributes = testAttributes;
        dynamicEval(testAttributes);
    }



    public void addAttribute(String name, String value){
        this.testCaseAttributes.put(name, value);
    }



    public void addAttributeIfValueNotEmpty(String name, String value){
        if (!value.isEmpty()){ 
            addAttribute(name, value);
        }
    }



    public String nullToEmptyString(String s) {
        return StringUtils.nullToEmptyString(s);
    }



    public void dynamicEval(Map<String, String> testCaseAttributes) {
        //Map<String, String> attributes = testCaseAttributes;

        // eval "save" first
        saveAll(evalAttribute("save"));


        testCaseAttributes.keySet().forEach(

                key -> {
                    if (! key.equals("save")){

                        String value = nullToEmptyString(testCaseAttributes.get(key));
                        String newValue = XmlDynamicData.getDynamicValue(
                                XmlDynamicData.getSavedData(), value);

                        if ( ! value.equals(newValue) ){
                            testCaseAttributes.replace(key, newValue);
                            log("Attribute replaced: " + key + "=" + newValue);
                        }
                    }
                }
                );

    }



    /**
     * Save String variable into memory so that 
     * they can be used for following test cases. 
     * 
     * @param saveString
     */
    public void saveAll(String saveString) {

        if (! saveString.isEmpty()){

            List.of(saveString.split(";")).forEach(

                    savedNameAndValue -> {

                        String savedName =  savedNameAndValue.split("=")[0];
                        String savedValue =  savedNameAndValue.split("=")[1];
                        XmlDynamicData.saveData(
                                savedName, 
                                XmlDynamicData.getDynamicValue(
                                        XmlDynamicData.getSavedData(),
                                        savedValue));

                    });

            String saved = XmlDynamicData.getSavedData().toString(); 
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

                if (objectValue!=null){
                    XmlDynamicData.saveData(saveName, objectValue.toString());
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

        String saved = XmlDynamicData.getSavedData().toString(); 

        testCaseAttributes.put("save", saved);
        log("Saved data: save=" + saved);

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


    public List<String> evalListAttribute(String attribute, String separator){

        return List.of(evalAttribute(attribute).split(separator)).stream()

                .filter( element -> ! element.isEmpty())

                .collect(Collectors.toList());
    }



    public String addFailure(Throwable th) {

        logLines( Failure.stackToString(th) );
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
        return nullToEmptyString(getTestCaseAttributes().get("failure"));
    }



    public String getExpectedFailureRegExp() {
        return nullToEmptyString(getTestCaseAttributes().get("expectedFailureRegExp"));
    }


    
    public boolean hasExpectedFailure(String actualFailure) {

        return actualFailure.matches(
                // only accept expected failures wrapped as base.failures.Failure
                "(?s)^core\\.failures\\.Failure: " + getExpectedFailureRegExp());                
    }
    
    

    public String getJvmProperty(JvmArgs property) {
        return SystemUtils.getPropertyOrEmptyString(property);
    }



    public void removeAttr(String attribute) {
        getTestCaseAttributes().remove(attribute);
    }



    public void removeFailure() {
        removeAttr("failure");
    }



    public boolean isSkipped() {        
        return Optional.ofNullable(getTestCaseAttributes().get("skip"))
                .orElse("")
                .equalsIgnoreCase("true");
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

