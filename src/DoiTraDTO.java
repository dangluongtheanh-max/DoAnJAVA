// DoiTraDTO.java
package DTO;

import java.sql.Date;

public class DoiTraDTO {

    private int maDoiTra;           // IDENTITY, chỉ đọc
    private int maHoaDon;           // NOT NULL
    private int maSP;               // NOT NULL
    private Integer maIMEI;         // NULL được
    private int soLuongTra;         // DEFAULT 1, > 0
    private String lyDo;
    private int maNV;               // NOT NULL
    private Date ngayYeuCau;        // DEFAULT GETDATE()
    private String trangThai;       // 'DangXuLy' hoặc 'HoanThanh'
    private String ghiChu;

    public DoiTraDTO() {
        this.soLuongTra = 1;
        this.trangThai = "DangXuLy";
    }

    // Getters & Setters
    public int getMaDoiTra() { return maDoiTra; }
    public void setMaDoiTra(int maDoiTra) { this.maDoiTra = maDoiTra; }

    public int getMaHoaDon() { return maHoaDon; }
    public void setMaHoaDon(int maHoaDon) { this.maHoaDon = maHoaDon; }

    public int getMaSP() { return maSP; }
    public void setMaSP(int maSP) { this.maSP = maSP; }

    public Integer getMaIMEI() { return maIMEI; }
    public void setMaIMEI(Integer maIMEI) { this.maIMEI = maIMEI; }

    public int getSoLuongTra() { return soLuongTra; }
    public void setSoLuongTra(int soLuongTra) { this.soLuongTra = soLuongTra; }

    public String getLyDo() { return lyDo; }
    public void setLyDo(String lyDo) { this.lyDo = lyDo; }

    public int getMaNV() { return maNV; }
    public void setMaNV(int maNV) { this.maNV = maNV; }

    public Date getNgayYeuCau() { return ngayYeuCau; }
    public void setNgayYeuCau(Date ngayYeuCau) { this.ngayYeuCau = ngayYeuCau; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }

    @Override
    public String toString() {
        return "DoiTraDTO{" +
                "maDoiTra=" + maDoiTra +
                ", maHoaDon=" + maHoaDon +
                ", maSP=" + maSP +
                ", soLuongTra=" + soLuongTra +
                ", trangThai='" + trangThai + '\'' +
                '}';
    }
}