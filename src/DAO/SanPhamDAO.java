package DAO;

import DTO.SanPhamDTO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SanPhamDAO {

    // 🔹 Đổi đúng thông tin DB của bạn
    private static final String URL =
        "jdbc:sqlserver://localhost:1433;databaseName=YourDB;encrypt=true;trustServerCertificate=true";
    private static final String USER = "sa";
    private static final String PASS = "123";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    // ================= CRUD =================

    // 1. Lấy danh sách sản phẩm
    public List<SanPhamDTO> getAll() {
        List<SanPhamDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM SanPham";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapResult(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. Lấy 1 sản phẩm theo mã
    public SanPhamDTO getById(int maSP) {
        String sql = "SELECT * FROM SanPham WHERE MaSP = ?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, maSP);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResult(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 3. Thêm sản phẩm
    public boolean insert(SanPhamDTO sp) {
        String sql = "INSERT INTO SanPham (TenSP, Gia, SoLuong, MoTa) VALUES (?, ?, ?, ?)";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, sp.getTenSP());
            ps.setDouble(2, sp.getGia());
            ps.setInt(3, sp.getSoLuong());
            ps.setString(4, sp.getMoTa());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 4. Cập nhật sản phẩm
    public boolean update(SanPhamDTO sp) {
        String sql = """
            UPDATE SanPham
            SET TenSP = ?, Gia = ?, SoLuong = ?, MoTa = ?
            WHERE MaSP = ?
        """;

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, sp.getTenSP());
            ps.setDouble(2, sp.getGia());
            ps.setInt(3, sp.getSoLuong());
            ps.setString(4, sp.getMoTa());
            ps.setInt(5, sp.getMaSP());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 5. Xóa sản phẩm
    public boolean delete(int maSP) {
        String sql = "DELETE FROM SanPham WHERE MaSP = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, maSP);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ================= HELPER =================
    // Map ResultSet → DTO (rất quan trọng để code sạch)
    private SanPhamDTO mapResult(ResultSet rs) throws SQLException {
        return new SanPhamDTO(
            rs.getInt("MaSP"),
            rs.getString("TenSP"),
            rs.getDouble("Gia"),
            rs.getInt("SoLuong"),
            rs.getString("MoTa")
        );
    }
}
