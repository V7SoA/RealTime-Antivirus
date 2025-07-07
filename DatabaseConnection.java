import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static volatile Connection connection;

    private DatabaseConnection() {
        // Private constructor to prevent instantiation
    }

    public static Connection getConnection() {
        if (connection == null) {
            synchronized (DatabaseConnection.class) {
                if (connection == null) {
                    try {
                        // Load PostgreSQL JDBC driver
                        Class.forName("org.postgresql.Driver");

                        // Database connection details
                        String url = "jdbc:postgresql://localhost:5432/antivirusdb";
                        String user = "postgres";
                        String password = "root";

                        connection = DriverManager.getConnection(url, user, password);

                        // Initialize DB schema if not exists
                        initializeDatabase();
                    } catch (ClassNotFoundException | SQLException e) {
                        e.printStackTrace();
                        connection = null;
                    }
                }
            }
        }
        return connection;
    }

    private static void initializeDatabase() {
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS threats (
                hash TEXT PRIMARY KEY,
                decision BOOLEAN NOT NULL
            )
            """;

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(createTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
