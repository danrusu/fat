package main.java.integrations;

import static java.util.stream.Collectors.joining;
import static main.java.base.Assert.softlyAssertAll;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Predicate;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import main.java.base.Assert;

public class TestRailClient {

	private final Map<String, String> HEADERS = Map.of(
			"Content-Type", 
			"application/json;charset=utf-8");

	private String url;
	private String text;
	private String username;
	private String channel;

	private TestRailClient(SlackApiBuilder slackApiBuilder) {
		this.url = slackApiBuilder.url;
		this.text = slackApiBuilder.text;
		this.username = slackApiBuilder.username;
		this.channel = slackApiBuilder.channel;
	}

	public static class SlackApiBuilder {

		private String url;
		private String text;
		private String username;
		private String channel;

		public SlackApiBuilder(String url) {
			this.url = url;
			this.text = "";
			this.username = "";
			this.channel = "";
		}

		public SlackApiBuilder text(String text) {
			this.text += text;
			return this;
		}

		public SlackApiBuilder link(String url, String text) {
			this.text += "<" + url + " | " + text + ">";
			return this;
		}

		public SlackApiBuilder textLine(String text) {
			this.text += "\n" + text;
			return this;
		}

		public SlackApiBuilder username(String username) {
			this.username = username;
			return this;
		}

		public SlackApiBuilder channel(String channel) {
			this.channel = channel;
			return this;
		}

		public TestRailClient build() {
			return new TestRailClient(this);
		}

	}

	public String toJson() {
		Function<Entry<String, String>, String> entryToText = entry -> 
			quote(entry.getKey()) + ": " + quote(entry.getValue());

		Predicate<Entry<String, String>> entryHasNotEmptyValue = entry -> false == entry.getValue().isEmpty();

		return String.join("\n",
				
				"{", 
				
				Map.of("url", url, 
						"text", text, 
						"username", username, 
						"channel", channel)
					.entrySet().stream()	
					.filter(entryHasNotEmptyValue)
					.map(entryToText)
					.collect(joining(",\n")),

				"}");

	}

	private String quote(String text) {
		return String.format("\"%s\"", text);
	}

	//  RestAssured dependency
	public void post() {

		RestAssured.baseURI = url; 
		Response response = RestAssured.given()
				.headers(HEADERS) 
				.body(toJson()) 
				.post();

		softlyAssertAll(
				() -> Assert.isEqual(200, response.statusCode(), "SLACK_API_RESPONSE_CODE"),
				() -> Assert.isEqual("ok", response.body().asString(), "SLACK_API_RESPONSE_BODY"));		 		 
	}

	// lightweight, no dependency
	public void postSimple() {
		
		List<Header> headerList = List.of( 
				new BasicHeader("Content-Type","application/json;charset=utf-8"));

		CloseableHttpClient httpClient = HttpClientBuilder.create()
				.setDefaultHeaders(headerList) 
				.build();

		HttpPost httpPost = new HttpPost(url);

		try {
			httpPost.setEntity(new StringEntity(toJson())); 
			CloseableHttpResponse response = httpClient.execute(httpPost);
			
			HttpEntity entity = response.getEntity();
			String body = EntityUtils.toString(entity, "UTF-8");
			
			softlyAssertAll(
					
					() -> Assert.isEqual(200, 
							response.getStatusLine().getStatusCode(), 
							"SLACK_API_RESPONSE_CODE"),
					
					() -> Assert.isEqual("ok", 
							body, 
							"SLACK_WEB_HOOK_RESPONSE_REASON"));

		} catch (IOException e) {

			throw new Error("SlackApi post failed", e);
		}		
	}

}
