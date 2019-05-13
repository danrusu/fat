package test.java.utils;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;


import main.java.utils.SlackWebHook;
import main.java.utils.SlackWebHook.SlackWebHookBuilder;

public class SlackReportTest {
	
	final String expectedSlackWebHoojJson = String.join("\n",
			"{",
			"\"username\": \"fatTester\",",
			"\"url\": \"https://hooks.slack.com/services/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\"",
			"\"channel\": \"#test-automation\",",
			"\"text\": \"<http://danrusu.ro/logs/log_demo_browsers/result.html | QA report>\nfat framework demo\"",
			"}");
	
	@Test
	public void test() {
		
			SlackWebHook slackWebHook = new SlackWebHookBuilder("https://hooks.slack.com/services/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")
				.link("http://danrusu.ro/logs/log_demo_browsers/result.html", "QA report")			
				.textLine("fat framework")
				.text(" demo")
				.username("fatTester")
				.channel("#test-automation")
				.build();
						
			System.out.println(slackWebHook.toJson());
			
			// remove "," because the order of lines in JSON string can be different
			Assert.assertThat(
					Arrays.asList(expectedSlackWebHoojJson.replaceAll(",", "").split("\n")),
					containsInAnyOrder(slackWebHook.toJson().replaceAll(",", "").split("\n")));
			
			slackWebHook.post();					
		}	

}
