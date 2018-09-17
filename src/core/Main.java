package core;


import core.runners.SuiteRunner;
import core.testCase.TestCaseDocs;
import core.testCase.TestCasesPackages;
import utils.SystemUtils;
import utils.ThreadUtils;


/**
 * 
 * Main entry of tool (fat.jar).
 * 
 * @author dan.rusu
 *
 */
public class Main {


    public static void main(String[] args) {

        // setup parallelism; by default use only the main thread + 2 extra threads
        ThreadUtils.setMaxThreads(SystemUtils.getIntPropertyOrDefault(JvmArgs.threads, 2));


        if (usage(args)) {

            if (! args[0].equals("docs")){
                // args[0] = suiteConfigXml
                runSuite(args[0]);
            }

            else {
                extractDocumentation(args);
            }
        }
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



    /**
     * Display fat.jar usage information.
     */
    private static boolean usage(String ...args){
        
        if ( args.length == 0 ){
            System.out.println(
                    "\nWrong arguments.\n"
                            + "\nUsage:"
                            + "\n\nRun test:"
                            + "\njava [-Duser] [-Dbrowser] [-DjenkinsJobName] [-DjenkinsBuildNr] [-DsendResultsToServer] [-Ddebug] -jar fat.jar config.xml "
                            + "\n\nList available test cases modules:"
                            + "\njava -jar fat.jar docs"
                            + "\n\nList test case's documentation:"
                            + "\njava -jar fat.jar docs testCaseName");

            return false;
        }

        return true;
    }

}

