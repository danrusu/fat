package main.java.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.base.Functions;

import main.java.base.failures.Failure;

/**
 * Static functionalities for dynamic initialization of fields with a specified type.
 * 
 * @author dan.rusu
 *
 */
public final class FieldsReplacer_Default{


	private FieldsReplacer_Default(){
		throw new AssertionError("This helper class must not be istantiated!");
	}

	

	public static void initFields(
			Object instance,  
			Class<?> type,
			Function<Object, Object> initFunction){
		
		boolean initSucceeded = true;
		for (String fieldName : getFieldNames(instance, type)){
			Field field;
			try {

				field = instance.getClass().getDeclaredField(fieldName);
				field.setAccessible(true);
				
				field.set(instance, initFunction.apply(getValue(instance, field)));
				
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
				initSucceeded = false;
			}

			if (!initSucceeded){
				throw new Failure("Init failed for " + instance.getClass().getName());
			}
		}
	}
	
	
	
	/**
	 * Get a list of all field names that have a specified 'type' for a class instance.
	 * 
	 * @param instance - object to get fields from
	 * @param type - searched type
	 * @return - a list of field names
	 */
	public static List<String> getFieldNames(Object instance, Class<?> type){
	    
	    List<Field> x = Arrays.asList(instance.getClass().getSuperclass().getDeclaredFields());
	    List<Field> y = Arrays.asList(instance.getClass().getDeclaredFields());
	    
	    Stream<Field> superFields = x.stream();
	    Stream<Field> localFields =y.stream();
	     
	    
		return Optional.ofNullable(
				/*Arrays.asList(instance.getClass()..getDeclaredFields())
					.stream()*/
		            Stream.of(superFields, localFields)
		            .flatMap(Functions.identity())
					.filter( field -> field.getType().equals(type))
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
	public static Map<String, Object> getFields(Object instance, Class<?> type){
		return Optional.ofNullable(
				Arrays.asList(instance.getClass().getDeclaredFields())
					.stream()
					.filter(field -> field.getType().equals(type))
					.collect(Collectors.toMap( 
							field -> field.getName(), 
							field -> {
								field.setAccessible(true); 
								return getValue(instance, field);
								}
							)
					)
					
				).orElse(new TreeMap<>());
	}	

	
	
	/**
	 * Wrapper for java.lang.reflect.Field get(instance) method.
	 * 
	 * @param instance - an instance that has the field
	 * @param field - Field to get the value
	 * @return
	 */
	public static Object getValue(Object instance, Field field){
		try {
			return field.get(instance);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			throw new Failure(e, instance.getClass().getName() + "error. ");
		}
	}

}
