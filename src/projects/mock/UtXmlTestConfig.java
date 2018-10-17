package projects.mock;


import static base.Logger.logHeader;

import java.util.Map;
import java.util.stream.IntStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import base.Assert;
import base.runnerConfig.TestConfig;
import base.runners.SuiteRunner;
import base.xml.XmlTestConfig;

public class UtXmlTestConfig {

    private String xmlTestFile;
	private XmlTestConfig testXml;
	private Map<Integer, TestConfig> tests;

	@Before
	public void setUp() throws Exception {
		xmlTestFile = "XML/unitTests/" 
				+ this.getClass().getSimpleName()
				+ ".xml";
		
		 testXml = new XmlTestConfig();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		logHeader("Read test XML");
		tests = testXml.getTestConfig(xmlTestFile);
		
		
		logHeader("Verify the tests map");
		Assert.isTrue("Check number of tests = 10",
				tests.size()==10);
		
		// check that all first nine tests have one test cases and correct name 
		IntStream.range(1, tests.size()).forEach(
				
				i -> { 
						Assert.isTrue("Check number of test cases for test nr " + i + " = 1",
						tests.get(i).getTestCases().size() == 1);

				 		Assert.isTrue("Check test name for test nr " + i + " = " + "Test" + i,
							tests.get(i).getName().equals("Test" + i));
					}
				);
		
		Assert.isTrue("Check test name for test nr 10 = Test10",
				tests.get(10).getName().equals("Test10"));
		Assert.isTrue("Check number of test cases for test nr 10 = 10",
				tests.get(10).getTestCases().size() == 10);
	

		// also unit test for TestRunner
		SuiteRunner.run(xmlTestFile);
	}

}
