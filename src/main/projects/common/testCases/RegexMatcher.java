package main.projects.common.testCases;

import static main.base.Assert.isEqual;

import main.base.testCase.TestCase;

/**
 * Assert text matches regex.
 * 
 * @author Dan.Rusu
 *
 */
public class RegexMatcher extends TestCase{


    public void run(String text, String regex){

        isEqual(
                true,
                
                text.matches(regex),
                
                String.format(
                        "Regex match: (\"%s\").matches(\"%s\")", 
                        text,
                        regex));
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
