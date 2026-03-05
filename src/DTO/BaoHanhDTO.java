// BaoHanhDTO.java
package DTO;

import java.math.BigDecimal;
import java.sql.Date;

public class BaoHanhDTO {
    private int maBaoHanh;
    private Integer maIMEI;           // có thể null
    private int maSP;
    private int maHoaDon;
    private Integer maNVTiepNhan;     // có thể null
    private Integer maNVXuLy;         // có thể null

    private Date ngayTiepNhan;
    private Date ngayHenTra;          // có thể null
    private Date ngayTra;             // có thể null

    private String moTaLoi;
    private String hinhThucXuLy;      // "SuaChuaTaiCho", "GuiHang", "ThayTheMoi"
    private String ketQuaXuLy;
    private BigDecimal chiPhiPhatSinh;
    private String trangThai;         // "DangXuLy", "DaGuiHang", "ChoLinhKien", "DaTraKhach"

    // Constructor rỗng
    public BaoHanhDTO() {
        this.chiPhiPhatSinh = BigDecimal.ZERO;
        this.trangThai = "DangXuLy";
    }

    // Getters & Setters
    public int getMaBaoHanh() { return maBaoHanh; }
    public void setMaBaoHanh(int maBaoHanh) { this.maBaoHanh = maBaoHanh; }

    public Integer getMaIMEI() { return maIMEI; }
    public void setMaIMEI(Integer maIMEI) { this.maIMEI = maIMEI; }

    public int getMaSP() { return maSP; }
    public void setMaSP(int maSP) { this.maSP = maSP; }

    public int getMaHoaDon() { return maHoaDon; }
    public void setMaHoaDon(int maHoaDon) { this.maHoaDon = maHoaDon; }

    public Integer getMaNVTiepNhan() { return maNVTiepNhan; }
    public void setMaNVTiepNhan(Integer maNVTiepNhan) { this.maNVTiepNhan = maNVTiepNhan; }

    public Integer getMaNVXuLy() { return maNVXuLy; }
    public void setMaNVXuLy(Integer maNVXuLy) { this.maNVXuLy = maNVXuLy; }

    public Date getNgayTiepNhan() { return ngayTiepNhan; }
    public void setNgayTiepNhan(Date ngayTiepNhan) { this.ngayTiepNhan = ngayTiepNhan; }

    public Date getNgayHenTra() { return ngayHenTra; }
    public void setNgayHenTra(Date ngayHenTra) { this.ngayHenTra = ngayHenTra; }

    public Date getNgayTra() { return ngayTra; }
    public void setNgayTra(Date ngayTra) { this.ngayTra = ngayTra; }

    public String getMoTaLoi() { return moTaLoi; }
    public void setMoTaLoi(String moTaLoi) { this.moTaLoi = moTaLoi; }

    public String getHinhThucXuLy() { return hinhThucXuLy; }
    public void setHinhThucXuLy(String hinhThucXuLy) { this.hinhThucXuLy = hinhThucXuLy; }

    public String getKetQuaXuLy() { return ketQuaXuLy; }
    public void setKetQuaXuLy(String ketQuaXuLy) { this.ketQuaXuLy = ketQuaXuLy; }

    public BigDecimal getChiPhiPhatSinh() { return chiPhiPhatSinh; }
    public void setChiPhiPhatSinh(BigDecimal chiPhiPhatSinh) { this.chiPhiPhatSinh = chiPhiPhatSinh; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    @Override
    public String toString() {
        return "BaoHanhDTO{" +
                "maBaoHanh=" + maBaoHanh +
                ", maSP=" + maSP +
                ", maHoaDon=" + maHoaDon +
                ", trangThai='" + trangThai + '\'' +
                '}';
    }
}