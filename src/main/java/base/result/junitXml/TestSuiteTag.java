package main.java.base.result.junitXml;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.joining;
import static main.java.base.result.Html.endTag;
import static main.java.base.result.Html.tag;
import static main.java.utils.StringUtils.indent;

import java.util.List;
import java.util.Map;

public class TestSuiteTag {
    
    private final String TEST_SUITE_TAG_NAME = "testsuite";

    public static class TestSuiteTagBuilder{
        private String id;
        private String name;
        private String tests;
        private String failures;
        private String time;
        private List<TestCaseTag> testCaseTags = emptyList();


        public TestSuiteTagBuilder id(String id) {
            this.id = id;
            return this;
        }


        public TestSuiteTagBuilder name(String name) {
            this.name = name;
            return this;
        }


        public TestSuiteTagBuilder tests(String tests) {
            this.tests = tests;
            return this;
        }


        public TestSuiteTagBuilder failures(String failures) {
            this.failures = failures;
            return this;
        }


        public TestSuiteTagBuilder time(String time) {
            this.time = time;
            return this;
        }


        public TestSuiteTagBuilder testCaseTags(List<TestCaseTag> testCaseTags) {
            this.testCaseTags = testCaseTags;
            return this;
        }


        public TestSuiteTag build() {
            
            return new TestSuiteTag(this);
        }

    }


    private String id;
    private String name;
    private String tests;
    private String failures;
    private String time;

    private List<TestCaseTag> testCaseTags;

    
    private TestSuiteTag(TestSuiteTagBuilder testSuiteTagBuilder) {
        id = testSuiteTagBuilder.id;
        name = testSuiteTagBuilder.name;
        tests = testSuiteTagBuilder.tests;
        failures = testSuiteTagBuilder.failures;
        time = testSuiteTagBuilder.time;
        testCaseTags = testSuiteTagBuilder.testCaseTags;
    }


    public String getId() {
        return id;
    }


    public String getName() {
        return name;
    }


    public String getTests() {
        return tests;
    }


    public String getFailures() {
        return failures;
    }


    public String getTime() {
        return time;
    }


    public List<TestCaseTag> getTestCaseTags() {
        return testCaseTags;
    }


    @Override
    public String toString() {
        
        String testSuiteTagLine = tag(
                
                TEST_SUITE_TAG_NAME, 
                
                Map.of("id", id,
                        "name", name,
                        "tests", tests,
                        "failures", failures,
                        "time", time));
        
        return  String.join("\n",
                
                indent(3) + testSuiteTagLine,                
                
                getTestCaseTagsString(), 
                
                indent(3) + endTag(TEST_SUITE_TAG_NAME));
    }


    private CharSequence getTestCaseTagsString() {
            
            return testCaseTags.stream()
                    .map(TestCaseTag::toString)
                    .collect(joining("\n"));
    }            
    
}
