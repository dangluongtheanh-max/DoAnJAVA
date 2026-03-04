package BUS;

import DAO.ChiTietHoaDonDAO;
import DAO.HoaDonDAO;
import DTO.ChiTietHoaDonDTO;
import DTO.HoaDonDTO;
import util.DBConnection;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * BUS thanh toán hóa đơn.
 *
 * Mục tiêu:
 * - Tính tổng tiền hàng từ CT_HoaDon.
 * - Áp dụng giảm giá, thuế VAT.
 * - Kiểm tra tiền khách đưa.
 * - Cập nhật HoaDon (Ma_KH, TongTienHang, GiamGia, ThueVAT, TongThanhToan) và TrangThai.
 *
 * Lưu ý: Hiện các DAO của bạn chưa có hàm "update đầy đủ thông tin thanh toán", nên BUS này
 * sẽ tự thực hiện UPDATE HoaDon bằng DBConnection để chốt hóa đơn.
 */
public class ThanhToanBUS {

    // Quy ước trạng thái (đang dùng trong HoaDonBUS/HoaDonDAO)
    public static final int TRANG_THAI_CHUA_THANH_TOAN = 0;
    public static final int TRANG_THAI_DA_THANH_TOAN = 1;
    public static final int TRANG_THAI_HUY = 2;

    private final HoaDonDAO hoaDonDAO = new HoaDonDAO();
    private final ChiTietHoaDonDAO ctHoaDonDAO = new ChiTietHoaDonDAO();

    /**
     * Kết quả trả về sau khi chốt thanh toán.
     */
    public static class KetQuaThanhToan {
        private final int maHD;
        private final BigDecimal tongTienHang;
        private final BigDecimal giamGia;
        private final BigDecimal thueVAT;
        private final BigDecimal tongThanhToan;
        private final BigDecimal tienKhachDua;
        private final BigDecimal tienThua;

        public KetQuaThanhToan(int maHD,
                               BigDecimal tongTienHang,
                               BigDecimal giamGia,
                               BigDecimal thueVAT,
                               BigDecimal tongThanhToan,
                               BigDecimal tienKhachDua,
                               BigDecimal tienThua) {
            this.maHD = maHD;
            this.tongTienHang = tongTienHang;
            this.giamGia = giamGia;
            this.thueVAT = thueVAT;
            this.tongThanhToan = tongThanhToan;
            this.tienKhachDua = tienKhachDua;
            this.tienThua = tienThua;
        }

        public int getMaHD() { return maHD; }
        public BigDecimal getTongTienHang() { return tongTienHang; }
        public BigDecimal getGiamGia() { return giamGia; }
        public BigDecimal getThueVAT() { return thueVAT; }
        public BigDecimal getTongThanhToan() { return tongThanhToan; }
        public BigDecimal getTienKhachDua() { return tienKhachDua; }
        public BigDecimal getTienThua() { return tienThua; }
    }

    /**
     * Tính tổng tiền hàng từ chi tiết hóa đơn (SUM(Thanh_Tien)).
     */
    public BigDecimal tinhTongTienHang(int maHD) {
        ArrayList<ChiTietHoaDonDTO> ds = ctHoaDonDAO.getByHoaDon(maHD);
        BigDecimal sum = BigDecimal.ZERO;
        for (ChiTietHoaDonDTO ct : ds) {
            if (ct.getThanhTien() != null) {
                sum = sum.add(ct.getThanhTien());
            }
        }
        return lamTron0(sum);
    }

    /**
     * Công thức chốt tổng thanh toán: TongTienHang - GiamGia + ThueVAT.
     */
    public BigDecimal tinhTongThanhToan(BigDecimal tongTienHang, BigDecimal giamGia, BigDecimal thueVAT) {
        tongTienHang = nvl(tongTienHang);
        giamGia = nvl(giamGia);
        thueVAT = nvl(thueVAT);
        return lamTron0(tongTienHang.subtract(giamGia).add(thueVAT));
    }

