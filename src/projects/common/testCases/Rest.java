package projects.common.testCases;
import static base.Logger.log;
import static base.Logger.logLines;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;

import base.Assert;
import base.failures.Failure;
import base.testCase.WebPageTestCase;
import io.restassured.http.Method;
import io.restassured.response.Response;
import utils.http.HttpRest;



public class Rest extends WebPageTestCase{

	private String url = "";	
	private String bodyFilePath = "";
	private String destination = "";
	private String httpMethod = "";
	private String xmlHeadersKeys = "";
	private String xmlHeadersValues = "";	

	@Override
	public void run(){	

		String[] headersKeys=null;
		String[] headersValues=null; 
		String body = null;

		if( attributeExists("url") ){
			url = evalAttribute("url");
		}

		if (attributeExists("destination"))
		{
			destination = evalAttribute("destination");			
		}
		
		httpMethod=evalAttribute("httpMethod");

		if (attributeExists("xmlHeadersKeys") && attributeExists("xmlHeadersValues"))
		{						
			xmlHeadersKeys=evalAttribute("xmlHeadersKeys");
			headersKeys = xmlHeadersKeys.split("\\|");

			xmlHeadersValues=evalAttribute("xmlHeadersValues");
			headersValues=xmlHeadersValues.split("\\|");

			Assert.assertTrue(
					"Check if the xmlHeadersKeys has the same length as xmlHeadersValues: "
							+ " found xmlHeadersKeys=" +  headersKeys.length
							+ " | found xmlHeadersValues=" + headersValues.length, 
					headersKeys.length == headersValues.length
					);
		}


		try {

			if (attributeExists("bodyFilePath"))
			{
				bodyFilePath = evalAttribute("bodyFilePath");			
				body = FileUtils.readFileToString(new File(bodyFilePath), "UTF-8");
			}

			Response response = HttpRest.request(
			        Method.valueOf(httpMethod.toUpperCase()), 
			        url, 
			        body,
			        headersKeys,
			        headersValues);			
			
			// write the response in a file 
			try{		
				Files.write(Paths.get(destination), response.asByteArray());
				log("Destination file path for response: " + destination);
			}
			catch(IOException e){
				throw new Failure("Couldn't write to file " + destination, e);
			}
			
			Assert.assertTrue(
					"Check if the request was successful: "
							+ "expected: statusCode=200; "
							+ "found: statusCode=" + response.statusCode(), 
					response.statusCode() == 200
					);

		} catch (Exception e) {
			logLines(e.toString());
			Assert.assertTrue("Check for request's success." + e.getMessage(), false);
		}
	}





	@Override
	public String getTestCaseScenario(){
		return "\n  Generic testcase for running Http requests ."				
				+ "\nTest data: [url], [headers], httpMethod, [bodyFilePath]";
	}

}
