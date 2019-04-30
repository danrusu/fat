package main.java.projects.danrusu.testCases;

import static io.restassured.RestAssured.request;
import static io.restassured.http.Method.GET;
import static main.java.base.Logger.log;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import main.java.base.testCase.TestCase;

public class CalculateApi extends TestCase {

	private final String API_CALCULATE = "http://danrusu.ro/api/calculate.php";


	@Override
	public void run() {

		String firstNumber = evalAttribute("firstNumber");

		String secondNumber = evalAttribute("secondNumber");

		String operation = "" + Operation.valueOf(evalAttribute("operation").toUpperCase()).getValue();

		String expectedResult = evalAttribute("expectedResult");

		String finalUrl = addToUrl(API_CALCULATE, "firstNumber=" + firstNumber, "secondNumber=" + secondNumber,
				"operation=" + operation);

		log("URL:" + finalUrl);

		Response response = request(GET, finalUrl);

		String responseBody = response.getBody().asString();
		log("Response body: " + responseBody);
		log("Response headers:\n" + response.getHeaders());

		ValidatableResponse validatableResponse = response.then();
				
		// Validation (each throws if failure)
		validatableResponse.statusCode(200);

		validatableResponse.header("Content-type", 
				is("application/json;charset=utf-8"));

		validatableResponse.body(

				"numbers", hasSize(2), "numbers", hasItems(firstNumber, secondNumber),

				"operation", is(operation));

		validatableResponse.body("result", is(expectedResult));

	}

	
	private String addToUrl(String url, String... more) {

		return url + "?" + String.join("&", more);
	}

	
	@Override
	public String getTestCaseScenario() {

		return newScenario("Validate API: " + API_CALCULATE);
	}

}
