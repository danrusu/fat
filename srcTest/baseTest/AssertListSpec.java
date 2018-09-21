package baseTest;

import static base.Assert.assertList;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

class AssertListSpec {
    
    
    List<Integer> list = List.of(1, 2, 3, 4, 5);
    List<Integer> copyOfList = List.of(1, 2, 3, 4, 5);
    List<Integer> listWithDifferentSize = List.of(1, 2, 4, 3, 5);
    List<Integer> listWithDifferentOrder = List.of(1, 2, 3, 5);

   
    
    @Test
    void assertListTestSameLists() {    
        assertList(list, copyOfList);
    }

    
    
    @Test
    void assertListTestDifferentSizedLists() {
        
        assertThrows(
                
               AssertionError.class,
                
                () -> assertList(list, listWithDifferentSize));
    }
    
    
    
    @Test
    void assertListTestDifferentOrder() {
        
        assertThrows(
                
                AssertionError.class,
                
                () -> assertList(list, listWithDifferentOrder));
    }
    
}

