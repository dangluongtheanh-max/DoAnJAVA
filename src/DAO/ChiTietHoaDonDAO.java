package DAO;

import DTO.ChiTietHoaDonDTO;
import util.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;

public class ChiTietHoaDonDAO {

    public ArrayList<ChiTietHoaDonDTO> getByHoaDon(int maHD) {
        ArrayList<ChiTietHoaDonDTO> dsCTHoaDon = new ArrayList<>();
        String sql = "SELECT * FROM CT_HoaDon WHERE Ma_HD = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, maHD);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ChiTietHoaDonDTO ct = new ChiTietHoaDonDTO();
                ct.setMaHD(rs.getInt("Ma_HD"));
                ct.setMaSP(rs.getInt("Ma_SP"));
                ct.setMaImei(rs.getInt("Ma_Imei"));
                ct.setSoLuong(rs.getInt("So_Luong"));
                ct.setThanhTien(rs.getBigDecimal("Thanh_Tien"));

                dsCTHoaDon.add(ct);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsCTHoaDon;
    }

     public void xoaCTHoaDon(int maHD){
        String sql = "DELETE FROM CT_HoaDon WHERE Ma_HD = ? "; //? = 1001

        try(Connection connect = DBConnection.getConnection();
            PreparedStatement ps = connect.prepareStatement(sql)){
            
            ps.setInt(1, maHD); // xóa tất cả các dòng có mã hđ 1001 trong sql
            ps.executeUpdate();

            }catch(Exception e){
                e.printStackTrace();
            }
    }

    public void themSanPham(ChiTietHoaDonDTO ctHoaDon){
        String sql = """
            INSERT INTO CT_Hoa_Don
            (Ma_HD,Ma_SP,Ma_Imei,SoLuong,DonGia,ThanhTien,TrangThai)
            VALUES(?,?,?,?,?,?,?)
            """;
            try(Connection connect = DBConnection.getConnection();
                PreparedStatement ps = connect.prepareStatement(sql)){

                ps.setInt(1, ctHoaDon.getMaHD());
                ps.setInt(2, ctHoaDon.getMaSP());
                if(ctHoaDon.getMaImei() == null)
                    ps.setNull(3, ctHoaDon.getMaImei());
                else
                    ps.setInt(3, ctHoaDon.getMaImei());

                ps.setInt(4, ctHoaDon.getSoLuong());
                ps.setBigDecimal(5, ctHoaDon.getDonGia());
                ps.setBigDecimal(6, ctHoaDon.getThanhTien());
                ps.setInt(7, ctHoaDon.getTrangThai());

                ps.executeUpdate();

            }catch(Exception e){
                e.printStackTrace();
            }
    }
    
    public void xoaSanPham(int maHD, int maSP){
        String sql = "DELETE FROM CT_HoaDon WHERE Ma_HD = ? AND Ma_SP = ?";

        try(Connection connect = DBConnection.getConnection();
            PreparedStatement ps = connect.prepareStatement(sql)){

                ps.setInt(1, maHD);
                ps.setInt(2, maSP);
                ps.executeUpdate();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void tangSoLuongSP(int maHD,int MaSP,BigDecimal donGia){
        String sql = """
                UPDATE CT_HoaDon
                SET SoLuong = SoLuong + 1,
                    ThanhTien = ThanhTien + ?
                WHERE Ma_HD = ? AND Ma_SP = ?     
                """; //vì Ma_HD và Ma_SP là khóa chính trong SQL nên sử dụng chúng để truy vấn đến 1 hàng của CTHĐ
        try(Connection connect = DBConnection.getConnection();
            PreparedStatement ps = connect.prepareStatement(sql)){
            
            ps.setInt(2,maHD);
            ps.setInt(3, MaSP);
            
            ps.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
        }        
    }
    
    public void giamSoLuongSP(int maHD, int maSP, BigDecimal donGia) {    
        String sql = """
            UPDATE CT_HoaDon
            SET SoLuong = SoLuong - 1,
                ThanhTien = ThanhTien - ?
            WHERE Ma_HD = ? AND Ma_SP = ? AND SoLuong > 1
        """;
    
        try (Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {
    
            ps.setBigDecimal(1, donGia);
            ps.setInt(2, maHD);
            ps.setInt(3, maSP);
    
            ps.executeUpdate();
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    public void capNhatTongTien(int maHD) {
        String sql = """
            UPDATE HoaDon
            SET TongTienHang = (
                SELECT ISNULL(SUM(ThanhTien), 0)
                FROM CT_HoaDon
                WHERE Ma_HD = ?
            ),
            TongThanhToan = TongTienHang - GiamGia + ThueVAT
            WHERE Ma_HD = ?
        """;
    
        try (Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {
    
            ps.setInt(1, maHD);
            ps.setInt(2, maHD);
            ps.executeUpdate();
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
    

    