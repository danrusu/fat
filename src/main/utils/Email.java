package main.utils;

import static main.base.Logger.log;
import static main.base.Logger.logSplitByLines;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
//import javax.activation.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public interface Email {
	

	public static void sendMail(String to, 
			String subject, 
			String message) {
		
		
		String  mailServer = "smtp.gmail.com";

		// Sender's email ID needs to be mentioned
		String from = "jenkins@wat.azets.com";
		// Authentication
		String mailUser = "cdoxcdox4@gmail.com";
		String mailPass = "cdoxcdox4-200+400";
		
		/*String mailUser = "danginkg@gmail.com";
		String mailPass = "a3nhnkXS!";*/


		logSplitByLines("Send email:"
				+ "\nMail server: " + mailServer
				+ "\nTo: " + to
				+ "\nFrom: " + from
				+ "\nSubject: " + subject
				+ "\nMessage: " + message);
		
		// Get system properties
		Properties properties = System.getProperties();
		
		

		// Setup mail server
		properties.setProperty("mail.smtp.host", mailServer);

		properties.setProperty("mail.smtp.auth", "true"); 

        
		//To use SSL
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class",  "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.port", "465");
        
        
        
		// Get the default Session object - needed for authentication
        Session session = Session.getDefaultInstance(properties, new Authenticator(){
        	        protected PasswordAuthentication getPasswordAuthentication() {
        	            return new PasswordAuthentication(mailUser, mailPass);
        	        }
        	});
	    //session.setDebug(true);

	    
		try {
			// Create a default MimeMessage object.
			MimeMessage mimeMessage = new MimeMessage(session);

			// Set From: header field of the header.
			mimeMessage.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			// Set Subject: header field
			mimeMessage.setSubject(subject);

			// Now set the actual message
			mimeMessage.setText(message);

			// Send message
			Transport.send(mimeMessage);
			log("Sent message successfully to " + to);

		}catch (MessagingException mex) {
			logSplitByLines("" + mex);
		}
	}
}