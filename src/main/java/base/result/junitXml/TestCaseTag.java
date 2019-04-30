package main.java.base.result.junitXml;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.joining;
import static main.java.base.result.Html.endTag;
import static main.java.base.result.Html.tag;
import static main.java.utils.StringUtils.indent;

import java.util.List;
import java.util.Map;


public class TestCaseTag {
    
    private final String TEST_CASE_TAG_NAME = "testcase";

    public static class TestCaseTagBuilder{
        private String id;
        private String name;
        private String time;
        private List<FailureTag> failureTags = emptyList();


        public TestCaseTagBuilder id(String id) {
            this.id = id;
            return this;
        }


        public TestCaseTagBuilder name(String name) {
            this.name = name;
            return this;
        }
        
        
        public TestCaseTagBuilder time(String time) {
            this.time = time;
            return this;
        }


        public TestCaseTagBuilder failures(List<FailureTag> failureTags) {
            this.failureTags = failureTags;
            return this;
        }


        public TestCaseTag build() {

            return new TestCaseTag(this);
        }

    }


    private String id;
    private String name;
    private String time;
    private List<FailureTag> failureTags;


    private TestCaseTag(TestCaseTagBuilder testCaseTagBuilder) {
        id = testCaseTagBuilder.id;
        name = testCaseTagBuilder.name;
        time = testCaseTagBuilder.time;
        failureTags = testCaseTagBuilder.failureTags;
    }


    public String getId() {
        return id;
    }


    public String getName() {
        return name;
    }



    public String getTime() {
        return time;
    }


    public List<FailureTag> getFailureTags() {
        return failureTags;
    }        

    
    @Override
    public String toString() {
        
        String testCaseTagLine = tag(
                
                TEST_CASE_TAG_NAME, 
                
                Map.of("id", id,
                        "name", name,
                        "time", time));
        
        return  String.join("\n",
                
                indent(6) + testCaseTagLine,                
                
                getFailureTagsString(), 
                
                indent(6) + endTag(TEST_CASE_TAG_NAME));
    }


    private CharSequence getFailureTagsString() {
            
            return failureTags.stream()
                    .map(FailureTag::toString)
                    .collect(joining("\n"));
    } 
}
