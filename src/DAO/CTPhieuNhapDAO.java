package DAO;

import DTO.CTPhieuNhapDTO;
import java.sql.*;
import java.util.ArrayList;

public class CTPhieuNhapDAO {

    // Thêm một chi tiết phiếu nhập mới
    public boolean insert(CTPhieuNhapDTO ct) {
        boolean result = false;
        String sql = "INSERT INTO CT_PhieuNhap (Ma_PN, Ma_SP, So_Luong, Don_Gia, Thanh_Tien, Trang_Thai) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection con = DBConnection.getConnection();
            PreparedStatement pst = con.prepareStatement(sql)) {
            
            pst.setInt(1, ct.getMaPhieuNhap());
            pst.setInt(2, ct.getMaSP());
            pst.setInt(3, ct.getSoLuong());
            pst.setBigDecimal(4, ct.getDonGia());
            pst.setBigDecimal(5, ct.getThanhTien());
            pst.setInt(6, ct.getTrangThai());

            result = pst.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi Insert CT_PhieuNhap: " + e.getMessage());
        }
        return result;
    }

    // Lấy danh sách chi tiết của một phiếu nhập cụ thể
    public ArrayList<CTPhieuNhapDTO> getByMaPN(int maPN) {
        ArrayList<CTPhieuNhapDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM CT_PhieuNhap WHERE Ma_PN = ? AND Trang_Thai = 1";
        
        try (Connection con = DBConnection.getConnection();
            PreparedStatement pst = con.prepareStatement(sql)) {
            
            pst.setInt(1, maPN);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    list.add(createCTFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Hủy các chi tiết của một phiếu nhập (khi phiếu nhập bị hủy)
    public boolean deleteByMaPN(int maPN) {
        String sql = "UPDATE CT_PhieuNhap SET Trang_Thai = 0 WHERE Ma_PN = ?";
        try (Connection con = DBConnection.getConnection();
            PreparedStatement pst = con.prepareStatement(sql)) {
            
            pst.setInt(1, maPN);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private CTPhieuNhapDTO createCTFromResultSet(ResultSet rs) throws SQLException {
        CTPhieuNhapDTO ct = new CTPhieuNhapDTO();
        ct.setMaPhieuNhap(rs.getInt("Ma_PN"));
        ct.setMaSP(rs.getInt("Ma_SP"));
        ct.setSoLuong(rs.getInt("So_Luong"));
        ct.setDonGia(rs.getBigDecimal("Don_Gia"));
        ct.setThanhTien(rs.getBigDecimal("Thanh_Tien"));
        ct.setTrangThai(rs.getInt("Trang_Thai"));
        return ct;
    }
}