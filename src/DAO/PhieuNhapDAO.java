package DAO;

import DTO.PhieuNhapDTO;
import DTO.PhieuNhapDTO.TrangThaiPhieuNhap;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO thao tác với bảng PHIEUNHAP trong DB.
 * Tên cột DB: MaPN, MaNhaCungCap, MaNV, NgayNhap, TongTien, GhiChu, TrangThai
 */
public class PhieuNhapDAO {

    /* ================== INSERT ================== */
    public boolean insert(PhieuNhapDTO pn) {
        // Không truyền MaPN vì là IDENTITY tự tăng
        String sql = "INSERT INTO PHIEUNHAP (MaNhaCungCap, MaNV, NgayNhap, TongTien, GhiChu, TrangThai) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, pn.getMaNhaCungCap());
            pst.setInt(2, pn.getMaNV());
            pst.setDate(3, Date.valueOf(pn.getNgayNhap())); // LocalDate → java.sql.Date (DB là DATE)
            pst.setBigDecimal(4, pn.getTongTien());
            pst.setString(5, pn.getGhiChu());
            pst.setString(6, pn.getTrangThaiDbValue()); // lưu chuỗi 'HoanThanh'/'Huy'

            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi insert PhieuNhap: " + e.getMessage());
        }
        return false;
    }

    /* ================== INSERT — TRẢ VỀ MaPN VỪA TẠO ================== */
    public int insertReturnKey(PhieuNhapDTO pn) {
        String sql = "INSERT INTO PHIEUNHAP (MaNhaCungCap, MaNV, NgayNhap, TongTien, GhiChu, TrangThai) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pst.setInt(1, pn.getMaNhaCungCap());
            pst.setInt(2, pn.getMaNV());
            pst.setDate(3, Date.valueOf(pn.getNgayNhap()));
            pst.setBigDecimal(4, pn.getTongTien());
            pst.setString(5, pn.getGhiChu());
            pst.setString(6, pn.getTrangThaiDbValue());

            pst.executeUpdate();
            try (ResultSet keys = pst.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1); // trả về MaPN vừa tạo
            }
        } catch (SQLException e) {
            System.err.println("Lỗi insertReturnKey PhieuNhap: " + e.getMessage());
        }
        return -1;
    }

    /* ================== CẬP NHẬT TRẠNG THÁI ================== */
    public boolean updateTrangThai(int maPN, TrangThaiPhieuNhap trangThai) {
        String sql = "UPDATE PHIEUNHAP SET TrangThai = ? WHERE MaPN = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, trangThai.getDbValue()); // lưu chuỗi khớp DB
            pst.setInt(2, maPN);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi updateTrangThai PhieuNhap: " + e.getMessage());
        }
        return false;
    }

    /* ================== CẬP NHẬT TỔNG TIỀN ================== */
    public boolean updateTongTien(int maPN, java.math.BigDecimal tongTien) {
        String sql = "UPDATE PHIEUNHAP SET TongTien = ? WHERE MaPN = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setBigDecimal(1, tongTien);
            pst.setInt(2, maPN);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi updateTongTien: " + e.getMessage());
        }
        return false;
    }

    /* ================== LẤY THEO MaPN ================== */
    public PhieuNhapDTO getById(int maPN) {
        String sql = "SELECT * FROM PHIEUNHAP WHERE MaPN = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, maPN);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) return mapResult(rs);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi getById PhieuNhap " + maPN + ": " + e.getMessage());
        }
        return null;
    }

    /* ================== LẤY TẤT CẢ ================== */
    public List<PhieuNhapDTO> getAll() {
        List<PhieuNhapDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM PHIEUNHAP ORDER BY MaPN DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) list.add(mapResult(rs));
        } catch (SQLException e) {
            System.err.println("Lỗi getAll PhieuNhap: " + e.getMessage());
        }
        return list;
    }

    /* ================== LẤY THEO TRẠNG THÁI ================== */
    public List<PhieuNhapDTO> getByTrangThai(TrangThaiPhieuNhap trangThai) {
        List<PhieuNhapDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM PHIEUNHAP WHERE TrangThai = ? ORDER BY MaPN DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, trangThai.getDbValue());
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) list.add(mapResult(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi getByTrangThai PhieuNhap: " + e.getMessage());
        }
        return list;
    }

    /* ================== MAP RESULT ================== */
    private PhieuNhapDTO mapResult(ResultSet rs) throws SQLException {
        return new PhieuNhapDTO(
                rs.getInt("MaPN"),
                rs.getInt("MaNV"),
                rs.getInt("MaNhaCungCap"),
                rs.getDate("NgayNhap").toLocalDate(),           // java.sql.Date → LocalDate
                rs.getBigDecimal("TongTien"),
                rs.getString("GhiChu"),
                TrangThaiPhieuNhap.fromDbValue(rs.getString("TrangThai"))
        );
    }
}