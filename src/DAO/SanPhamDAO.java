package DAO;

import DTO.SanPhamDTO;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;

public class SanPhamDAO {

    // Lấy toàn bộ danh sách sản phẩm
    public ArrayList<SanPhamDTO> getAll() {
        ArrayList<SanPhamDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM SANPHAM";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                SanPhamDTO sp = new SanPhamDTO();

                sp.setMaSP(rs.getInt("MaSP"));
                sp.setTenSP(rs.getString("TenSP"));
                sp.setMaLoai(rs.getInt("MaLoai"));
                sp.setThuongHieu(rs.getString("ThuongHieu"));
                sp.setMauSac(rs.getString("MauSac"));
                sp.setGia(rs.getBigDecimal("Gia"));
                sp.setGiaGoc(rs.getBigDecimal("GiaGoc"));
                sp.setSoLuongTon(rs.getInt("SoLuongTon"));
                sp.setSoLuongToiThieu(rs.getInt("SoLuongToiThieu"));
                sp.setSoLuongToiDa(rs.getInt("SoLuongToiDa"));
                sp.setThoiHanBaoHanhThang(rs.getInt("ThoiHanBaoHanhThang"));
                sp.setMoTa(rs.getString("MoTa"));
                sp.setTrangThai(rs.getString("TrangThai"));

                list.add(sp);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
