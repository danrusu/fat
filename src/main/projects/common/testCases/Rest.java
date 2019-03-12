package main.projects.common.testCases;

import static main.base.Logger.log;
import static main.base.Logger.logSplitByLines;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.io.FileUtils;

import io.restassured.http.Method;
import io.restassured.response.Response;
import main.base.Assert;
import main.base.failures.Failure;
import main.base.testCase.WebPageTestCase;
import main.utils.http.HttpRestAssured;

/**
 * generic testecase to run http methods  
 * 
 * @author Mihaela Ioica
 *
 */
public class Rest extends WebPageTestCase{

	private String url;	
	private String bodyFilePath;
	private String destination;
	private String httpMethod;
	private Map<String, String> headers;

	

	@Override
	public void run(){	

		String body = null;

		url = evalAttribute("url");

		destination = evalAttribute("destination");			
		
		httpMethod = evalAttribute("httpMethod");

		
        if (attributeExists("xmlHeadersKeys") || attributeExists("xmlHeadersValues")){
		    
			List<String> headersKeys = evalListAttribute("xmlHeadersKeys", "\\|");
			List<String> headersValues = evalListAttribute("xmlHeadersValues", "\\|");

			Assert.isEqual(
			        
					headersKeys.size(),
							
					headersValues.size(),
					
					"Check headers keys/values lists");
			
			
			headers = IntStream.range(0, headersKeys.size())
			    .mapToObj(i -> i)
			    .collect(Collectors.toMap(
			            headersKeys::get, 
			            headersValues::get));
			    
		}


		try {

			if (attributeExists("bodyFilePath")){
			    
				bodyFilePath = evalAttribute("bodyFilePath");			
				body = FileUtils.readFileToString(new File(bodyFilePath), "UTF-8");
			}

			Response response = HttpRestAssured.request(
			        
			        Method.valueOf(httpMethod.toUpperCase()), 
			        
			        url, 
			        
			        body,
			        
			        Optional.ofNullable(headers).orElse(
			                Map.of("Content-Type", "application/json;charset=utf-8")));			
			
			// write the response in a file 
			try{		
				Files.write(Paths.get(destination), response.asByteArray());
				log("Destination file path for response: " + destination);
			}
			catch(IOException e){
				throw new Failure(e, "Couldn't write to file " + destination);
			}
			
			Assert.isEqual(200,
					response.statusCode(),
					"Check statusCode: ");

		} catch (Exception e) {
			logSplitByLines(e.toString());
			Assert.fail("Check for request's success." + e.getMessage());
		}
	}





	@Override
	public String getTestCaseScenario(){
	    
		return newScenario("Generic testcase for running Http requests.",				
		        "Test data: url, httpMethod, [xmlHeadersKeys], [xmlHeadersValues], [bodyFilePath]");
	}

}

