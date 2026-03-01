package DAO;

import DTO.NhanVienDTO;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;

public class NhanVienDAO { // đưa DS nhân viên từ data base vào mảng

    public ArrayList<NhanVienDTO> getAll() {
        ArrayList<NhanVienDTO> dsNhanVien = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien";

        try (Connection con = DBConnection.getConnection(); //kết nối với data base
             PreparedStatement ps = con.prepareStatement(sql); //chuẩn bị sql
             ResultSet rs = ps.executeQuery()) { //chạy câu lệnh và chứa kết quả

            while (rs.next()) {
                NhanVienDTO nv = new NhanVienDTO();
                nv.setMaNV(rs.getInt("Ma_NV"));
                nv.setTenNV(rs.getString("Ten_NV"));
                nv.setVaiTro(rs.getString("Vai_Tro"));
                nv.setUsername(rs.getString("Username"));
                nv.setTrangThai(rs.getInt("Trang_Thai"));

                dsNhanVien.add(nv);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsNhanVien;
    }

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
