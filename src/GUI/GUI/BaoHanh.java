package GUI;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import util.DBConnection;
/**
 * Panel Quản lý Bảo hành
 * Bảng: [LAPTOPSTORE].[dbo].[BAOHANH]
 * Cột : MaBaoHanh, MaIMEI, MaSP, MaHoaDon, MaNVTiepNhan, MaNVXuLy,
 *       NgayTiepNhan, NgayHenTra, NgayTra, MoTaLoi, HinhThucXuLy,
 *       KetQuaXuLy, ChiPhiPhatSinh, TrangThai
 */
public class BaoHanh extends JPanel {

    // ── Màu ──────────────────────────────────────────────────────────────────
    private static final Color PRIMARY      = new Color(21, 101, 192);
    private static final Color PRIMARY_DARK = new Color(10,  60, 130);
    private static final Color ACCENT       = new Color(0,  188, 212);
    private static final Color CONTENT_BG   = new Color(236, 242, 250);
    private static final Color SUCCESS      = new Color(46,  160,  67);
    private static final Color DANGER       = new Color(211,  47,  47);
    private static final Color WARNING      = new Color(245, 124,   0);
    private static final Color ROW_ALT      = new Color(245, 249, 255);

    // ── Font ─────────────────────────────────────────────────────────────────
    private static final Font F_TITLE  = new Font("Segoe UI", Font.BOLD,  20);
    private static final Font F_LABEL  = new Font("Segoe UI", Font.BOLD,  12);
    private static final Font F_INPUT  = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Font F_BTN    = new Font("Segoe UI", Font.BOLD,  12);
    private static final Font F_TABLE  = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Font F_HEADER = new Font("Segoe UI", Font.BOLD,  12);

    // Giá trị đúng theo CHECK constraint trong DB
    private static final String[] HINH_THUC_XL = {
        "SuaChuaTaiCho", "GuiHang", "ThayTheMoi"
    };
    private static final String[] TRANG_THAI = {
        "DangXuLy", "DaGuiHang", "ChoLinhKien", "DaTraKhach"
    };
    private static final String[] FILTER_TT = {
        "Tất cả", "DangXuLy", "DaGuiHang", "ChoLinhKien", "DaTraKhach"
    };

    // Tên cột hiển thị trên bảng
    private static final String[] COL_NAMES = {
        "Mã BH", "Mã IMEI", "Mã SP", "Mã Hóa đơn",
        "NV Tiếp nhận", "NV Xử lý",
        "Ngày tiếp nhận", "Ngày hẹn trả", "Ngày trả",
        "Mô tả lỗi", "Hình thức XL", "Kết quả XL",
        "Chi phí (VNĐ)", "Trạng thái"
    };

    // ── Form fields ───────────────────────────────────────────────────────────
    private JTextField tfMaBH, tfMaIMEI, tfMaSP, tfMaHoaDon;
    private JTextField tfMaNVTN, tfMaNVXL;
    private JTextField tfNgayTN, tfNgayHen, tfNgayTra;
    private JTextField tfChiPhi, tfSearch;
    private JTextArea  taMoTa, taKetQua;
    private JComboBox<String> cbHinhThuc, cbTrangThai, cbFilterTT;

    private JTable            table;
    private DefaultTableModel tableModel;
    private JLabel            lblStatus, lblCount;
    private JButton           btnAdd, btnUpdate, btnDelete;

    private final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");

