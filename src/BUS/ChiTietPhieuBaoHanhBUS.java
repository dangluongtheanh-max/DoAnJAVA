package BUS;
import DAO.ChiTietPhieuBaoHanhDAO;
import DTO.ChiTietPhieuBaoHanhDTO;
import java.util.ArrayList;

public class ChiTietPhieuBaoHanhBUS {

    private ArrayList<ChiTietPhieuBaoHanhDTO> list = new ArrayList<>();

    public ArrayList<ChiTietPhieuBaoHanhDTO> getAll() {
        return list;
    }

    public boolean add(ChiTietPhieuBaoHanhDTO dto) {
        if (ChiTietPhieuBaoHanhDAO.insert(dto)) {
            list.add(dto);
            return true;
        }
        return false;
    }

    public ArrayList<ChiTietPhieuBaoHanhDTO> getByMaPhieu(int maPBH) {
        ArrayList<ChiTietPhieuBaoHanhDTO> result = new ArrayList<>();
        for (ChiTietPhieuBaoHanhDTO c : list) {
            if (c.getMaPhieuBaoHanh() == maPBH) {
                result.add(c);
            }
        }
        return result;
    }
}

