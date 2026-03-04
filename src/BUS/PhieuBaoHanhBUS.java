package BUS;
import java.util.ArrayList;
import DTO.PhieuBaoHanhDTO;
import DAO.PhieuBaoHanhDAO;

public class PhieuBaoHanhBUS {

    private PhieuBaoHanhDAO dao = new PhieuBaoHanhDAO();
    private ArrayList<PhieuBaoHanhDTO> list;

    public PhieuBaoHanhBUS() {
        list = PhieuBaoHanhDAO.getAll();   // load dữ liệu lúc khởi tạo
    }

    public ArrayList<PhieuBaoHanhDTO> getAll() {
        return list;
    }

    public boolean add(PhieuBaoHanhDTO dto) {
        if (PhieuBaoHanhDAO.insert(dto)) {
            list.add(dto);
            return true;
        }
        return false;
    }

    // Tìm theo mã
    public PhieuBaoHanhDTO getById(int maPBH) {
        for (PhieuBaoHanhDTO p : list) {
            if (p.getMaPhieuBH() == maPBH) {
                return p;
            }
        }
        return null;
    }
}
