package test.java.base.xml;

import static main.java.base.Assert.assertList;
import static main.java.base.Assert.isEqual;
import static main.java.base.Assert.verifyAllAsserts;
import static main.java.base.xmlSuite.DataProvider.getDataFromProvider;
import static main.java.base.xmlSuite.DataProvider.getDataProviderPath;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;

public class DataProviderTest {

 
    
    @Test
    public void readDataProviderFileTest() throws IOException{
        
    	String dataSeparator = "\\s*,\\s*"; 
    	int dataColumnsCount = 2;
        
        List<List<String>> dataFromProvider = getDataFromProvider( 
                getDataProviderPath("mockUsers.txt"),
                dataSeparator,
                dataColumnsCount);
        
        
        verifyAllAsserts(
                
                () -> assertList(
                        List.of("user1", "password1"), 
                        dataFromProvider.get(0)),                
                
        
                () -> assertList(
                        List.of("user2", "password2"), 
                        dataFromProvider.get(1)),

                
                () -> assertList(
                        List.of("user3", "password3"), 
                        dataFromProvider.get(2)),
        
                
                () -> isEqual(
                        3,
                        dataFromProvider.size(), 
                        "Check data list size"));
    }
    
}

