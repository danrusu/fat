package main.java.projects.common.testCases;

import static main.java.base.Logger.log;
import static main.java.base.failures.ThrowablesWrapper.*;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import main.java.base.failures.Failure;
import main.java.base.testCase.TestCase;
import main.java.projects.common.TextReplacementMode;

/**
 * Replace first/all occurrences of regex in file with replaceText.
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
        Charset charset = StandardCharsets.UTF_8;

        log("Replace " + mode 
                + " text \"" + regexp 
                + "\" with \"" + replacement 
                + "\" in " + source);   

        String content = getTextFromFile(sourceFilePath, charset);

        TextReplacementMode textReplacementMode = getTextReplacementMode(mode);

        String newContent = replace(content, regexp, replacement, textReplacementMode);

        overwriteFileWithReplacedContent(sourceFilePath, charset, newContent);
    }


	private Path overwriteFileWithReplacedContent(Path sourceFilePath, Charset charset, String newContent) {
		return supplyAndMapThrowableToFailure(

                () -> Files.write(sourceFilePath, newContent.getBytes(charset)));
	}


	private TextReplacementMode getTextReplacementMode(String mode) {
		return supplyUnchecked(

                () -> TextReplacementMode.valueOf(mode),

                TextReplacementMode.first);
	}


	private String getTextFromFile(Path sourceFilePath, Charset charset) {
		return supplyAndMapThrowableToFailure(

                () -> new String(Files.readAllBytes(sourceFilePath), charset));
	}


	private String replace(String content, String regexp, String replacement, TextReplacementMode textReplacementMode) {
		switch (textReplacementMode){
		
            case all:
                return content.replaceAll(regexp, replacement);	

            case first:
                return content.replaceFirst(regexp, replacement);	

        }
		throw new Failure("Replacement method not implemented: " + textReplacementMode);
	}


    @Override
    public String getTestCaseScenario(){
        return newScenario("Replace first/all occurences of regex "
        		+ " in file with replaceText",
                "Test data: source, regex, replaceText, mode.");
    }

}
