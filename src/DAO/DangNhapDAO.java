package DAO;

import DTO.NhanVienDTO;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DangNhapDAO{
   public NhanVienDTO findByUsername(String username) {

    String sql = "SELECT * FROM NhanVien WHERE Username = ?";

    try (Connection connect = DBConnection.getConnection();
         PreparedStatement ps = connect.prepareStatement(sql)) {

        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            NhanVienDTO nv = new NhanVienDTO();
            nv.setMaNV(rs.getInt("Ma_NV"));
            nv.setTenNV(rs.getString("Ten_NV"));
            nv.setUsername(rs.getString("Username"));
            nv.setPassword(rs.getString("Password"));
            nv.setVaiTro(rs.getString("Vai_Tro"));
            nv.setTrangThai(rs.getInt("Trang_Thai"));
            return nv;
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return null;
}

}
