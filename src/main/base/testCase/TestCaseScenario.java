package main.base.testCase;

public interface TestCaseScenario {
    

    
    public String getTestCaseScenario();
    
    
    
    public default String newScenario(String ...scenarioLines) {
        return String.join("\n", scenarioLines);
    }
    
}

