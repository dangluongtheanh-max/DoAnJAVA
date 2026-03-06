package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
        "jdbc:sqlserver://localhost:1433;"
      + "databaseName=LAPTOPSTORE;"
      + "encrypt=true;"
      + "trustServerCertificate=true";

    private static final String USER = "sa";
    private static final String PASSWORD = "13376655"; // PHẢI TRÙNG SQL Server

    public static Connection getConnection() {
        try {
            Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
            // System.out.println(">>> Kết nối SQL Server thành công");
            return con;
        } catch (SQLException e) {
            System.out.println(">>> Kết nối SQL Server THẤT BẠI");
            e.printStackTrace();
            return null;
        }
    }
}
