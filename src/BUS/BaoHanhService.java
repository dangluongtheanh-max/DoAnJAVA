// BaoHanhService.java
package BUS;

import DAO.BaoHanhDAO;
import DTO.BaoHanhDTO;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class BaoHanhService {

    private final BaoHanhDAO dao = new BaoHanhDAO();

    private static final List<String> HINH_THUC_HOP_LE = Arrays.asList(
            "SuaChuaTaiCho", "GuiHang", "ThayTheMoi"
    );

    private static final List<String> TRANG_THAI_HOP_LE = Arrays.asList(
            "DangXuLy", "DaGuiHang", "ChoLinhKien", "DaTraKhach"
    );

    public boolean themBaoHanhMoi(BaoHanhDTO bh) throws SQLException {
        // Validate cơ bản
        if (bh.getMaSP() <= 0 || bh.getMaHoaDon() <= 0) {
            throw new IllegalArgumentException("Mã sản phẩm và mã hóa đơn là bắt buộc");
        }

        if (bh.getHinhThucXuLy() == null || !HINH_THUC_HOP_LE.contains(bh.getHinhThucXuLy())) {
            throw new IllegalArgumentException("Hình thức xử lý không hợp lệ");
        }

        if (bh.getChiPhiPhatSinh() == null || bh.getChiPhiPhatSinh().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Chi phí phát sinh không được âm");
        }

        if (bh.getTrangThai() != null && !TRANG_THAI_HOP_LE.contains(bh.getTrangThai())) {
            throw new IllegalArgumentException("Trạng thái không hợp lệ");
        }

        return dao.themBaoHanh(bh);
    }

    public boolean capNhatBaoHanh(BaoHanhDTO bh) throws SQLException {
        if (bh.getMaBaoHanh() <= 0) {
            throw new IllegalArgumentException("Mã bảo hành không hợp lệ");
        }
        return dao.capNhatBaoHanh(bh);
    }

    public List<BaoHanhDTO> layTatCaBaoHanhDangXuLy() throws SQLException {
        return dao.layDanhSachBaoHanhDangXuLy();
    }

    // Có thể thêm:
    // public BaoHanhDTO layChiTietBaoHanh(int ma) throws SQLException
    // public boolean traHangChoKhach(int maBaoHanh, java.sql.Date ngayTra, String ketQua) throws SQLException
}