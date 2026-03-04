package DTO;
public class ChiTietPhieuBaoHanhDTO {
    private int maChiTiet;
    private int maPhieuBaoHanh;
    private int maSanPham;
    private String moTaLoi;
    private String huongXuLy;

    public ChiTietPhieuBaoHanhDTO() {
    }

    public ChiTietPhieuBaoHanhDTO(int maChiTiet, int maPhieuBaoHanh,
            int maSanPham, String moTaLoi,
            String huongXuLy) {
        this.maChiTiet = maChiTiet;
        this.maPhieuBaoHanh = maPhieuBaoHanh;
        this.maSanPham = maSanPham;
        this.moTaLoi = moTaLoi;
        this.huongXuLy = huongXuLy;
    }

    public int getMaChiTiet() {
        return maChiTiet;
    }

    public void setMaChiTiet(int maChiTiet) {
        this.maChiTiet = maChiTiet;
    }

    public int getMaPhieuBaoHanh() {
        return maPhieuBaoHanh;
    }

    public void setMaPhieuBaoHanh(int maPhieuBaoHanh) {
        this.maPhieuBaoHanh = maPhieuBaoHanh;
    }

    public int getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(int maSanPham) {
        this.maSanPham = maSanPham;
    }

    public String getMoTaLoi() {
        return moTaLoi;
    }

    public void setMoTaLoi(String moTaLoi) {
        this.moTaLoi = moTaLoi;
    }

    public String getHuongXuLy() {
        return huongXuLy;
    }

    public void setHuongXuLy(String huongXuLy) {
        this.huongXuLy = huongXuLy;
    }
}
