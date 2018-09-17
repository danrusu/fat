package utils.server;
import static core.Logger.log;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import utils.Ssh;

public class MainServer {


    public static class FileNamesAndUrls{
        
        private List<String> names;
        private List<String> urls;

        public FileNamesAndUrls(List<String> names, List<String> urls) {
            this.names = List.copyOf(names);
            this.urls = List.copyOf(urls);
        }

        public List<String> getNames() {
            return names;
        }

        public List<String> getUrls() {
            return urls;
        }

    }


    public static final String ip = "";
    public static final String localIpUrl = "";
    public static final String url = "";

    public static final String sshAdminUser = "";
    public static final String sshAdminPass = "";
    public static final String sshPort = "22";

    public static final String databaseName = "";
    public static final String databaseUser = "";
    public static final String databasePass = "";
    public static final String databasePort = "3306";

    public static final String htmlRoot = "/var/www/html/";

    public static final String logsFolder = "logs/";
    public static final String logsFolderFullPath = htmlRoot + logsFolder;

    public static final String tempFolder = "temp/";
    public static final String tempFolderFullPath = htmlRoot + tempFolder;



    public static void sendFileToServer(
            String sourceFilePath,
            String destinationPath, 
            String uniqResultFileName){

        Ssh.sendFile(sshAdminUser,
                sshAdminPass,
                ip,
                Integer.parseInt(sshPort),
                sourceFilePath,
                destinationPath + uniqResultFileName);	
    }



    public static FileNamesAndUrls sendFilesToServer(List<String> sourceFilePathsArray){ 

        log("Documents to upload: " + sourceFilePathsArray);

        List<String> fileUrls = new ArrayList<>();
        List<String> fileNames = new ArrayList<>();

        sourceFilePathsArray.forEach(sourceFilePath -> {

            String sourceFileName = Paths.get(sourceFilePath).getFileName().toString();           

            sendFileToServer(
                    sourceFilePath,
                    tempFolderFullPath, 
                    sourceFileName);

            fileNames.add(sourceFileName);
            fileUrls.add(url + tempFolder + sourceFileName);
        });

        return new FileNamesAndUrls(fileNames, fileUrls);
    }

}

