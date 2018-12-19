package test.base.xml;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.Test;

import main.base.Assert;
import main.base.xml.DataProvider;

public class DataProviderTest {

 
    
    @Test
    public void readDataProviderFileTest() throws IOException{
        
        
        List<List<String>> data = DataProvider.getData(
                Paths.get(System.getProperty("user.dir"), 
                        "resources",
                        "dataProviders", 
                        "mockUsers.txt"),
                ", ",
                2);
        
        
        Assert.verifyAll(
                
                () -> Assert.assertList(
                        List.of("user1", "password1"), 
                        data.get(0)),                
                
        
                () -> Assert.assertList(
                        List.of("user2", "password2"), 
                        data.get(1)),

                
                () -> Assert.assertList(
                        List.of("user3", "password3"), 
                        data.get(2)),
        
                
                () -> Assert.isEqual(
                        "Check data list size",
                        3, 
                        data.size()));
    }
    
}

