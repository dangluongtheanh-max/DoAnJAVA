package DAO;

import DTO.ImeiPhieuNhapDTO;
import DTO.ImeiPhieuNhapDTO.TrangThaiIMEI;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO thao tác với bảng IMEI trong DB.
 * Lưu ý: DB không có bảng IMEI_PhieuNhap riêng.
 * IMEI được lưu trực tiếp trong bảng IMEI, liên kết với phiếu nhập qua MaSP.
 */
public class ImeiPhieuNhapDAO {

    /* ================== MAP RESULT ================== */
    private ImeiPhieuNhapDTO mapResult(ResultSet rs) throws SQLException {
        TrangThaiIMEI trangThai = TrangThaiIMEI.fromDbValue(rs.getString("TrangThai"));
        return new ImeiPhieuNhapDTO(
                0,                          // maPN - không có trong bảng IMEI, để 0
                rs.getInt("MaSP"),
                rs.getInt("MaIMEI"),
                rs.getString("ImeiCode"),
                trangThai
        );
    }

    /* ================== THÊM 1 IMEI ================== */
    public boolean insertImei(ImeiPhieuNhapDTO imei) {
        // INSERT vào bảng IMEI (đúng tên bảng trong DB)
        String sql = "INSERT INTO IMEI (ImeiCode, MaSP, TrangThai, NgayNhap) VALUES (?, ?, ?, GETDATE())";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, imei.getImeiCode());
            ps.setInt(2, imei.getMaSP());
            ps.setString(3, imei.getTrangThaiDbValue()); // lưu chuỗi khớp DB

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi insertImei: " + e.getMessage());
        }
        return false;
    }

    /* ================== THÊM DANH SÁCH IMEI (BATCH) ================== */
    public boolean insertDanhSachImei(List<ImeiPhieuNhapDTO> list) {
        String sql = "INSERT INTO IMEI (ImeiCode, MaSP, TrangThai, NgayNhap) VALUES (?, ?, ?, GETDATE())";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            con.setAutoCommit(false); // dùng transaction cho batch
            for (ImeiPhieuNhapDTO imei : list) {
                ps.setString(1, imei.getImeiCode());
                ps.setInt(2, imei.getMaSP());
                ps.setString(3, imei.getTrangThaiDbValue());
                ps.addBatch();
            }
            ps.executeBatch();
            con.commit();
            return true;
        } catch (SQLException e) {
            System.err.println("Lỗi insertDanhSachImei: " + e.getMessage());
        }
        return false;
    }

    /* ================== KIỂM TRA IMEI TỒN TẠI (theo MaIMEI) ================== */
    public boolean kiemTraTonTaiImei(int maImei) {
        String sql = "SELECT 1 FROM IMEI WHERE MaIMEI = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, maImei);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            System.err.println("Lỗi kiemTraTonTaiImei: " + e.getMessage());
        }
        return false;
    }

    /* ================== KIỂM TRA IMEI TỒN TẠI (theo ImeiCode) ================== */
    public boolean kiemTraTonTaiImeiCode(String imeiCode) {
        String sql = "SELECT 1 FROM IMEI WHERE ImeiCode = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, imeiCode);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            System.err.println("Lỗi kiemTraTonTaiImeiCode: " + e.getMessage());
        }
        return false;
    }

    /* ================== LẤY DANH SÁCH IMEI THEO SẢN PHẨM ================== */
    public List<ImeiPhieuNhapDTO> findByMaSP(int maSP) {
        List<ImeiPhieuNhapDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM IMEI WHERE MaSP = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, maSP);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapResult(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi findByMaSP: " + e.getMessage());
        }
        return list;
    }

    /* ================== LẤY DANH SÁCH IMEI ĐANG TRONG KHO THEO SẢN PHẨM ================== */
    public List<ImeiPhieuNhapDTO> findTrongKhoByMaSP(int maSP) {
        List<ImeiPhieuNhapDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM IMEI WHERE MaSP = ? AND TrangThai = 'TrongKho'";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, maSP);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapResult(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi findTrongKhoByMaSP: " + e.getMessage());
        }
        return list;
    }

    /* ================== LẤY 1 IMEI THEO MaIMEI ================== */
    public ImeiPhieuNhapDTO findByMaImei(int maImei) {
        String sql = "SELECT * FROM IMEI WHERE MaIMEI = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, maImei);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapResult(rs);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi findByMaImei: " + e.getMessage());
        }
        return null;
    }

    /* ================== LẤY 1 IMEI THEO ImeiCode ================== */
    public ImeiPhieuNhapDTO findByImeiCode(String imeiCode) {
        String sql = "SELECT * FROM IMEI WHERE ImeiCode = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, imeiCode);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapResult(rs);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi findByImeiCode: " + e.getMessage());
        }
        return null;
    }

    /* ================== CẬP NHẬT TRẠNG THÁI ================== */
    public boolean updateTrangThai(int maImei, TrangThaiIMEI trangThai) {
        String sql = "UPDATE IMEI SET TrangThai = ? WHERE MaIMEI = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, trangThai.getDbValue()); // lưu chuỗi khớp DB
            ps.setInt(2, maImei);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi updateTrangThai: " + e.getMessage());
        }
        return false;
    }

    /* ================== XÓA IMEI ================== */
    public boolean deleteByMaImei(int maImei) {
        String sql = "DELETE FROM IMEI WHERE MaIMEI = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, maImei);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi deleteByMaImei: " + e.getMessage());
        }
        return false;
    }
}