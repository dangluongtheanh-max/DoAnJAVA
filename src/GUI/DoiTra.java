package GUI;

import BUS.DoiTraBUS;
import DTO.DoiTraDTO;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

/**
 * Panel Quản lý Đổi trả — GUI layer.
 * Mọi nghiệp vụ và truy cập CSDL đều đi qua DoiTraBUS (BUS).
 */
public class DoiTra extends JPanel {

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

    // Lấy danh sách trạng thái từ BUS
    private static final String[] TRANG_THAI = DoiTraBUS.TRANG_THAI_HOP_LE.toArray(new String[0]);
    private static final String[] FILTER_TT;
    static {
        FILTER_TT = new String[TRANG_THAI.length + 1];
        FILTER_TT[0] = "Tất cả";
        System.arraycopy(TRANG_THAI, 0, FILTER_TT, 1, TRANG_THAI.length);
    }

    // ── Cột bảng ─────────────────────────────────────────────────────────────
    private static final String[] COL_NAMES = {
        "Mã Đổi Trả", "Mã Hóa Đơn", "Mã SP", "Mã IMEI",
        "Số Lượng Trả", "Lý Do", "Mã NV",
        "Ngày Yêu Cầu", "Trạng Thái", "Ghi Chú"
    };

    // ── Form fields ───────────────────────────────────────────────────────────
    private JTextField tfMaDT, tfMaHD, tfMaSP, tfMaIMEI;
    private JTextField tfSoLuong, tfMaNV, tfNgay, tfSearch;
    private JTextArea  taLyDo, taGhiChu;
    private JComboBox<String> cbTrangThai, cbFilterTT;

    private JTable            table;
    private DefaultTableModel tableModel;
    private JLabel            lblStatus, lblCount;
    private JButton           btnAdd, btnUpdate, btnDelete;

    private final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");

    // BUS layer — mọi nghiệp vụ và DB đi qua đây
    private final DoiTraBUS service = new DoiTraBUS();

    // ══════════════════════════════════════════════════════════════════════════
    public DoiTra() {
        setLayout(new BorderLayout(0, 0));
        setBackground(CONTENT_BG);
        add(buildHeader(),    BorderLayout.NORTH);
        add(buildBody(),      BorderLayout.CENTER);
        add(buildStatusBar(), BorderLayout.SOUTH);
        SwingUtilities.invokeLater(() -> loadDataAsync(null, null));
    }

    // ══════════════════════════════════════════════════════════════════════════
    // HEADER
    // ══════════════════════════════════════════════════════════════════════════
    private JPanel buildHeader() {
        JPanel p = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setPaint(new GradientPaint(0, 0, PRIMARY_DARK, getWidth(), 0, PRIMARY));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        p.setOpaque(false);
        p.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        JLabel title = new JLabel("QUẢN LÍ ĐỔI TRẢ");
        title.setFont(F_TITLE); title.setForeground(Color.WHITE);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);

        JLabel lb = new JLabel("Tìm kiếm:"); lb.setFont(F_LABEL); lb.setForeground(Color.WHITE);
        tfSearch = styledField(18);
        tfSearch.setToolTipText("Tìm theo mã đổi trả, mã hóa đơn, mã SP");
        tfSearch.addActionListener(e -> doSearch());

        cbFilterTT = new JComboBox<>(FILTER_TT);
        cbFilterTT.setFont(F_INPUT);
        cbFilterTT.setPreferredSize(new Dimension(155, 30));

        JButton btnS = mkButton("Tìm",    new Color(0, 150, 180), Color.WHITE);
        JButton btnR = mkButton("Tải lại", new Color(0, 110, 140), Color.WHITE);
        btnS.addActionListener(e -> doSearch());
        btnR.addActionListener(e -> { clearForm(); loadDataAsync(null, null); });

        right.add(lb); right.add(tfSearch); right.add(cbFilterTT);
        right.add(btnS); right.add(btnR);

