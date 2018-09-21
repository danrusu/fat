package base.runnerConfig;

import java.util.Map;

import base.JvmArgs;
import base.results.ResultFileType;
import utils.StringUtils;
import utils.SystemUtils;

public interface TestConfigUtils {

    
    
    // retries defaults to 1
    public static int getTestRetries(
            Map<Integer, TestConfig> tests, 
            int testId
            ) {
        
        return StringUtils.toInt(
                
                tests.get(testId)
                    .getTestAttributes()
                    .get("retries"),                     
                1,
                false);
    }
    
    
    
    // saveScreenShots defaults to false
    public static boolean getSaveScreenShots(
            Map<Integer, TestConfig> tests, 
            int testId
            ){
        
        return StringUtils.toBoolean(tests
                .get(testId)
                .getTestAttributes()
                .get("saveScreenShots"));
    }
    
    
    // saveScreenShots defaults to false
    public static ResultFileType getResultFileType(){
        
        return ResultFileType.valueOf(
                SystemUtils.getPropertyOrDefaultIfNull(
                        JvmArgs.resultFileType, 
                        ResultFileType.html.name()));
    }

    
    
    // needToCloseBrowserAtTestEnd defaults to true
    public static boolean needToCloseBrowserAtTestEnd(TestConfig testConfig) {
    
        return StringUtils.toBoolean(
                testConfig.getTestAttributes().get("closeBrowserAtEnd"),
                true);
                
    }
    
}

