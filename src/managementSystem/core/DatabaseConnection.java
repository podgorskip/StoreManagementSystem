package managementSystem.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * A Singleton class that is responsible for establishing a connection with a database.
 */
public class DatabaseConnection {
    private static Connection connection;

    private DatabaseConnection() { }

    static {

        String url = "jdbc:mysql://localhost:3306/store";
        String user = "root";
        String password = "Macbook.793672861:D";

        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns a single connection object.
     * @return Connection object with a connection established
     */
    public static Connection getConnection() {
        return connection;
    }
}
