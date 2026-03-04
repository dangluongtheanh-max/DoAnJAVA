package DAO;

import DTO.ImeiPhieuNhapDTO;
import Utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ImeiPhieuNhapDAO {

    /* ================== MAP RESULT ================== */
    private ImeiPhieuNhapDTO mapResult(ResultSet rs) throws SQLException {
        return new ImeiPhieuNhapDTO(
                rs.getInt("Ma_PN"),
                rs.getInt("Ma_SP"),
                rs.getInt("Ma_Imei"),
                rs.getInt("Trang_Thai")
        );
    }

    /* ================== THÊM 1 IMEI ================== */
    public boolean insertImei(ImeiPhieuNhapDTO imei) {
        String sql = "INSERT INTO IMEI_PhieuNhap (Ma_PN, Ma_SP, Ma_Imei, Trang_Thai) VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, imei.getMaPN());
            ps.setInt(2, imei.getMaSP());
            ps.setInt(3, imei.getMaImei());
            ps.setInt(4, imei.getTrangThai());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /* ================== THÊM DANH SÁCH IMEI ================== */
    public boolean insertDanhSachImei(List<ImeiPhieuNhapDTO> list) {
        String sql = "INSERT INTO IMEI_PhieuNhap (Ma_PN, Ma_SP, Ma_Imei, Trang_Thai) VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            for (ImeiPhieuNhapDTO imei : list) {
                ps.setInt(1, imei.getMaPN());
                ps.setInt(2, imei.getMaSP());
                ps.setInt(3, imei.getMaImei());
                ps.setInt(4, imei.getTrangThai());
                ps.addBatch();
            }
            ps.executeBatch();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /* ================== KIỂM TRA IMEI TỒN TẠI ================== */
    public boolean kiemTraTonTaiImei(int maImei) {
        String sql = "SELECT 1 FROM IMEI_PhieuNhap WHERE Ma_Imei = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, maImei);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /* ================== LẤY THEO PHIẾU NHẬP ================== */
    public List<ImeiPhieuNhapDTO> findByMaPN(int maPN) {
        List<ImeiPhieuNhapDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM IMEI_PhieuNhap WHERE Ma_PN = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, maPN);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResult(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /* ================== LẤY THEO SẢN PHẨM ================== */
    public List<ImeiPhieuNhapDTO> findByMaSP(int maSP) {
        List<ImeiPhieuNhapDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM IMEI_PhieuNhap WHERE Ma_SP = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, maSP);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResult(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /* ================== LẤY 1 IMEI ================== */
    public ImeiPhieuNhapDTO findByImei(int maImei) {
        String sql = "SELECT * FROM IMEI_PhieuNhap WHERE Ma_Imei = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, maImei);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResult(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /* ================== CẬP NHẬT TRẠNG THÁI ================== */
    public boolean updateTrangThaiImei(int maImei, int trangThai) {
        String sql = "UPDATE IMEI_PhieuNhap SET Trang_Thai = ? WHERE Ma_Imei = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, trangThai);
            ps.setInt(2, maImei);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /* ================== XÓA IMEI ================== */
    public boolean deleteByImei(int maImei) {
        String sql = "DELETE FROM IMEI_PhieuNhap WHERE Ma_Imei = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, maImei);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
