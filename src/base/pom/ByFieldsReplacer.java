package base.pom;

import java.util.stream.IntStream;

import org.openqa.selenium.By;

import utils.ClassUtils;
import utils.FieldsReplacer;

/**
 * Functionality to initialize by generic selectors that have a string template.  
 * 
 * Implement this interface to be able to use FieldsReplacer
 * functionality for 'private' class members.
 * 
 * 
 * @author dan.rusu
 *
 */
public interface ByFieldsReplacer {



    public default <T> void initByFields(
            T instance,  
            String toReplace, 
            String replacement,
            int parentLevel){

        FieldsReplacer.initFields(
                instance, 
                By.class, 
                x -> ByUtils.replace((By)x, toReplace, replacement),
                parentLevel);
    }

    

    public default <T> void initByFields(
            T instance,  
            String toReplace, 
            String replacement){
        

        IntStream.rangeClosed(0, ClassUtils.getParentsCount(this)).forEach( 
                i -> initByFields(instance, toReplace, replacement, i));
    }
}
