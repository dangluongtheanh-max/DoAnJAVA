package Utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    // 🔧 SỬA THÔNG TIN THEO DATABASE CỦA BẠN
    private static final String URL =
            "jdbc:sqlserver://localhost:1433;databaseName=;encrypt=true;trustServerCertificate=true";
    private static final String USER = "sa";
    private static final String PASSWORD = "123456";

    public static Connection getConnection() {
        try {
            // Load driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Connection getConn() {
    return getConnection();
}
}

