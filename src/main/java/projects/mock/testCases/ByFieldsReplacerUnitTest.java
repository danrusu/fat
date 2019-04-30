package main.java.projects.mock.testCases;
import static main.java.base.pom.ByUtils.byToString;

import main.java.base.Assert;
import main.java.base.testCase.TestCase;
import main.java.projects.mock.pages.ByFieldsReplacerTestData;

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
				true, 
				byToString(o1.cssSelector).equals("#id_section"), 
				"test public member (css selector)");
		
		Assert.isEqual(
				true,
				byToString(o1.getXpathSelector()).equals("//*[@id=id_section]"),
				"test private member (xpath selector)");
		
		Assert.isEqual(
				true,
				byToString(o1.idSelector).equals("id_section"),
				"test public member (id selector)");
		
		Assert.isEqual(
				true,
				byToString(o1.classNameSelector).equals("class_section"),
				"test public member (className selector)");
		
		
		Assert.isEqual(
				true,
				byToString(o1.field).equals("#counter@tex"),
				"test field with no replacement (css selector)");
		
	}

	@Override
	public String getTestCaseScenario() {
		return "Unit test for testing StringFieldsReplacerInterface.";
	}
}
