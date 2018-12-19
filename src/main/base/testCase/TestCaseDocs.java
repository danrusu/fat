package main.base.testCase;

import static main.base.failures.ThrowablesWrapper.supplyAndMapThrowableToFailure;
import static main.utils.ClassUtils.getClassesNamesInPackages;
import static main.utils.FileUtils.getRelativePath;

import java.util.List;

import main.utils.ClassUtils;

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


        return supplyAndMapThrowableToFailure(
                
                () -> ((TestCase)ClassUtils.newInstance(testCaseName)).getTestCaseScenario(),
                
                "Not defined. Method implemented automaticaly.");
    }

}

