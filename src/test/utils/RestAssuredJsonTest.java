package test.utils;

import static io.restassured.RestAssured.request;
//import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// https://github.com/rest-assured/rest-assured/wiki/GettingStarted#rest-assured
// https://github.com/rest-assured/rest-assured/wiki/Usage


// Rest-assured json-path:
//http://groovy-lang.org/processing-xml.html#_gpath

//Jayway json-path
//https://github.com/json-path/JsonPath


import io.restassured.http.Method;
import io.restassured.response.ValidatableResponse;

//import static io.restassured.module.jsv.JsonSchemaValidator.*;

// http://www.json.org/



// https://github.com/rest-assured/rest-assured/wiki/Usage#example-1---json
public class RestAssuredJsonTest {

    ValidatableResponse response;

    /* expected JSON:
     * {
  "lotto":
  {
    "ticketPrice":11.11,
    "lottoId":5,
    "winning-numbers":[2,45,34,23,7,5,3],
    "winners":
    [{
      "winnerId":23,
      "numbers":[2,45,34,23,3,5]
    },
    {
      "winnerId":54,
      "numbers":[52,3,12,11,18,22]
    }]
  }
}
     */
    @BeforeEach
    public void before() {

        Method method = Method.GET;

        response = request(method, "http://danrusu.ro/api/lotto.json").then();

    }



    @Test
    public void verifyStatusCode() {
        
        response.statusCode(200);
    }



    @Test 
    public void verifyLottoId() {

        response.body("lotto.lottoId", equalTo(5));
    }



    @Test // test array values and length
    public void verifyWinnersIds() {

        response.body(

                "lotto.winners", hasSize(2),

                "lotto.winners.winnerId", hasItems(23, 54));
    }



    @Test // float validation
    public void verifyTicketPrice() {

        response.body("lotto.ticketPrice", is(11.11f));
    }



    @Test // all chained together
    public void verifyAll() {

        response.statusCode(200)
        
            .body(
                "lotto.lottoId", equalTo(5),
                "lotto.winners", hasSize(2),
                "lotto.winners.winnerId", hasItems(23, 54),
                "lotto.ticketPrice", is(11.11f));  
    }

}

