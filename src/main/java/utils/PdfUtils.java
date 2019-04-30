package main.java.utils;

import static main.java.base.Logger.debug;
import static main.java.base.Logger.log;
import static main.java.base.Logger.logSplitByLines;
import static main.java.base.Logger.writeTextToFileInLogDir;
import static main.java.base.failures.ThrowablesWrapper.supplyAndMapThrowableToFailure;
import static main.java.utils.FileUtils.createDir;

import java.awt.Rectangle;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import javax.imageio.ImageIO;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import main.java.base.failures.ThrowablesWrapper;
import main.java.projects.common.StringComparison;
//PDF basic functionalities
public interface PdfUtils {


    public static enum PdfCompareMethods{
        pdfbox,    // using org.apache.pdfbox
        compareit; // using /user.dir/pdf/CompareIt/wincmp3.exe
    };



    public static String getText(String filename){

        return supplyAndMapThrowableToFailure(

                () -> {
                    //Loading an existing document
                    File file = new File(filename);
                    PDDocument document = PDDocument.load(file);

                    //Instantiate PDFTextStripper class
                    PDFTextStripper pdfStripper = new PDFTextStripper();

                    //Retrieving text from PDF document
                    String text = pdfStripper.getText(document);


                    //Closing the document
                    document.close();

                    return text;
                });
    }



    public static String[] getTextLines(String filename){

        return supplyAndMapThrowableToFailure(

                () -> getText(filename).split("\n"));
    }



    public static String getText(String filename, int pageNr, Rectangle rect ){

        return supplyAndMapThrowableToFailure(

                () -> {
                    //Loading an existing document
                    File file = new File( filename );
                    PDDocument document = PDDocument.load( file );

                    PDFTextStripperByArea stripper = new PDFTextStripperByArea();
                    stripper.setSortByPosition( true );

                    stripper.addRegion( "region1", rect );
                    PDPage firstPage = document.getPage( pageNr );

                    stripper.extractRegions( firstPage );


                    log( "PDF text from area:" + rect );
                    String text = stripper.getTextForRegion( "region1" );

                    //Closing the document
                    document.close();

                    return text;
                });
    }



    /**
     * Compare PDF files by text, line by line and log all differences.
     * 
     * @param filename1 first PDF to compare	
     * @param filename2 second PDF to compare
     * @return
     * @throws IOException 
     * @throws InvalidPasswordException 
     * @throws Exception
     */
    public static boolean comparePDFbyLineText(String filename1, String filename2){

        String[] file1Lines = supplyAndMapThrowableToFailure(
                () -> getTextLines(filename1));

        String[] file2Lines = supplyAndMapThrowableToFailure(
                () -> getTextLines(filename2));


        if (file1Lines.length != file2Lines.length){

            log( "Different nr of lines! " 
                    + filename1 + ": " + file1Lines.length
                    + "; " + filename2 + ": " + file2Lines.length);

            return false;
        }

        // test line by line and log all differences (do not break at first difference)
        boolean filesAreIdentical = true;

        for (int i=0; i < file1Lines.length; i++){
            if (! file1Lines[i].equals(file2Lines[i])){
                logSplitByLines("Different text on line " + i + "\n"
                        + filename1 + "- " + file1Lines[i] + "\n"
                        + filename2 + "- " + file2Lines[i] + "\n");
                filesAreIdentical = false;				
            }
        }

        return filesAreIdentical;
    }


    public static boolean compareStringsByLineText(String expected, String actual){

        writeTextToFileInLogDir(expected, "expectedPdf.txt");
        writeTextToFileInLogDir(actual, "actualPdf.txt");

        String[] expectedLines = expected.split("\n");
        String[] actualLines = actual.split("\n");

        if (expectedLines.length != actualLines.length){
            log( "Different nr of lines! " 
                    + expectedLines + ": " + expectedLines.length
                    + "; " + actualLines + ": " + actualLines.length);
            return false;
        }

        // test line by line and log all differences (do not break at first difference)
        boolean strAreIdentical = true;

        for (int i=0; i<expectedLines.length; i++){
            if (! expectedLines[i].equals(actualLines[i])){
                logSplitByLines("Different text on line " + i + "\n"
                        + "str1- " + expectedLines[i] + "\n"
                        + "str2- " + actualLines[i] + "\n");
                strAreIdentical = false;				
            }
        }

        return strAreIdentical;
    }



    public static boolean comparePDFbyText(String expected, String actual){

        return supplyAndMapThrowableToFailure(
                () -> getText(expected).equals(getText(actual)));

    }



