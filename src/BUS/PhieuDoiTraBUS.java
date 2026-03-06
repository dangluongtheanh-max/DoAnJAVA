package BUS;
import java.util.ArrayList;
import DAO.PhieuDoiTraDAO;
import DTO.PhieuDoiTraDTO;

public class PhieuDoiTraBUS {

    private ArrayList<PhieuDoiTraDTO> list = new ArrayList<>();

    public ArrayList<PhieuDoiTraDTO> getAll() {
        return list;
    }

    public boolean add(PhieuDoiTraDTO dto) {
        if (PhieuDoiTraDAO.insert(dto)) {
            list.add(dto);
            return true;
        }
        return false;
    }

    public PhieuDoiTraDTO getById(int id) {
        for (PhieuDoiTraDTO p : list) {
            if (p.getMaPhieuDoiTra() == id) return p;
        }
        return null;
    }
}
