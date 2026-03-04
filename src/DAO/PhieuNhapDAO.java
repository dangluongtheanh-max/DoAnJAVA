package DAO;

import DTO.PhieuNhapDTO;
import java.sql.*;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class PhieuNhapDAO {

    public boolean insert(PhieuNhapDTO pn) {
        boolean result = false;
        // Câu lệnh SQL khớp với cấu trúc bảng PhieuNhap trong file SQL
        String sql = "INSERT INTO PhieuNhap (Ma_CH, Ma_NV, Ma_NCC, Ngay_Nhap, Tong_Tien, Trang_Thai) VALUES (?, ?, ?, ?, ?, ?)";
        
        // Sử dụng DBConnection đã khai báo của bạn
        try (Connection con = DBConnection.getConnection();
            PreparedStatement pst = con.prepareStatement(sql)) {
            
            pst.setInt(1, pn.getMaCuaHang()); 
            pst.setInt(2, pn.getMaNV());
            pst.setInt(3, pn.getMaNCC());
            
            // Chuyển đổi thời gian: SQL DATETIME tương ứng với Timestamp trong Java
            pst.setTimestamp(4, Timestamp.valueOf(pn.getNgayNhap())); 
            
            // Sử dụng BigDecimal để giữ độ chính xác cho kiểu DECIMAL(18,2)
            pst.setBigDecimal(5, pn.getTongTien());
            
            pst.setInt(6, pn.getTrangThai());

            // executeUpdate trả về số dòng bị tác động, nếu > 0 tức là insert thành công
            result = pst.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi Insert PhieuNhap: " + e.getMessage());
        }
        return result;
    }

    public boolean updateTrangThai(int maPN, int trangThai) {
    // Câu lệnh SQL cập nhật dựa trên khóa chính Ma_PN
        String sql = "UPDATE PhieuNhap SET Trang_Thai = ? WHERE Ma_PN = ?";
        
        try (Connection con = DBConnection.getConnection(); // Đổi sang DBConnection
            PreparedStatement pst = con.prepareStatement(sql)) {
            
            pst.setInt(1, trangThai);
            pst.setInt(2, maPN);

            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi Update Trang_Thai: " + e.getMessage());
        }
        return false;
    }

    public PhieuNhapDTO getById(int maPN) {
        PhieuNhapDTO pn = null;
        // Câu lệnh SQL truy vấn theo khóa chính Ma_PN
        String sql = "SELECT * FROM PhieuNhap WHERE Ma_PN = ?";
        
        // 1. Sử dụng DBConnection bạn đã viết
        try (Connection con = DBConnection.getConnection(); 
            PreparedStatement pst = con.prepareStatement(sql)) {
            
            // 2. Gán giá trị cho dấu ? (Parameter mapping)
            pst.setInt(1, maPN);
            
            // 3. Thực thi truy vấn và tự động đóng ResultSet
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    pn = createPhieuNhapFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            // In lỗi chi tiết để dễ debug
            System.err.println("Lỗi khi truy vấn PhieuNhap ID " + maPN + ": " + e.getMessage());
            e.printStackTrace();
        }
        return pn;
    }

    public ArrayList<PhieuNhapDTO> getAll() {
        ArrayList<PhieuNhapDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM PhieuNhap ORDER BY Ma_PN DESC"; // Lấy tất cả phiếu nhập, mới nhất trước
        
        try (Connection con = DBConnection.getConnection(); 
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                list.add(createPhieuNhapFromResultSet(rs));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi lấy danh sách PhieuNhap: " + e.getMessage());
        }
        return list;
    }

    private PhieuNhapDTO createPhieuNhapFromResultSet(ResultSet rs) throws SQLException {
        PhieuNhapDTO pn = new PhieuNhapDTO();
        pn.setMaPhieuNhap(rs.getInt("Ma_PN"));
        pn.setMaCuaHang(rs.getInt("Ma_CH"));
        pn.setMaNV(rs.getInt("Ma_NV"));
        pn.setMaNCC(rs.getInt("Ma_NCC"));
        pn.setNgayNhap(rs.getTimestamp("Ngay_Nhap").toLocalDateTime());
        pn.setTongTien(rs.getBigDecimal("Tong_Tien"));
        pn.setTrangThai(rs.getInt("Trang_Thai"));
        return pn;
    }
    
}