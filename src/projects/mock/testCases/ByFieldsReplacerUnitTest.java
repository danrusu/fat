package projects.mock.testCases;

import static core.Assert.assertTrue;

import core.pom.WebPage;
import core.testCase.TestCase;
import projects.mock.pages.ByFieldsReplacerTestData;

/**
 * Unit test for testing StringFieldsReplacerInterface. 
 * 
 * @author dan.rusu
 *
 */
public class ByFieldsReplacerUnitTest extends TestCase{
	
	@Override
	public void run() {
		ByFieldsReplacerTestData o1 = new ByFieldsReplacerTestData("@text","section");
		
		
		assertTrue("test public member (css selector)", 
		        WebPage.byToString(o1.cssSelector).equals("#id_section"));
		
		assertTrue("test private member (xpath selector)", 
		        WebPage.byToString(o1.getXpathSelector()).equals("//*[@id=id_section]"));
		
		assertTrue("test public member (id selector)", 
		        WebPage.byToString(o1.idSelector).equals("id_section"));
		
		assertTrue("test public member (className selector)", 
		        WebPage.byToString(o1.classNameSelector).equals("class_section"));
		
		
		assertTrue("test field with no replacement (css selector)", 
		        WebPage.byToString(o1.field).equals("#counter@tex"));
		
	}

	
	
	@Override
	public String getTestCaseScenario() {
		return "Unit test for testing StringFieldsReplacerInterface.";
	}
}
