package managementSystem.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {
    Connection connection;

    public Connect(String database) {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + database, "root", "Macbook.793672861:D");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Connection connection() {
        return connection;
    }
}
