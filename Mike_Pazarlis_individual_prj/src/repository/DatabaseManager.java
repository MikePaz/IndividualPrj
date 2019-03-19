package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    private final String DB_URL = "jdbc:mysql://localhost:3306/messages?zeroDateTimeBehavior=convertToNull&serverTimezone=Europe/Athens&useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&autoReconnect=true";
    private final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    private final String username = "root";
    private final String password = "6983829187";

    private Connection connection;

    public DatabaseManager() {
        createDBConnection();
    }

    public Connection getDatabaseConnection() {
        return connection;
    }

    public boolean closeConnection() {
        try {
            connection.close();
            return true;
        } catch (SQLException e) {
            System.out.println("Failed to close the database connection: " + e);
            e.printStackTrace();
        }
        return false;
    }

    private void createDBConnection() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Connecting to database...");
            connection = DriverManager.getConnection(DB_URL, username, password);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
