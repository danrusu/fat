package main.base.pom;

import org.openqa.selenium.By;

import main.base.failures.Failure;

public interface ByUtils {

    
    public static By addPathToCssSelector(By by, String path){
        
        return By.cssSelector(byToString(by) + path);
    }



    public static By addPathToXpathSelector(By by, String path){
        
        return By.xpath(byToString(by) + path);
    }
    
    
    /**
     * Extracts the String selector from a By object.
     */
    public static String byToString(By by){
        
        return by.toString()
                .replaceAll("[^:]*:(?<byString>.*)", "${byString}")
                .trim();       
    }



    /**
     * Replace string in ByCssSelector, ByXPath, ById or ByClassName selector.
     * 
     * @param by
     * @param toReplace
     * @param replacement
     * @return
     */
    static By replace(By by, String toReplace, String replacement){
        
        String finalBy = byToString(by).replace(toReplace, replacement);
        
        switch (by.getClass().getSimpleName()){
    
            case "ByCssSelector": 
                return By.cssSelector(finalBy);
    
            case "ByXPath": 
                return By.xpath(finalBy);
    
            case "ById": 
                return By.id(finalBy);
    
            case "ByClassName": 
                return By.className(finalBy);
    
    
            default:
                throw new Failure("Wrong " + by 
                        + "; must be cByCssSelector, ByXPath, ById or ByClassName!");
        }
    }
    
}

