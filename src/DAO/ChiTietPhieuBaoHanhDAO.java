package DAO;
import DTO.ChiTietPhieuBaoHanhDTO;
import java.sql.*;
import java.util.ArrayList;


public class ChiTietPhieuBaoHanhDAO {

    public static ArrayList<ChiTietPhieuBaoHanhDTO> getByPhieu(int maPhieuBH) {
        ArrayList<ChiTietPhieuBaoHanhDTO> list = new ArrayList<>();

        try {
            Connection cn = DBConnection.getConnection();
            String sql = "SELECT * FROM ChiTietPhieuBaoHanh WHERE MaPhieuBH = ?";
            PreparedStatement pst = cn.prepareStatement(sql);
            pst.setInt(1, maPhieuBH);

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                ChiTietPhieuBaoHanhDTO c = new ChiTietPhieuBaoHanhDTO(
                    rs.getInt("MaCTPBH"),
                    rs.getInt("MaPhieuBH"),
                    rs.getInt("MaSanPham"),
                    rs.getString("Loi"),
                    rs.getString("KetQua")
                );
                list.add(c);
            }
            cn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static boolean insert(ChiTietPhieuBaoHanhDTO dto) {
        try {
            Connection cn = DBConnection.getConnection();
            String sql = "INSERT INTO ChiTietPhieuBaoHanh VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst = cn.prepareStatement(sql);

            pst.setInt(1, dto.getMaChiTiet());
            pst.setInt(2, dto.getMaPhieuBaoHanh());
            pst.setInt(3, dto.getMaSanPham());
            pst.setString(4, dto.getMoTaLoi());
            pst.setString(5, dto.getHuongXuLy());

            return pst.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}

