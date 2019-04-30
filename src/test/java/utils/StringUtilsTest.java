package test.java.utils;

import static main.java.base.Assert.assertList;
import static main.java.base.Assert.isEqual;
import static main.java.utils.StringUtils.equalsIgnoring;
import static main.java.utils.StringUtils.joinObjectsToString;
import static main.java.utils.StringUtils.removeAllRegex;
import static main.java.utils.StringUtils.splitBy;

import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
public class StringUtilsTest {


    final String text1 = "This is Azets test automation 1234.";
    final String text2 = "This is Visma test automation 1111.";

    final String textAfterRemovingAllRegex = "testautomation";

    final String[] regex = { "^This is", "Azets|Visma", "\\d{4}\\.$", "\\s" }; 


    @Nested
    public class SplitByTest{


        @Test
        void sameFinalListSize_test() {

            assertList(                                

                    List.of("users", "password", "age"),

                    splitBy(
                            "users, password, age",
                            ", ",
                            3));
        }


        @Test
        void biggerFinalListSize_test() {

            assertList(                                

                    List.of("users", "password", "age", "", ""),

                    splitBy(
                            "users, password, age",
                            ", ",
                            5));
        }


        @Test
        void smallerFinalListSize_test() {

            assertList(                                

                    List.of("users", "password", "age"),

                    splitBy(
                            "users, password, age",
                            ", ",
                            1));
        }
    }


    @Test
    void removeAllRegex_test() {

        isEqual(                                

                textAfterRemovingAllRegex,

                removeAllRegex(text1, regex));
    }


    @Test
    void equalsIgnoring_test() {

        isEqual(
                true,
                
                equalsIgnoring(
                        text1, 
                        text2, 
                        regex));
    }

    
    @Nested
    class JoinObjectsToStringTest{
        
        @Test
        void equalsIgnoring_test() {

            isEqual(
                    
                    "1-10-11.11-3-abc",                    

                    joinObjectsToString("-", 
                            1, 
                            10L, 
                            11.11F, 
                            Integer.parseInt("3"), 
                            "abc"));
        }
    }
}

