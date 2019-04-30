package main.java.utils;

import static main.java.base.Logger.error;
import static main.java.base.Logger.log;
import static main.java.base.Logger.logLines;
import static main.java.base.Logger.logSplitByLines;

import com.google.common.base.Function;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class Ssh {

	private final int SSH_MAX_MILLISECONDS_TIMEOUT = 60000;

	private JSch ssh;

	private String userName;
	private String host;
	private int port;

	private Session session;
	private Channel channel;


	public Ssh(String userName, String host, int port) {
		this.userName = userName;
		this.host = host;
		this.port = port;

		ssh = new JSch();
		JSch.setConfig("StrictHostKeyChecking", "no"); 
	}


	public void sendFileWithPassword(
			String sourcePath, 
			String destPath,
			String password) {

		log("SSH send file (auth: PASSWORD)");

		sendFile(sourcePath, destPath,
				this::getSessionWithPassword,
				password);
	}


	public void sendFileWithPrivateKey(
			String sourcePath, 
			String destPath,
			String privateKeyFilePath) {

		log("SSH send file (auth: PRIVATE_KEY)");

		sendFile(sourcePath, destPath,
				this::getSessionWithPrivateKey,
				privateKeyFilePath);
	}


	private Session getSessionWithPrivateKey(String privateKeyFilePath) {

		JSch ssh = new JSch();
		JSch.setConfig("StrictHostKeyChecking", "no"); 
		try {

			ssh.addIdentity(privateKeyFilePath);
			session = ssh.getSession(userName, host, port);		

		} catch (JSchException e) {
			throw new RuntimeException(e);
		}

		return session;
	}


	private Session getSessionWithPassword(String password) {

		try {		
			session = ssh.getSession(userName, host, port);		
			session.setPassword(password);

		} catch (JSchException e) {
			throw new RuntimeException(e);
		}

		return session;
	}


	private void sendFile(
			String sourcePath, 
			String destPath,
			Function<String, Session> authenticatedSessionSupplier,
			String authentication) {

		logLines("SSH Source: " + sourcePath,
				"SSH Destination: " + host + ":/" + destPath);

		try {
			session = authenticatedSessionSupplier.apply(authentication);
			session.setTimeout(SSH_MAX_MILLISECONDS_TIMEOUT);

			log("SSH waiting for connection...");
			session.connect();                        
			log("SSSH session connected. Waiting for transfer...");

			channel = session.openChannel("sftp");
			channel.connect();

			ChannelSftp sftp = (ChannelSftp) channel;            
			sftp.put(sourcePath, destPath);

		} catch (Exception e) {
			error("SSH FAILED!!!");
			logSplitByLines("" + e);

		} finally {
			if (channel != null) { channel.disconnect(); }
			if (session != null) { session.disconnect(); }
		}

		log("SSH send finished");
	}

}
