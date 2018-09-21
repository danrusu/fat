package utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.google.common.base.Function;

import base.failures.Failure;


/**
 * Static functionalities for dynamic initialization of fields with a specified type.
 * 
 * @author dan.rusu
 *
 */
@SuppressWarnings("unchecked")
public interface FieldsReplacer{



    public static <T> Field getDeclaredField(T instance, String fieldName, int parentLevel) 
            throws NoSuchFieldException, SecurityException {

        Class<T> currentClass = (Class<T>) instance.getClass();

        for (int i=0; i < parentLevel; i++) {
            currentClass =  (Class<T>) currentClass.getSuperclass();
        }

        return currentClass.getDeclaredField(fieldName);
    }



    public static <T> Field[] getDeclaredFields(T instance, int parentLevel) 
            throws NoSuchFieldException, SecurityException {

        Class<T> currentClass = (Class<T>) instance.getClass();

        for (int i=0; i < parentLevel; i++) {
            currentClass =  (Class<T>) currentClass.getSuperclass();
        }

        return currentClass.getDeclaredFields();
    }



    public static <T,U> void initFields(
            T instance,  
            Class<U> type,
            Function<U,U> initFunction,
            int parentLevel){

        boolean initSucceeded = true;

        try {

            for (String fieldName : getFieldNames(instance, type, parentLevel)){

                Field field;

                field = getDeclaredField(instance, fieldName, parentLevel);
                field.setAccessible(true);

                field.set(instance, initFunction.apply((U) getValue(instance, field)));        
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            initSucceeded = false;
        }

        if (!initSucceeded){
            throw new Failure("Init failed for " + instance.getClass().getName());
        }
    }

    
    
    public static <T,U> void initFields(
            T instance,  
            Class<U> type,
            Function<U,U> initFunction){
     
        initFields(instance, type, initFunction, 0);
    }



    /**
     * Get a list of all field names that have a specified 'type' for a class instance.
     * 
     * @param instance - object to get fields from
     * @param type - searched type
     * @return - a list of field names
     * @throws SecurityException 
     * @throws NoSuchFieldException 
     */
    public static <T,U> List<String> getFieldNames(T instance, Class<U> type, int parentLevel) 
            throws NoSuchFieldException, SecurityException{

        return Optional.ofNullable(Arrays.asList(getDeclaredFields(instance, parentLevel))
                .stream()
                .filter(field -> field.getType().equals(type))
                .map(field -> field.getName())
                .collect(Collectors.toList())	

                ).orElse(new ArrayList<String>());
    }




    /**
     * Return a map of (fieldName, fieldValue) entries
     * for all fields with a specific 'type' in the 'instance'
     * 
     * @param instance - a class instance
     * @param type - fields type to search for 
     * @return (fieldName, fieldValue)
     */
    public static <T,U> Map<String, T> getFields(T instance, Class<U> type){

        return Optional.ofNullable(
                Arrays.asList(instance.getClass().getDeclaredFields())
                .stream()
                .filter(field -> field.getType().equals(type))
                .collect(Collectors.toMap( 
                        field -> field.getName(), 
                        field -> {
                            field.setAccessible(true); 
                            return getValue(instance, field);
                        }))

                ).orElse(new TreeMap<>());
    }	



    /**
     * Wrapper for java.lang.reflect.Field get(instance) method.
     * 
     * @param instance - an instance that has the field
     * @param field - Field to get the value
     * @return
     */
    public static <T> T getValue(T instance, Field field){

        try {

            return (T)field.get(instance);

        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
            throw new Failure(instance.getClass().getName() + "error. ", e);
        }
    }

}
