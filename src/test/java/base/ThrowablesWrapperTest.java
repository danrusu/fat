package test.java.base;

import static main.java.base.Assert.assertList;
import static main.java.base.Assert.isEqual;
import static main.java.base.failures.ThrowablesWrapper.runAllAndGetTrowables;
import static main.java.base.failures.ThrowablesWrapper.supplyUnchecked;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ThrowablesWrapperTest {

    final String nullString = null;

    final String goodString = "good";

    final int[] array = new int[] { 1, 2, 3, 4, 5 };



    @Test
    void runAllAndGetTrowables_test() {

        List<Throwable> thrown = runAllAndGetTrowables(

                () -> { goodString.toUpperCase(); },

                () -> { nullString.toUpperCase(); }, 

                () -> { System.out.println(array[9]); });

        System.out.println("Exceptions:");
        thrown.stream().forEach(System.out::println);

        assertList(

                List.of(
                        NullPointerException.class, 
                        ArrayIndexOutOfBoundsException.class),

                thrown.stream()
                    .map(th -> th.getClass())
                    .collect(Collectors.toList()));
    }

    
    @Nested
    class SupplyUncheckedTest{

        
        @Test
        public void throwing_assignment_test() {

            String value = supplyUnchecked(

                    () -> nullString.toUpperCase(),

                    "assignment failed");

            isEqual("assignment failed", 
                    value);
        }


        @Test
        public void not_throwing_assignment_test() {

            String value = supplyUnchecked(

                    () -> goodString.toUpperCase(),

                    "assignment failed");

            isEqual(goodString.toUpperCase(), 
                    value);
        }
    }

}
