package BUS;

import DAO.ThongKeDAO;
import DTO.ThongKe.ThongKeDoanhThuDTO;
import DTO.ThongKe.ThongKeHoaDonBanDTO;
import DTO.ThongKe.ThongKeSanPhamBanDTO;
import DTO.ThongKe.ThongKeTheLoaiBanDTO;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @Nhóm 13
 */
public class ThongKeBUS {

    private final ThongKeDAO thongKeDAO;

    public ThongKeBUS() {
        thongKeDAO = new ThongKeDAO();
    }

    /* =====================================================
       DOANH THU
    ===================================================== */

    // Doanh thu theo từng năm
    public ArrayList<ThongKeDoanhThuDTO> thongKeDoanhThuTheoNam() {
        return thongKeDAO.thongKeDoanhThuTheoNam();
    }

    // Doanh thu theo từng tháng trong năm
    public ArrayList<ThongKeDoanhThuDTO> thongKeDoanhThuTheoThang(int nam) {
        return thongKeDAO.thongKeDoanhThuTheoThang(nam);
    }

    // Doanh thu từng ngày trong tháng
    public ArrayList<ThongKeDoanhThuDTO> thongKeDoanhThuTungNgayTrongThang(int nam, int thang) {
        return thongKeDAO.thongKeDoanhThuTungNgayTrongThang(nam, thang);
    }

    // Doanh thu từ ngày đến ngày
    public ArrayList<ThongKeDoanhThuDTO> thongKeDoanhThuTuNgayDenNgay(Date ngayBatDau, Date ngayKetThuc) {
        return thongKeDAO.thongKeDoanhThuTuNgayDenNgay(ngayBatDau, ngayKetThuc);
    }

    /* =====================================================
       THỐNG KÊ SẢN PHẨM BÁN
    ===================================================== */

    public ArrayList<ThongKeSanPhamBanDTO> thongKeSanPhamBanTrongKhoangThoiGian(
            Date ngayBatDau, Date ngayKetThuc) {
        return thongKeDAO.thongKeSanPhamBanTrongKhoangThoiGian(ngayBatDau, ngayKetThuc);
    }

    /* =====================================================
       THỐNG KÊ LOẠI SẢN PHẨM
    ===================================================== */

    public ArrayList<ThongKeTheLoaiBanDTO> thongKeLoaiSanPhamTrongKhoangThoiGian(
            Date ngayBatDau, Date ngayKetThuc) {
        return thongKeDAO.thongKeLoaiSanPhamTrongKhoangThoiGian(ngayBatDau, ngayKetThuc);
    }

    /* =====================================================
       THỐNG KÊ HÓA ĐƠN BÁN
    ===================================================== */

    public ArrayList<ThongKeHoaDonBanDTO> thongKeHoaDonTrongKhoangThoiGian(
            Date ngayBatDau, Date ngayKetThuc) {
        return thongKeDAO.thongKeHoaDonTrongKhoangThoiGian(ngayBatDau, ngayKetThuc);
    }
}
