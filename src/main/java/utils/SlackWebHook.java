package main.java.utils;

import static java.util.stream.Collectors.joining;

import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Predicate;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import main.java.base.Assert;

public class SlackWebHook {
	
	private static final String ASSERTION_MESSAGE = "SLACK_WEB_HOOK_POST_MESSAGE";

	private final Map<String, String> HEADERS = Map.of(
			"Content-Type", "application/json;charset=utf-8");
	
	private String url;
	private String text;
	private String username;
	private String channel;
	
	
	private SlackWebHook(SlackWebHookBuilder slackWebHookBuilder) {
		this.url = slackWebHookBuilder.url;
		this.text = slackWebHookBuilder.text;
		this.username = slackWebHookBuilder.username;
		this.channel = slackWebHookBuilder.channel;
	}

	public static class SlackWebHookBuilder{
		
		private String url;
		private String text;
		private String username;
		private String channel;
		
		public SlackWebHookBuilder(String url) {
			this.url = url;
			this.text = "";
			this.username = "";
			this.channel = "";
		}
	
		public SlackWebHookBuilder text(String text) {
			this.text += text;
			return this;
		}
		
		public SlackWebHookBuilder link(String url, String text) {
			this.text += "<" + url + " | " + text + ">";
			return this;
		}
		
		public SlackWebHookBuilder textLine(String text) {
			this.text += "\n" + text;
			return this;
		}

		public SlackWebHookBuilder username(String username) {
			this.username = username;
			return this;
		}

		public SlackWebHookBuilder channel(String channel) {
			this.channel = channel;
			return this;
		}

		public SlackWebHook build() {
			return new SlackWebHook(this);
		}
	
	}
			
	public String toJson() {
		Function<Entry<String, String>, String> entryToText = entry -> 
			quote(entry.getKey()) + ": " + quote(entry.getValue());
									
		Predicate<Entry<String, String>> entryHasNotEmptyValue = entry -> false == entry.getValue().isEmpty();
		
		return String.join("\n",
				"{", 
				Map.of(
						"url", url,
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
	
	public void post() {
		RestAssured.baseURI = url;
		Response response = RestAssured.given()
				.headers(HEADERS)
				.body(toJson())
				.request(Method.POST);		
		
		Assert.isEqual(200, response.statusCode(), ASSERTION_MESSAGE);
		Assert.isEqual("ok", response.body().asString(), ASSERTION_MESSAGE);
	}	
	
}
