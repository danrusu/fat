package main.java.projects.common.testCases;

import static main.java.base.Logger.debug;

import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Collectors;

import main.java.base.testCase.TestCase;
import main.java.utils.FileUtils;


public class MssqlGetColumnsMap extends TestCase{
    
    

    @Override
    public void run() {       
        
        Map<Integer, Map<String, String>> inputData = evalMssqlQueryResultAttribute("mapInputFile");
        
        String keyColumnName = evalAttribute("keyColumnName");
        String valueColumnName = evalAttribute("valueColumnName");
        
        Path mapOutputFile = evalPathAttribute("mapOutputFile");
        
        
        // { index = { key1=val1, key2=val2 } -> { val1=val2 } 
        Map<String, String> columnsMap = inputData.values().stream()
            //.unordered()
            //.parallel()
            .peek(x -> debug("" + x))
            .collect(Collectors.toMap(
                    
                    resultRowMap -> resultRowMap.get(keyColumnName), 
                    
                    resultRowMap -> resultRowMap.get(valueColumnName)));
        
        FileUtils.writeObject(mapOutputFile, columnsMap);
        
        if(attributeExists("textOutputFile")){   
            
            FileUtils.writeObject( 
                    evalPathAttribute("textOutputFile"),
                    columnsMap.toString());
        }
    }

    
    
    @Override
    public String getTestCaseScenario() {

        return newScenario("Get and save columns map from a Mssql result file.",
                "Data: mapInputFile, keyColumnName, valueColumnName, mapOutputFile, [textOutputFile]",
                "Details: ",
                "mapInputFile: Map<Integer, Map<String, String>> { 1={ key1=val11, key2=val21 }, 2={ key1=val12, ky2=val22 } ... }",
                "mapOutputFile: Map<String, String> { val11=val21, val12=val22, ... }");
    }
    
}

