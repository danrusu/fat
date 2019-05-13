package main.java.projects.common.testCases;

import main.java.base.testCase.WebPageTestCase;
import main.java.utils.SlackWebHook;
import main.java.utils.SlackWebHook.SlackWebHookBuilder;

/**
 * author dan.rusu
 */
public class SlackReport extends WebPageTestCase{


	@Override
	public void run(){	
		
		String webHookUrl = evalAttribute("webHookUrl");
		String text = evalAttribute("text");			
		String linkUrl = evalAttribute("linkUrl");			
		String linkText = evalAttribute("linkText");			
		String channel = evalAttribute("channel");
		String username = evalAttribute("username");
		
		SlackWebHook slackWebHook = new SlackWebHookBuilder(webHookUrl)
			.text(text)
			.link(linkUrl, linkText)
			.username(username)
			.channel(channel)
			.build();
		
		slackWebHook.post();			
	}
	

	@Override
	public String getTestCaseScenario(){
	    
		return newScenario("Send Slack message to Channel",				
		        "Test data: webHookUrl, text, [linkUrl], [linkText], [username], [channel]");
	}

}
