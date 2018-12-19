package main.projects.common.testCases;

import static main.base.Logger.log;
import static main.base.failures.ThrowablesWrapper.supplyUnchecked;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import main.base.failures.ThrowablesWrapper;
import main.base.testCase.TestCase;
import main.projects.common.TextReplacementMode;

/**
 * Replace first/all occurrences of specified text in source file with replaceText.
 * Charset: UTF8
 * 
 * @author Dan.Rusu
 *
 */
public class FileReplaceText extends TestCase{



    @Override
    public void run(){


        String source = evalAttribute("source");
        String regexp = evalAttribute("regexp");
        String replacement = evalAttribute("replacement");
        String mode = evalAttribute("mode");

        Path sourceFilePath =  Paths.get(source);

        log("Replace " + mode 
                + " text \"" + regexp 
                + "\" with \"" + replacement 
                + "\" in " + source);   


        Charset charset = StandardCharsets.UTF_8;
        String content;

        // read file into string
        content = ThrowablesWrapper.supplyAndMapThrowableToFailure(

                () -> new String(Files.readAllBytes(sourceFilePath), charset));


        TextReplacementMode textReplacementMode = supplyUnchecked(

                () -> TextReplacementMode.valueOf(mode),

                TextReplacementMode.first);


        // replace text
        switch (textReplacementMode){

            case all:
                content = content.replaceAll(regexp, replacement);	
                break;

            case first:
                content = content.replaceFirst(regexp, replacement);	
                break;

        }


        String newContent = content;
        ThrowablesWrapper.supplyAndMapThrowableToFailure(

                () -> Files.write(sourceFilePath, newContent.getBytes(charset)));

        log("Text replacement - succeeded.");
    }



    @Override
    public String getTestCaseScenario(){
        return "\nReplace first/all occurences of specified text in file with replaceText"
                + "\nTest data: source, text, replaceText, mode.";
    }

}

