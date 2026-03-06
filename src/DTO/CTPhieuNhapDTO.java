package DTO;

import java.math.BigDecimal;

public class CTPhieuNhapDTO {
    private int maPhieuNhap;
    private int maSP;
    private int soLuong;
    private BigDecimal donGia;
    private BigDecimal thanhTien;
    private int trangThai; // 1 = hợp lệ, 0 = hủy

    // Constructor mặc định
    public CTPhieuNhapDTO() {
        this.trangThai = 1; // mặc định hợp lệ
        this.soLuong = 0;
        this.donGia = BigDecimal.ZERO;
        this.thanhTien = BigDecimal.ZERO;
    }

    // Constructor đầy đủ
    public CTPhieuNhapDTO(int maPhieuNhap, int maSP, int soLuong, BigDecimal donGia, BigDecimal thanhTien, int trangThai) 
                        {
        this.maPhieuNhap = maPhieuNhap;
        this.maSP = maSP;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.thanhTien = thanhTien;
        this.trangThai = trangThai;
    }

    // Getter & Setter
    public int getMaPhieuNhap() {
        return maPhieuNhap;
    }

    public void setMaPhieuNhap(int maPhieuNhap) {
        this.maPhieuNhap = maPhieuNhap;
    }

    public int getMaSP() {
        return maSP;
    }

    public void setMaSP(int maSP) {
        this.maSP = maSP;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public BigDecimal getDonGia() {
        return donGia;
    }

    public void setDonGia(BigDecimal donGia) {
        this.donGia = donGia;
    }

    public BigDecimal getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(BigDecimal thanhTien) {
        this.thanhTien = thanhTien;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }
}
