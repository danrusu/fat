package utils.database;

import java.sql.Connection;

import utils.server.MainServer;

public interface MainDatabase {


    public static Connection connect() {

        return Sql.connect(

                Driver.mysql,

                MainServer.ip,
                MainServer.databasePort,
                MainServer.databaseName,

                MainServer.databaseUser,
                MainServer.databasePass);
    }

}
