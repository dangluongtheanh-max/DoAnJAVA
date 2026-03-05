// DoiTraDAO.java
package DAO;

import DTO.DoiTraDTO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import util.DBConnection;

public class DoiTraDAO {

    // Thêm yêu cầu đổi trả mới
    public boolean themDoiTra(DoiTraDTO dt) throws SQLException {
        String sql = """
                INSERT INTO DOITRA (
                    MaHoaDon, MaSP, MaIMEI, SoLuongTra,
                    LyDo, MaNV, NgayYeuCau, TrangThai, GhiChu
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, dt.getMaHoaDon());
            ps.setInt(2, dt.getMaSP());
            ps.setObject(3, dt.getMaIMEI(), Types.INTEGER);
            ps.setInt(4, dt.getSoLuongTra());
            ps.setString(5, dt.getLyDo());
            ps.setInt(6, dt.getMaNV());
            ps.setDate(7, dt.getNgayYeuCau() != null ? dt.getNgayYeuCau() : new Date(System.currentTimeMillis()));
            ps.setString(8, dt.getTrangThai() != null ? dt.getTrangThai() : "DangXuLy");
            ps.setString(9, dt.getGhiChu());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        dt.setMaDoiTra(rs.getInt(1));
                    }
                }
            }
            return affected > 0;
        }
    }

    // Cập nhật trạng thái (thường là hoàn thành đổi trả)
    public boolean capNhatDoiTra(DoiTraDTO dt) throws SQLException {
        String sql = """
                UPDATE DOITRA SET
                    MaIMEI        = ?,
                    SoLuongTra    = ?,
                    LyDo          = ?,
                    TrangThai     = ?,
                    GhiChu        = ?
                WHERE MaDoiTra = ?
                """;

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, dt.getMaIMEI(), Types.INTEGER);
            ps.setInt(2, dt.getSoLuongTra());
            ps.setString(3, dt.getLyDo());
            ps.setString(4, dt.getTrangThai());
            ps.setString(5, dt.getGhiChu());
            ps.setInt(6, dt.getMaDoiTra());

            return ps.executeUpdate() > 0;
        }
    }

    // Hoàn thành đổi trả (thường dùng riêng)
    public boolean hoanThanhDoiTra(int maDoiTra, String ghiChu) throws SQLException {
        String sql = """
                UPDATE DOITRA
                SET TrangThai = N'HoanThanh',
                    GhiChu    = ?
                WHERE MaDoiTra = ?
                  AND TrangThai = N'DangXuLy'
                """;

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, ghiChu);
            ps.setInt(2, maDoiTra);

            return ps.executeUpdate() > 0;
        }
    }

    // Lấy danh sách yêu cầu đổi trả đang xử lý
    public List<DoiTraDTO> layDanhSachDangXuLy() throws SQLException {
        List<DoiTraDTO> list = new ArrayList<>();
        String sql = """
                SELECT MaDoiTra, MaHoaDon, MaSP, MaIMEI, SoLuongTra,
                       LyDo, MaNV, NgayYeuCau, TrangThai, GhiChu
                FROM DOITRA
                WHERE TrangThai = N'DangXuLy'
                ORDER BY NgayYeuCau DESC
                """;

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                DoiTraDTO dt = new DoiTraDTO();
                dt.setMaDoiTra(rs.getInt("MaDoiTra"));
                dt.setMaHoaDon(rs.getInt("MaHoaDon"));
                dt.setMaSP(rs.getInt("MaSP"));
                dt.setMaIMEI(rs.getObject("MaIMEI", Integer.class));
                dt.setSoLuongTra(rs.getInt("SoLuongTra"));
                dt.setLyDo(rs.getString("LyDo"));
                dt.setMaNV(rs.getInt("MaNV"));
                dt.setNgayYeuCau(rs.getDate("NgayYeuCau"));
                dt.setTrangThai(rs.getString("TrangThai"));
                dt.setGhiChu(rs.getString("GhiChu"));

                list.add(dt);
            }
        }
        return list;
    }

    // Có thể thêm:
    // layTheoMaHoaDon(int maHoaDon)
    // layTheoMa(int maDoiTra)
}