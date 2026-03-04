package DAO;

import DTO.PhieuDoiTraDTO;
import java.sql.*;
import java.util.ArrayList;

public class PhieuDoiTraDAO {

    public static ArrayList<PhieuDoiTraDTO> getAll() {
        ArrayList<PhieuDoiTraDTO> list = new ArrayList<>();

        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT * FROM PhieuDoiTra";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                PhieuDoiTraDTO p = new PhieuDoiTraDTO(
                        rs.getInt("MaPhieuDT"),
                        rs.getInt("MaHoaDon"),
                        rs.getDate("NgayDoiTra").toLocalDate(),
                        rs.getString("LyDo"),
                        rs.getString("HinhThuc"));
                list.add(p);
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static boolean insert(PhieuDoiTraDTO dto) {
        try {
            Connection cn = DBConnection.getConnection();
            String sql = "INSERT INTO PhieuDoiTra VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst = cn.prepareStatement(sql);

            pst.setInt(1, dto.getMaPhieuDoiTra());
            pst.setInt(2, dto.getMaHoaDon());
            pst.setDate(3, java.sql.Date.valueOf(dto.getNgayDoiTra())); // CHỖ NÀY
            pst.setString(4, dto.getLyDo());
            pst.setString(5, dto.getHinhThuc());

            return pst.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
