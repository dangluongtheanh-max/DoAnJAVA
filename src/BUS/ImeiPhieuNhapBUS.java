package BUS;

import DAO.ImeiPhieuNhapDAO;
import DTO.ImeiPhieuNhapDTO;
import java.util.List;

public class ImeiPhieuNhapBUS {

    private final ImeiPhieuNhapDAO imeiDAO = new ImeiPhieuNhapDAO();

    /* ================== NHẬP 1 IMEI ================== */
    public boolean nhapImei(ImeiPhieuNhapDTO imei) {
        if (imeiDAO.kiemTraTonTaiImeiCode(imei.getImeiCode())) {
            return false; // IMEI đã tồn tại
        }
        return imeiDAO.insertImei(imei);
    }

    /* ================== NHẬP DANH SÁCH IMEI ================== */
    public boolean nhapDanhSachImei(List<ImeiPhieuNhapDTO> list) {
        for (ImeiPhieuNhapDTO imei : list) {
            if (imeiDAO.kiemTraTonTaiImeiCode(imei.getImeiCode())) {
                return false;
            }
        }
        return imeiDAO.insertDanhSachImei(list);
    }

    /* ================== LẤY IMEI THEO SẢN PHẨM ================== */
    public List<ImeiPhieuNhapDTO> getImeiTheoSanPham(int maSP) {
        return imeiDAO.findByMaSP(maSP);
    }

    /* ================== LẤY IMEI TRONG KHO THEO SẢN PHẨM ================== */
    public List<ImeiPhieuNhapDTO> getImeiTrongKhoTheoSanPham(int maSP) {
        return imeiDAO.findTrongKhoByMaSP(maSP);
    }

    /* ================== KIỂM TRA IMEI ĐÃ BÁN CHƯA ================== */
    public boolean isImeiDaBan(int maImei) {
        ImeiPhieuNhapDTO imei = imeiDAO.findByMaImei(maImei);
        return imei != null && imei.getTrangThai() == ImeiPhieuNhapDTO.TrangThaiIMEI.DA_BAN;
    }

    /* ================== ĐÁNH DẤU IMEI ĐÃ BÁN ================== */
    public boolean danhDauImeiDaBan(int maImei) {
        return imeiDAO.updateTrangThai(maImei, ImeiPhieuNhapDTO.TrangThaiIMEI.DA_BAN);
    }

    /* ================== ĐÁNH DẤU IMEI CHƯA BÁN ================== */
    public boolean huyBanImei(int maImei) {
        return imeiDAO.updateTrangThai(maImei, ImeiPhieuNhapDTO.TrangThaiIMEI.TRONG_KHO);
    }

    /* ================== LẤY 1 IMEI THEO MÃ ================== */
    public ImeiPhieuNhapDTO getImeiTheoMa(int maImei) {
        return imeiDAO.findByMaImei(maImei);
    }

    /* ================== LẤY 1 IMEI THEO CODE ================== */
    public ImeiPhieuNhapDTO getImeiTheoCode(String imeiCode) {
        return imeiDAO.findByImeiCode(imeiCode);
    }

    /* ================== XÓA IMEI (HIẾM DÙNG) ================== */
    public boolean xoaImei(int maImei) {
        return imeiDAO.deleteByMaImei(maImei);
    }

    /* ================== KIỂM TRA TỒN TẠI IMEI CODE ================== */
    public boolean kiemTraTonTaiImeiCode(String imeiCode) {
        return imeiDAO.kiemTraTonTaiImeiCode(imeiCode);
    }
}