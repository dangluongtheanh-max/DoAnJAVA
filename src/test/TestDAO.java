package test;

import DAO.SanPhamDAO;
public class TestDAO {
    public static void main(String[] args) {
        SanPhamDAO dao = new SanPhamDAO();
        dao.getAll().forEach(sp ->
            System.out.println(sp.getTenSP())
        );
    }
}
