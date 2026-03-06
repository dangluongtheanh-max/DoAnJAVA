package BUS;

import DAO.NhanVienDAO;
import DTO.NhanVienDTO;

public class DangNhapBUS {

    private NhanVienDAO nvDAO = new NhanVienDAO();

    // Đăng nhập bằng CCCD
    public NhanVienDTO dangNhap(String cccd) {

        NhanVienDTO nv = nvDAO.findByCCCD(cccd);

        if (nv == null) return null;

        // kiểm tra trạng thái
        if (!nv.getTrangThai().equalsIgnoreCase("DangLam")) {
            return null;
        }

        return nv;
    }

    // Kiểm tra quản lý
    public boolean isQuanLy(NhanVienDTO nv) {
        return nv.getVaiTro().equalsIgnoreCase("QuanLy");
    }

    // Kiểm tra nhân viên bán hàng
    public boolean isNhanVien(NhanVienDTO nv) {
        return nv.getVaiTro().equalsIgnoreCase("NhanVienBanHang");
    }
}