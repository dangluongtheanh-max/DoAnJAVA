package DTO;

import java.math.BigDecimal;

public class SanPhamDTO {
    private int maSP;
    private String tenSP;
    private String loaiSP;
    private int coImei;
    private String hang;
    private BigDecimal giaBan;
    private int thoiGianBH;
    private int trangThai;

    public SanPhamDTO() {
    }

    public SanPhamDTO(int maSP, String tenSP, String loaiSP,
                      int coImei, String hang,
                      BigDecimal giaBan, int thoiGianBH,
                      int trangThai) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.loaiSP = loaiSP;
        this.coImei = coImei;
        this.hang = hang;
        this.giaBan = giaBan;
        this.thoiGianBH = thoiGianBH;
        this.trangThai = trangThai;
    }

    public int getMaSP() {
        return maSP;
    }

    public void setMaSP(int maSP) {
        this.maSP = maSP;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public String getLoaiSP() {
        return loaiSP;
    }

    public void setLoaiSP(String loaiSP) {
        this.loaiSP = loaiSP;
    }

    public int getCoImei() {
        return coImei;
    }

    public void setCoImei(int coImei) {
        this.coImei = coImei;
    }

    public String getHang() {
        return hang;
    }

    public void setHang(String hang) {
        this.hang = hang;
    }

    public BigDecimal getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(BigDecimal giaBan) {
        this.giaBan = giaBan;
    }

    public int getThoiGianBH() {
        return thoiGianBH;
    }

    public void setThoiGianBH(int thoiGianBH) {
        this.thoiGianBH = thoiGianBH;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }
}

