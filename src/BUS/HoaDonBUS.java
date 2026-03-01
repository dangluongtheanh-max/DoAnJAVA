package BUS;


import DAO.HoaDonDAO;
import DAO.ChiTietHoaDonDAO;
import DTO.ChiTietHoaDonDTO;
import DTO.HoaDonDTO;

import java.math.BigDecimal;
import java.util.ArrayList;

public class HoaDonBUS {
    private HoaDonDAO hoaDonDAO = new HoaDonDAO();
    private ChiTietHoaDonDAO ctHoaDonDAO = new ChiTietHoaDonDAO();

    public int taoHoaDon(int maCH,int maNV){
        return hoaDonDAO.taoHoaDon(maCH,maNV);
    }
    public HoaDonDTO timHoaDonTheoMa(int maHD){
        return hoaDonDAO.timHoaDonTheoMa(maHD);
    }
    public ArrayList<ChiTietHoaDonDTO> getCTHoaDon(int maHD) {
        return ctHoaDonDAO.getByHoaDon(maHD);
    }
    
    public void themSPVaoHoaDon(ChiTietHoaDonDTO ctHoaDonDTO){
        ArrayList<ChiTietHoaDonDTO> dsCTHoaDon = ctHoaDonDAO.getByHoaDon(ctHoaDonDTO.getMaHD());

        boolean spDaTonTai = false;
        for(ChiTietHoaDonDTO item : dsCTHoaDon){
            if(item.getMaSP() == ctHoaDonDTO.getMaSP())
                ctHoaDonDAO.tangSoLuongSP(ctHoaDonDTO.getMaHD(), ctHoaDonDTO.getMaSP(), ctHoaDonDTO.getDonGia());
            
            spDaTonTai = true;
            break;
        }
        if(!spDaTonTai)
            ctHoaDonDAO.themSanPham(ctHoaDonDTO);

        ctHoaDonDAO.capNhatTongTien(ctHoaDonDTO.getMaHD());
    }

    public void giamSoLuongSP(int maHD,int maSP,BigDecimal donGia){
        ctHoaDonDAO.giamSoLuongSP(maHD, maSP, donGia);
        ctHoaDonDAO.capNhatTongTien(maHD);
    }

    public void xoaSanPham(int maHD,int maSP,BigDecimal donGia){
        ctHoaDonDAO.xoaSanPham(maHD, maSP);
        ctHoaDonDAO.capNhatTongTien(maHD);
    }

    public void huyHoaDon(int maHD){
        ctHoaDonDAO.xoaCTHoaDon(maHD);
        hoaDonDAO.capNhatTrangThaiHoaDon(maHD, 2); //2 = hủy
    }
    
    public void thanhToanHoaDon(int maHD) {
        hoaDonDAO.capNhatTrangThaiHoaDon(maHD, 1); // 1 = đã thanh toán
    }


}

    