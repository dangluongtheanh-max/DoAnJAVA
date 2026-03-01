package DAO;

import DTO.SanPhamDTO;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;

public class SanPhamDAO {

    public ArrayList<SanPhamDTO> getAll() { // đưa DS sản phẩm từ data base vào mảng
        ArrayList<SanPhamDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM SanPham";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                SanPhamDTO sp = new SanPhamDTO();
                sp.setMaSP(rs.getInt("Ma_SP"));
                sp.setTenSP(rs.getString("Ten_SP"));
                sp.setLoaiSP(rs.getString("Loai_SP"));
                sp.setCoImei(rs.getInt("Co_Imei"));
                sp.setHang(rs.getString("Hang"));
                sp.setGiaBan(rs.getBigDecimal("Gia_Ban"));
                sp.setTrangThai(rs.getInt("Trang_Thai"));

                list.add(sp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
