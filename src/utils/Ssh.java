package utils;
import static base.Logger.log;
import static base.Logger.logLines;
import static base.Logger.logSplitByLines;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public final class Ssh {

    
    
    private Ssh(){
        throw new AssertionError("This helper class must not be istantiated!");
    }

    
    
    public static void sendFile(
            String userName, 
            String password, 
            String host, int port, 
            String sourcePath,
            String destPath) {


        logLines(
                "Send file via ssh",
                "Source: " + sourcePath,
                "Destination: " + host + ":/" + destPath);
        
        Session session = null;
        Channel channel = null;
        
        try {
            JSch ssh = new JSch();
            JSch.setConfig("StrictHostKeyChecking", "no");
            
            session = ssh.getSession(userName, host, port);
            session.setPassword(password);
            session.connect();
            
            log("Ssh session connected.");
            channel = session.openChannel("sftp");
            channel.connect();
            
            ChannelSftp sftp = (ChannelSftp) channel;
            sftp.put(sourcePath, destPath);

            
        } catch (Exception e) {
            log("Username:" + userName);
            logSplitByLines(""+e);

            
        } finally {
            if (channel != null) {
                channel.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        }
        
        log("File " + sourcePath  
                + " sent to " + host + ":/" + destPath );
    }
    
}

