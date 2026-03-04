package DAO;

import DTO.ThongSoKyThuatDTO;
import Utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ThongSoKyThuatDAO {

    public ThongSoKyThuatDTO getByMaSP(int maSP) {
        String sql = "SELECT * FROM ThongSoKyThuat WHERE Ma_SP = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, maSP);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                ThongSoKyThuatDTO ts = new ThongSoKyThuatDTO();
                ts.setMaSP(rs.getInt("Ma_SP"));
                ts.setCpu(rs.getString("CPU"));
                ts.setRam(rs.getString("RAM"));
                ts.setSsd(rs.getString("SSD"));
                ts.setVga(rs.getString("VGA"));
                ts.setMoTa(rs.getString("MoTa"));
                ts.setTrangThai(rs.getString("Trang_Thai"));
                return ts;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insert(ThongSoKyThuatDTO ts) {
        String sql = """
            INSERT INTO ThongSoKyThuat(Ma_SP, CPU, RAM, SSD, VGA, MoTa, Trang_Thai)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, ts.getMaSP());
            ps.setString(2, ts.getCpu());
            ps.setString(3, ts.getRam());
            ps.setString(4, ts.getSsd());
            ps.setString(5, ts.getVga());
            ps.setString(6, ts.getMoTa());
            ps.setString(7, ts.getTrangThai());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(ThongSoKyThuatDTO ts) {
        String sql = """
            UPDATE ThongSoKyThuat
            SET CPU=?, RAM=?, SSD=?, VGA=?, MoTa=?, Trang_Thai=?
            WHERE Ma_SP=?
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, ts.getCpu());
            ps.setString(2, ts.getRam());
            ps.setString(3, ts.getSsd());
            ps.setString(4, ts.getVga());
            ps.setString(5, ts.getMoTa());
            ps.setString(6, ts.getTrangThai());
            ps.setInt(7, ts.getMaSP());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteByMaSP(int maSP) {
        String sql = "DELETE FROM ThongSoKyThuat WHERE Ma_SP=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, maSP);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Thêm vào trong class ThongSoKyThuatDAO
    public boolean isExist(int maSP) {
        String sql = "SELECT 1 FROM ThongSoKyThuat WHERE Ma_SP = ?";
        try (Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, maSP);
            ResultSet rs = ps.executeQuery();
            return rs.next(); // Trả về true nếu tìm thấy
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
