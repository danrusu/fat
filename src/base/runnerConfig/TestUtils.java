package base.runnerConfig;

import static utils.StringUtils.toBoolean;
import static base.runnerConfig.TestAttribute.closeBrowserAtEnd;
import static base.runnerConfig.TestAttribute.saveScreenShots;

import java.util.Map;

public interface TestUtils {

    
    
    // saveScreenShots defaults to false
    public static boolean getSaveScreenShots(Map<Integer, TestConfig> tests, int testId) {
        
        return toBoolean(tests.get(testId).getTestAttribute(saveScreenShots.name()), true);
    }
    
       
    
    // closeBrowserAtEnd defaults to true
    public static boolean needToCloseBrowserAtEnd(TestConfig testConfig) {
    
        return toBoolean(testConfig.getTestAttribute(closeBrowserAtEnd.name()), true);                
    }
    
}

