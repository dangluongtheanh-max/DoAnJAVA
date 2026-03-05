package DTO;

/**
 * DTO dùng để liên kết IMEI với Phiếu Nhập trong quá trình xử lý nghiệp vụ.
 * Lưu ý: Đây là object trung gian dùng ở tầng UI/Service,
 * không map trực tiếp xuống DB (DB không có bảng ImeiPhieuNhap).
 * IMEI lưu ở bảng IMEI, chi tiết phiếu nhập ở bảng CHITIETPHIEUNHAP.
 */
public class ImeiPhieuNhapDTO {

    public enum TrangThaiIMEI {
        TRONG_KHO("TrongKho"),
        DA_BAN("DaBan"),
        BAO_HANH("BaoHanh"),
        DOI_TRA("DoiTra"),
        LOI("Loi");

        private final String dbValue;

        TrangThaiIMEI(String dbValue) {
            this.dbValue = dbValue;
        }

        public String getDbValue() {
            return dbValue;
        }

        public static TrangThaiIMEI fromDbValue(String value) {
            for (TrangThaiIMEI t : values()) {
                if (t.dbValue.equalsIgnoreCase(value)) return t;
            }
            throw new IllegalArgumentException("TrangThaiIMEI không hợp lệ: " + value);
        }
    }

    private int maPN;
    private int maSP;
    private int maImei;
    private String imeiCode;
    private TrangThaiIMEI trangThai;

    public ImeiPhieuNhapDTO() {
        this.trangThai = TrangThaiIMEI.TRONG_KHO;
    }

    public ImeiPhieuNhapDTO(int maPN, int maSP, int maImei, String imeiCode, TrangThaiIMEI trangThai) {
        this.maPN = maPN;
        this.maSP = maSP;
        this.maImei = maImei;
        this.imeiCode = imeiCode;
        this.trangThai = trangThai;
    }

    public int getMaPN() { return maPN; }
    public void setMaPN(int maPN) { this.maPN = maPN; }

    public int getMaSP() { return maSP; }
    public void setMaSP(int maSP) { this.maSP = maSP; }

    public int getMaImei() { return maImei; }
    public void setMaImei(int maImei) { this.maImei = maImei; }

    public String getImeiCode() { return imeiCode; }
    public void setImeiCode(String imeiCode) { this.imeiCode = imeiCode; }

    public TrangThaiIMEI getTrangThai() { return trangThai; }
    public void setTrangThai(TrangThaiIMEI trangThai) { this.trangThai = trangThai; }

    /** Trả về giá trị chuỗi để lưu xuống DB */
    public String getTrangThaiDbValue() {
        return trangThai != null ? trangThai.getDbValue() : null;
    }

    @Override
    public String toString() {
        return "ImeiPhieuNhapDTO{" +
                "maPN=" + maPN +
                ", maSP=" + maSP +
                ", maImei=" + maImei +
                ", imeiCode='" + imeiCode + '\'' +
                ", trangThai=" + (trangThai != null ? trangThai.getDbValue() : "null") +
                '}';
    }
}