package base.testCase;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum TestCasesPackages {

    projects_mock_testCases,
   
    projects_common_testCases,    

    // Tested Projects
    projects_consignor_testCases,
    projects_pmt_testC;    
    
    
    
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

