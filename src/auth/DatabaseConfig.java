package auth;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConfig {
    private static final String URL = "jdbc:mysql://localhost:3307/auth_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            throw new RuntimeException("DB Connection Failed", e);
        }
    }
}
