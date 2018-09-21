package baseTest;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import base.Assert;
import base.failures.ThrowablesWrapper;

public class ThrowablesWrapperSpec {

    String nullString = null;

    String goodString = "good";

    int[] array = new int[] { 1, 2, 3, 4, 5 };



    @Test
    public void WrapAllTest() {


        List<Throwable> thrown = ThrowablesWrapper.wrapAll(
                
                () -> { goodString.toUpperCase(); },

                () -> { nullString.toUpperCase(); }, 

                () -> { System.out.println(array[9]); } 
        );

        System.out.println("Exceptions:");
        thrown.stream().forEach(System.out::println);
        

        
        Assert.assertList(
                
                List.of(NullPointerException.class, ArrayIndexOutOfBoundsException.class),
                
                thrown.stream()
                    .map(th -> th.getClass())
                    .collect(Collectors.toList()));
        
    }



    @Test
    public void WrapAssignmentTestNeg() {

        String x = ThrowablesWrapper.wrapAssignment(

                () -> nullString.toUpperCase(),

                "assignment failed");

        Assert.equals("x was correct assigned", 
                "assignment failed", 
                x);
    }



    @Test
    public void WrapAssignmentTestPos() {

        String x = ThrowablesWrapper.wrapAssignment(

                () -> goodString.toUpperCase(),

                "assignment failed");

        Assert.equals("x was correct assigned", 
                goodString.toUpperCase(), 
                x);
    }

}

