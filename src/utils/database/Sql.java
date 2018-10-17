package utils.database;

import static base.Logger.log;
import static base.failures.ThrowablesWrapper.unchecked;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public interface Sql {


    public static Connection connect(

            Driver driver,

            String databaseIp,
            String databasePort,
            String databaseName,

            String username, 
            String password){


        return driver.getConnection(
                databaseIp,
                databasePort,
                databaseName,
                username, 
                password);
    }



    private static String getColumnName(
            ResultSetMetaData resultSetMetaData, 
            int columnIndex) {
        
        return unchecked(() -> resultSetMetaData.getColumnName(columnIndex));
    }



    private static String getColumnValue(ResultSet resultSet, int columnIndex) {
        
        return unchecked(() -> resultSet.getString(columnIndex));
    }



    // INSERT, UPDATE, DELETE statement or an SQL statement that returns nothing
    public static int executeUpdate(Connection connection, String sqlQuery){
        
        return unchecked(() -> connection.createStatement().executeUpdate(sqlQuery));
    }



    //  SELECT
    public static Map<Integer, Map<String, String >> executeQuery(
            Connection connection, 
            String sqlQuery){

        return unchecked(() ->{

            Map<Integer, Map<String, String >> rowsMap = new TreeMap<>();


            ResultSet resultSet = connection
                    .createStatement() 
                    .executeQuery(sqlQuery);  // never null


            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

            int columnsCount = resultSetMetaData.getColumnCount();            
            log("SQL result: " 
                    + resultSet.getFetchSize() + " rows; "
                    + resultSetMetaData.getColumnCount() + " columns");


            // get columns names
            Map<Integer, String> columnNames = IntStream
                    .rangeClosed(1, columnsCount)
                    .parallel()
                    .boxed()
                    .collect(Collectors.toMap(
                            Function.identity(), // key
                            i -> getColumnName(resultSetMetaData, i)) // value
                    );

            int rowIndex = 0;
            while(resultSet.next()){

                Map<String, String> columnsMap = IntStream
                        .rangeClosed(1, columnsCount)
                        .boxed()
                        .collect(Collectors.toMap(
                                i -> columnNames.get(i),
                                i -> getColumnValue(resultSet, i)));

                rowsMap.put(rowIndex++, columnsMap);
            }

            return  rowsMap;
        });

    }



    public static void disconnect(Connection connection){
        unchecked(() -> {
            connection.close(); 
            return null;
        });
    }
    
}

