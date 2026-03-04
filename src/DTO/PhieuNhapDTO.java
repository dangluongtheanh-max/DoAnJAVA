package DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PhieuNhapDTO {
    private int maPhieuNhap;
    private int maCuaHang;
    private int maNV;
    private int maNCC;
    private LocalDateTime ngayNhap;
    private BigDecimal tongTien;
    private int trangThai; // 0 = nháp,1 = đã nhập kho, 2 = hủy

    public PhieuNhapDTO() {
        this.trangThai = 0;
        this.tongTien = BigDecimal.ZERO;
        this.ngayNhap = LocalDateTime.now();
    }

    // Constructor đầy đủ
    public PhieuNhapDTO(int maPhieuNhap, int maCuaHang, int maNV, int maNCC,
                        LocalDateTime ngayNhap, BigDecimal tongTien, int trangThai) {
        this.maPhieuNhap = maPhieuNhap;
        this.maCuaHang = maCuaHang;
        this.maNV = maNV;
        this.maNCC = maNCC;
        this.ngayNhap = ngayNhap;
        this.tongTien = tongTien;
        this.trangThai = trangThai;
    }

    // Getter & Setter
    public int getMaPhieuNhap() {
        return maPhieuNhap;
    }

    public int getMaCuaHang() {
        return maCuaHang;
    }

    public int getMaNV() {
        return maNV;
    }

    public void setMaNV(int maNV) {
        this.maNV = maNV;
    }

    public void setMaPhieuNhap(int maPhieuNhap) {
        this.maPhieuNhap = maPhieuNhap;
    }

    public void setMaCuaHang(int maCuaHang) {
        this.maCuaHang = maCuaHang;
    }

    public int getMaNCC() {
        return maNCC;
    }

    public void setMaNCC(int maNCC) {
        this.maNCC = maNCC;
    }

    public LocalDateTime getNgayNhap() {
        return ngayNhap;
    }

    public void setNgayNhap(LocalDateTime ngayNhap) {
        this.ngayNhap = ngayNhap;
    }

    public BigDecimal getTongTien() {
        return tongTien;
    }

    public void setTongTien(BigDecimal tongTien) {
        this.tongTien = tongTien;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }
}

