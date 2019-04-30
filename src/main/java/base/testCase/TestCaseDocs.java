package main.java.base.testCase;

import static main.java.base.failures.ThrowablesWrapper.supplyUnchecked;
import static main.java.utils.ClassUtils.getClassesNamesInPackages;
import static main.java.utils.FileUtils.getRelativePath;

import java.util.List;

import main.java.utils.ClassUtils;

/**
 * Class for generating Test Case documentation via TestScenario interface. 
 * 
 * @author Dan.Rusu
 *
 */
public class TestCaseDocs {


    public static void run(List<String> suitePackages) {

        getClassesNamesInPackages(
                
                getRelativePath("fat.jar"),
                
                suitePackages)
        
        .forEach(System.out::println);                     
    }



    public static void run(String testcaseName) {

        System.out.println(TestCaseDocs.getTestScenario(testcaseName));
    }



    public static String getTestScenario(String testCaseName) {


        return supplyUnchecked(
                
                () -> ((TestCase)ClassUtils.newInstance(testCaseName)).getTestCaseScenario(),
                
                "Not defined. Method implemented automaticaly.");
    }

}

