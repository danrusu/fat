package main.java.projects.common.testCases;

import java.nio.file.Path;

import main.java.base.pom.WebPageJs;
import main.java.base.testCase.WebPageTestCase;
import main.java.utils.FileUtils;
import main.java.utils.ThreadUtils;

/**
 * @author Dan.Rusu 
 */
public class FetchPdf extends WebPageTestCase{

    
	@Override
	public void run(){
	    
	    String url = evalAttribute("url");		        
		Path destination = evalPathAttribute("destination");
		
		System.out.println(url);
		System.out.println(destination);
		
		String script = "fetch(\"" + url + "\")"
		        + "\n.then(response => response.blob())"
		        + "\n.then(blob => new Response(blob).text())"
		        + "\n.then(text => {console.log(text); window.pdfText = text;});";
		
		System.out.println(script);
		
		
		WebPageJs.getJsExecutor(driver).executeScript(script);
		
		ThreadUtils.sleep(5000);
		
		String blobText = (String)WebPageJs.getJsExecutor(driver).executeScript("return window.pdfText;");
				
        FileUtils.write(destination, blobText.getBytes());
        
        System.out.println(blobText);        
	}
		
	
	@Override
	public String getTestCaseScenario(){
	    
		return newScenario("Save blob on disk.");
	}
}