    // ══════════════════════════════════════════════════════════════════════════
    public BaoHanh() {
        setLayout(new BorderLayout(0, 0));
        setBackground(CONTENT_BG);
        add(buildHeader(),    BorderLayout.NORTH);
        add(buildBody(),      BorderLayout.CENTER);
        add(buildStatusBar(), BorderLayout.SOUTH);
        SwingUtilities.invokeLater(() -> loadDataAsync(null, null));
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  HEADER
    // ══════════════════════════════════════════════════════════════════════════
    private JPanel buildHeader() {
        JPanel p = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setPaint(new GradientPaint(0,0,PRIMARY_DARK,getWidth(),0,PRIMARY));
                g2.fillRect(0,0,getWidth(),getHeight());
                g2.dispose();
            }
        };
        p.setOpaque(false);
        p.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        JLabel title = new JLabel("QUẢN LÝ BẢO HÀNH");
        title.setFont(F_TITLE); title.setForeground(Color.WHITE);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);

        JLabel lb = new JLabel("Tìm kiếm:"); lb.setFont(F_LABEL); lb.setForeground(Color.WHITE);
        tfSearch = styledField(18);
        tfSearch.setToolTipText("Tìm theo mã BH, IMEI, mã SP, mã hóa đơn");
        tfSearch.addActionListener(e -> doSearch());

        cbFilterTT = new JComboBox<>(FILTER_TT);
        cbFilterTT.setFont(F_INPUT);
        cbFilterTT.setPreferredSize(new Dimension(155, 30));

        JButton btnS = mkButton("Tìm",    new Color(0,150,180), Color.WHITE);
        JButton btnR = mkButton("Tải lại", new Color(0,110,140), Color.WHITE);
        btnS.addActionListener(e -> doSearch());
        btnR.addActionListener(e -> { clearForm(); loadDataAsync(null, null); });

        right.add(lb); right.add(tfSearch); right.add(cbFilterTT);
        right.add(btnS); right.add(btnR);

        p.add(title, BorderLayout.WEST);
        p.add(right, BorderLayout.EAST);
        return p;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  BODY  (form bên trái + bảng bên phải)
    // ══════════════════════════════════════════════════════════════════════════
    private JSplitPane buildBody() {
        JSplitPane sp = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT, buildFormPanel(), buildTablePanel());
        sp.setDividerLocation(350);
        sp.setDividerSize(5);
        sp.setBackground(CONTENT_BG);
        sp.setBorder(null);
        return sp;
    }

    // ── Form ─────────────────────────────────────────────────────────────────
    private JPanel buildFormPanel() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(10,10,10,5),
            new ShadowBorder()
        ));

        // Tiêu đề form
        JLabel lbF = new JLabel("Thông tin phiếu bảo hành");
        lbF.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbF.setForeground(PRIMARY);
        lbF.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(0,0,2,0,ACCENT),
            BorderFactory.createEmptyBorder(8,12,8,0)
        ));
        card.add(lbF, BorderLayout.NORTH);

        // Scroll cho form (nhiều trường)
        JPanel fields = new JPanel(new GridBagLayout());
        fields.setBackground(Color.WHITE);
        fields.setBorder(BorderFactory.createEmptyBorder(8,12,4,12));

        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(3,3,3,3);

        // Khởi tạo fields
        tfMaBH    = styledField(14); 
        tfMaIMEI  = styledField(14);
        tfMaSP    = styledField(14);
        tfMaHoaDon= styledField(14);
        tfMaNVTN  = styledField(14);
        tfMaNVXL  = styledField(14);
        tfNgayTN  = styledField(12); tfNgayTN.setText(SDF.format(new java.util.Date()));
        tfNgayHen = styledField(12); tfNgayHen.setText(SDF.format(new java.util.Date()));
        tfNgayTra = styledField(12); tfNgayTra.setToolTipText("Để trống nếu chưa trả");
        tfChiPhi  = styledField(12); tfChiPhi.setText("0");

        taMoTa    = mkArea(2);
        taKetQua  = mkArea(2);

        cbHinhThuc = new JComboBox<>(HINH_THUC_XL);
        cbHinhThuc.setFont(F_INPUT);
        cbHinhThuc.setPreferredSize(new Dimension(0,30));

        cbTrangThai = new JComboBox<>(TRANG_THAI);
        cbTrangThai.setFont(F_INPUT);
        cbTrangThai.setPreferredSize(new Dimension(0,30));

        // MaBaoHanh là IDENTITY -> chỉ đọc, không nhập khi thêm mới
        tfMaBH.setEditable(false);
        tfMaBH.setBackground(new Color(240,244,250));
        tfMaBH.setToolTipText("Mã tự động do hệ thống cấp");

        // Định nghĩa từng hàng: {label, component}
        Object[][] rows = {
            {"Mã bảo hành (tự động)",      tfMaBH},
            {"Mã IMEI",                    tfMaIMEI},
            {"Mã sản phẩm *",              tfMaSP},
            {"Mã hóa đơn *",               tfMaHoaDon},
            {"NV tiếp nhận",               tfMaNVTN},
            {"NV xử lý",                   tfMaNVXL},
            {"Ngày tiếp nhận (dd/MM/yyyy)", tfNgayTN},
            {"Ngày hẹn trả (dd/MM/yyyy)",   tfNgayHen},
            {"Ngày trả (dd/MM/yyyy)",         tfNgayTra},
            {"Mô tả lỗi",                  wrapScroll(taMoTa, 50)},
            {"Hình thức xử lý",            cbHinhThuc},
            {"Kết quả xử lý",              wrapScroll(taKetQua, 50)},
            {"Chi phí phát sinh (VNĐ)",    tfChiPhi},
            {"Trạng thái",                 cbTrangThai},
        };

        for (int i = 0; i < rows.length; i++) {
            g.gridy = i;
            g.gridx = 0; g.weightx = 0;
            JLabel lbl = new JLabel((String) rows[i][0]);
            lbl.setFont(F_LABEL); lbl.setForeground(new Color(50,70,105));
            fields.add(lbl, g);
            g.gridx = 1; g.weightx = 1;
            fields.add((Component) rows[i][1], g);
        }

        JScrollPane scrollForm = new JScrollPane(fields);
        scrollForm.setBorder(null);
        scrollForm.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollForm.getVerticalScrollBar().setUnitIncrement(12);

        card.add(scrollForm,    BorderLayout.CENTER);
        card.add(buildBtnPanel(), BorderLayout.SOUTH);
        return card;
    }

    private JScrollPane wrapScroll(JTextArea ta, int height) {
        JScrollPane sp = new JScrollPane(ta);
        sp.setBorder(BorderFactory.createLineBorder(new Color(200,210,225)));
        sp.setPreferredSize(new Dimension(0, height));
        return sp;
    }

    private JTextArea mkArea(int rows) {
        JTextArea ta = new JTextArea(rows, 14);
        ta.setFont(F_INPUT);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        return ta;
    }

    private JPanel buildBtnPanel() {
        JPanel p = new JPanel(new GridLayout(2,2,6,6));
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createEmptyBorder(6,12,12,12));

        btnAdd    = mkButton("+ Thêm mới",      SUCCESS,                  Color.WHITE);
        btnUpdate = mkButton("Cập nhật",      WARNING,                  Color.WHITE);
        btnDelete = mkButton("Xóa phiếu",     DANGER,                   Color.WHITE);
        JButton btnClear = mkButton("Làm mới", new Color(100,110,130),   Color.WHITE);

        btnAdd.addActionListener(e    -> doInsert());
        btnUpdate.addActionListener(e -> doUpdate());
        btnDelete.addActionListener(e -> doDelete());
        btnClear.addActionListener(e  -> clearForm());

        p.add(btnAdd); p.add(btnUpdate);
        p.add(btnDelete); p.add(btnClear);
        return p;
    }

    // ── Table ─────────────────────────────────────────────────────────────────
    private JPanel buildTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CONTENT_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(10,5,10,10));

        tableModel = new DefaultTableModel(COL_NAMES, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setFont(F_TABLE);
        table.setRowHeight(30);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0,0));
        table.setSelectionBackground(new Color(21,101,192,45));
        table.setSelectionForeground(Color.BLACK);
        table.setFillsViewportHeight(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JTableHeader th = table.getTableHeader();
        th.setFont(F_HEADER); th.setBackground(PRIMARY);
        th.setForeground(Color.WHITE); th.setReorderingAllowed(false);
        th.setPreferredSize(new Dimension(0,34));
        ((DefaultTableCellRenderer)th.getDefaultRenderer())
            .setHorizontalAlignment(SwingConstants.CENTER);

        // Renderer: màu trạng thái + xen kẽ
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object val, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t,val,sel,foc,row,col);
                setFont(F_TABLE);
                setBorder(BorderFactory.createEmptyBorder(0,6,0,6));
                if (!sel) setBackground(row%2==0 ? Color.WHITE : ROW_ALT);
                setForeground(Color.BLACK);
                // Cột TrangThai = index 13
                if (col == 13 && val != null) {
                    switch (val.toString()) {
                        case "DangXuLy":    setForeground(WARNING);               setFont(F_BTN); break;
                        case "DaGuiHang":   setForeground(new Color(21,101,192));  setFont(F_BTN); break;
                        case "ChoLinhKien": setForeground(new Color(156,39,176));  setFont(F_BTN); break;
                        case "DaTraKhach":  setForeground(SUCCESS);               setFont(F_BTN); break;
                    }
                }
                return this;
            }
        });

        // Chiều rộng từng cột
        int[] widths = {80,100,80,90, 100,90, 110,110,100, 120,110,110, 110,130};
        for (int i=0; i<widths.length; i++)
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        table.getSelectionModel().addListSelectionListener(
            e -> { if (!e.getValueIsAdjusting()) fillFormFromTable(); });

        JScrollPane sp = new JScrollPane(table,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        sp.setBorder(new ShadowBorder());
        sp.getViewport().setBackground(Color.WHITE);
        panel.add(sp, BorderLayout.CENTER);
        return panel;
    }

    // ── Status bar ────────────────────────────────────────────────────────────
    private JPanel buildStatusBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(new Color(225,232,245));
        bar.setBorder(new MatteBorder(1,0,0,0,new Color(200,210,230)));

        lblStatus = new JLabel("  Đang khởi động...");
        lblStatus.setFont(new Font("Segoe UI", Font.ITALIC,12));
        lblStatus.setForeground(new Color(70,90,120));
        lblStatus.setBorder(BorderFactory.createEmptyBorder(3,8,3,0));

        lblCount = new JLabel("0 bản ghi  ");
        lblCount.setFont(new Font("Segoe UI",Font.BOLD,12));
        lblCount.setForeground(PRIMARY);
        lblCount.setBorder(BorderFactory.createEmptyBorder(3,0,3,12));

        bar.add(lblStatus, BorderLayout.WEST);
        bar.add(lblCount,  BorderLayout.EAST);
        return bar;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  DATABASE (SwingWorker — không block UI)
    // ══════════════════════════════════════════════════════════════════════════

    private void loadDataAsync(String keyword, String trangThai) {
        setStatus("Đang tải dữ liệu...", new Color(70,90,120));
        setBtnsEnabled(false);

        new SwingWorker<List<Object[]>, Void>() {
            @Override protected List<Object[]> doInBackground() throws Exception {
                return fetchRows(keyword, trangThai);
            }
            @Override protected void done() {
                setBtnsEnabled(true);
                try {
                    List<Object[]> data = get();
                    tableModel.setRowCount(0);
                    for (Object[] r : data) tableModel.addRow(r);
                    setStatus("Tải thành công", SUCCESS);
                    lblCount.setText(data.size() + " bản ghi  ");
                } catch (Exception ex) {
                    Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
                    setStatus("Lỗi DB: " + cause.getMessage(), DANGER);
                    lblCount.setText("—  ");
                    cause.printStackTrace();
                }
            }
        }.execute();
    }

    private List<Object[]> fetchRows(String kw, String tt) throws SQLException {
        boolean hasKW = kw != null && !kw.isBlank();
        boolean hasTT = tt != null && !tt.isBlank() && !tt.equals("Tất cả");

        StringBuilder sql = new StringBuilder(
            "SELECT MaBaoHanh, MaIMEI, MaSP, MaHoaDon, " +
            "MaNVTiepNhan, MaNVXuLy, " +
            "CONVERT(NVARCHAR,NgayTiepNhan,103) AS NgayTiepNhan, " +
            "CONVERT(NVARCHAR,NgayHenTra,103)   AS NgayHenTra, " +
            "CONVERT(NVARCHAR,NgayTra,103)       AS NgayTra, " +
            "CAST(MoTaLoi AS NVARCHAR(MAX))          AS MoTaLoi, " +
            "CAST(HinhThucXuLy AS NVARCHAR(MAX))      AS HinhThucXuLy, " +
            "CAST(KetQuaXuLy AS NVARCHAR(MAX))         AS KetQuaXuLy, " +
            "ChiPhiPhatSinh, TrangThai " +
            "FROM BAOHANH WHERE 1=1");

        if (hasKW) sql.append(" AND (MaBaoHanh LIKE ? OR MaIMEI LIKE ? OR MaSP LIKE ? OR MaHoaDon LIKE ?)");
        if (hasTT) sql.append(" AND TrangThai = ?");
        sql.append(" ORDER BY NgayTiepNhan DESC");

        List<Object[]> result = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {
            int idx = 1;
            if (hasKW) {
                String like = "%" + kw + "%";
                ps.setNString(idx++, like); ps.setNString(idx++, like);
                ps.setNString(idx++, like); ps.setNString(idx++, like);
            }
            if (hasTT) ps.setNString(idx, tt);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(new Object[]{
                    rs.getString("MaBaoHanh"),
                    rs.getString("MaIMEI"),
                    rs.getString("MaSP"),
                    rs.getString("MaHoaDon"),
                    rs.getString("MaNVTiepNhan"),
                    rs.getString("MaNVXuLy"),
                    rs.getString("NgayTiepNhan"),
                    rs.getString("NgayHenTra"),
                    rs.getString("NgayTra"),
                    rs.getString("MoTaLoi"),
                    rs.getString("HinhThucXuLy"),
                    rs.getString("KetQuaXuLy"),
                    rs.getString("ChiPhiPhatSinh"),
                    rs.getString("TrangThai")
                });
            }
        }
        return result;
    }

    // ── INSERT ────────────────────────────────────────────────────────────────
    // MaBaoHanh là IDENTITY (tự tăng) → không truyền vào INSERT
    private void doInsert() {
        if (!validateForm(false)) return;
        String sql =
            "INSERT INTO BAOHANH (MaIMEI,MaSP,MaHoaDon," +
            "MaNVTiepNhan,MaNVXuLy,NgayTiepNhan,NgayHenTra,NgayTra," +
            "MoTaLoi,HinhThucXuLy,KetQuaXuLy,ChiPhiPhatSinh,TrangThai) " +
            "VALUES (?,?,?,?,?,CAST(? AS DATE),CAST(? AS DATE),?,?,?,?,?,?)";
        setBtnsEnabled(false);
        new SwingWorker<Void,Void>() {
            String errMsg;
            @Override protected Void doInBackground() {
                try (Connection con = DBConnection.getConnection();
                     PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setNString(1,  tfMaIMEI.getText().trim());
                    ps.setNString(2,  tfMaSP.getText().trim());
                    ps.setNString(3,  nullIfBlank(tfMaHoaDon.getText()));
                    ps.setNString(4,  nullIfBlank(tfMaNVTN.getText()));
                    ps.setNString(5,  nullIfBlank(tfMaNVXL.getText()));
                    ps.setString (6,  toSqlDate(tfNgayTN.getText()));
                    ps.setString (7,  toSqlDate(tfNgayHen.getText()));
                    String ngayTra = tfNgayTra.getText().trim();
                    if (ngayTra.isEmpty()) ps.setNull(8, Types.DATE);
                    else                   ps.setString(8, toSqlDate(ngayTra));
                    ps.setNString(9,  taMoTa.getText().trim());
                    ps.setNString(10, cbHinhThuc.getSelectedItem().toString());
                    ps.setNString(11, taKetQua.getText().trim());
                    ps.setBigDecimal(12, parseMoney(tfChiPhi.getText()));
                    ps.setNString(13, cbTrangThai.getSelectedItem().toString());
                    ps.executeUpdate();
                } catch (SQLException ex) {
                    errMsg = "Lỗi thêm: " + ex.getMessage();
                    ex.printStackTrace();
                }
                return null;
            }
            @Override protected void done() {
                setBtnsEnabled(true);
                if (errMsg!=null){showErr(errMsg);return;}
                setStatus("Thêm phiếu thành công!", SUCCESS);
                clearForm(); loadDataAsync(null,null);
            }
        }.execute();
    }

    // ── UPDATE ────────────────────────────────────────────────────────────────
    private void doUpdate() {
        if (table.getSelectedRow()<0){showErr("Vui lòng chọn một phiếu để cập nhật!");return;}
        if (!validateForm(false)) return;
        String sql =
            "UPDATE BAOHANH SET MaIMEI=?,MaSP=?,MaHoaDon=?," +
            "MaNVTiepNhan=?,MaNVXuLy=?," +
            "NgayTiepNhan=CAST(? AS DATE),NgayHenTra=CAST(? AS DATE)," +
            "NgayTra=?,MoTaLoi=?,HinhThucXuLy=?,KetQuaXuLy=?," +
            "ChiPhiPhatSinh=?,TrangThai=? WHERE MaBaoHanh=?";
        final String maBH = tfMaBH.getText().trim();
        setBtnsEnabled(false);
        new SwingWorker<Integer,Void>() {
            String errMsg;
            @Override protected Integer doInBackground() {
                try (Connection con = DBConnection.getConnection();
                     PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setNString(1,  tfMaIMEI.getText().trim());
                    ps.setNString(2,  tfMaSP.getText().trim());
                    ps.setNString(3,  nullIfBlank(tfMaHoaDon.getText()));
                    ps.setNString(4,  nullIfBlank(tfMaNVTN.getText()));
                    ps.setNString(5,  nullIfBlank(tfMaNVXL.getText()));
                    ps.setString (6,  toSqlDate(tfNgayTN.getText()));
                    ps.setString (7,  toSqlDate(tfNgayHen.getText()));
                    String ngayTra = tfNgayTra.getText().trim();
                    if (ngayTra.isEmpty()) ps.setNull(8, Types.DATE);
                    else                   ps.setString(8, toSqlDate(ngayTra));
                    ps.setNString(9,  taMoTa.getText().trim());
                    ps.setNString(10, cbHinhThuc.getSelectedItem().toString());
                    ps.setNString(11, taKetQua.getText().trim());
                    ps.setBigDecimal(12, parseMoney(tfChiPhi.getText()));
                    ps.setNString(13, cbTrangThai.getSelectedItem().toString());
                    ps.setNString(14, maBH);
                    return ps.executeUpdate();
                } catch (SQLException ex) {
                    errMsg = "Lỗi cập nhật: " + ex.getMessage();
                    ex.printStackTrace(); return 0;
                }
            }
            @Override protected void done() {
                setBtnsEnabled(true);
                if (errMsg!=null){showErr(errMsg);return;}
                try {
                    if (get()>0){ setStatus("Cập nhật phiếu ["+maBH+"] thành công!",SUCCESS); loadDataAsync(null,null);}
                    else showErr("Không tìm thấy mã bảo hành để cập nhật.");
                } catch (Exception ex){showErr(ex.getMessage());}
            }
        }.execute();
    }

    // ── DELETE ────────────────────────────────────────────────────────────────
    private void doDelete() {
        int row = table.getSelectedRow();
        if (row<0){showErr("Vui lòng chọn một phiếu để xóa!");return;}
        String maBH = getCell(row,0);
        int ok = JOptionPane.showConfirmDialog(this,
            "Xóa phiếu bảo hành ["+maBH+"]?\nHành động này không thể hoàn tác.",
            "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (ok!=JOptionPane.YES_OPTION) return;
        setBtnsEnabled(false);
        new SwingWorker<Void,Void>() {
            String errMsg;
            @Override protected Void doInBackground() {
                try (Connection con = DBConnection.getConnection();
                     PreparedStatement ps = con.prepareStatement(
                         "DELETE FROM BAOHANH WHERE MaBaoHanh=?")) {
                    ps.setNString(1, maBH);
                    ps.executeUpdate();
                } catch (SQLException ex) {
                    errMsg = "Lỗi xóa: " + ex.getMessage(); ex.printStackTrace();
                }
                return null;
            }
            @Override protected void done() {
                setBtnsEnabled(true);
                if (errMsg!=null){showErr(errMsg);return;}
                setStatus("Đã xóa phiếu ["+maBH+"]", DANGER);
                clearForm(); loadDataAsync(null,null);
            }
        }.execute();
    }

    // ── SEARCH ────────────────────────────────────────────────────────────────
    private void doSearch() {
        String kw = tfSearch.getText().trim();
        String tt = cbFilterTT.getSelectedItem().toString();
        loadDataAsync(kw.isEmpty()?null:kw, tt.equals("Tất cả")?null:tt);
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  HELPERS
    // ══════════════════════════════════════════════════════════════════════════

    private boolean validateForm(boolean checkPK) {
        // MaBaoHanh là IDENTITY, không cần validate khi thêm mới
        if (tfMaIMEI.getText().isBlank())  {showErr("Nhập Mã IMEI!");     tfMaIMEI.requestFocus(); return false;}
        if (tfMaSP.getText().isBlank())    {showErr("Nhập Mã sản phẩm!"); tfMaSP.requestFocus();   return false;}
        if (!isValidDate(tfNgayTN.getText()))  {showErr("Ngày tiếp nhận không hợp lệ (yyyy-MM-dd)!"); tfNgayTN.requestFocus();  return false;}
        if (!isValidDate(tfNgayHen.getText())) {showErr("Ngày hẹn trả không hợp lệ (yyyy-MM-dd)!");  tfNgayHen.requestFocus(); return false;}
        String ngTra = tfNgayTra.getText().trim();
        if (!ngTra.isEmpty() && !isValidDate(ngTra)) {showErr("Ngày trả không hợp lệ (yyyy-MM-dd)!"); tfNgayTra.requestFocus(); return false;}
        return true;
    }

    /** Chuyển dd/MM/yyyy → yyyy-MM-dd để gửi lên SQL Server */
    private String toSqlDate(String ddMMyyyy) {
        try {
            SDF.setLenient(false);
            java.util.Date d = SDF.parse(ddMMyyyy.trim());
            return new SimpleDateFormat("yyyy-MM-dd").format(d);
        } catch (Exception e) { return ddMMyyyy.trim(); }
    }

    private boolean isValidDate(String s) {
        try { SDF.setLenient(false); SDF.parse(s); return true; }
        catch (Exception e) { return false; }
    }

    private java.math.BigDecimal parseMoney(String s) {
        try { return new java.math.BigDecimal(s.trim().replace(",","")); }
        catch (Exception e) { return java.math.BigDecimal.ZERO; }
    }

    private String nullIfBlank(String s) { return (s==null||s.isBlank()) ? null : s.trim(); }

    private void fillFormFromTable() {
        int row = table.getSelectedRow();
        if (row<0) return;
        tfMaBH.setText(getCell(row,0));  // readonly - chỉ hiện để tham khảo
        tfMaIMEI.setText(getCell(row,1));
        tfMaSP.setText(getCell(row,2));
        tfMaHoaDon.setText(getCell(row,3));
        tfMaNVTN.setText(getCell(row,4));
        tfMaNVXL.setText(getCell(row,5));
        tfNgayTN.setText(getCell(row,6));
        tfNgayHen.setText(getCell(row,7));
        tfNgayTra.setText(getCell(row,8));
        taMoTa.setText(getCell(row,9));
        cbHinhThuc.setSelectedItem(getCell(row,10));
        taKetQua.setText(getCell(row,11));
        tfChiPhi.setText(getCell(row,12));
        cbTrangThai.setSelectedItem(getCell(row,13));
    }

    private String getCell(int r, int c) {
        Object v = tableModel.getValueAt(r,c); return v!=null ? v.toString() : "";
    }

    private void clearForm() {
        tfMaBH.setText("(tự động)");
        tfMaIMEI.setText(""); tfMaSP.setText(""); tfMaHoaDon.setText("");
        tfMaNVTN.setText(""); tfMaNVXL.setText("");
        tfNgayTN.setText(SDF.format(new java.util.Date()));
        tfNgayHen.setText(SDF.format(new java.util.Date()));
        tfNgayTra.setText("");
        taMoTa.setText(""); cbHinhThuc.setSelectedIndex(0); taKetQua.setText("");
        tfChiPhi.setText("0");
        cbTrangThai.setSelectedIndex(0);
        table.clearSelection();
        setStatus("Sẵn sàng", new Color(70,90,120));
    }

    private void setBtnsEnabled(boolean on) {
        if (btnAdd!=null)    btnAdd.setEnabled(on);
        if (btnUpdate!=null) btnUpdate.setEnabled(on);
        if (btnDelete!=null) btnDelete.setEnabled(on);
    }

    private void setStatus(String msg, Color c) {
        lblStatus.setText("  "+msg); lblStatus.setForeground(c);
    }

    private void showErr(String msg) {
        setStatus("[!] "+msg, DANGER);
        JOptionPane.showMessageDialog(this, msg, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    // ── Button factory ────────────────────────────────────────────────────────
    private JButton mkButton(String label, Color bg, Color fg) {
        JButton btn = new JButton(label) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                Color c = !isEnabled()            ? bg.darker().darker()
                        : getModel().isPressed()  ? bg.darker()
                        : getModel().isRollover() ? bg.brighter() : bg;
                g2.setColor(c);
                g2.fillRoundRect(0,0,getWidth(),getHeight(),10,10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(F_BTN); btn.setForeground(fg);
        btn.setContentAreaFilled(false); btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(6,12,6,12));
        return btn;
    }

    private JTextField styledField(int cols) {
        JTextField tf = new JTextField(cols);
        tf.setFont(F_INPUT);
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200,210,225)),
            BorderFactory.createEmptyBorder(3,7,3,7)
        ));
        tf.setPreferredSize(new Dimension(tf.getPreferredSize().width, 30));
        return tf;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  SHADOW BORDER
    // ══════════════════════════════════════════════════════════════════════════
    static class ShadowBorder extends AbstractBorder {
        @Override public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            for (int i=4;i>0;i--){
                g2.setColor(new Color(0,0,0,8*i));
                g2.drawRoundRect(x+i,y+i,w-i*2-1,h-i*2-1,10,10);
            }
            g2.setColor(new Color(210,220,235));
            g2.drawRoundRect(x,y,w-1,h-1,10,10);
            g2.dispose();
        }
        @Override public Insets getBorderInsets(Component c){return new Insets(6,6,6,6);}
        @Override public Insets getBorderInsets(Component c,Insets i){i.set(6,6,6,6);return i;}
    }
}