    public static void addText(String filename, int pageNr, String text){

        ThrowablesWrapper.supplyAndMapThrowableToFailure(

                () -> {

                    //Loading an existing document
                    PDDocument document = PDDocument.load(new File(filename));

                    //Retrieving the pages of the document 
                    PDPage page = document.getPage(pageNr);
                    PDPageContentStream contentStream = new PDPageContentStream(document, 
                            page, 
                            PDPageContentStream.AppendMode.APPEND, 
                            false);


                    //Begin the Content stream 
                    contentStream.beginText(); 

                    //Setting the font to the Content stream  
                    contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);

                    //Setting the position for the line 
                    contentStream.newLineAtOffset(25, 500);


                    //Adding text in the form of string 
                    contentStream.showText(text);      

                    //Ending the content stream
                    contentStream.endText();


                    //Closing the content stream
                    contentStream.close();

                    //Saving the document
                    document.save(new File(filename));

                    //Closing the document
                    document.close();

                    return true;
                }, 

                "Cound not add text at page" + pageNr + "for " + filename);
    }



    public static int getPagesCount(String filename){

        return ThrowablesWrapper.supplyAndMapThrowableToFailure(

                () -> {

                    PDDocument document = PDDocument.load(new File(filename));

                    int nrOfPages = document.getNumberOfPages();

                    document.close();

                    return nrOfPages;
                }, 

                "Could not get the number of pages for " + filename);
    }



    public static boolean comparePDFbyText(
            String[] expectedPdfFiles, 
            String[] actualPdfFiles,
            StringComparison comparison){

        StringBuffer expectedTextBuffer = new StringBuffer();
        StringBuffer actualTextBuffer = new StringBuffer();

        return ThrowablesWrapper.supplyAndMapThrowableToFailure(

                () -> {

                    for (int i=0; i < expectedPdfFiles.length; i++){
                        expectedTextBuffer.append(getText(expectedPdfFiles[i]));
                    }

                    for (int i=0; i < actualPdfFiles.length; i++){
                        actualTextBuffer.append(getText(actualPdfFiles[i]));
                    }   

                    String expectedText = expectedTextBuffer.toString();
                    debug("Expected pdf text: \n" + expectedText);
                    String actualText = actualTextBuffer.toString();
                    debug("Actual pdf text: \n" + actualText);

                    switch(comparison) {

                        case expectedContains:
                        case expectedStartsWith:
                        case expectedEndsWith:
                        case actualContains:
                        case actualStartsWith:
                        case actualEndsWith:
                            return comparison.getComparisonFunction().test(expectedText, actualText);


                        default: 
                            return compareStringsByLineText(expectedText, actualText);
                    }
                },

                "Compare " + expectedPdfFiles + " with " + actualPdfFiles + " by text content");		
    }



    public static boolean compareIt(
            String sourcePdf, 
            String targetPdf, 
            String htmlReportFileName){
        
        Exec executor = new Exec();

        executor.setCommand( "cmd.exe" );

        executor.addArg( "/C" );
        executor.addArg( "start" );

        executor.addArg( System.getProperty("user.dir") + "\\pdf\\CompareIt\\wincmp3.exe" );

        executor.addArg(sourcePdf);
        executor.addArg(targetPdf);

        // H - HTML report
        // S-include statistics
        // N - include line numbers
        // E - delete report if the files are identical
        executor.addArg("/G:HSNE");		
        executor.addArg(htmlReportFileName);

        try {
            executor.run( "compareit.txt" );
        } catch (Exception e) {
            logSplitByLines(""+e);
            return false;
        }

        try {
            executor.join( 5 );
        } catch (Exception e) {
            logSplitByLines(""+e);
            return false;
        }
        finally {
            // log output
            try {
                executor.getOutput(true);
            } catch (IOException e) {
                logSplitByLines(""+e);
            }
        }

        // wincmp3.exe has /E flag, so report is deleted if the files are identical
        if (Files.notExists(
                Paths.get(htmlReportFileName), 
                LinkOption.NOFOLLOW_LINKS)){

            return true;
        }

        return false;
    }



    public static List<RenderedImage> getImagesFromPDF(String filename)  {


        return supplyAndMapThrowableToFailure(

                () -> {

                    List<RenderedImage> images = new ArrayList<>();

                    for (PDPage page : PDDocument.load(new File(filename)).getPages()) {

                        images.addAll(getImagesFromResources(page.getResources()));
                    }



                    return images;
                });
    }



    private static List<RenderedImage> getImagesFromResources(PDResources resources){

        return supplyAndMapThrowableToFailure(

                () -> {

                    List<RenderedImage> images = new ArrayList<>();

                    for (COSName xObjectName : resources.getXObjectNames()) {

                        PDXObject xObject = resources.getXObject(xObjectName);


                        if (xObject instanceof PDFormXObject) {


                            images.addAll(getImagesFromResources(((PDFormXObject) xObject).getResources()));

                        } else if (xObject instanceof PDImageXObject) {

                            images.add(((PDImageXObject) xObject).getImage());
                        }
                    }

                    return images;
                });
    }



    public static void saveImages(
            String filename, 
            String destinationDir) {

        final var imageList = getImagesFromPDF(filename);

        createDir(Paths.get(destinationDir));

        IntStream.range(0, imageList.size()).forEach( 

                index -> writeImageToDisk(
                        destinationDir, 
                        imageList, 
                        index));
    }



    public static Boolean writeImageToDisk(
            String destinationDir, 
            List<RenderedImage> imageList, 
            int index) {

        return supplyAndMapThrowableToFailure(() -> {

            File imageDestination = Paths.get(destinationDir)
                    .resolve("image_" + index + ".png")
                    .toFile();

            ImageIO.write(
                    imageList.get(index), 
                    "png",   
                    imageDestination);

            log("Saved image to disk: " + imageDestination.toString());

            return true;
        });
    }

}

