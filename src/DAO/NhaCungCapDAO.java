package DAO;

import DTO.NhaCungCapDTO;
import java.sql.*;
import java.util.ArrayList;

public class NhaCungCapDAO {
    
    private final String TABLE_NAME = "LAPTOPSTORE.dbo.NHACUNGCAP";

    public ArrayList<NhaCungCapDTO> selectAll() {
        ArrayList<NhaCungCapDTO> ketQua = new ArrayList<>();
        Connection con = DBConnection.getConnection(); 
        
        if (con == null) {
            System.err.println("LỖI: Không kết nối được SQL Server.");
            return ketQua; 
        }

        try {
            String sql = "SELECT * FROM " + TABLE_NAME;
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                // Đọc trạng thái dưới dạng chuỗi (String) thay vì int
                String trangThaiChuoi = rs.getString("TrangThai");
                
                // Phiên dịch: Nếu chuỗi là "HoatDong" (hoặc chứa chữ này), biến thành số 1, ngược lại là 2
                int trangThaiSo = 1; // Mặc định là 1
                if (trangThaiChuoi != null && !trangThaiChuoi.trim().equalsIgnoreCase("HoatDong")) {
                    trangThaiSo = 2; 
                }

                NhaCungCapDTO ncc = new NhaCungCapDTO(
                    rs.getInt("MaNhaCungCap"),
                    rs.getString("TenNhaCungCap"),
                    rs.getString("SoDienThoai"),
                    rs.getString("Email"),
                    rs.getString("DiaChi"),
                    trangThaiSo // Đưa số nguyên vào DTO như bình thường
                );
                ketQua.add(ncc);
            }
            
            rs.close();
            pst.close();
            con.close(); 
            
        } catch (SQLException e) {
            System.err.println("LỖI SQL KHI SELECT: " + e.getMessage());
        }
        return ketQua;
    }

    public boolean insert(NhaCungCapDTO ncc) {
        int ketQua = 0;
        Connection con = DBConnection.getConnection();
        if (con == null) return false;

        try {
            String sql = "INSERT INTO " + TABLE_NAME + " (MaNhaCungCap, TenNhaCungCap, SoDienThoai, Email, DiaChi, TrangThai) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, ncc.getMaNCC());
            pst.setString(2, ncc.getTenNCC());
            pst.setString(3, ncc.getSDT());
            pst.setString(4, ncc.getEmail());
            pst.setString(5, ncc.getDiaChi());
            
            // Phiên dịch ngược lại: Từ số 1 thành chữ "HoatDong" để lưu vào DB
            String trangThaiLuuDB = (ncc.getTrangThai() == 1) ? "HoatDong" : "NgungHoatDong";
            pst.setString(6, trangThaiLuuDB);

            ketQua = pst.executeUpdate();
            pst.close();
            con.close();
        } catch (SQLException e) {
            System.err.println("LỖI SQL KHI INSERT: " + e.getMessage());
        }
        return ketQua > 0;
    }

    public boolean update(NhaCungCapDTO ncc) {
        int ketQua = 0;
        Connection con = DBConnection.getConnection();
        if (con == null) return false;

        try {
            String sql = "UPDATE " + TABLE_NAME + " SET TenNhaCungCap=?, SoDienThoai=?, Email=?, DiaChi=? WHERE MaNhaCungCap=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, ncc.getTenNCC());
            pst.setString(2, ncc.getSDT());
            pst.setString(3, ncc.getEmail());
            pst.setString(4, ncc.getDiaChi());
            pst.setInt(5, ncc.getMaNCC());

            ketQua = pst.executeUpdate();
            pst.close();
            con.close();
        } catch (SQLException e) {
             System.err.println("LỖI SQL KHI UPDATE: " + e.getMessage());
        }
        return ketQua > 0;
    }

    public boolean updateTrangThai(int maNCC, int trangThaiMoi) {
        int ketQua = 0;
        Connection con = DBConnection.getConnection();
        if (con == null) return false;

        try {
            String sql = "UPDATE " + TABLE_NAME + " SET TrangThai=? WHERE MaNhaCungCap=?";
            PreparedStatement pst = con.prepareStatement(sql);
            
            // Đổi số thành chữ trước khi update
            String trangThaiLuuDB = (trangThaiMoi == 1) ? "HoatDong" : "NgungHoatDong";
            pst.setString(1, trangThaiLuuDB);
            pst.setInt(2, maNCC);

            ketQua = pst.executeUpdate();
            pst.close();
            con.close();
        } catch (SQLException e) {
            System.err.println("LỖI SQL KHI UPDATE TRẠNG THÁI: " + e.getMessage());
        }
        return ketQua > 0;
    }

    public boolean delete(int maNCC) {
        int ketQua = 0;
        Connection con = DBConnection.getConnection();
        if (con == null) return false;

        try {
            String sql = "DELETE FROM " + TABLE_NAME + " WHERE MaNhaCungCap=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, maNCC);

            ketQua = pst.executeUpdate();
            pst.close();
            con.close();
        } catch (SQLException e) {
             System.err.println("LỖI SQL KHI XÓA: " + e.getMessage());
        }
        return ketQua > 0;
    }
}