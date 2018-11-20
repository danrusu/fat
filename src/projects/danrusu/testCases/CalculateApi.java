package projects.danrusu.testCases;

import static base.Logger.log;
import static io.restassured.RestAssured.request;
import static io.restassured.http.Method.GET;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import base.testCase.TestCase;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;


public class CalculateApi extends TestCase {

	
	private final String API_CALCULATE = "http://danrusu.ro/api/calculate.php";
	
	
	private enum Operations{
		
		SUM(1),
		
		MULTIPLICATION(2),
		
		DIVISION(3);
		
		
		Operations(int value){
			this.value = value;
		}
		
		
		private int value;
		
		
		public int getValue() {
			return value;
		}
	}
	
	
	@Override
	public void run() {
		
		
		String firstNumber = evalAttribute("firstNumber");
		
		String secondNumber = evalAttribute("secondNumber");
		
		String operation = "" + Operations
				.valueOf(evalAttribute("operation").toUpperCase())
				.getValue();
		
		String expectedResult = evalAttribute("expectedResult");  
		
		
		String finalUrl = addToUrl(API_CALCULATE, 
				"firstNumber=" + firstNumber, 
				"secondNumber=" + secondNumber, 
				"operation=" + operation);
		
				
		log("URL:" +  finalUrl);
		
		Response response = request(GET, finalUrl);		
		
		log("Response body: " + response.getBody().asString());
		log("Response headers:\n" + response.getHeaders());
		
		
		ValidatableResponse validatableResponse = response.then();
		

		// Validation (each throws if failure)
		validatableResponse.statusCode(200);
		
		validatableResponse.header("Content-type", is("application/json"));		
        
		validatableResponse.body(
				
        			"numbers", hasSize(2),
        			"numbers", hasItems(firstNumber, secondNumber),
        			
        			"operation", is(operation));
        			
		
		validatableResponse.body("result", is(expectedResult));  
	}

	
	private String addToUrl(String url, String ...more) {
		
		return url + "?" + String.join("&", more);
	}
	

	@Override
	public String getTestCaseScenario() {
		
		return newScenario("Validate API: " + API_CALCULATE);
	}

}
