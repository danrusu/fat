package utils;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.stream.Collectors;

import core.failures.Failure;
import core.failures.ThrowablesWrapper;


/**
 * Class actions.
 * 
 * @author dan.rusu
 *
 */
public interface ClassUtils {


    /**
     * Get simple class name for current Class.
     * 
     * @return
     */
    public static String getClassName() {

        String className = Thread.currentThread().getStackTrace()[2].getClassName();
        int lastIndex = className.lastIndexOf('.');
        return className.substring(lastIndex + 1);
    }



    /**
     * Get a list of classes within a jar package.
     * This will be used for extracting test documentation from jars.
     * 
     * @param jarName - the jar for searching the package
     * @param packageName - the package for searching the classes
     * @return - a list of all classes names that are in the specified package
     */
    public static List<String> getClassesNamesInPackages(
            String jarName, 
            List<String> suitePackages){

        List<String> classes = new ArrayList<> ();

        for(String packageName : suitePackages){

            packageName = packageName.replaceAll("\\." , "/");

            try(JarInputStream jarFile = new JarInputStream
                    (new FileInputStream (jarName));)
            {

                JarEntry jarEntry;
                while(true) {
                    jarEntry=jarFile.getNextJarEntry ();
                    if(jarEntry == null){
                        break;
                    }
                    if((jarEntry.getName ().startsWith (packageName)) &&
                            (jarEntry.getName ().endsWith (".class")) ) {

                        classes.add (jarEntry.getName()
                                .replaceAll("/", "\\.")
                                .replaceAll("\\.class", ""));
                    }
                }
            }
            catch( Exception e){
                e.printStackTrace ();
            }
        }
        return classes;
    }



    /**
     * Find class by name in a list of packages.
     * 
     * @param testCaseName - class name to search for
     * @param suitePackages - a list of packages to search in
     * @return - first class found in the packages list
     */
    public static Class<?> findClass(String testCaseName, List<String> suitePackages) {

        Class<?> testClass = null;
        for (String suitePackage : suitePackages){

            try {
                testClass = Class.forName(
                        suitePackage 
                        + "."
                        + testCaseName);


                break;
            }
            catch(ClassNotFoundException cnfEx){
            }

        }
        // if class not found
        if (testClass == null){
            throw new Failure("Class <" + testCaseName +"> was not found"
                    + " in the suite packages: " + suitePackages);
        }

        return testClass;
    }



    public static Object newInstance(String className){

        return ThrowablesWrapper.wrapThrowable(
                () -> Class.forName(className).getConstructor().newInstance());          
    }



    public static List<String> findPackageNamesEndingWith(String suffix) {

        List<String> result = new ArrayList<>();
        System.out.println(Package.getPackages());
        for(Package p : Package.getPackages()) {
            if (p.getName().endsWith(suffix)) {
                result.add(p.getName());
            }
        }
        return result;
    }



    public static <T> List<String> getParents(T instance) {

        List<String> parents = new ArrayList<>();
        Class<?> baseClassName = instance.getClass();

        Class<?> superClass = baseClassName.getSuperclass();


        while(! superClass.equals(Object.class)) {

            //System.out.println(superClass.getSimpleName());
            parents.add(superClass.getSimpleName());

            superClass = superClass.getSuperclass();

        }

        return parents;
    }



    public static <T> int getParentsCount(T instance) {

        return getParents(instance).size();
    }



    public static <T> List<String> getDeclaredFieldsNames(T instance){

        return Arrays.asList(instance.getClass().getDeclaredFields()).stream()
                .map(field -> field.getName())
                .collect(Collectors.toList());        
    }
}
