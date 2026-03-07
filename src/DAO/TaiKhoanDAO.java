package DAO;
import DTO.SharedData;
import DTO.TaiKhoanDTO;
import java.sql.*;
import DAO.DBConnection;

public class TaiKhoanDAO {
    public TaiKhoanDTO checkLogin(String user, String pass) {
        // SQL JOIN để lấy VaiTro và TenNV từ bảng NHANVIEN dựa trên MaNV
        String sql = "SELECT tk.MaNV, tk.TenDangNhap, nv.VaiTro, nv.TenNV " +
                     "FROM TAIKHOAN tk " +
                     "JOIN NHANVIEN nv ON tk.MaNV = nv.MaNV " +
                     "WHERE tk.TenDangNhap = ? AND tk.MatKhauHash = ? AND tk.TrangThai = N'HoatDong'";
        
        try (Connection conn = DBConnection.getConnection(); // Sử dụng class kết nối của bạn
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setString(1, user);
            pst.setString(2, pass);
            
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                TaiKhoanDTO dto = new TaiKhoanDTO();
                dto.setMaNV(rs.getInt("MaNV"));
                dto.setTenDangNhap(rs.getString("TenDangNhap"));
                dto.setVaiTro(rs.getString("VaiTro")); // Ví dụ: "QuanLy" hoặc "NhanVienBanHang"
                dto.setTenNV(rs.getNString("TenNV"));
                return dto;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}