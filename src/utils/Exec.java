package utils;
import static base.Logger.getLogDirPath;
import static base.Logger.log;
import static base.Logger.logLines;

import java.io.BufferedReader;
//import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
import java.lang.ProcessBuilder.Redirect;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

/**
 * Class to Allow for execution of other programs from java.
 *
 * This provides a simple interface to fire off a command and get the resulting output. By default, the run method is asynchronous
 * (non-blocking) If blocking is required, the user can call join() -- which will cause a wait until the subprocess terminates.
 * There are also methods to wait for chars or lines of output. (Which are dependent on flushes from said processes )
 *
 * @TODO -- get process input redirect working. This is only partially working as is, but it is not clear if it will be needed.
 *
 * @author danginkgo
 *
 */
public class Exec
{

   private final List< String > Commands;
   private File                 ExecPath;
   private Process              process;
   private File                 output;
   private BufferedReader       ProcOutput;
   // private BufferedWriter ProcInput;
   Map< String , String >       CustomEnv;

   /**
    * Constructor
    */
   public Exec( ){
      Commands = new Vector< String >( );
      ExecPath = new File( "/" );
      output = null;
      process = null;
      CustomEnv = new HashMap< String , String >( );
   }

   /**
    * When building up a new command exec environment, call this first before adding additional params
    *
    * @param execString
    *           The command (ie program name) to run.
    */
   public void setCommand( String execCommand ){
      CustomEnv.clear( );
      Commands.clear( );
      Commands.add( execCommand );
   }

   /**
    * Make sure command has been set before adding args
    *
    * @param execArg
    *           - the argument to be added
    */
   public void addArg( String execArg ){
      Commands.add( execArg );
   }
   
   
   public void addArgs(String... args){
	   Arrays.asList(args).forEach(x->Commands.add(x));
   }
   

   /**
    * Add a custom environment variable scoped for the next subprocess run
    *
    * @param varName
    *           the variable name
    * @param value
    *           the value
    */
   public void addEnvironmentVar( String varName , String value ){
      CustomEnv.put( varName , value );
   }

   /**
    * @return whether a process is currently running
    */
   public boolean isRunning( ){
      if ( process != null  && process.isAlive())
      {
            return true;
      }
      else
      {
    	  return false;
      }
   }

   /**
    * attempt to hard kill a subprocess. Use this method with caution
    */
   public void kill( ){
      if ( isRunning( ) )
      {
    	 System.out.println("Destroying a thread");
         process.destroy( );
      }
   }

   /**
    * @param execPath
    *           The path to the specified command ( typically the folder the program resides in )
    */
   public void setPath( File execPath ){
      ExecPath = execPath;
   }

   /**
    * @param execPath
    *           The path to the specified command ( typically the folder the program resides in )
    */
   public void setPath( String execPath ){
      ExecPath = new File( execPath );
   }

   /**
    * Wait until the subprocess terminates or throw Exception if timeOutInseconds has elapsed Will kill process if the timer goes
    *
    * @param timeOutInseconds
    *           number seconds until giving up
    * @throws Exception
    */
   public void join( int timeOutInseconds ) throws Exception{
      final Date start = new Date( );
      final long end = start.getTime( ) + ( timeOutInseconds * 1000 );
      while ( isRunning( ) )
      {
         final Date curr = new Date( );
         if ( curr.getTime( ) > end )
         {
            kill( );
            throw new Exception( "Exec join() - timeout " );
         }
         // Quick sleep for the scheduler
         try
         {
            Thread.sleep( 10 );
         }
         catch ( final InterruptedException e1 )
         {
         }
      }
   }

   /**
    * Wait until the subprocess has written a char of output.
    *
    * @param timeOutInseconds
    *           number seconds until giving up
    * @return the char of subprocess output just read. -1 is returned if the process has terminated and all output has been read.
    * @throws Exception
    *            if the timeout period has elapsed
    */
   public int waitForChar( int timeOutInseconds ) throws Exception{
      final Date start = new Date( );
      final long end = start.getTime( ) + ( timeOutInseconds * 1000 );

      while ( isRunning( ) ){
         final Date curr = new Date( );
         if ( curr.getTime( ) > end )
         {
            kill( );
            throw new Exception( "Exec waitForChar() - timeout " );
         }
         final int read = ProcOutput.read( );
         if ( read >= 0 )
         {
            return read;
         }
         try
         {
            Thread.sleep( 10 );
         }
         catch ( final InterruptedException e )
         {
         }
      }
      //
      // Proc is done -- but there may still be some unread output
      //
      return ProcOutput.read( );
   }

