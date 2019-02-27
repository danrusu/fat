package main.projects.mock.testCases;
import static main.base.pom.ByUtils.byToString;

import main.base.Assert;
import main.base.testCase.TestCase;
import main.projects.mock.pages.ByFieldsReplacerTestData;

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
		
		
		Assert.isEqual(
				"test public member (css selector)", 
				true, 
				byToString(o1.cssSelector).equals("#id_section"));
		
		Assert.isEqual(
				"test private member (xpath selector)",
				true,
				byToString(o1.getXpathSelector()).equals("//*[@id=id_section]"));
		
		Assert.isEqual(
				"test public member (id selector)",
				true,
				byToString(o1.idSelector).equals("id_section"));
		
		Assert.isEqual(
				"test public member (className selector)",
				true,
				byToString(o1.classNameSelector).equals("class_section"));
		
		
		Assert.isEqual(
				"test field with no replacement (css selector)",
				true,
				byToString(o1.field).equals("#counter@tex"));
		
	}

	@Override
	public String getTestCaseScenario() {
		return "Unit test for testing StringFieldsReplacerInterface.";
	}
}
