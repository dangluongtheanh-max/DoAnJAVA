package DTO;


import java.util.Date;

public class PhieuBaoHanhDTO {
    private int maPhieuBH;
    private int maKhachHang;
    private Date ngayLap;
    private String trangThai; // Đang bảo hành / Hoàn thành

    public PhieuBaoHanhDTO() {}

    public PhieuBaoHanhDTO(int maPhieuBH, int maKhachHang, Date ngayLap, String trangThai) {
        this.maPhieuBH = maPhieuBH;
        this.maKhachHang = maKhachHang;
        this.ngayLap = ngayLap;
        this.trangThai = trangThai;
    }

    public int getMaPhieuBH() {
        return maPhieuBH;
    }

    public void setMaPhieuBH(int maPhieuBH) {
        this.maPhieuBH = maPhieuBH;
    }

    public int getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(int maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    public Date getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(Date ngayLap) {
        this.ngayLap = ngayLap;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}
