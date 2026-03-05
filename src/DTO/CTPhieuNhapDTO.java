package DTO;

import java.math.BigDecimal;

/**
 * DTO ánh xạ bảng CHITIETPHIEUNHAP trong DB.
 *
 * Bảng CHITIETPHIEUNHAP gồm:
 *   MaChiTietPN, MaPN, MaSP, SoLuong, DonGiaNhap, ThanhTien (COMPUTED), GhiChu
 *
 * Lưu ý:
 *  - ThanhTien là COMPUTED PERSISTED trong DB (= SoLuong * DonGiaNhap),
 *    chỉ dùng để đọc ra, không gửi xuống khi INSERT/UPDATE.
 *  - DB không có cột TrangThai trong bảng này.
 */
public class CTPhieuNhapDTO {

    private int maChiTietPN;           // MaChiTietPN - PRIMARY KEY
    private int maPhieuNhap;           // MaPN - FK → PHIEUNHAP
    private int maSP;                  // MaSP - FK → SANPHAM
    private int soLuong;               // SoLuong - CHECK > 0
    private BigDecimal donGiaNhap;     // DonGiaNhap - CHECK >= 0
    private BigDecimal thanhTien;      // ThanhTien - COMPUTED, chỉ đọc
    private String ghiChu;            // GhiChu

    public CTPhieuNhapDTO() {
        this.soLuong = 0;
        this.donGiaNhap = BigDecimal.ZERO;
        this.thanhTien = BigDecimal.ZERO;
    }

    // Constructor dùng khi INSERT (không truyền maChiTietPN và thanhTien vì DB tự xử lý)
    public CTPhieuNhapDTO(int maPhieuNhap, int maSP, int soLuong,
                          BigDecimal donGiaNhap, String ghiChu) {
        this.maPhieuNhap = maPhieuNhap;
        this.maSP = maSP;
        this.soLuong = soLuong;
        this.donGiaNhap = donGiaNhap;
        this.ghiChu = ghiChu;
        this.thanhTien = donGiaNhap.multiply(BigDecimal.valueOf(soLuong)); // tính tạm ở client
    }

    // Constructor đầy đủ dùng khi đọc từ DB
    public CTPhieuNhapDTO(int maChiTietPN, int maPhieuNhap, int maSP, int soLuong,
                          BigDecimal donGiaNhap, BigDecimal thanhTien, String ghiChu) {
        this.maChiTietPN = maChiTietPN;
        this.maPhieuNhap = maPhieuNhap;
        this.maSP = maSP;
        this.soLuong = soLuong;
        this.donGiaNhap = donGiaNhap;
        this.thanhTien = thanhTien; // lấy từ DB, không tự tính
        this.ghiChu = ghiChu;
    }

    public int getMaChiTietPN() { return maChiTietPN; }
    public void setMaChiTietPN(int maChiTietPN) { this.maChiTietPN = maChiTietPN; }

    public int getMaPhieuNhap() { return maPhieuNhap; }
    public void setMaPhieuNhap(int maPhieuNhap) { this.maPhieuNhap = maPhieuNhap; }

    public int getMaSP() { return maSP; }
    public void setMaSP(int maSP) { this.maSP = maSP; }

    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) {
        if (soLuong <= 0) throw new IllegalArgumentException("SoLuong phải > 0");
        this.soLuong = soLuong;
    }

    public BigDecimal getDonGiaNhap() { return donGiaNhap; }
    public void setDonGiaNhap(BigDecimal donGiaNhap) {
        if (donGiaNhap.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("DonGiaNhap phải >= 0");
        this.donGiaNhap = donGiaNhap;
    }

    /** Chỉ đọc — ThanhTien do DB tính (COMPUTED PERSISTED), không dùng để INSERT/UPDATE */
    public BigDecimal getThanhTien() { return thanhTien; }
    public void setThanhTien(BigDecimal thanhTien) { this.thanhTien = thanhTien; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }

    @Override
    public String toString() {
        return "CTPhieuNhapDTO{" +
                "maChiTietPN=" + maChiTietPN +
                ", maPhieuNhap=" + maPhieuNhap +
                ", maSP=" + maSP +
                ", soLuong=" + soLuong +
                ", donGiaNhap=" + donGiaNhap +
                ", thanhTien=" + thanhTien +
                ", ghiChu='" + ghiChu + '\'' +
                '}';
    }
}