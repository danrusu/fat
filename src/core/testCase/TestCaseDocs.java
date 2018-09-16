package core.testCase;

import java.nio.file.Paths;
import java.util.List;

import core.failures.ThrowablesWrapper;
import utils.ClassUtils;

/**
 * Class for generating Test Case documentation via TestScenario interface. 
 * @author Dan.Rusu
 *
 */
public class TestCaseDocs {



    public static void run(List<String> suitePackages) {

        String jarPath = Paths.get(System.getProperty("user.dir"), "watt.jar").toString();

        ClassUtils.getClassesNamesInPackages(jarPath, suitePackages).forEach(
                c -> {
                    final String[] items = c.split("\\.");
                    if (items.length>1){
                        System.out.println(items[items.length-1]);
                    }
                }
                );
    }



    public static void run(String testcaseName) {

        System.out.println(TestCaseDocs.getTestScenario(testcaseName));
    }



    public static String getTestScenario(String testCaseName) {


        return ThrowablesWrapper.wrapAssignment(
                
                () -> ((TestCase)ClassUtils.newInstance(testCaseName)).getTestCaseScenario())
                
                .orElse(" Not defined. Method implemented automaticaly.");
    }

}

