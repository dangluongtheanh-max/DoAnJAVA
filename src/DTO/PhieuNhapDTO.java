package DTO;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO ánh xạ bảng PHIEUNHAP trong DB.
 *
 * Bảng PHIEUNHAP gồm: MaPN, MaNhaCungCap, MaNV, NgayNhap, TongTien, GhiChu, TrangThai
 * Lưu ý: DB không có cột MaCuaHang trong bảng này.
 */
public class PhieuNhapDTO {

    public enum TrangThaiPhieuNhap {
        HOAN_THANH("HoanThanh"),
        HUY("Huy");

        private final String dbValue;

        TrangThaiPhieuNhap(String dbValue) {
            this.dbValue = dbValue;
        }

        public String getDbValue() {
            return dbValue;
        }

        public static TrangThaiPhieuNhap fromDbValue(String value) {
            for (TrangThaiPhieuNhap t : values()) {
                if (t.dbValue.equalsIgnoreCase(value)) return t;
            }
            throw new IllegalArgumentException("TrangThaiPhieuNhap không hợp lệ: " + value);
        }
    }

    private int maPhieuNhap;           // MaPN - PRIMARY KEY
    private int maNV;                  // MaNV - FK → NHANVIEN
    private int maNhaCungCap;          // MaNhaCungCap - FK → NHACUNGCAP
    private LocalDate ngayNhap;        // NgayNhap - kiểu DATE trong DB
    private BigDecimal tongTien;       // TongTien
    private String ghiChu;            // GhiChu
    private TrangThaiPhieuNhap trangThai; // TrangThai

    public PhieuNhapDTO() {
        this.trangThai = TrangThaiPhieuNhap.HOAN_THANH;
        this.tongTien = BigDecimal.ZERO;
        this.ngayNhap = LocalDate.now();
    }

    public PhieuNhapDTO(int maPhieuNhap, int maNV, int maNhaCungCap,
                        LocalDate ngayNhap, BigDecimal tongTien,
                        String ghiChu, TrangThaiPhieuNhap trangThai) {
        this.maPhieuNhap = maPhieuNhap;
        this.maNV = maNV;
        this.maNhaCungCap = maNhaCungCap;
        this.ngayNhap = ngayNhap;
        this.tongTien = tongTien;
        this.ghiChu = ghiChu;
        this.trangThai = trangThai;
    }

    public int getMaPhieuNhap() { return maPhieuNhap; }
    public void setMaPhieuNhap(int maPhieuNhap) { this.maPhieuNhap = maPhieuNhap; }

    public int getMaNV() { return maNV; }
    public void setMaNV(int maNV) { this.maNV = maNV; }

    public int getMaNhaCungCap() { return maNhaCungCap; }
    public void setMaNhaCungCap(int maNhaCungCap) { this.maNhaCungCap = maNhaCungCap; }

    public LocalDate getNgayNhap() { return ngayNhap; }
    public void setNgayNhap(LocalDate ngayNhap) { this.ngayNhap = ngayNhap; }

    public BigDecimal getTongTien() { return tongTien; }
    public void setTongTien(BigDecimal tongTien) { this.tongTien = tongTien; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }

    public TrangThaiPhieuNhap getTrangThai() { return trangThai; }
    public void setTrangThai(TrangThaiPhieuNhap trangThai) { this.trangThai = trangThai; }

    /** Trả về giá trị chuỗi để lưu xuống DB */
    public String getTrangThaiDbValue() {
        return trangThai != null ? trangThai.getDbValue() : null;
    }

    @Override
    public String toString() {
        return "PhieuNhapDTO{" +
                "maPhieuNhap=" + maPhieuNhap +
                ", maNV=" + maNV +
                ", maNhaCungCap=" + maNhaCungCap +
                ", ngayNhap=" + ngayNhap +
                ", tongTien=" + tongTien +
                ", ghiChu='" + ghiChu + '\'' +
                ", trangThai=" + (trangThai != null ? trangThai.getDbValue() : "null") +
                '}';
    }
}