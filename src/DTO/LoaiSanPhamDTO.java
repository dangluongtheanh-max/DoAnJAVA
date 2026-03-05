package DTO;

public class LoaiSanPhamDTO {
    private int maLoai;
    private String tenLoai;
    private String moTa;
    private boolean coQuanLyIMEI;

    public LoaiSanPhamDTO() {}

    public LoaiSanPhamDTO(int maLoai, String tenLoai, String moTa, boolean coQuanLyIMEI) {
        this.maLoai = maLoai;
        this.tenLoai = tenLoai;
        this.moTa = moTa;
        this.coQuanLyIMEI = coQuanLyIMEI;
    }

    public int getMaLoai() { return maLoai; }
    public void setMaLoai(int maLoai) { this.maLoai = maLoai; }

    public String getTenLoai() { return tenLoai; }
    public void setTenLoai(String tenLoai) { this.tenLoai = tenLoai; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }

    public boolean isCoQuanLyIMEI() { return coQuanLyIMEI; }
    public void setCoQuanLyIMEI(boolean coQuanLyIMEI) { this.coQuanLyIMEI = coQuanLyIMEI; }
}
