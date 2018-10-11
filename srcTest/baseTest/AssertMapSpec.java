package baseTest;

import static base.Assert.assertMap;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;

import org.junit.jupiter.api.Test;



class AssertMapSpec {
    
    
    Map<Integer, String> expectedMap = Map.of(
            1, "maria",
            2, "tania",
            3, "adela");
    
    Map<Integer, String> copyOfMap = Map.of(
            1, "maria",
            2, "tania",
            3, "adela");
    
    Map<Integer, String> mapWithDifferentSize = Map.of(
            1, "maria",
            2, "tania",
            3, "adela",
            4, "dorina");
    
    Map<Integer, String> mapWithDifferentOrder = Map.of(
            2, "tania",
            1, "maria",
            3, "adela");

   
    
    @Test
    void assertMapTestSameMaps() {    
        assertMap(expectedMap, copyOfMap);
    }

    
    
    @Test
    void assertListTestDifferentSizedLists() {
        
        assertThrows(
                
               AssertionError.class,
                
                () -> assertMap(expectedMap, mapWithDifferentSize));
    }
    
    
    
    @Test
    void assertMapTestDifferentOrder() {
        
        assertMap(expectedMap, mapWithDifferentOrder);
    }
    
}

