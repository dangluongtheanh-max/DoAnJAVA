package BUS;

import DAO.TaiKhoanDAO;
import DTO.TaiKhoanDTO;
import DTO.SharedData;

public class TaiKhoanBUS {
    private TaiKhoanDAO dao = new TaiKhoanDAO();

    public boolean login(String user, String pass) {
        // Kiểm tra cơ bản
        if (user.isEmpty() || pass.isEmpty()) {
            return false;
        }

        // Gọi DAO kiểm tra Database
        TaiKhoanDTO tk = dao.checkLogin(user, pass);
        
        if (tk != null) {
            // Lưu thông tin vào phiên làm việc
            SharedData.currentRole = tk.getVaiTro();
            SharedData.currentTenNV = tk.getTenNV();
            SharedData.currentMaNV = tk.getMaNV();
            return true; // Đăng nhập thành công
        }
        
        return false; // Thất bại
    }
}
