package DTO;

import java.math.BigDecimal;

public class ChiTietHoaDonDTO {
    private int maHD;
    private int maSP;
    private Integer maImei; // NULL nếu không có IMEI
    private int soLuong;
    private BigDecimal donGia;
    private BigDecimal thanhTien;
    private int trangThai;

    public ChiTietHoaDonDTO() {
    }

    public ChiTietHoaDonDTO(int maHD, int maSP, Integer maImei,
                       int soLuong, BigDecimal donGia,
                       BigDecimal thanhTien, int trangThai) {
        this.maHD = maHD;
        this.maSP = maSP;
        this.maImei = maImei;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.thanhTien = thanhTien;
        this.trangThai = trangThai;
    }

    public int getMaHD() {
        return maHD;
    }

    public void setMaHD(int maHD) {
        this.maHD = maHD;
    }

    public int getMaSP() {
        return maSP;
    }

    public void setMaSP(int maSP) {
        this.maSP = maSP;
    }

    public Integer getMaImei() {
        return maImei;
    }

    public void setMaImei(Integer maImei) {
        this.maImei = maImei;
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
