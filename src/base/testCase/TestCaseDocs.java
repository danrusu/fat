package base.testCase;

import java.nio.file.Paths;
import java.util.List;

import base.failures.ThrowablesWrapper;
import utils.ClassUtils;

/**
 * Class for generating Test Case documentation via TestScenario interface. 
 * 
 * @author Dan.Rusu
 *
 */
public class TestCaseDocs {



    public static void run(List<String> suitePackages) {


        ClassUtils.getClassesNamesInPackages(
                
                Paths.get(System.getProperty("user.dir"), "fat.jar"),
                
                suitePackages)
        
        .forEach(System.out::println);                     
    }



    public static void run(String testcaseName) {

        System.out.println(TestCaseDocs.getTestScenario(testcaseName));
    }



    public static String getTestScenario(String testCaseName) {


        return ThrowablesWrapper.uncheckedAssignment(
                
                () -> ((TestCase)ClassUtils.newInstance(testCaseName)).getTestCaseScenario())
                
                .orElse("Not defined. Method implemented automaticaly.");
    }

}

