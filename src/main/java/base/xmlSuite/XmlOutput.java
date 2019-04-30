package main.java.base.xmlSuite;

import static main.java.base.Logger.getLogDirPath;
import static main.java.base.Logger.log;

import java.io.File;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

public class XmlOutput
{
	private File          Output;

	public XmlOutput()
	{   
		Output = null;
	}


	protected void saveOutput( File outputFile , XmlDoc outputDoc )
	{
		final TransformerFactory transformerFactory = TransformerFactory.newInstance( );
		Transformer transformer = null;
		
		try
		{
			transformer = transformerFactory.newTransformer( );
			transformer.setOutputProperty( OutputKeys.INDENT , "yes" );

		}
		catch ( final TransformerConfigurationException e )
		{
			log( "XMLOutput save" + e );
		}
		final StreamResult result = new StreamResult( outputFile );
		try
		{
			transformer.transform( outputDoc.getDomSrc( ) , result );
		}
		catch ( final Exception e )
		{
			log( "XMLOutput save" + e );
		}
	}



	/**
	 * Save output results in an XML file.
	 * 
	 * @param outputDoc - the results XmlDoc
	 */
	public void saveOutput( XmlDoc outputDoc )
	{
		// TODO -- generate file name for output here		
		try
		{
			Output = new File( getLogDirPath() 
			        + outputDoc.getRoot( ).getAttribute( "name" ) + "_result.xml" );
			log( "XMlOutput output " + Output.getPath( ) );
			saveOutput( Output , outputDoc );

		}
		catch ( final Exception e )
		{
			log( "XMlOutput caught exception" + e );
		}

	}



	public File getOutput( )
	{
		return Output;
	}


}
