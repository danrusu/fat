package main.projects.common.testCases;

import static main.base.Assert.assertMap;
import static main.utils.StreamUtils.mergeStringFunction;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.stream.Collectors;

import main.base.testCase.TestCase;



/**
 * Assert maps from files (Map<String, String>).
 * 
 * @author Dan.Rusu
 *
 */
public class AreEqualMap extends TestCase{



	@Override
	public void run(){
	    
	    Map<String, String> expectedMap = evalMapAttribute("expectedMapFile");
		Map<String, String> actualMap = evalMapAttribute("actualMapFile");	   
		
				
		TreeMap<String, String> expectedMapOrdered = new TreeMap<>(expectedMap);
		TreeMap<String, String> actualMapOrdered = new TreeMap<>(actualMap);
				
		
		// get first actualMap.(size) elements from expectedMap for assertion 
		Map<String, String> expectedMapFinal = expectedMapOrdered.entrySet().stream()
		        
		        .limit(actualMap.size())
		        
		        .collect(Collectors.toMap(
		                
		                Entry::getKey,
		                
		                Entry::getValue,
		                   
		                mergeStringFunction,
		        
		                TreeMap<String, String>::new));
		
		        
		assertMap(expectedMapFinal, actualMapOrdered);		
	}
	
	
	
	@Override
	public String getTestCaseScenario(){
	    
		return newScenario("Assert maps from files(Map<String, String>).",
				"Test data: expectedMapFile, actualMapFile");
	}
	
}

