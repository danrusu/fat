package main.java.base.result.junitXml;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.joining;
import static main.java.base.result.Html.endTag;
import static main.java.base.result.Html.tag;

import java.util.List;
import java.util.Map;

public class TestSuitesTag {
    
    private final String XML_META = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
    private final String TEST_SUITES_TAG_NAME = "testsuites";

    public static class TestSuitesTagBuilder{
        private String id;
        private String name;
        private String tests;
        private String failures;
        private String time;
        private List<TestSuiteTag> testSuiteTags = emptyList();


        public TestSuitesTagBuilder id(String id) {
            this.id = id;
            return this;
        }


        public TestSuitesTagBuilder name(String name) {
            this.name = name;
            return this;
        }


        public TestSuitesTagBuilder tests(String tests) {
            this.tests = tests;
            return this;
        }


        public TestSuitesTagBuilder failures(String failures) {
            this.failures = failures;
            return this;
        }


        public TestSuitesTagBuilder time(String time) {
            this.time = time;
            return this;
        }


        public TestSuitesTagBuilder testSuites(List<TestSuiteTag> testSuiteTags) {
            this.testSuiteTags = testSuiteTags;
            return this;
        }


        public TestSuitesTag build() {
            
            return new TestSuitesTag(this);
        }

    }


    private String id;
    private String name;
    private String tests;
    private String failures;
    private String time;

    private List<TestSuiteTag> testSuiteTags;

    
    private TestSuitesTag(TestSuitesTagBuilder testSuitesTagBuilder) {
        id = testSuitesTagBuilder.id;
        name = testSuitesTagBuilder.name;
        tests = testSuitesTagBuilder.tests;
        failures = testSuitesTagBuilder.failures;
        time = testSuitesTagBuilder.time;
        testSuiteTags = testSuitesTagBuilder.testSuiteTags;
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


    public List<TestSuiteTag> getTestSuites() {
        return testSuiteTags;
    }


    @Override
    public String toString() {
        
       String testSuitesTagLine = tag(
               
               TEST_SUITES_TAG_NAME, 
               
               Map.of("id", id,
                       "name", name,
                       "tests", tests,
                       "failures", failures,
                       "time", time));
       
       
       return String.join("\n",
               XML_META,
               testSuitesTagLine,
               getTestSuitesTagsString(),               
               endTag(TEST_SUITES_TAG_NAME));               
    }


    private String getTestSuitesTagsString() {
        
        return testSuiteTags.stream()
                .map(TestSuiteTag::toString)
                .collect(joining("\n"));
    }        

}
