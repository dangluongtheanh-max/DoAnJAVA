package DAO;

import DTO.ThongKe.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.*;
import java.util.Date;
import util.DBConnection;

public class ThongKeDAO {

    /* =====================================================
       DOANH THU THEO NĂM
    ===================================================== */
    public ArrayList<ThongKeDoanhThuDTO> thongKeDoanhThuTheoNam() {
        ArrayList<ThongKeDoanhThuDTO> list = new ArrayList<>();

        String sql = """
            SELECT YEAR(NgayLap) AS Nam,
                   SUM(ISNULL(Tong_Thanh_Toan,0)) AS DoanhThu
            FROM HoaDon
            GROUP BY YEAR(NgayLap)
            ORDER BY Nam
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int nam = rs.getInt("Nam");
                long doanhThu = rs.getLong("DoanhThu");
                long von = tinhVonTrongNam(conn, nam);
                list.add(new ThongKeDoanhThuDTO(
                        String.valueOf(nam),
                        von,
                        doanhThu,
                        doanhThu - von
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /* =====================================================
       DOANH THU THEO THÁNG TRONG NĂM
    ===================================================== */
    public ArrayList<ThongKeDoanhThuDTO> thongKeDoanhThuTheoThang(int nam) {
        ArrayList<ThongKeDoanhThuDTO> list = new ArrayList<>();

        for (int thang = 1; thang <= 12; thang++) {
            long doanhThu = getDoanhThuTheoThang(nam, thang);
            long von = tinhVonTheoThang(nam, thang);

            list.add(new ThongKeDoanhThuDTO(
                    String.valueOf(thang),
                    von,
                    doanhThu,
                    doanhThu - von
            ));
        }
        return list;
    }

    /* =====================================================
       DOANH THU TỪNG NGÀY TRONG THÁNG
    ===================================================== */
    public ArrayList<ThongKeDoanhThuDTO> thongKeDoanhThuTungNgayTrongThang(int nam, int thang) {
        ArrayList<ThongKeDoanhThuDTO> list = new ArrayList<>();
        int soNgay = YearMonth.of(nam, thang).lengthOfMonth();

        for (int ngay = 1; ngay <= soNgay; ngay++) {
            long doanhThu = getDoanhThuTheoNgay(nam, thang, ngay);
            long von = tinhVonTheoNgay(nam, thang, ngay);

            list.add(new ThongKeDoanhThuDTO(
                    String.valueOf(ngay),
                    von,
                    doanhThu,
                    doanhThu - von
            ));
        }
        return list;
    }

    /* =====================================================
       DOANH THU TỪ NGÀY ĐẾN NGÀY
    ===================================================== */
    public ArrayList<ThongKeDoanhThuDTO> thongKeDoanhThuTuNgayDenNgay(Date from, Date to) {
        ArrayList<ThongKeDoanhThuDTO> list = new ArrayList<>();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        Calendar cal = Calendar.getInstance();
        cal.setTime(from);

        while (!cal.getTime().after(to)) {
            Date ngay = cal.getTime();
            long doanhThu = getDoanhThuTheoNgay(ngay);
            long von = tinhVonTheoNgay(ngay);

            list.add(new ThongKeDoanhThuDTO(
                    df.format(ngay),
                    von,
                    doanhThu,
                    doanhThu - von
            ));
            cal.add(Calendar.DATE, 1);
        }
        return list;
    }

    /* =====================================================
       THỐNG KÊ SẢN PHẨM BÁN
    ===================================================== */
    public ArrayList<ThongKeSanPhamBanDTO> thongKeSanPhamBanTrongKhoangThoiGian(Date from, Date to) {
        ArrayList<ThongKeSanPhamBanDTO> list = new ArrayList<>();

        String sql = """
            SELECT SP.Ma_SP, SP.Ten_SP,
                   SUM(CT.So_Luong) AS SoLuong,
                   COUNT(DISTINCT HD.Ma_HD) AS SoDon,
                   SUM(ISNULL(CT.Thanh_Tien,0)) AS DoanhThu
            FROM CT_HoaDon CT
            JOIN HoaDon HD ON CT.Ma_HD = HD.Ma_HD
            JOIN SanPham SP ON CT.Ma_SP = SP.Ma_SP
            WHERE CONVERT(date, HD.NgayLap) BETWEEN ? AND ?
            GROUP BY SP.Ma_SP, SP.Ten_SP
            ORDER BY SoLuong DESC
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, new java.sql.Date(from.getTime()));
            ps.setDate(2, new java.sql.Date(to.getTime()));

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new ThongKeSanPhamBanDTO(
                        rs.getInt("Ma_SP"),
                        rs.getString("Ten_SP"),
                        rs.getInt("SoLuong"),
                        rs.getInt("SoDon"),
                        rs.getLong("DoanhThu")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /* =====================================================
       THỐNG KÊ THEO LOẠI SẢN PHẨM
    ===================================================== */
    public ArrayList<ThongKeTheLoaiBanDTO> thongKeLoaiSanPhamTrongKhoangThoiGian(Date from, Date to) {
        ArrayList<ThongKeTheLoaiBanDTO> list = new ArrayList<>();

        String sql = """
            SELECT SP.Loai_SP,
                   SUM(CT.So_Luong) AS SoLuong,
                   COUNT(DISTINCT HD.Ma_HD) AS SoDon,
                   COUNT(DISTINCT SP.Ma_SP) AS SoSP,
                   SUM(ISNULL(CT.Thanh_Tien,0)) AS DoanhThu
            FROM CT_HoaDon CT
            JOIN HoaDon HD ON CT.Ma_HD = HD.Ma_HD
            JOIN SanPham SP ON CT.Ma_SP = SP.Ma_SP
            WHERE CONVERT(date, HD.NgayLap) BETWEEN ? AND ?
            GROUP BY SP.Loai_SP
            ORDER BY SoLuong DESC
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, new java.sql.Date(from.getTime()));
            ps.setDate(2, new java.sql.Date(to.getTime()));

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new ThongKeTheLoaiBanDTO(
                        rs.getString("Loai_SP"),
                        rs.getInt("SoLuong"),
                        rs.getInt("SoDon"),
                        rs.getInt("SoSP"),
                        rs.getLong("DoanhThu")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /* =====================================================
       THỐNG KÊ HÓA ĐƠN
    ===================================================== */
    public ArrayList<ThongKeHoaDonBanDTO> thongKeHoaDonTrongKhoangThoiGian(Date from, Date to) {
        ArrayList<ThongKeHoaDonBanDTO> list = new ArrayList<>();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        Calendar cal = Calendar.getInstance();
        cal.setTime(from);

        while (!cal.getTime().after(to)) {
            Date ngay = cal.getTime();

            String sql = """
                SELECT COUNT(DISTINCT HD.Ma_HD) AS SoDon,
                       ISNULL(SUM(CT.So_Luong),0) AS SoSP,
                       COUNT(DISTINCT CT.Ma_SP) AS SoLoai,
                       ISNULL(SUM(CT.Thanh_Tien),0) AS DoanhThu
                FROM HoaDon HD
                LEFT JOIN CT_HoaDon CT ON HD.Ma_HD = CT.Ma_HD
                WHERE CONVERT(date, HD.NgayLap) = ?
            """;

            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setDate(1, new java.sql.Date(ngay.getTime()));
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    list.add(new ThongKeHoaDonBanDTO(
                            df.format(ngay),
                            rs.getInt("SoDon"),
                            rs.getInt("SoSP"),
                            rs.getInt("SoLoai"),
                            rs.getLong("DoanhThu")
                    ));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            cal.add(Calendar.DATE, 1);
        }
        return list;
    }

    /* =====================================================
       HÀM PHỤ
    ===================================================== */
    private long tinhVonTrongNam(Connection conn, int nam) throws SQLException {
        String sql = "SELECT SUM(Tong_Tien) FROM PhieuNhap WHERE YEAR(Ngay_Nhap)=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, nam);
        ResultSet rs = ps.executeQuery();
        return rs.next() ? rs.getLong(1) : 0;
    }

    private long tinhVonTheoThang(int nam, int thang) {
        return tinhVon("YEAR(Ngay_Nhap)=? AND MONTH(Ngay_Nhap)=?", nam, thang);
    }

    private long tinhVonTheoNgay(int nam, int thang, int ngay) {
        return tinhVon("YEAR(Ngay_Nhap)=? AND MONTH(Ngay_Nhap)=? AND DAY(Ngay_Nhap)=?", nam, thang, ngay);
    }

    private long tinhVonTheoNgay(Date ngay) {
        return tinhVon("CONVERT(date, Ngay_Nhap)=?", new java.sql.Date(ngay.getTime()));
    }

    private long tinhVon(String condition, Object... params) {
        String sql = "SELECT SUM(Tong_Tien) FROM PhieuNhap WHERE " + condition;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getLong(1) : 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private long getDoanhThuTheoThang(int nam, int thang) {
        return getDoanhThu("YEAR(NgayLap)=? AND MONTH(NgayLap)=?", nam, thang);
    }

    private long getDoanhThuTheoNgay(int nam, int thang, int ngay) {
        return getDoanhThu("YEAR(NgayLap)=? AND MONTH(NgayLap)=? AND DAY(NgayLap)=?", nam, thang, ngay);
    }

    private long getDoanhThuTheoNgay(Date ngay) {
        return getDoanhThu("CONVERT(date, NgayLap)=?", new java.sql.Date(ngay.getTime()));
    }

    private long getDoanhThu(String condition, Object... params) {
        String sql = "SELECT SUM(Tong_Thanh_Toan) FROM HoaDon WHERE " + condition;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getLong(1) : 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}