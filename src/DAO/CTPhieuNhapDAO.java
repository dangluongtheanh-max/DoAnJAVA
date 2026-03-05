package DAO;

import DTO.CTPhieuNhapDTO;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO thao tác với bảng CHITIETPHIEUNHAP trong DB.
 * Tên cột DB: MaChiTietPN, MaPN, MaSP, SoLuong, DonGiaNhap, ThanhTien (COMPUTED), GhiChu
 * Lưu ý:
 *  - Không có cột TrangThai trong bảng này.
 *  - ThanhTien là COMPUTED PERSISTED, không INSERT/UPDATE thủ công.
 *  - Hủy chi tiết = xóa dòng hoặc hủy cả PhieuNhap ở tầng trên.
 */
public class CTPhieuNhapDAO {

    /* ================== INSERT ================== */
    public boolean insert(CTPhieuNhapDTO ct) {
        // Không truyền MaChiTietPN (IDENTITY) và ThanhTien (COMPUTED)
        String sql = "INSERT INTO CHITIETPHIEUNHAP (MaPN, MaSP, SoLuong, DonGiaNhap, GhiChu) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, ct.getMaPhieuNhap());
            pst.setInt(2, ct.getMaSP());
            pst.setInt(3, ct.getSoLuong());
            pst.setBigDecimal(4, ct.getDonGiaNhap());  // đúng tên cột DB
            pst.setString(5, ct.getGhiChu());

            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi insert CTPhieuNhap: " + e.getMessage());
        }
        return false;
    }

    /* ================== INSERT BATCH ================== */
    public boolean insertBatch(List<CTPhieuNhapDTO> list) {
        String sql = "INSERT INTO CHITIETPHIEUNHAP (MaPN, MaSP, SoLuong, DonGiaNhap, GhiChu) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            con.setAutoCommit(false);
            for (CTPhieuNhapDTO ct : list) {
                pst.setInt(1, ct.getMaPhieuNhap());
                pst.setInt(2, ct.getMaSP());
                pst.setInt(3, ct.getSoLuong());
                pst.setBigDecimal(4, ct.getDonGiaNhap());
                pst.setString(5, ct.getGhiChu());
                pst.addBatch();
            }
            pst.executeBatch();
            con.commit();
            return true;
        } catch (SQLException e) {
            System.err.println("Lỗi insertBatch CTPhieuNhap: " + e.getMessage());
        }
        return false;
    }

    /* ================== LẤY THEO MaPN ================== */
    public List<CTPhieuNhapDTO> getByMaPN(int maPN) {
        List<CTPhieuNhapDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM CHITIETPHIEUNHAP WHERE MaPN = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, maPN);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) list.add(mapResult(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi getByMaPN CTPhieuNhap: " + e.getMessage());
        }
        return list;
    }

    /* ================== LẤY THEO MaChiTietPN ================== */
    public CTPhieuNhapDTO getById(int maChiTietPN) {
        String sql = "SELECT * FROM CHITIETPHIEUNHAP WHERE MaChiTietPN = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, maChiTietPN);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) return mapResult(rs);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi getById CTPhieuNhap: " + e.getMessage());
        }
        return null;
    }

    /* ================== XÓA THEO MaPN (khi hủy phiếu nhập) ================== */
    public boolean deleteByMaPN(int maPN) {
        String sql = "DELETE FROM CHITIETPHIEUNHAP WHERE MaPN = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, maPN);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi deleteByMaPN CTPhieuNhap: " + e.getMessage());
        }
        return false;
    }

    /* ================== XÓA THEO MaChiTietPN ================== */
    public boolean deleteById(int maChiTietPN) {
        String sql = "DELETE FROM CHITIETPHIEUNHAP WHERE MaChiTietPN = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, maChiTietPN);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi deleteById CTPhieuNhap: " + e.getMessage());
        }
        return false;
    }

    /* ================== MAP RESULT ================== */
    private CTPhieuNhapDTO mapResult(ResultSet rs) throws SQLException {
        return new CTPhieuNhapDTO(
                rs.getInt("MaChiTietPN"),
                rs.getInt("MaPN"),
                rs.getInt("MaSP"),
                rs.getInt("SoLuong"),
                rs.getBigDecimal("DonGiaNhap"),  // đúng tên cột DB
                rs.getBigDecimal("ThanhTien"),    // đọc từ computed column
                rs.getString("GhiChu")
        );
    }
}