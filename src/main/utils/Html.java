package main.utils;

import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Function;



/**
 * Utility for formatting plain text to HTML.
 * #FP, #noSideEffects
 * 
 * @author dan.rusu
 *
 */
public interface Html {



    // e.g. attributes = [ "width=\"100\"", "title=\"age\"" ]
    public static String startTag(
            String tagName, 
            String ...attributes){

        return String.join(

                "",

                "\n<", 
                tagName, 
                List.of(attributes).stream()
                .reduce("", (acc,x) -> acc + " " + x), 
                ">\n");
    }



    public static String endTag(String tagName){

        return "\n</" + tagName + ">\n";
    }


    
    // e.g. attributes = [ "width=\"100\"", "title=\"age\"" ]
    public static String tagFromText(
            String tagName,
            String text, 
            Function<String, String> stringTransformFunction, 
            String... attributes){

        // This is used in generating reports.
        // If an attribute does not exist (null), then
        // nothing needs to be added to the html report
        return (text != null) ?

                String.join("", 
                        startTag(tagName, attributes), 
                        stringTransformFunction.apply(text), 
                        endTag(tagName)) 

                : "";
    }



    public static String tagFromText(
            String tagName,  
            String text,
            String ...attributes){

        return tagFromText(tagName, text, x -> x, attributes);
    }



    public static String divFromText(
            String text, 
            Function<String, String> stringTransformFunction, 
            String ...attributes){

        return tagFromText("div", text, stringTransformFunction, attributes);
    }



    public static String divFromText(
            String text, 
            String ...attributes){

        return divFromText(text, x -> x, attributes);
    }



    public static String detailsHead() {

        return head("Details",
                
                // TODO create custom responsive css for reports
                new String[]{
                    "../css/reports.css", 
                    "../css/responsive.css"
                },

                new String[]{
                    "../js/jQuery_v2.2.4.js", 
                    "../js/reports.js"
                });
    }



    public static String head(
            String title, 
            String[] cssFiles, 
            String[] scriptFiles) {		

        BinaryOperator<String> cssAccumulator = (acc, cssFile) -> acc                   
                    + "\n<link rel=\"stylesheet\" type=\"text/css\" href=\"" + cssFile + "\"/>";
        
        BinaryOperator<String> scriptAccumulator = (acc, script) -> acc 
                + "\n<script src=\"" + script + "\"></script>";
        
        return Html.tagFromText(
                
                "head", 

                String.join("",
                        
                        Html.tagFromText("title", title),

                        // add css files
                        List.of(cssFiles).stream().reduce("", cssAccumulator),

                        // add js scripts
                        List.of(scriptFiles).stream().reduce("", scriptAccumulator),
                
                        "<link rel=\"icon\" type=\"image/png\" href=\"images/favicon.ico\">"
                 )
         );               
    }



    public static String createTableColumns(String ...columnsText) {

        return List.of(columnsText).stream()
                .reduce( "", (acc, col) -> acc + Html.tagFromText("td", col) );
    }



    public static String createTableRow(String ...columnsText){

        return Html.tagFromText("tr",
                List.of(columnsText).stream()
                .reduce("", (acc, x) -> acc + Html.tagFromText("td", x)));
    }



    public static String createTableHeaderRow(String ...columnsText){

        return Html.tagFromText(
                "tr",
                List.of(columnsText).stream()
                .reduce("", (acc, x) -> acc + tagFromText("th", x)));
    }



    public static String attribute(String attributeName, String attributeValue) {
        
        return attributeName + "=\"" + attributeValue + "\"";    
    }



    public static String htmlText(String text) {
        
        return text.replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;");
    }

}

