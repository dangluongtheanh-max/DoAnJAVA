package DTO;
import java.time.LocalDate;

public class PhieuDoiTraDTO {
    private int maPhieuDoiTra;
    private int maHoaDon;
    private LocalDate ngayDoiTra;
    private String lyDo;
    private String hinhThuc; // Đổi / Trả

    public PhieuDoiTraDTO() {}

    public PhieuDoiTraDTO(int maPhieuDoiTra, int maHoaDon,
                          LocalDate ngayDoiTra,
                          String lyDo, String hinhThuc) {
        this.maPhieuDoiTra = maPhieuDoiTra;
        this.maHoaDon = maHoaDon;
        this.ngayDoiTra = ngayDoiTra;
        this.lyDo = lyDo;
        this.hinhThuc = hinhThuc;
    }

    public int getMaPhieuDoiTra() {
        return maPhieuDoiTra;
    }

    public void setMaPhieuDoiTra(int maPhieuDoiTra) {
        this.maPhieuDoiTra = maPhieuDoiTra;
    }

    public int getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(int maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public LocalDate getNgayDoiTra() {
        return ngayDoiTra;
    }

    public void setNgayDoiTra(LocalDate ngayDoiTra) {
        this.ngayDoiTra = ngayDoiTra;
    }

    public String getLyDo() {
        return lyDo;
    }

    public void setLyDo(String lyDo) {
        this.lyDo = lyDo;
    }

    public String getHinhThuc() {
        return hinhThuc;
    }

    public void setHinhThuc(String hinhThuc) {
        this.hinhThuc = hinhThuc;
    }
}
