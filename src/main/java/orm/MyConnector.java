package orm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class MyConnector {

    private static Connection connection;

    private static String connectionString = "jdbc:mysql://localhost:3306/";

    public static Connection createConnection(String username, String password, String DbName) throws SQLException {
        Properties props = new Properties();
        props.setProperty("user", username);
        props.setProperty("password", password);
        connection = DriverManager.getConnection(connectionString + DbName, props);
        return connection;
    }

    public static Connection getConnection(){
        return connection;
    }
}
