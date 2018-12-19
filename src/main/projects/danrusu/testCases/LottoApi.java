package main.projects.danrusu.testCases;

import static io.restassured.RestAssured.request;
import static io.restassured.http.Method.GET;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import main.base.testCase.TestCase;


public class LottoApi extends TestCase {

	
	private final String API_LOTTO = "http://danrusu.ro/api/lotto.json";
	
	
	@Override
	public void run() {
		
		Response response = request(GET, API_LOTTO);
		
		ValidatableResponse validatableResponse = response.then();
		
		
		validatableResponse.statusCode(200)
        
        	.body(
        			"lotto.lottoId", equalTo(5),
        			"lotto.winners", hasSize(2),
        			"lotto.winners.winnerId", hasItems(23, 54),
        			"lotto.ticketPrice", is(11.11f));  
	}
	

	@Override
	public String getTestCaseScenario() {
		
		return newScenario("Validate JSON from " + API_LOTTO);
	}

}
