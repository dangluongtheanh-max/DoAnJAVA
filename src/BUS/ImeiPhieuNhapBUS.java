package BUS;

import DAO.ImeiPhieuNhapDAO;
import DTO.ImeiPhieuNhapDTO;
import java.util.List;

public class ImeiPhieuNhapBUS {

    private final ImeiPhieuNhapDAO imeiDAO = new ImeiPhieuNhapDAO();

    /* ================== NHẬP 1 IMEI ================== */
    public boolean nhapImei(ImeiPhieuNhapDTO imei) {
        if (imeiDAO.kiemTraTonTaiImei(imei.getMaImei())) {
            return false; // IMEI đã tồn tại
        }
        return imeiDAO.insertImei(imei);
    }

    /* ================== NHẬP DANH SÁCH IMEI ================== */
    public boolean nhapDanhSachImei(List<ImeiPhieuNhapDTO> list) {
        for (ImeiPhieuNhapDTO imei : list) {
            if (imeiDAO.kiemTraTonTaiImei(imei.getMaImei())) {
                return false;
            }
        }
        return imeiDAO.insertDanhSachImei(list);
    }

    /* ================== LẤY IMEI THEO PHIẾU NHẬP ================== */
    public List<ImeiPhieuNhapDTO> getImeiTheoPhieuNhap(int maPN) {
        return imeiDAO.findByMaPN(maPN);
    }

    /* ================== LẤY IMEI THEO SẢN PHẨM ================== */
    public List<ImeiPhieuNhapDTO> getImeiTheoSanPham(int maSP) {
        return imeiDAO.findByMaSP(maSP);
    }

    /* ================== KIỂM TRA IMEI ĐÃ BÁN CHƯA ================== */
    public boolean isImeiDaBan(int maImei) {
        ImeiPhieuNhapDTO imei = imeiDAO.findByImei(maImei);
        return imei != null && imei.getTrangThai() == 1;
    }

    /* ================== ĐÁNH DẤU IMEI ĐÃ BÁN ================== */
    public boolean danhDauImeiDaBan(int maImei) {
        return imeiDAO.updateTrangThaiImei(maImei, 1);
    }

    /* ================== ĐÁNH DẤU IMEI CHƯA BÁN ================== */
    public boolean huyBanImei(int maImei) {
        return imeiDAO.updateTrangThaiImei(maImei, 0);
    }

    /* ================== LẤY 1 IMEI ================== */
    public ImeiPhieuNhapDTO getImei(int maImei) {
        return imeiDAO.findByImei(maImei);
    }

    /* ================== XÓA IMEI (HIẾM DÙNG) ================== */
    public boolean xoaImei(int maImei) {
        return imeiDAO.deleteByImei(maImei);
    }
}