    /**
     * Chốt thanh toán hóa đơn.
     *
     * @param maHD         mã hóa đơn
     * @param maKH         mã khách hàng (có thể = 0 nếu bán lẻ/khách vãng lai, tùy thiết kế DB)
     * @param giamGia      số tiền giảm giá (>=0)
     * @param thueVAT      số tiền thuế VAT (>=0)
     * @param tienKhachDua tiền khách đưa (>= TongThanhToan)
     * @return KetQuaThanhToan
     */
    public KetQuaThanhToan thanhToanHoaDon(int maHD,
                                           int maKH,
                                           BigDecimal giamGia,
                                           BigDecimal thueVAT,
                                           BigDecimal tienKhachDua) {

        HoaDonDTO hd = hoaDonDAO.timHoaDonTheoMa(maHD);
        if (hd == null) {
            throw new IllegalArgumentException("Không tìm thấy hóa đơn Ma_HD = " + maHD);
        }
        if (hd.getTrangThai() == TRANG_THAI_DA_THANH_TOAN) {
            throw new IllegalStateException("Hóa đơn đã thanh toán rồi (Ma_HD = " + maHD + ")");
        }
        if (hd.getTrangThai() == TRANG_THAI_HUY) {
            throw new IllegalStateException("Hóa đơn đã bị hủy (Ma_HD = " + maHD + ")");
        }

        ArrayList<ChiTietHoaDonDTO> dsCT = ctHoaDonDAO.getByHoaDon(maHD);
        if (dsCT == null || dsCT.isEmpty()) {
            throw new IllegalStateException("Hóa đơn chưa có sản phẩm, không thể thanh toán (Ma_HD = " + maHD + ")");
        }

        BigDecimal tongTienHang = tinhTongTienHang(maHD);
        giamGia = nvl(giamGia);
        thueVAT = nvl(thueVAT);

        if (giamGia.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Giảm giá không hợp lệ (<0)");
        }
        if (thueVAT.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Thuế VAT không hợp lệ (<0)");
        }
        if (giamGia.compareTo(tongTienHang) > 0) {
            throw new IllegalArgumentException("Giảm giá không được lớn hơn tổng tiền hàng");
        }

        BigDecimal tongThanhToan = tinhTongThanhToan(tongTienHang, giamGia, thueVAT);

        tienKhachDua = nvl(tienKhachDua);
        if (tienKhachDua.compareTo(tongThanhToan) < 0) {
            throw new IllegalArgumentException(
                    "Tiền khách đưa không đủ. Cần: " + tongThanhToan + ", nhận: " + tienKhachDua);
        }

        BigDecimal tienThua = lamTron0(tienKhachDua.subtract(tongThanhToan));

        // Cập nhật DB: gán khách hàng + chốt tổng tiền + set trạng thái đã thanh toán
        capNhatThongTinThanhToan(maHD, maKH, tongTienHang, giamGia, thueVAT, tongThanhToan);
        hoaDonDAO.capNhatTrangThaiHoaDon(maHD, TRANG_THAI_DA_THANH_TOAN);

        return new KetQuaThanhToan(
                maHD,
                tongTienHang,
                lamTron0(giamGia),
                lamTron0(thueVAT),
                tongThanhToan,
                lamTron0(tienKhachDua),
                tienThua
        );
    }

    
    private void capNhatThongTinThanhToan(int maHD,
                                          int maKH,
                                          BigDecimal tongTienHang,
                                          BigDecimal giamGia,
                                          BigDecimal thueVAT,
                                          BigDecimal tongThanhToan) {

        Objects.requireNonNull(tongTienHang, "tongTienHang");
        Objects.requireNonNull(giamGia, "giamGia");
        Objects.requireNonNull(thueVAT, "thueVAT");
        Objects.requireNonNull(tongThanhToan, "tongThanhToan");

        
        String sql = "UPDATE HoaDon SET Ma_KH = ?, TongTienHang = ?, GiamGia = ?, ThueVAT = ?, TongThanhToan = ? WHERE Ma_HD = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, maKH);
            ps.setBigDecimal(2, lamTron0(tongTienHang));
            ps.setBigDecimal(3, lamTron0(giamGia));
            ps.setBigDecimal(4, lamTron0(thueVAT));
            ps.setBigDecimal(5, lamTron0(tongThanhToan));
            ps.setInt(6, maHD);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi cập nhật thanh toán cho hóa đơn Ma_HD = " + maHD, e);
        }
    }

    // ===== Helpers =====

    private static BigDecimal nvl(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v;
    }

    /**
     * Làm tròn về 0 chữ số thập phân (tiền VNĐ thường không dùng lẻ).
     * Nếu bạn cần lưu 2 số thập phân thì đổi scale=2.
     */
    private static BigDecimal lamTron0(BigDecimal v) {
        if (v == null) return BigDecimal.ZERO;
        return v.setScale(0, RoundingMode.HALF_UP);
    }
}