        p.add(title, BorderLayout.WEST);
        p.add(right, BorderLayout.EAST);
        return p;
    }

    // ══════════════════════════════════════════════════════════════════════════
    // BODY
    // ══════════════════════════════════════════════════════════════════════════
    private JSplitPane buildBody() {
        JSplitPane sp = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT, buildFormPanel(), buildTablePanel());
        sp.setDividerLocation(350);
        sp.setDividerSize(5);
        sp.setResizeWeight(0.5); 
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


        JLabel lbF = new JLabel("Thông tin phiếu đổi trả");
        lbF.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbF.setForeground(PRIMARY);
        lbF.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(0, 0, 2, 0, ACCENT),
            BorderFactory.createEmptyBorder(8, 12, 8, 0)));
        card.add(lbF, BorderLayout.NORTH);

        JPanel fields = new JPanel(new GridBagLayout());
        fields.setBackground(Color.WHITE);
        fields.setBorder(BorderFactory.createEmptyBorder(8, 12, 4, 12));

        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(4, 3, 4, 3);

        tfMaDT = styledField(14);
        tfMaDT.setEditable(false);
        tfMaDT.setBackground(new Color(240, 244, 250));
        tfMaDT.setToolTipText("Mã tự động do hệ thống cấp");

        tfMaHD    = styledField(14);
        tfMaSP    = styledField(14);
        tfMaIMEI  = styledField(14);
        tfMaIMEI.setToolTipText("Để trống nếu không có");
        tfSoLuong = styledField(6);  tfSoLuong.setText("1");
        tfMaNV    = styledField(14);
        tfNgay    = styledField(12); tfNgay.setText(SDF.format(new java.util.Date()));
        taLyDo    = mkArea(3);
        taGhiChu  = mkArea(3);

        cbTrangThai = new JComboBox<>(TRANG_THAI);
        cbTrangThai.setFont(F_INPUT);
        cbTrangThai.setPreferredSize(new Dimension(0, 30));

        Object[][] rows = {
            { "Mã đổi trả (tự động)",       tfMaDT },
            { "Mã hóa đơn *",               tfMaHD },
            { "Mã sản phẩm *",              tfMaSP },
            { "Mã IMEI",                    tfMaIMEI },
            { "Số lượng trả *",             tfSoLuong },
            { "Mã nhân viên *",             tfMaNV },
            { "Ngày yêu cầu (dd/MM/yyyy)",  tfNgay },
            { "Lý do",                      wrapScroll(taLyDo,   60) },
            { "Ghi chú",                    wrapScroll(taGhiChu, 60) },
            { "Trạng thái",                 cbTrangThai },
        };

        for (int i = 0; i < rows.length; i++) {
            g.gridy = i;
            g.gridx = 0; g.weightx = 0;
            JLabel lbl = new JLabel((String) rows[i][0]);
            lbl.setFont(F_LABEL); lbl.setForeground(new Color(50, 70, 105));
            fields.add(lbl, g);
            g.gridx = 1; g.weightx = 1;
            fields.add((Component) rows[i][1], g);
        }

        JScrollPane scrollForm = new JScrollPane(fields);
        scrollForm.setBorder(null);
        scrollForm.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollForm.getVerticalScrollBar().setUnitIncrement(12);

        card.add(scrollForm,      BorderLayout.CENTER);
        card.add(buildBtnPanel(), BorderLayout.SOUTH);
        return card;
    }

    private JPanel buildBtnPanel() {
        JPanel p = new JPanel(new GridLayout(2, 2, 6, 6));
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createEmptyBorder(6, 12, 12, 12));

        btnAdd    = mkButton("Thêm mới",  SUCCESS,                Color.WHITE);
        btnUpdate = mkButton("Cập nhật",  WARNING,                Color.WHITE);
        btnDelete = mkButton("Xóa",       DANGER,                 Color.WHITE);
        JButton btnClear = mkButton("Làm mới", new Color(100, 110, 130), Color.WHITE);

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
        panel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 10));

        tableModel = new DefaultTableModel(COL_NAMES, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setFont(F_TABLE);
        table.setRowHeight(30);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(21, 101, 192, 45));
        table.setSelectionForeground(Color.BLACK);
        table.setFillsViewportHeight(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JTableHeader th = table.getTableHeader();
        th.setFont(F_HEADER); th.setBackground(PRIMARY);
        th.setForeground(Color.WHITE); th.setReorderingAllowed(false);
        th.setPreferredSize(new Dimension(0, 34));
        ((DefaultTableCellRenderer) th.getDefaultRenderer())
            .setHorizontalAlignment(SwingConstants.CENTER);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object val, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                setFont(F_TABLE);
                setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 6));
                if (!sel) setBackground(row % 2 == 0 ? Color.WHITE : ROW_ALT);
                setForeground(Color.BLACK);
                if (col == 8 && val != null) {
                    switch (val.toString()) {
                        case "DangXuLy":  setForeground(WARNING); setFont(F_BTN); break;
                        case "HoanThanh": setForeground(SUCCESS); setFont(F_BTN); break;
                    }
                }
                setHorizontalAlignment(col == 4 ? SwingConstants.CENTER : SwingConstants.LEFT);
                return this;
            }
        });

        int[] widths = { 80, 90, 70, 70, 90, 140, 70, 110, 100, 140 };
        for (int i = 0; i < widths.length; i++)
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
        bar.setBackground(new Color(225, 232, 245));
        bar.setBorder(new MatteBorder(1, 0, 0, 0, new Color(200, 210, 230)));

        lblStatus = new JLabel("  Đang khởi động...");
        lblStatus.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblStatus.setForeground(new Color(70, 90, 120));
        lblStatus.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 0));

        lblCount = new JLabel("0 bản ghi  ");
        lblCount.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblCount.setForeground(PRIMARY);
        lblCount.setBorder(BorderFactory.createEmptyBorder(3, 0, 3, 12));

        bar.add(lblStatus, BorderLayout.WEST);
        bar.add(lblCount,  BorderLayout.EAST);
        return bar;
    }

    // ══════════════════════════════════════════════════════════════════════════
    // DATABASE — SwingWorker gọi qua BUS, không động vào DB trực tiếp
    // ══════════════════════════════════════════════════════════════════════════

    private void loadDataAsync(String keyword, String trangThai) {
        setStatus("Đang tải dữ liệu...", new Color(70, 90, 120));
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
                    setStatus("[!] Lỗi: " + cause.getMessage(), DANGER);
                    lblCount.setText("--  ");
                    cause.printStackTrace();
                }
            }
        }.execute();
    }

    /** Lấy dữ liệu qua BUS, lọc keyword phía client */
    private List<Object[]> fetchRows(String kw, String tt) throws Exception {
        boolean hasTT = tt != null && !tt.isBlank() && !tt.equals("Tất cả");
        List<DoiTraDTO> list = hasTT
                ? service.layDanhSachTheoTrangThai(tt)
                : service.layTatCaDoiTra();

        List<Object[]> result = new ArrayList<>();
        for (DoiTraDTO dt : list) {
            String maDT   = String.valueOf(dt.getMaDoiTra());
            String maHD   = String.valueOf(dt.getMaHoaDon());
            String maSP   = String.valueOf(dt.getMaSP());
            String maIMEI = dt.getMaIMEI() != null ? dt.getMaIMEI().toString() : "";
            String maNV   = String.valueOf(dt.getMaNV());

            if (kw != null && !kw.isBlank()) {
                String lower = kw.toLowerCase();
                boolean match = maDT.contains(lower)
                        || maHD.contains(lower)
                        || maSP.contains(lower);
                if (!match) continue;
            }

            result.add(new Object[]{
                maDT, maHD, maSP, maIMEI,
                dt.getSoLuongTra(),
                dt.getLyDo(),
                maNV,
                dt.getNgayYeuCau() != null ? SDF.format(dt.getNgayYeuCau()) : "",
                dt.getTrangThai(),
                dt.getGhiChu()
            });
        }
        return result;
    }

    // ── INSERT — qua BUS ─────────────────────────────────────────────────────
    private void doInsert() {
        if (!validateForm()) return;
        setBtnsEnabled(false);
        new SwingWorker<Void, Void>() {
            String errMsg;
            @Override protected Void doInBackground() {
                try {
                    service.taoYeuCauDoiTra(buildDTOFromForm());
                } catch (IllegalArgumentException ex) {
                    errMsg = "Dữ liệu không hợp lệ: " + ex.getMessage();
                } catch (Exception ex) {
                    errMsg = "Lỗi thêm: " + ex.getMessage();
                    ex.printStackTrace();
                }
                return null;
            }
            @Override protected void done() {
                setBtnsEnabled(true);
                if (errMsg != null) { showErr(errMsg); return; }
                setStatus("Thêm phiếu đổi trả thành công!", SUCCESS);
                clearForm(); loadDataAsync(null, null);
            }
        }.execute();
    }

    // ── UPDATE — qua BUS ─────────────────────────────────────────────────────
    private void doUpdate() {
        if (table.getSelectedRow() < 0) { showErr("Vui lòng chọn một phiếu để cập nhật!"); return; }
        if (!validateForm()) return;
        final String maDT = tfMaDT.getText().trim();
        setBtnsEnabled(false);
        new SwingWorker<Boolean, Void>() {
            String errMsg;
            @Override protected Boolean doInBackground() {
                try {
                    DoiTraDTO dto = buildDTOFromForm();
                    dto.setMaDoiTra(parseInt(maDT));
                    return service.capNhatYeuCauDoiTra(dto);
                } catch (IllegalArgumentException ex) {
                    errMsg = "Dữ liệu không hợp lệ: " + ex.getMessage(); return false;
                } catch (Exception ex) {
                    errMsg = "Lỗi cập nhật: " + ex.getMessage();
                    ex.printStackTrace(); return false;
                }
            }
            @Override protected void done() {
                setBtnsEnabled(true);
                if (errMsg != null) { showErr(errMsg); return; }
                try {
                    if (get()) {
                        setStatus("Cập nhật phiếu [" + maDT + "] thành công!", SUCCESS);
                        loadDataAsync(null, null);
                    } else showErr("Không tìm thấy phiếu để cập nhật.");
                } catch (Exception ex) { showErr(ex.getMessage()); }
            }
        }.execute();
    }

    // ── DELETE — qua BUS ─────────────────────────────────────────────────────
    private void doDelete() {
        int row = table.getSelectedRow();
        if (row < 0) { showErr("Vui lòng chọn một phiếu để xóa!"); return; }
        String maDT = getCell(row, 0);
        int ok = JOptionPane.showConfirmDialog(this,
            "Xóa phiếu đổi trả [" + maDT + "]?\nHành động này không thể hoàn tác.",
            "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (ok != JOptionPane.YES_OPTION) return;
        setBtnsEnabled(false);
        new SwingWorker<Void, Void>() {
            String errMsg;
            @Override protected Void doInBackground() {
                try {
                    service.xoaDoiTra(parseInt(maDT));
                } catch (Exception ex) {
                    errMsg = "Lỗi xóa: " + ex.getMessage();
                    ex.printStackTrace();
                }
                return null;
            }
            @Override protected void done() {
                setBtnsEnabled(true);
                if (errMsg != null) { showErr(errMsg); return; }
                setStatus("Đã xóa phiếu [" + maDT + "]", DANGER);
                clearForm(); loadDataAsync(null, null);
            }
        }.execute();
    }

    // ── SEARCH ────────────────────────────────────────────────────────────────
    private void doSearch() {
        String kw = tfSearch.getText().trim();
        String tt = cbFilterTT.getSelectedItem().toString();
        loadDataAsync(kw.isEmpty() ? null : kw, tt.equals("Tất cả") ? null : tt);
    }

    // ══════════════════════════════════════════════════════════════════════════
    // HELPERS
    // ══════════════════════════════════════════════════════════════════════════

    /** Đọc form → DoiTraDTO, kiểu dữ liệu khớp với DoiTraDAO */
    private DoiTraDTO buildDTOFromForm() {
        DoiTraDTO dt = new DoiTraDTO();
        dt.setMaHoaDon(parseInt(tfMaHD.getText()));
        dt.setMaSP(parseInt(tfMaSP.getText()));
        dt.setMaIMEI(parseIntOrNull(tfMaIMEI.getText()));   // nullable Integer
        dt.setSoLuongTra(parseInt(tfSoLuong.getText()));
        dt.setMaNV(parseInt(tfMaNV.getText()));
        dt.setNgayYeuCau(toSqlDateObj(tfNgay.getText()));
        dt.setLyDo(nullIfBlank(taLyDo.getText()));
        dt.setGhiChu(nullIfBlank(taGhiChu.getText()));
        dt.setTrangThai(cbTrangThai.getSelectedItem().toString());
        return dt;
    }

    private boolean validateForm() {
        if (tfMaHD.getText().isBlank())   { showErr("Nhập mã hóa đơn!");    tfMaHD.requestFocus();    return false; }
        if (!isInt(tfMaHD.getText()))     { showErr("Mã hóa đơn phải là số!"); tfMaHD.requestFocus(); return false; }
        if (tfMaSP.getText().isBlank())   { showErr("Nhập mã sản phẩm!");   tfMaSP.requestFocus();    return false; }
        if (!isInt(tfMaSP.getText()))     { showErr("Mã sản phẩm phải là số!"); tfMaSP.requestFocus(); return false; }
        if (tfSoLuong.getText().isBlank()) { showErr("Nhập số lượng trả!"); tfSoLuong.requestFocus(); return false; }
        if (!isInt(tfSoLuong.getText()) || parseInt(tfSoLuong.getText()) <= 0) {
            showErr("Số lượng phải là số nguyên > 0!"); tfSoLuong.requestFocus(); return false;
        }
        if (tfMaNV.getText().isBlank())   { showErr("Nhập mã nhân viên!"); tfMaNV.requestFocus();     return false; }
        if (!isInt(tfMaNV.getText()))     { showErr("Mã nhân viên phải là số!"); tfMaNV.requestFocus(); return false; }
        if (!isValidDate(tfNgay.getText())) { showErr("Ngày yêu cầu không hợp lệ (dd/MM/yyyy)!"); tfNgay.requestFocus(); return false; }
        String imei = tfMaIMEI.getText().trim();
        if (!imei.isEmpty() && !isInt(imei)) { showErr("Mã IMEI phải là số!"); tfMaIMEI.requestFocus(); return false; }
        return true;
    }

    private boolean isValidDate(String s) {
        try { SDF.setLenient(false); SDF.parse(s.trim()); return true; }
        catch (Exception e) { return false; }
    }

    private boolean isInt(String s) {
        try { Integer.parseInt(s.trim()); return true; }
        catch (Exception e) { return false; }
    }

    private int parseInt(String s) {
        try { return Integer.parseInt(s.trim()); }
        catch (Exception e) { return 0; }
    }

    private Integer parseIntOrNull(String s) {
        if (s == null || s.isBlank()) return null;
        try { return Integer.parseInt(s.trim()); }
        catch (Exception e) { return null; }
    }

    private java.sql.Date toSqlDateObj(String ddMMyyyy) {
        try {
            SDF.setLenient(false);
            java.util.Date d = SDF.parse(ddMMyyyy.trim());
            return new java.sql.Date(d.getTime());
        } catch (Exception e) { return null; }
    }

    private String nullIfBlank(String s) {
        return (s == null || s.isBlank()) ? null : s.trim();
    }

    private void fillFormFromTable() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        tfMaDT.setText(getCell(row, 0));
        tfMaHD.setText(getCell(row, 1));
        tfMaSP.setText(getCell(row, 2));
        tfMaIMEI.setText(getCell(row, 3));
        tfSoLuong.setText(getCell(row, 4));
        taLyDo.setText(getCell(row, 5));
        tfMaNV.setText(getCell(row, 6));
        tfNgay.setText(getCell(row, 7));
        cbTrangThai.setSelectedItem(getCell(row, 8));
        taGhiChu.setText(getCell(row, 9));
    }

    private String getCell(int r, int c) {
        Object v = tableModel.getValueAt(r, c); return v != null ? v.toString() : "";
    }

    private void clearForm() {
        tfMaDT.setText("(Tự động)");
        tfMaHD.setText(""); tfMaSP.setText(""); tfMaIMEI.setText("");
        tfSoLuong.setText("1"); tfMaNV.setText("");
        tfNgay.setText(SDF.format(new java.util.Date()));
        taLyDo.setText(""); taGhiChu.setText("");
        cbTrangThai.setSelectedIndex(0);
        table.clearSelection();
        setStatus("Sẵn sàng", new Color(70, 90, 120));
    }

    private void setBtnsEnabled(boolean on) {
        if (btnAdd    != null) btnAdd.setEnabled(on);
        if (btnUpdate != null) btnUpdate.setEnabled(on);
        if (btnDelete != null) btnDelete.setEnabled(on);
    }

    private void setStatus(String msg, Color c) {
        lblStatus.setText("  " + msg); lblStatus.setForeground(c);
    }

    private void showErr(String msg) {
        setStatus("[!] " + msg, DANGER);
        JOptionPane.showMessageDialog(this, msg, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    // ── UI Helpers ────────────────────────────────────────────────────────────
    private JButton mkButton(String label, Color bg, Color fg) {
        JButton btn = new JButton(label) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color c = !isEnabled()            ? bg.darker().darker()
                        : getModel().isPressed()  ? bg.darker()
                        : getModel().isRollover() ? bg.brighter() : bg;
                g2.setColor(c); g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose(); super.paintComponent(g);
            }
        };
        btn.setFont(F_BTN); btn.setForeground(fg);
        btn.setContentAreaFilled(false); btn.setFocusPainted(false); btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        return btn;
    }

    private JTextField styledField(int cols) {
        JTextField tf = new JTextField(cols);
        tf.setFont(F_INPUT);
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 210, 225)),
            BorderFactory.createEmptyBorder(3, 7, 3, 7)));
        tf.setPreferredSize(new Dimension(tf.getPreferredSize().width, 30));
        return tf;
    }

    private JTextArea mkArea(int rows) {
        JTextArea ta = new JTextArea(rows, 14);
        ta.setFont(F_INPUT); ta.setLineWrap(true); ta.setWrapStyleWord(true);
        return ta;
    }

    private JScrollPane wrapScroll(JTextArea ta, int height) {
        JScrollPane sp = new JScrollPane(ta);
        sp.setBorder(BorderFactory.createLineBorder(new Color(200, 210, 225)));
        sp.setPreferredSize(new Dimension(0, height));
        return sp;
    }

    // ══════════════════════════════════════════════════════════════════════════
    // SHADOW BORDER
    // ══════════════════════════════════════════════════════════════════════════
    static class ShadowBorder extends AbstractBorder {
        @Override public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            for (int i = 4; i > 0; i--) {
                g2.setColor(new Color(0, 0, 0, 8 * i));
                g2.drawRoundRect(x + i, y + i, w - i * 2 - 1, h - i * 2 - 1, 10, 10);
            }
            g2.setColor(new Color(210, 220, 235));
            g2.drawRoundRect(x, y, w - 1, h - 1, 10, 10);
            g2.dispose();
        }
        @Override public Insets getBorderInsets(Component c) { return new Insets(6, 6, 6, 6); }
        @Override public Insets getBorderInsets(Component c, Insets i) { i.set(6,6,6,6); return i; }
    }
}