package main.base;

import static main.utils.SystemUtils.getIntPropertyOrDefault;
import static main.utils.ThreadUtils.setMaxThreads;

import main.base.runners.SuiteRunner;
import main.base.testCase.TestCaseDocs;
import main.base.testCase.TestCasesPackages;


/**
 * 
 * Main entry of application (fat.jar).
 * 
 * @author dan.rusu
 *
 */
public class Main {


    public static void main(String[] args) {

        // setup parallelism; by default use only the main thread + 2 extra threads
        setMaxThreads(getIntPropertyOrDefault(JvmArgs.threads, 2));


        if (usage(args)) {

            if (haveSuiteConfigXml(args)){
                
                runSuite(args[0]);
            }

            else {
                extractDocumentation(args);
            }
        }
    }



    private static boolean haveSuiteConfigXml(String[] args) {

        return args[0].equals("docs") == false;
    }



    private static void runSuite(String suiteConfigXml) {
        SuiteRunner.run(suiteConfigXml);
    }



    private static void extractDocumentation(String[] args) {

        // list all available test cases names
        if ( args.length == 1 ){
            TestCaseDocs.run(TestCasesPackages.getAll());
        }

        // list test case's internal documentation (getTestScenario)
        else {
            String testcaseName = args[1];
            TestCaseDocs.run(testcaseName);
        }
    }



    private static boolean usage(String[] args){
        
        if ( args.length == 0 ){
            
            System.out.println(String.join(
                    
                    "\n",
                    
                    "Wrong arguments.",
                    
                    "\nUsage:",
                    
                    "\nRun test:",                    
                    "java [-Duser] [-Dbrowser] [-DjenkinsJobName] [-DjenkinsBuildNr] "
                            + "[-DsendResultsToServer] [-Ddebug] -jar fat.jar config.xml ",
                    
                    "\nList available test cases modules:",
                    "java -jar fat.jar docs",
                    
                    "\nList test case's documentation:",
                    "java -jar fat.jar docs testCaseName"));

            return false;
        }

        return true;
    }

}
