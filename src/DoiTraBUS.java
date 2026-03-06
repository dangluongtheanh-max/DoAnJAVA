// DoiTraBUS.java
package BUS;

import DAO.DoiTraDAO;
import DTO.DoiTraDTO;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class DoiTraBUS {

    private final DoiTraDAO DAO = new DoiTraDAO();

    // public để GUI tham chiếu — tránh khai báo trùng lặp
    public static final List<String> TRANG_THAI_HOP_LE = Arrays.asList(
            "DangXuLy", "HoanThanh"
    );

    public boolean taoYeuCauDoiTra(DoiTraDTO dt) throws SQLException {
        if (dt.getMaHoaDon() <= 0)
            throw new IllegalArgumentException("Mã hóa đơn không hợp lệ");
        if (dt.getMaSP() <= 0)
            throw new IllegalArgumentException("Mã sản phẩm không hợp lệ");
        if (dt.getMaNV() <= 0)
            throw new IllegalArgumentException("Mã nhân viên không hợp lệ");
        if (dt.getSoLuongTra() < 1)
            throw new IllegalArgumentException("Số lượng trả phải lớn hơn 0");
        if (dt.getTrangThai() != null && !TRANG_THAI_HOP_LE.contains(dt.getTrangThai()))
            throw new IllegalArgumentException("Trạng thái không hợp lệ");
        return DAO.themDoiTra(dt);
    }

    public boolean capNhatYeuCauDoiTra(DoiTraDTO dt) throws SQLException {
        if (dt.getMaDoiTra() <= 0)
            throw new IllegalArgumentException("Mã đổi trả không hợp lệ");
        if (dt.getTrangThai() != null && !TRANG_THAI_HOP_LE.contains(dt.getTrangThai()))
            throw new IllegalArgumentException("Trạng thái không hợp lệ");
        return DAO.capNhatDoiTra(dt);
    }

    public boolean hoanThanhDoiTra(int maDoiTra, String ghiChu) throws SQLException {
        if (maDoiTra <= 0)
            throw new IllegalArgumentException("Mã đổi trả không hợp lệ");
        return DAO.hoanThanhDoiTra(maDoiTra, ghiChu);
    }

    public boolean xoaDoiTra(int maDoiTra) throws SQLException {
        if (maDoiTra <= 0)
            throw new IllegalArgumentException("Mã đổi trả không hợp lệ");
        return DAO.xoaDoiTra(maDoiTra);
    }

    public List<DoiTraDTO> layTatCaDoiTra() throws SQLException {
        return DAO.layTatCaDoiTra();
    }

    public List<DoiTraDTO> layDanhSachTheoTrangThai(String trangThai) throws SQLException {
        if (!TRANG_THAI_HOP_LE.contains(trangThai))
            throw new IllegalArgumentException("Trạng thái không hợp lệ: " + trangThai);
        return DAO.layDanhSachTheoTrangThai(trangThai);
    }

    /** @deprecated dùng layDanhSachTheoTrangThai("DangXuLy") thay thế */
    @Deprecated
    public List<DoiTraDTO> layTatCaYeuCauDangXuLy() throws SQLException {
        return DAO.layDanhSachDangXuLy();
    }
}