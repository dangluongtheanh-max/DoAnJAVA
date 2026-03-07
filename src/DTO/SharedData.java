
package DTO;

public class SharedData {
    // Biến static để lưu vai trò: "QuanLy" hoặc "NhanVienBanHang"
    public static String currentRole = null; 
    // Lưu tên để hiển thị lên Dashboard
    public static String currentTenNV = null;
    // Lưu mã NV để dùng cho các chức năng lập hóa đơn, nhập hàng sau này
    public static int currentMaNV = -1;
}