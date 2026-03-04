package BUS;

import DAO.SanPhamDAO;
import DTO.SanPhamDTO;
import java.util.ArrayList;
import java.util.List;

public class SanPhamBUS {

    private SanPhamDAO sanPhamDAO;

    public SanPhamBUS() {
        sanPhamDAO = new SanPhamDAO();
    }

    // ================== LẤY DANH SÁCH ==================
    public List<SanPhamDTO> getAllSanPham() {
        return sanPhamDAO.getAll();
    }

    // ================== THÊM ==================
    public boolean themSanPham(SanPhamDTO sp) {
        if (!kiemTraHopLe(sp)) return false;
        if (kiemTraTrungMa(sp.getMaSP())) return false;

        return sanPhamDAO.insert(sp);
    }

    // ================== SỬA ==================
    public boolean suaSanPham(SanPhamDTO sp) {
        if (!kiemTraHopLe(sp)) return false;

        return sanPhamDAO.update(sp);
    }

    // ================== XÓA ==================
    public boolean xoaSanPham(int maSP) {
        return sanPhamDAO.delete(maSP);
    }

    // ================== TÌM KIẾM ==================
    public ArrayList<SanPhamDTO> timKiemTheoTen(String keyword) {
        ArrayList<SanPhamDTO> ketQua = new ArrayList<>();
        for (SanPhamDTO sp : getAllSanPham()) {
            if (sp.getTenSP().toLowerCase().contains(keyword.toLowerCase())) {
                ketQua.add(sp);
            }
        }
        return ketQua;
    }

    // ================== KIỂM TRA ==================
    private boolean kiemTraHopLe(SanPhamDTO sp) {
        if (sp.getTenSP() == null || sp.getTenSP().isEmpty()) return false;
        if (sp.getGia() < 0) return false;
        if (sp.getSoLuong() < 0) return false;
        return true;
    }

    private boolean kiemTraTrungMa(int maSP) {
        for (SanPhamDTO sp : getAllSanPham()) {
            if (sp.getMaSP() == maSP) {
                return true;
            }
        }
        return false;
    }
}
