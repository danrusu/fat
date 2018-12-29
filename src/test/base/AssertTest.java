package test.base;

import static main.base.Assert.assertList;
import static main.base.Assert.assertMap;
import static main.base.Assert.isEqual;
import static main.base.Assert.isEqualAsFloat;
import static main.base.Assert.isEqualIgnoreCase;
import static main.base.Assert.verifyAllAsserts;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;


class AssertTest{


    @Nested
    class AssertMapTest {

        final Map<Integer, String> expectedMap = Map.of(
                1, "maria",
                2, "tania",
                3, "adela");

        final Map<Integer, String> copyOfExpectedMap = Map.of(
                1, "maria",
                2, "tania",
                3, "adela");

        final Map<Integer, String> mapWithDifferentSize = Map.of(
                1, "maria",
                2, "tania",            
                3, "adela",
                4, "dorina");

        final Map<Integer, String> mapWithDifferentOrder = Map.of(
                2, "tania",
                1, "maria",
                3, "adela");


        @Test
        void same_maps_test() {    
            assertMap(expectedMap, expectedMap);
        }


        @Test
        void same_content_maps_test() {    
            assertMap(expectedMap, copyOfExpectedMap);
        }


        @Test
        void different_sized_maps_test() {

            assertThrows(

                    AssertionError.class,

                    () -> assertMap(expectedMap, mapWithDifferentSize));
        }


        @Test
        void maps_with_same_content_and_different_order_test() {

            assertMap(expectedMap, mapWithDifferentOrder);
        }

    }

    
    @Nested
    class AssertListTest {
        
        
        final List<Integer> list = List.of(1, 2, 3, 4, 5);
        final List<Integer> copyOfList = List.of(1, 2, 3, 4, 5);
        
        final List<Integer> listWithDifferentSize = List.of(1, 2, 4, 3, 5);
        final List<Integer> listWithDifferentOrder = List.of(1, 2, 3, 5);

       
        
        @Test
        void same_lists_test() {    
            
            assertList(list, list);
        }
        
        
        @Test
        void same_content_lists_test() {    
            
            assertList(list, copyOfList);
        }

        
        @Test
        void different_sized_lists_test() {
            
            assertThrows(
                    
                   AssertionError.class,
                    
                    () -> assertList(list, listWithDifferentSize));
        }
        
           
        @Test
        void same_content_and_different_order_lists_test() {
            
            assertThrows(
                    
                    AssertionError.class,
                    
                    () -> assertList(list, listWithDifferentOrder));
        }
        
    }

    
    @Nested
    class VerifyAllTest { 


        @Test
        public void throwing_test() {

            assertThrows(

                    AssertionError.class,

                    () -> verifyAllAsserts(

                            () -> isEqual(
                                    "Test two strings 1", 
                                    "expected", 
                                    "actual"),

                            () -> isEqual(
                                    "Test two strings match case 3", 
                                    "expected", 
                                    "Expected"),

                            () -> isEqualIgnoreCase(
                                    "Test two strings ignore case 2", 
                                    "expected", 
                                    "Expected"),

                            () -> isEqualAsFloat(
                                    "Test two float strings", 
                                    "11.11", 
                                    "11.12")));
        }


        @Test
        public void not_throwing_test() {

           verifyAllAsserts(

                    () -> isEqual( 
                            "expected", 
                            "expected"),

                    () -> isEqualIgnoreCase(
                            "expected", 
                            "Expected"),

                    () -> isEqualAsFloat(
                            "Test two float strings", 
                            "11.11", 
                            "11.11"));
        }

    }
}
