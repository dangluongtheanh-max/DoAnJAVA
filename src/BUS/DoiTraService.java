// DoiTraService.java
package BUS;

import DAO.DoiTraDAO;
import DTO.DoiTraDTO;

import java.sql.SQLException;
import java.util.List;

public class DoiTraService {

    private final DoiTraDAO DAO = new DoiTraDAO();

    public boolean taoYeuCauDoiTra(DoiTraDTO dt) throws SQLException {
        // Validate cơ bản
        if (dt.getMaHoaDon() <= 0) {
            throw new IllegalArgumentException("Mã hóa đơn không hợp lệ");
        }
        if (dt.getMaSP() <= 0) {
            throw new IllegalArgumentException("Mã sản phẩm không hợp lệ");
        }
        if (dt.getMaNV() <= 0) {
            throw new IllegalArgumentException("Mã nhân viên không hợp lệ");
        }
        if (dt.getSoLuongTra() < 1) {
            throw new IllegalArgumentException("Số lượng trả phải lớn hơn 0");
        }

        return DAO.themDoiTra(dt);
    }

    public boolean capNhatYeuCauDoiTra(DoiTraDTO dt) throws SQLException {
        if (dt.getMaDoiTra() <= 0) {
            throw new IllegalArgumentException("Mã đổi trả không hợp lệ");
        }
        return DAO.capNhatDoiTra(dt);
    }

    public boolean hoanThanhDoiTra(int maDoiTra, String ghiChu) throws SQLException {
        if (maDoiTra <= 0) {
            throw new IllegalArgumentException("Mã đổi trả không hợp lệ");
        }
        return DAO.hoanThanhDoiTra(maDoiTra, ghiChu);
    }

    public List<DoiTraDTO> layTatCaYeuCauDangXuLy() throws SQLException {
        return DAO.layDanhSachDangXuLy();
    }
}