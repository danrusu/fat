package utils.database;
import static core.Logger.debug;
import static core.Logger.log;
import static core.failures.ThrowablesWrapper.wrapThrowable;

import java.sql.Connection;
import java.sql.DriverManager;

import core.failures.Failure;

public enum Driver{
    mysql, 
    sqlserver;



    //mysql: "com.mysql.jdbc.Driver"
    //mssql: "com.microsoft.sqlserver.jdbc.SQLServerDriver"
    public void loadDriver(Driver driver){

        log("Loading driver " + driver.getName());

        wrapThrowable(
                "Cannot find the driver in the classpath!",
                () -> {

                    Class.forName(driver.getName());
                    log("Driver loaded!");
                    return null;
                });    
    }



    public String getName() {
        switch (this){

            case mysql:
                return "com.mysql.jdbc.Driver";

                //mssql
            case sqlserver: 
                return "com.microsoft.sqlserver.jdbc.SQLServerDriver";

            default:
                return "unknownDatabaseDriver";
        }
    }



    public String getDatabaseConnectionUrl(String databaseIp, String databasePort, String databaseName) {
        switch (this){

            case mysql:
                return "jdbc:" + this.name() + "://"  
                + databaseIp + ":" + databasePort + "/" + databaseName;

            // mssql
            case sqlserver: 
                return "jdbc:" + this.name() + "://"  
                + databaseIp + ":" + databasePort + ";" + databaseName + ";";

            default:
                throw new Failure("Coud not handle this tipe of connection: " + this.name());
        }
    }



    public Connection getConnection(String databaseIp,
            String databasePort,
            String databaseName,

            String username, 
            String password) {


        return wrapThrowable(
                
                "Cannot connect the database!",
                
                () -> {
                    String url;
                    Connection connection;
                    loadDriver(this);

                    switch (this){

                        case mysql:
                            
                            url = this.getDatabaseConnectionUrl(databaseIp, databasePort, databaseName);
                            debug("Connecting to database " + url);

                            connection = DriverManager.getConnection(url, username, password);
                            log("Database connected!");

                            return connection;

                        case sqlserver: 
                            
                            url = this.getDatabaseConnectionUrl(databaseIp, databasePort, databaseName) 
                            + "user=" + username + ";"                        
                            + "password=" + password + ";";
                            debug("Connecting to database " + url);

                            connection = DriverManager.getConnection(url);
                            log("Database connected!");

                            return connection;
                            
                        default:
                            throw new Failure("Coud not handle this type of connection: " + this.name());
                    }
                });
    }

}