   /**
    * Wait until the subprocess has written a line of output.
    *
    * @param timeOutInseconds
    *           number seconds until giving up
    * @return the line of subprocess output just read. Null is returned if the process has terminated and all output has been
    *         read.
    * @throws Exception
    *            if the timeout period has elapsed
    */
   public String waitForLine( int timeOutInseconds ) throws Exception{
      final Date start = new Date( );
      final long end = start.getTime( ) + ( timeOutInseconds * 1000 );

      while ( isRunning( ) )
      {
         final Date curr = new Date( );
         if ( curr.getTime( ) > end )
         {
            kill( );
            throw new Exception( "Exec waitForLine() - timeout " );
         }
         final String s = ProcOutput.readLine( );
         if ( s != null )
         {
            // log(">>" + s );
            return s;
         }
         try
         {
            Thread.sleep( 10 );
         }
         catch ( final InterruptedException e )
         {
         }
      }
      //
      // Proc is done -- but there may still be some unread output
      //
      return ProcOutput.readLine( );
   }

   /**
    * Get all output (stdout and stderr) generated thus far from the subprocess
    *
    * @param logOutput
    *           -- whether to send the output to Logger
    * @return all output read from the subprocess, as a string vector
    * @throws IOException
    */
   public Vector< String > getOutput( boolean logOutput ) throws IOException{
      final Vector< String > returnVector = new Vector< String >( );
      final FileInputStream fis = new FileInputStream( output );
      final BufferedReader bufferedReader = new BufferedReader( new InputStreamReader( fis , "UTF-8" ) );
      String line = bufferedReader.readLine( );
      while ( line != null ){
         returnVector.add( line );
         if ( logOutput ){
            log( ">>>" + line );
         }
         line = bufferedReader.readLine( );
      }
      bufferedReader.close( );
      return returnVector;
   }

   
   
   public void deleteTmp( ){
      if ( output.exists( ) ){
         output.delete( );
      }
   }

   /**
    * Send a line of 'input' to the running subprocess
    *
    * @param input
    *           (stdin ) for the subproccess to receive
    * @throws IOException
    */
   // TODO -- get proc input working
   /*
    * public void writeInputLn( String input ) throws IOException { if ( isRunning() ) { ProcInput.write(input);
    * ProcInput.newLine(); ProcInput.flush(); } else { log("Exec writeInput : process NOT running!");
    *
    * } }
    */

   /**
    * Fire off the subprocess (Which is forked at the end of this call)
    *
    * @param outputFileName
    *           -- name of the output (file can be accessed with getFile())
    * @throws Exception
    */
   public void run( String outputFileName ) throws Exception{
      String logPath;
      if ( isRunning( ) )
      {
         throw new Exception( "Exec : process already running" );
      }

      //
      // Setup cmd String
      //
      String execString = "";
      for ( final String s : Commands )
      {
         execString += ' ' + s;
      }
      log( "Exec Cmd: " + execString + "\n");
    		  //ExecPath.getPath( ) 
      
      final ProcessBuilder pb = new ProcessBuilder( Commands );

      //
      // Setup environment as needed
      //
      final Map< String , String > env = pb.environment( );
      for ( final Entry< String , String > customEnv : CustomEnv.entrySet( ) )
      {
         env.put( customEnv.getKey( ) , customEnv.getValue( ) );
      }

      //
      // Setup to capture output to temp file
      //
      pb.directory( ExecPath );
      pb.redirectErrorStream( true );
      logPath = getLogDirPath().toString();      
      File temp = new File( logPath + "/temp/" );
      if ( false == temp.exists( ) )
      {
          temp.mkdirs( );
      }        
      output = new File( logPath + "/temp/" + outputFileName );
      
      if ( ProcOutput != null )
      {
          //
          //Close the old reader
          //
          ProcOutput.close( );
      }
      
      	
      if ( output.exists( ) )
      {                   
          output.delete( );
      }
          
      output.createNewFile( );
      
      
   
      ProcOutput = new BufferedReader( new InputStreamReader( new FileInputStream( output ) , "UTF-8" ) );
      pb.redirectOutput( Redirect.appendTo( output ) );
      
      //
      // Start process
      //
      process = pb.start( );
      
      
      //
      // TODO capture input stream of the running process
      //
      // ProcInput = new BufferedWriter( new OutputStreamWriter(
      // process.getOutputStream() ));
      // ProcInput.flush();
  
   }
   
   /**
    * Clean up - close ProcOutput buffer and delete output. 
    *    
    */
   public void cleanup(){
	   if(ProcOutput != null){
		   try {
			   ProcOutput.close();
			   ProcOutput = null;
		   }
		   catch (Exception e){
			   logLines("" + e);
		   }
	   }  

	   if ( output.exists( ) )
	   {                   
		   output.delete( );
	   }
   } 

}
