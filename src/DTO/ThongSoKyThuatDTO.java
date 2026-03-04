package DTO;

public class ThongSoKyThuatDTO {

    public int maTS;
    public int maSP;
    public String cpu;
    public String ram;
    public String ssd;
    public String vga;
    public String moTa;
    public String trangThai;

    public ThongSoKyThuatDTO() {}

    public ThongSoKyThuatDTO(int maTS, int maSP, String cpu, String ram,
                             String ssd, String vga, String moTa, String trangThai) {
        this.maTS = maTS;
        this.maSP = maSP;
        this.cpu = cpu;
        this.ram = ram;
        this.ssd = ssd;
        this.vga = vga;
        this.moTa = moTa;
        this.trangThai = trangThai;
    }

    public int getMaTS() {
        return maTS;
    }

    public void setMaTS(int maTS) {
        this.maTS = maTS;
    }

    public int getMaSP() {
        return maSP;
    }

    public void setMaSP(int maSP) {
        this.maSP = maSP;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getRam() {
        return ram;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }

    public String getSsd() {
        return ssd;
    }

    public void setSsd(String ssd) {
        this.ssd = ssd;
    }

    public String getVga() {
        return vga;
    }

    public void setVga(String vga) {
        this.vga = vga;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}
