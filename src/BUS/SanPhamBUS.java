package BUS;

import DAO.SanPhamDAO;
import DTO.SanPhamDTO;
import java.math.BigDecimal;
import java.util.ArrayList;

public class SanPhamBUS {

    private SanPhamDAO spDAO = new SanPhamDAO();
    private ArrayList<SanPhamDTO> dsSanPham = new ArrayList<>();

    // Load danh sách sản phẩm từ database
    public ArrayList<SanPhamDTO> getDanhSachSanPham() {
        dsSanPham = spDAO.getDanhSachSanPham();
        return dsSanPham;
    }

    // Tìm sản phẩm theo mã
    public SanPhamDTO timTheoMa(int maSP) {
        for (SanPhamDTO sp : dsSanPham) {
            if (sp.getMaSP() == maSP) {
                return sp;
            }
        }
        return null;
    }

    // Kiểm tra dữ liệu
    private boolean kiemTraDuLieu(SanPhamDTO sp) {

        if (sp.getTenSP() == null || sp.getTenSP().trim().isEmpty())
            return false;

        if (sp.getGia() == null || sp.getGia().compareTo(BigDecimal.ZERO) <= 0)
            return false;

        if (sp.getGiaGoc() == null || sp.getGiaGoc().compareTo(BigDecimal.ZERO) <= 0)
            return false;

        if (sp.getSoLuongTon() < 0)
            return false;

        if (sp.getSoLuongToiThieu() < 0)
            return false;

        if (sp.getSoLuongToiDa() < sp.getSoLuongToiThieu())
            return false;

        return true;
    }

    // Thêm sản phẩm
    public boolean themSanPham(SanPhamDTO sp) {

        if (!kiemTraDuLieu(sp))
            return false;

        boolean kq = spDAO.themSanPham(sp);

        if (kq)
            dsSanPham.add(sp);

        return kq;
    }

    // Sửa sản phẩm
    public boolean suaSanPham(SanPhamDTO sp) {

        if (!kiemTraDuLieu(sp))
            return false;

        boolean kq = spDAO.suaSanPham(sp);

        if (kq) {
            for (int i = 0; i < dsSanPham.size(); i++) {
                if (dsSanPham.get(i).getMaSP() == sp.getMaSP()) {
                    dsSanPham.set(i, sp);
                    break;
                }
            }
        }

        return kq;
    }

    // Xóa mềm sản phẩm
    public boolean xoaSanPham(int maSP) {

        boolean kq = spDAO.xoaSanPham(maSP);

        if (kq) {
            for (int i = 0; i < dsSanPham.size(); i++) {
                if (dsSanPham.get(i).getMaSP() == maSP) {
                    dsSanPham.remove(i);
                    break;
                }
            }
        }

        return kq;
    }

    // Tìm sản phẩm theo tên
    public ArrayList<SanPhamDTO> timSanPham(String ten) {
        return spDAO.timSanPham(ten);
    }

    // Lọc theo loại sản phẩm
    public ArrayList<SanPhamDTO> locTheoLoai(int maLoai) {

        ArrayList<SanPhamDTO> ketQua = new ArrayList<>();

        for (SanPhamDTO sp : dsSanPham) {
            if (sp.getMaLoai() == maLoai) {
                ketQua.add(sp);
            }
        }

        return ketQua;
    }

    // Lọc theo thương hiệu
    public ArrayList<SanPhamDTO> locTheoThuongHieu(String thuongHieu) {
ArrayList<SanPhamDTO> ketQua = new ArrayList<>();

        for (SanPhamDTO sp : dsSanPham) {
            if (sp.getThuongHieu().equalsIgnoreCase(thuongHieu)) {
                ketQua.add(sp);
            }
        }

        return ketQua;
    }

    // Lọc theo khoảng giá
    public ArrayList<SanPhamDTO> locTheoGia(BigDecimal min, BigDecimal max) {

        ArrayList<SanPhamDTO> ketQua = new ArrayList<>();

        for (SanPhamDTO sp : dsSanPham) {

            if (sp.getGia().compareTo(min) >= 0 &&
                sp.getGia().compareTo(max) <= 0) {

                ketQua.add(sp);
            }
        }

        return ketQua;
    }

}
