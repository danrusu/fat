package main.java.base.result.junitXml;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.joining;
import static main.java.base.result.Html.endTag;
import static main.java.base.result.Html.tag;
import static main.java.utils.StringUtils.indent;

import java.util.List;
import java.util.Map;

public class FailureTag {

    private final String FAILURE_TAG_NAME = "failure";
    
    public static class FailureTagBuilder{

        private String message;
        private String type;
        private List<String> text = emptyList();


        public FailureTagBuilder message(String message) {
            this.message = message;
            return this;
        }


        public FailureTagBuilder type(String type) {
            this.type = type;
            return this;
        }


        public FailureTagBuilder text(List<String> text) {
            this.text = text;
            return this;
        }


        public FailureTag build() {

            return new FailureTag(this);
        }

    }


    private String message;
    private String type;
    private List<String> text;


    private FailureTag(FailureTagBuilder failureTagBuilder) {
        message = failureTagBuilder.message;
        type = failureTagBuilder.type;
        text = failureTagBuilder.text;
    }


    public String getMessage() {
        return message;
    }


    public String getType() {
        return type;
    }


    public List<String> getText() {
        return text;
    }



    @Override
    public String toString() {
        
        //do not add empty error tags
        if (text.isEmpty()) {
            return "";
        }

        String failureTagLine = tag(

                FAILURE_TAG_NAME, 

                Map.of("message", message,
                        "type", type));

        return  String.join("\n",

                indent(9) + failureTagLine,                

                getFailureTagsString(), 

                indent(9) + endTag(FAILURE_TAG_NAME));
    }


    private String getFailureTagsString() {

        return text.stream().collect(joining("\n"));
    }

}
