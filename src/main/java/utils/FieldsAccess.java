package main.java.utils;

import java.util.Map;

/**
 * 
 * 
 * @author dan.rusu
 *
 */
public interface FieldsAccess {
	public default <T,U> Map<String, T> getFields(
			T instance,
			Class<U> type){
		
		return FieldsReplacer.getFields(
				instance, 
				type);
	}
}
