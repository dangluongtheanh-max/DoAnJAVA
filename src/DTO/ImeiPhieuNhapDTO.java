package DTO;

public class ImeiPhieuNhapDTO {

    public int maPN;
    public int maSP;
    public int maImei;
    public int trangThai;

    public ImeiPhieuNhapDTO() {}

    public ImeiPhieuNhapDTO(int maPN, int maSP, int maImei, int trangThai) {
        this.maPN = maPN;
        this.maSP = maSP;
        this.maImei = maImei;
        this.trangThai = trangThai;
    }

    public int getMaPN() {
        return maPN;
    }

    public void setMaPN(int maPN) {
        this.maPN = maPN;
    }

    public int getMaSP() {
        return maSP;
    }

    public void setMaSP(int maSP) {
        this.maSP = maSP;
    }

    public int getMaImei() {
        return maImei;
    }

    public void setMaImei(int maImei) {
        this.maImei = maImei;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }
}
