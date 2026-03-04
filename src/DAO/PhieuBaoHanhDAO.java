package DAO;

import DTO.PhieuBaoHanhDTO;
import java.sql.*;
import java.util.ArrayList;

public class PhieuBaoHanhDAO {

    // Lấy toàn bộ phiếu bảo hành
    public static ArrayList<PhieuBaoHanhDTO> getAll() {
        ArrayList<PhieuBaoHanhDTO> list = new ArrayList<>();

        try (Connection cn = DBConnection.getConnection();
                PreparedStatement pst = cn.prepareStatement("SELECT * FROM PhieuBaoHanh");
                ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                PhieuBaoHanhDTO p = new PhieuBaoHanhDTO(
                        rs.getInt("MaPhieuBH"),
                        rs.getInt("MaKhachHang"),
                        rs.getDate("NgayLap"),
                        rs.getString("TrangThai"));
                list.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Thêm phiếu bảo hành
    public static boolean insert(PhieuBaoHanhDTO dto) {
        try (Connection cn = DBConnection.getConnection();
                PreparedStatement pst = cn.prepareStatement(
                        "INSERT INTO PhieuBaoHanh VALUES (?, ?, ?, ?)")) {

            pst.setInt(1, dto.getMaPhieuBH());
            pst.setInt(2, dto.getMaKhachHang());
            pst.setDate(3, new java.sql.Date(dto.getNgayLap().getTime()));
            pst.setString(4, dto.getTrangThai());

            return pst.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Tìm theo mã
    public static PhieuBaoHanhDTO getById(int maPBH) {
        try (Connection cn = DBConnection.getConnection();
                PreparedStatement pst = cn.prepareStatement(
                        "SELECT * FROM PhieuBaoHanh WHERE MaPhieuBH = ?")) {

            pst.setInt(1, maPBH);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return new PhieuBaoHanhDTO(
                        rs.getInt("MaPhieuBH"),
                        rs.getInt("MaKhachHang"),
                        rs.getDate("NgayLap"),
                        rs.getString("TrangThai"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
