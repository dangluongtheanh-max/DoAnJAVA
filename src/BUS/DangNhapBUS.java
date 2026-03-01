package BUS;

import DAO.NhanVienDAO;
import DTO.NhanVienDTO;

public class DangNhapBUS{
    private NhanVienDAO nvDAO = new NhanVienDAO();
    
    public NhanVienDTO DangNhap(String username,String password){
        NhanVienDTO nv = nvDAO.findByUsername(username);

        if(nv == null) return null;
        if(!(nv.getPassword().equals(password))) return null;
        if(nv.getTrangThai() == 0) return null;

        return nv;
    }
    public boolean Admin(NhanVienDTO nv){
        return nv.getVaiTro().equalsIgnoreCase("Admin");
    }    
    public boolean NhanVien(NhanVienDTO nv){
        return nv.getVaiTro().equalsIgnoreCase("NhanVien");
    }
}