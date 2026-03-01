package DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class HoaDonDTO {
    private int maHD;
    private int maCH;
    private int maKH;
    private int maNV;
    private LocalDateTime ngayLap;
    private BigDecimal tongTienHang;
    private BigDecimal giamGia;
    private BigDecimal thueVAT;
    private BigDecimal tongThanhToan;
    private int trangThai;

    public HoaDonDTO() {
    }

    public HoaDonDTO(int maHD, int maCH, int maKH, int maNV,
                     LocalDateTime ngayLap, BigDecimal tongTienHang,
                     BigDecimal giamGia, BigDecimal thueVAT,
                     BigDecimal tongThanhToan, int trangThai) {
        this.maHD = maHD;
        this.maCH = maCH;
        this.maKH = maKH;
        this.maNV = maNV;
        this.ngayLap = ngayLap;
        this.tongTienHang = tongTienHang;
        this.giamGia = giamGia;
        this.thueVAT = thueVAT;
        this.tongThanhToan = tongThanhToan;
        this.trangThai = trangThai;
    }

    public int getMaHD() {
        return maHD;
    }

    public void setMaHD(int maHD) {
        this.maHD = maHD;
    }

    public int getMaCH() {
        return maCH;
    }

    public void setMaCH(int maCH) {
        this.maCH = maCH;
    }

    public int getMaKH() {
        return maKH;
    }

    public void setMaKH(int maKH) {
        this.maKH = maKH;
    }

    public int getMaNV() {
        return maNV;
    }

    public void setMaNV(int maNV) {
        this.maNV = maNV;
    }

    public LocalDateTime getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(LocalDateTime ngayLap) {
        this.ngayLap = ngayLap;
    }

    public BigDecimal getTongTienHang() {
        return tongTienHang;
    }

    public void setTongTienHang(BigDecimal tongTienHang) {
        this.tongTienHang = tongTienHang;
    }

    public BigDecimal getGiamGia() {
        return giamGia;
    }

    public void setGiamGia(BigDecimal giamGia) {
        this.giamGia = giamGia;
    }

    public BigDecimal getThueVAT() {
        return thueVAT;
    }

    public void setThueVAT(BigDecimal thueVAT) {
        this.thueVAT = thueVAT;
    }

    public BigDecimal getTongThanhToan() {
        return tongThanhToan;
    }

    public void setTongThanhToan(BigDecimal tongThanhToan) {
        this.tongThanhToan = tongThanhToan;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }
}
