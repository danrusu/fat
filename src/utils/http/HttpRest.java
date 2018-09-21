package utils.http;
import static base.Logger.log;
import static base.Logger.logLines;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;




public final class HttpRest {

	private HttpRest(){
		throw new AssertionError("This helper class must not be istantiated!");
	}



	public static Response request(
	        Method method, 
	        String url, 
	        String body, 
	        String[] headersKeys, 
	        String[] headersValues)
	{
	    
		RestAssured.baseURI = url;
		RequestSpecification request = RestAssured.given();
		
		if (headersKeys == null || headersValues == null)
		{
		    request.header("Content-Type", "application/json;charset=utf-8");
		}
		else
		{
			for (int i=0; i<headersKeys.length; i++)
			{
				request.header(
				        headersKeys[i].trim(), 
				        headersValues[i].trim()
				        );
			}
				
		}
		
		if (body != null){
		  request.body(body);
		}

		Response response = request.request(method);
		
		log("Response.statusCode: " + response.statusCode());
		log("Response.status: " + response.statusLine());
		logLines("Response.body: " + response.getBody().asString());

		return response;
	}
	
	

	public static Response put(String url, String body, String[] headersKeys, String[] headersValues){
		return request(Method.POST, url, body, headersKeys, headersValues);
	}
	
	
	
	public static Response get(String url, String body,String[] headersKeys, String[] headersValues){
		return request(Method.GET, url, body, headersKeys, headersValues);
	}
	
	
	
	public static Response get(String url){
		return get(url, null,null,null);
	}
	
}
