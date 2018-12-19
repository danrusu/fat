package main.utils.http;

import static main.base.Logger.log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


// This is not used anymore; use HttpRest instead (based on rest-assured)
public class HttpRequest {
	
	public static class HttpResult {
		private int resultCode;
		private String result;

		public HttpResult(int resultCode, String result){
			this.resultCode = resultCode;
			this.result = result;
			log("New HttpResult");
			log("Status code: " + resultCode);
			log("Body: " + result);
		}

		public int getResultCode() {
			return resultCode;
		}

		public String getResult() {
			return result;
		}

		public boolean isSucceeded(){
			return (this.resultCode == 200);
		}
	}

	private HttpRequest(){
		throw new AssertionError("This helper class must not be istantiated!");
	}	
	
	
	// HTTP PUT request - JSON body, no header
	public static HttpResult sendPut(String url, String jsonStringBody) throws Exception {
		log("SEND HTTP PUT " + url);
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		//add Request header
		con.setRequestMethod("PUT");
		con.setDoOutput(true);
		con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");


		// Send PUT request		
		OutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.write(jsonStringBody.getBytes("UTF-8"));
		wr.flush();
		wr.close();

		BufferedReader in = new BufferedReader(
				new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		return new HttpResult(con.getResponseCode(), response.toString());
	}
	
	
	
	// HTTP GET request for text content
	public static HttpResult sendGet(String url) throws Exception {

		log("SEND HTTP GET " + url);
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("GET");

		BufferedReader in = new BufferedReader(
				new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		return new HttpResult(con.getResponseCode(), response.toString());
	}




	// HTTP GET request
	public static void getFileFromUrl(String urlString, String localFilePath) throws Exception {

		log("SEND HTTP GET " + urlString);

		URL url = new URL(urlString);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		InputStream is = null;
		try {
			is = url.openStream();
			byte[] byteChunk = new byte[4096]; // Or whatever size you want to read in at a time.
			int n;

			while ( (n = is.read(byteChunk)) > 0 ) {
				baos.write(byteChunk, 0, n);
			}


			FileOutputStream fos = new FileOutputStream(localFilePath);
			fos.write(baos.toByteArray());
			fos.close();

		}
		catch (IOException e) {
			System.err.printf ("Failed while reading bytes from %s: %s", url.toExternalForm(), e.getMessage());
			e.printStackTrace ();
			// Perform any other exception handling that's appropriate.
		}
		finally {
			if (is != null) { is.close(); }
		}


	}


}
