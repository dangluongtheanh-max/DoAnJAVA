package BUS;

import DAO.SanPhamDAO;
import DTO.SanPhamDTO;

import java.math.BigDecimal;
import java.util.ArrayList;

public class SanPhamBUS {

    private SanPhamDAO sanPhamDAO;

    public SanPhamBUS() {
        sanPhamDAO = new SanPhamDAO();
    }

    // Lấy toàn bộ sản phẩm
    public ArrayList<SanPhamDTO> getAll() {
        return sanPhamDAO.getAll();
    }

    // Kiểm tra dữ liệu hợp lệ trước khi thêm / sửa
    public String validate(SanPhamDTO sp) {

        if (sp.getTenSP() == null || sp.getTenSP().trim().isEmpty())
            return "Tên sản phẩm không được để trống";

        if (sp.getGia() == null || sp.getGia().compareTo(BigDecimal.ZERO) < 0)
            return "Giá phải >= 0";

        if (sp.getGiaGoc() != null &&
                sp.getGiaGoc().compareTo(BigDecimal.ZERO) < 0)
            return "Giá gốc phải >= 0";

        if (sp.getSoLuongTon() < 0)
            return "Số lượng tồn không được âm";

        if (sp.getSoLuongToiThieu() < 0)
            return "Số lượng tối thiểu không được âm";

        if (sp.getSoLuongToiDa() < 0)
            return "Số lượng tối đa không được âm";

        if (sp.getThoiHanBaoHanhThang() < 0)
            return "Thời hạn bảo hành không hợp lệ";

        if (sp.getTrangThai() == null ||
                (!sp.getTrangThai().equals("DangBan") &&
                 !sp.getTrangThai().equals("NgungBan") &&
                 !sp.getTrangThai().equals("HetHang")))
            return "Trạng thái không hợp lệ";

        return "OK";
    }

}
