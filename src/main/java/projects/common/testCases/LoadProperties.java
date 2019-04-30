package main.java.projects.common.testCases;

import static main.java.base.failures.ThrowablesWrapper.executeUnchecked;
import static main.java.utils.FileUtils.getRelativePath;

import java.io.FileInputStream;
import java.util.Properties;

import main.java.base.testCase.TestCase;

/**
 * Load Java properties from file.
 * 
 * @author Dan.Rusu
 */
public class LoadProperties extends TestCase{


    @Override
    public void run(){

    	String filePath = getRelativePath(evalAttribute("filePath")).toString();
    	
    	Properties currentProperties = System.getProperties();
    	
    	executeUnchecked(() -> currentProperties.load(new FileInputStream(filePath)));
    	
    	
		System.setProperties(currentProperties);
    }


    @Override
    public String getTestCaseScenario(){

        return newScenario("Set globall system properties from file.",
                "Test data: filePath.");
    }
}
