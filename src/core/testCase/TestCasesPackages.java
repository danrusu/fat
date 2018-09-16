package core.testCase;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum TestCasesPackages {


    unitTest_testCases,

    
    // common modules
    projects_common_testCases, 


    // Tested Applications 
    projects_azetsWork_testCases,
    projects_pmt_testCases,
    projects_azetsInvoice_testCases,


    // performance tests
    projects_performance_testCases,

    
    // test (helper)
    projects_test_testCases;
    
    
    
    public String value() {
        return this.name().replaceAll("_", ".");
    }
    
    
    public static List<String> getAll(){
        return Arrays.asList(TestCasesPackages.values())
                .stream().unordered().parallel()
                .map(TestCasesPackages::value)
                .collect(Collectors.toList());
    }

}
