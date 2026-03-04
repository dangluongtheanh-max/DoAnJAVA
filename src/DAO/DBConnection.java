package DAO;
import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    public static Connection getConnection() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            String url = "jdbc:sqlserver://localhost:1433;"
                    + "databaseName=QLCHLT-PK;"
                    + "encrypt=true;"
                    + "trustServerCertificate=true;";//chuỗi kết nối csdl

            String user = "sa"; // hoặc user bạn tạo
            String pass = "1"; // mật khẩu

            return DriverManager.getConnection(url, user, pass);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
