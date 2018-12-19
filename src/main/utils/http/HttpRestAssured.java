package main.utils.http;

import static main.base.Logger.log;
import static main.base.Logger.logLines;

import java.util.Map;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;




public interface HttpRestAssured {



	public static Response request(
	        Method method, 
	        String url, 
	        String body, 
	        Map<String, String> headers){
	    
		RestAssured.baseURI = url;
		RequestSpecification request = RestAssured.given();
		
		headers.entrySet().stream()
		    
		    .forEach(headerEntry -> request.header(
		            headerEntry.getKey(),
		            headerEntry.getValue()));
		
		log("Headers: " + headers);
		
		if (body != null){
		  request.body(body);
		}

		Response response = request.request(method);
		
		logLines(
		        "Response.statusCode: " + response.statusCode(),
		        "Response.status: " + response.statusLine(),
		        "Response.body: " + response.getBody().asString());

		return response;
	}
	
	

	public static Response post(
	        String url, 
	        String body, 
	        Map<String, String> headers){
	    
		return request(Method.POST, url, body, headers);
	}
	
	
	
	public static Response get(
	        String url, 
	        String body,
	        Map<String, String> headers){
	    
		return request(Method.GET, url, body, headers);
	}
	
	
	
	public static Response get(String url){
	    
		return get(url, null, null);
	}
	
}

