package main.java.base.testCase;

import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.List;


public enum TestCasesPackages {
		
    COMMON,
    
    DANRUSU,
    
    DEMO,
    
    MOCK;
    
    
    public String getJavaPath() {
        
		return String.join(".",
				PROJECTS_JAVA_PATH,
        		this.name().toLowerCase(),
        		TEST_CASES_FOLDER);
    }
    
    
    public static final String PROJECTS_JAVA_PATH = "main.java.projects";
    public static final String TEST_CASES_FOLDER = "testCases";
    
    
    public static List<String> getAll(){
        return Arrays.asList(values())
                .stream().unordered().parallel()
                .map(TestCasesPackages::getJavaPath)
                .collect(toList());
    }
    
    
    public static void main(String... args) {
    	System.out.println(getAll());
    }

}
