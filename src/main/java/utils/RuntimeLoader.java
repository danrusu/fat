package main.java.utils;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public final class RuntimeLoader {
	
	
	private RuntimeLoader(){
		throw new AssertionError("This helper class must not be istantiated!");
	}
	

	public static void load(File jarFile){

		try {
			File file = jarFile;
			URL url = file.toURI().toURL();

			URLClassLoader classLoader = (URLClassLoader)ClassLoader.getSystemClassLoader();
			
			Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
			
			method.setAccessible(true);
			method.invoke(classLoader, url);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
