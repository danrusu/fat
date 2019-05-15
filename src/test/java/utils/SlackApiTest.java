package test.java.utils;

import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import main.java.integrations.SlackApi;
import main.java.integrations.SlackApi.SlackApiBuilder;

public class SlackApiTest {
	
	final String expectedSlackWebHoojJson = String.join("\n",
			"{",
			"\"username\": \"fatTester\",",
			"\"url\": \"https://hooks.slack.com/services/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\"",
			"\"channel\": \"#test-automation\",",
			"\"text\": \"<http://danrusu.ro/logs/log_demo_browsers/result.html | QA report>\nfat framework demo\"",
			"}");
	
	@Test
	public void test() {
		
			SlackApi slackApi = new SlackApiBuilder(
					"https://hooks.slack.com/services/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")
				.link("http://danrusu.ro/logs/log_demo_browsers/result.html", "QA report")			
				.textLine("fat framework")
				.text(" demo")
				.username("fatTester")
				.channel("#test-automation")
				.build();
				
			System.out.println(slackApi.toJson());
			
			// remove "," because the order of lines in JSON string can be different
			Assert.assertThat(
					Arrays.asList(expectedSlackWebHoojJson.replaceAll(",", "").split("\n")),
					containsInAnyOrder(slackApi.toJson().replaceAll(",", "").split("\n")));
			
			slackApi.post();		
			//slackWebHook.postSimple();
		}	

}
