package core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton utility class to manage the Oracle DB connection.
 * Uses ojdbc17.jar from the lib/ folder.
 */
public class DatabaseConnection {

    // --- UPDATE THESE TO MATCH YOUR ORACLE SETUP ---
    private static final String URL = "jdbc:oracle:thin:@localhost:1521/XE";
    private static final String USERNAME = "AUCTION";
    private static final String PASSWORD = "auction123";
    // ------------------------------------------------

    private static Connection connection = null;

    /**
     * Returns a single shared connection to the Oracle database.
     * Creates a new connection if one does not already exist or is closed.
     */
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                // ojdbc17 auto-registers the driver, but explicit loading is safe
                Class.forName("oracle.jdbc.OracleDriver");
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("[DB] Connection established successfully.");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("[DB ERROR] ojdbc17.jar not found in classpath. Check lib/ folder.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("[DB ERROR] Could not connect to Oracle. Check URL, username, and password.");
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * Closes the shared connection (call on app shutdown).
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("[DB] Connection closed.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Quick test - run this main() to verify the connection works
    public static void main(String[] args) {
        Connection conn = getConnection();
        if (conn != null) {
            System.out.println("[TEST] Connection test passed!");
            closeConnection();
        } else {
            System.out.println("[TEST] Connection test FAILED. See errors above.");
        }
    }
}
