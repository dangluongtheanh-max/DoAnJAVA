// BaoHanhDAO.java
package DAO;

import DTO.BaoHanhDTO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import util.DBConnection;

public class BaoHanhDAO {

    // Thêm mới bảo hành
    public boolean themBaoHanh(BaoHanhDTO bh) throws SQLException {
        String sql = """
                INSERT INTO BAOHANH (
                    MaIMEI, MaSP, MaHoaDon, MaNVTiepNhan,
                    NgayTiepNhan, NgayHenTra, MoTaLoi,
                    HinhThucXuLy, ChiPhiPhatSinh, TrangThai
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, bh.getMaIMEI(), Types.INTEGER);
            ps.setInt(2, bh.getMaSP());
            ps.setInt(3, bh.getMaHoaDon());
            ps.setObject(4, bh.getMaNVTiepNhan(), Types.INTEGER);
            ps.setDate(5, bh.getNgayTiepNhan() != null ? bh.getNgayTiepNhan() : new Date(System.currentTimeMillis()));
            ps.setDate(6, bh.getNgayHenTra());
            ps.setString(7, bh.getMoTaLoi());
            ps.setString(8, bh.getHinhThucXuLy());
            ps.setBigDecimal(9, bh.getChiPhiPhatSinh());
            ps.setString(10, bh.getTrangThai() != null ? bh.getTrangThai() : "DangXuLy");

            return ps.executeUpdate() > 0;
        }
    }

    // Cập nhật thông tin bảo hành (thường dùng khi xử lý, trả hàng...)
    public boolean capNhatBaoHanh(BaoHanhDTO bh) throws SQLException {
        String sql = """
                UPDATE BAOHANH SET
                    MaIMEI = ?,
                    MaSP = ?,
                    MaHoaDon = ?,
                    MaNVTiepNhan = ?,
                    MaNVXuLy = ?,
                    NgayHenTra = ?,
                    NgayTra = ?,
                    MoTaLoi = ?,
                    HinhThucXuLy = ?,
                    KetQuaXuLy = ?,
                    ChiPhiPhatSinh = ?,
                    TrangThai = ?
                WHERE MaBaoHanh = ?
                """;

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, bh.getMaIMEI(), Types.INTEGER);
            ps.setInt(2, bh.getMaSP());
            ps.setInt(3, bh.getMaHoaDon());
            ps.setObject(4, bh.getMaNVTiepNhan(), Types.INTEGER);
            ps.setObject(5, bh.getMaNVXuLy(), Types.INTEGER);
            ps.setDate(6, bh.getNgayHenTra());
            ps.setDate(7, bh.getNgayTra());
            ps.setString(8, bh.getMoTaLoi());
            ps.setString(9, bh.getHinhThucXuLy());
            ps.setString(10, bh.getKetQuaXuLy());
            ps.setBigDecimal(11, bh.getChiPhiPhatSinh());
            ps.setString(12, bh.getTrangThai());
            ps.setInt(13, bh.getMaBaoHanh());

            return ps.executeUpdate() > 0;
        }
    }

    // Lấy danh sách bảo hành đang xử lý
    public List<BaoHanhDTO> layDanhSachBaoHanhDangXuLy() throws SQLException {
        List<BaoHanhDTO> list = new ArrayList<>();
        String sql = """
                SELECT MaBaoHanh, MaIMEI, MaSP, MaHoaDon, MaNVTiepNhan, MaNVXuLy,
                    NgayTiepNhan, NgayHenTra, NgayTra, MoTaLoi, HinhThucXuLy,
                    KetQuaXuLy, ChiPhiPhatSinh, TrangThai
                FROM BAOHANH
                WHERE TrangThai IN (N'DangXuLy', N'DaGuiHang', N'ChoLinhKien')
                ORDER BY NgayTiepNhan DESC
                """;

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                BaoHanhDTO bh = new BaoHanhDTO();
                bh.setMaBaoHanh(rs.getInt("MaBaoHanh"));
                bh.setMaIMEI(rs.getObject("MaIMEI", Integer.class));
                bh.setMaSP(rs.getInt("MaSP"));
                bh.setMaHoaDon(rs.getInt("MaHoaDon"));
                bh.setMaNVTiepNhan(rs.getObject("MaNVTiepNhan", Integer.class));
                bh.setMaNVXuLy(rs.getObject("MaNVXuLy", Integer.class));
                bh.setNgayTiepNhan(rs.getDate("NgayTiepNhan"));
                bh.setNgayHenTra(rs.getDate("NgayHenTra"));
                bh.setNgayTra(rs.getDate("NgayTra"));
                bh.setMoTaLoi(rs.getString("MoTaLoi"));
                bh.setHinhThucXuLy(rs.getString("HinhThucXuLy"));
                bh.setKetQuaXuLy(rs.getString("KetQuaXuLy"));
                bh.setChiPhiPhatSinh(rs.getBigDecimal("ChiPhiPhatSinh"));
                bh.setTrangThai(rs.getString("TrangThai"));

                list.add(bh);
            }
        }
        return list;
    }

    // Bạn có thể thêm các hàm khác:
    // - layBaoHanhTheoMa(int ma)
    // - layBaoHanhTheoMaHoaDon(int maHD)
    // - traHang(int maBaoHanh, Date ngayTra, String ketQua)
}