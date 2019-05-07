package main.java.base.result;

import static java.util.stream.Collectors.joining;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BinaryOperator;
import java.util.function.Function;

/**
 * Utility for formatting plain text to HTML/XML #FP, #noSideEffects
 * 
 * @author dan.rusu
 */
public class Html {

	private static final String FAVICON = "../../reportsResources/favicon.png";
    private static final String REPORTS_CSS = "../../reportsResources/reports.css";
    private static final String REPORTS_JS = "../../reportsResources/reports.js";
    private static final String REPORTS_JQUERY = "../../reportsResources/jQuery_v2.2.4.js";

    // e.g. attributes = [ "width=\"100\"", "title=\"age\"" ]
    public static String tag(String tagName, String... attributes) {

        return String.join("",
                "\n<", 
                tagName, 
                List.of(attributes).stream()
                	.reduce("", (acc, x) -> acc + " " + x), 
                ">\n");
    }

    private static Function<Entry<String, String>, String> entryAtributeToString = entry -> 
    	entry.getKey() + "=\"" + entry.getValue() + "\"";

    public static String tag(String tagName, Map<String, String> attributes) {

        return "<" + tagName + " " 
        		+ attributes.entrySet().stream()
        			.map(entryAtributeToString).collect(joining(" "))
                + ">";
    }

    public static String endTagOnNewLine(String tagName) {

        return "\n</" + tagName + ">\n";
    }

    public static String endTag(String tagName){

        return "</" + tagName + ">"; 
    }

    // e.g. attributes = [ "width=\"100\"", "title=\"age\"" ]
    public static String tagFromText(String tagName, 
    		String text, 
    		Function<String, String> stringTransformFunction,
            String... attributes) {

        // This is used in generating reports.
        // If an attribute does not exist (null), then
        // nothing needs to be added to the html report
        return (text != null) ?

                String.join("", 
                		tag(tagName, attributes), 
                		stringTransformFunction.apply(text), 
                		endTagOnNewLine(tagName))

                : "";
    }

    public static String tagFromText(String tagName, String text, String... attributes) {

        return tagFromText(tagName, text, x -> x, attributes);
    }

    public static String divFromText(String text, Function<String, String> stringTransformFunction,
            String... attributes) {

        return tagFromText("div", text, stringTransformFunction, attributes);
    }

    public static String divFromText(String text, String... attributes) {

        return divFromText(text, x -> x, attributes);
    }

    public static String detailsHead(String title) {

        return head(
                "Report: " + title,
              
                new String[] { REPORTS_CSS },

                new String[] { REPORTS_JQUERY, REPORTS_JS });
    }

    public static String head(String title, String[] cssFiles, String[] scriptFiles) {

        BinaryOperator<String> cssAccumulator = (acc, cssFile) -> acc
                + "\n<link rel=\"stylesheet\" type=\"text/css\" href=\"" + cssFile + "\"/>";

        BinaryOperator<String> scriptAccumulator = (acc, script) -> acc + "\n<script src=\"" + script + "\"></script>";

        return Html.tagFromText(

                "head",

                String.join(
                        "",

                        Html.tagFromText("title", title),

                        // add css files
                        List.of(cssFiles).stream().reduce("", cssAccumulator),

                        // add js scripts
                        List.of(scriptFiles).stream().reduce("", scriptAccumulator),

                        "<link rel=\"icon\" type=\"image/png\" href=\"" + FAVICON + "\">")
                );
    }

    public static String createTableColumns(String... columnsText) {

        return List.of(columnsText).stream()
        		.reduce("", (acc, col) -> acc + Html.tagFromText("td", col));
    }

    public static String createTableRow(String... columnsText) {

        return Html.tagFromText("tr", List.of(columnsText).stream()
                	.reduce("", (acc, x) -> acc + Html.tagFromText("td", x)));
    }

    public static String createTableHeaderRow(String... columnsText) {

        return Html.tagFromText("tr", List.of(columnsText).stream()
        		.reduce("", (acc, x) -> acc + tagFromText("th", x)));
    }

    public static String attribute(String attributeName, String attributeValue) {

        return attributeName + "=\"" + attributeValue + "\"";
    }

    public static String htmlText(String text) {

        return text.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
    }

}
