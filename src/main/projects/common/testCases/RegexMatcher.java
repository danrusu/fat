package main.projects.common.testCases;

import static main.base.Assert.isTrue;

import main.base.testCase.TestCase;

/**
 * Assert text matches regex.
 * 
 * @author Dan.Rusu
 *
 */
public class RegexMatcher extends TestCase{


    public void run(String text, String regex){

        isTrue(
                String.format(
                        "Regex match: (\"%s\").matches(\"%s\")", 
                        text,
                        regex),		
                
                text.matches(regex));
    }


    
    @Override
    public void run(){
        
        run(evalAttribute("text"), evalAttribute("regex"));
    }


    @Override
    public String getTestCaseScenario(){

        return newScenario("Assert text matches regex.",
                "Test data: text, regex");
    }


}
