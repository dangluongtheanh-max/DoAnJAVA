package DAO;

import DTO.HoaDonDTO;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;

public class HoaDonDAO {

    public ArrayList<HoaDonDTO> getAll() {
        ArrayList<HoaDonDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM HoaDon";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                HoaDonDTO hd = new HoaDonDTO();

                hd.setMaHD(rs.getInt("Ma_HD"));
                hd.setMaKH(rs.getInt("Ma_KH"));
                hd.setMaNV(rs.getInt("Ma_NV"));
                hd.setNgayLap(rs.getTimestamp("NgayLap").toLocalDateTime());
                hd.setTongThanhToan(rs.getBigDecimal("Tong_Thanh_Toan"));
                hd.setTrangThai(rs.getInt("Trang_Thai"));

                list.add(hd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public int taoHoaDon(int maCH, int maNV){
        String sql = """
            INSERT INTO HoaDon
            (Ma_CH,Ma_NV,NgayLap,
            TongTienHang,GiamGia,ThueVAT,TongThanhToam,TrangThai)
            VALUES(?,?,GETDATE(),0,0,0,0,0)
            """;

            try(Connection connect = DBConnection.getConnection();
                PreparedStatement ps = connect.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){ // trong SQL có lệnh để sinh số ngẫu nhiên cho mã HĐ, đây chính là lệnh để xin số đó từ SQL

                    ps.setInt(1, maCH);
                    ps.setInt(2, maNV);
                    ps.executeUpdate(); //GỬI câu lệnh INSERT từ Java sang SQL Server để THỰC THI

                    ResultSet rs = ps.getGeneratedKeys(); //Lấy về KHÓA TỰ SINH (ID tự tăng) do SQL Server vừa tạo
                    if(rs.next()){
                        return rs.getInt(1);
                    }
                } catch(Exception e){
                    e.printStackTrace();
                }
                return -1;
    } 
    public HoaDonDTO timHoaDonTheoMa(int maHD){
        String sql = "SELECT * FROM HoaDon WHERE Ma_HD = ?";

        try(Connection connect = DBConnection.getConnection();
            PreparedStatement ps = connect.prepareStatement(sql)){
            ps.setInt(1, maHD); // gán mã HĐ đã nhập vào cho dấu ?
            ResultSet rs = ps.executeQuery();
            
            if(rs.next()){
                HoaDonDTO hd = new HoaDonDTO();
                hd.setMaHD(rs.getInt("Ma_HD"));
                hd.setMaCH(rs.getInt("Ma_CH"));
                hd.setMaKH(rs.getInt("Ma_KH"));
                hd.setMaNV(rs.getInt("Ma_NV"));
                hd.setNgayLap(rs.getTimestamp("NgayLap").toLocalDateTime());
                hd.setTongTienHang(rs.getBigDecimal("TongTienHang"));
                hd.setGiamGia(rs.getBigDecimal("GiamGia"));
                hd.setThueVAT(rs.getBigDecimal("ThueVAT"));
                hd.setTongThanhToan(rs.getBigDecimal("TongThanhToan"));
                hd.setTrangThai(rs.getInt("TrangThai"));
                return hd;
            }
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
    }

    public void capNhatTrangThaiHoaDon(int maHD,int trangThai){
        String sql = "UPDATE HoaDon SET TrangThai = ? WHERE Ma_HD = ? ";

        try(Connection connect = DBConnection.getConnection();
            PreparedStatement ps = connect.prepareStatement(sql)){
            ps.setInt(1, trangThai);
            ps.setInt(2, maHD);
            ps.executeUpdate();    
            }catch(Exception e){
                e.printStackTrace();
            }
    } 
}
