package GUI;

import DAO.NhanVienDAO;
import DTO.NhanVienDTO;
import java.awt.*;
import java.awt.event.*;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class NhanVienPanel extends JPanel {

    // =========================================================================
    // MÃ MÀU ĐỒNG BỘ GIAO DIỆN BÁN HÀNG
    // =========================================================================
    private static final Color PRIMARY_BLUE  = new Color(25, 118, 210); 
    private static final Color ACCENT_YELLOW = new Color(253, 216, 53); 
    private static final Color BG_LIGHT      = new Color(245, 247, 250); 
    
    private static final Color GRAY_BTN      = new Color(134, 142, 150);
    private static final Color GRAY_DARK     = new Color(108, 117, 125);
    private static final Color ORANGE_BTN    = new Color(243, 156, 18);
    private static final Color ORANGE_DARK   = new Color(211, 84, 0);
    private static final Color RED_BTN       = new Color(220, 53, 69);
    private static final Color RED_DARK      = new Color(180, 30, 45);
    
    private static final Color TABLE_HEADER_BG = new Color(240, 248, 255);
    private static final Color BORDER_COLOR  = new Color(222, 226, 230);
    private static final Color TEXT_PRIMARY  = new Color(33, 37, 41);
    private static final Color ROW_SELECTED  = new Color(220, 243, 229);
    private static final Color ROW_ALT       = new Color(250, 252, 251);
    private static final Color WHITE         = Color.WHITE;

    private static final Font FONT_TITLE  = new Font("Segoe UI", Font.BOLD, 22);
    private static final Font FONT_NORMAL = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font FONT_TABLE  = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 14);

    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");

    // =========================================================================
    // DATA & UI COMPONENTS
    // =========================================================================
    private final NhanVienDAO dao = new NhanVienDAO();
    private List<NhanVienDTO> employeeList = new ArrayList<>();
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField txtSearch;
    private JComboBox<String> cbFilterVaiTro;
    private JComboBox<String> cbFilterTrangThai;

    // =========================================================================
    // CONSTRUCTOR
    // =========================================================================
    public NhanVienPanel() {
        SDF.setLenient(false); 
        
        setLayout(new BorderLayout(0, 15));
        setBackground(BG_LIGHT);
        setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        buildUI();
        loadDataFromDB();
    }

    private void loadDataFromDB() {
        employeeList.clear();
        List<NhanVienDTO> data = dao.getAll();
        if (data != null) {
            employeeList.addAll(data);
        }
        refreshTable();
    }

    // =========================================================================
    // PHIÊN DỊCH DỮ LIỆU (MAPPER) 
    // =========================================================================
    private String formatVaiTro(String raw) {
        if (raw == null) return "";
        if (raw.equalsIgnoreCase("NhanVienBanHang")) return "Nhân viên bán hàng";
        if (raw.equalsIgnoreCase("QuanLy")) return "Quản lý";
        return raw; 
    }

    private String parseVaiTro(String display) {
        if (display == null) return "";
        if (display.equals("Nhân viên bán hàng")) return "NhanVienBanHang";
        if (display.equals("Quản lý")) return "QuanLy";
        return display; 
    }

    private String formatTrangThai(String raw) {
        if (raw == null) return "";
        if (raw.equalsIgnoreCase("DangLam") || raw.equals("1")) return "Đang làm việc";
        if (raw.equalsIgnoreCase("NghiViec") || raw.equals("0")) return "Đã nghỉ việc";
        return raw;
    }

    private String parseTrangThai(String display) {
        if (display == null) return "";
        if (display.equals("Đang làm việc")) return "DangLam";
        if (display.equals("Đã nghỉ việc")) return "NghiViec";
        return display;
    }

    // =========================================================================
    // BUILD UI
    // =========================================================================
    private void buildUI() {
        JPanel topHeader = new JPanel(new BorderLayout());
        topHeader.setBackground(PRIMARY_BLUE);
        topHeader.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitle = new JLabel("QUẢN LÝ NHÂN VIÊN");
        lblTitle.setFont(FONT_TITLE);
        lblTitle.setForeground(WHITE);
        topHeader.add(lblTitle, BorderLayout.WEST);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        actionPanel.setOpaque(false);

        JButton btnThem = buildSolidBtn("+ Nhân viên", ACCENT_YELLOW, new Color(251, 192, 45), Color.BLACK);
        JButton btnExport = buildOutlineBtn("Xuất file");
        
        btnThem.addActionListener(e -> showThemNhanVienDialog());

        actionPanel.add(btnExport);
        actionPanel.add(btnThem);
        topHeader.add(actionPanel, BorderLayout.EAST);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 0));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20)); 

        JPanel toolBar = new JPanel(new BorderLayout());
        toolBar.setBackground(WHITE);
        toolBar.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_COLOR, 1, true),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchPanel.setOpaque(false);
        
        cbFilterVaiTro = makeMaterialCombo(new String[]{"Tất cả vai trò", "Nhân viên bán hàng", "Quản lý"});
        cbFilterVaiTro.setPreferredSize(new Dimension(160, 35));
        cbFilterVaiTro.addActionListener(e -> doSearch()); 

        cbFilterTrangThai = makeMaterialCombo(new String[]{"Tất cả trạng thái", "Đang làm việc", "Đã nghỉ việc"});
        cbFilterTrangThai.setPreferredSize(new Dimension(160, 35));
        cbFilterTrangThai.addActionListener(e -> doSearch()); 

        txtSearch = new JTextField(20);
        txtSearch.setFont(FONT_NORMAL);
        txtSearch.setPreferredSize(new Dimension(220, 35));
        String ph = "Tìm kiếm nhân viên...";
        txtSearch.setText(ph);
        txtSearch.setForeground(Color.GRAY);
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_COLOR, 1, true),
            BorderFactory.createEmptyBorder(4, 10, 4, 10)));
            
        txtSearch.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) { if (txtSearch.getText().equals(ph)) { txtSearch.setText(""); txtSearch.setForeground(TEXT_PRIMARY); } }
            @Override public void focusLost(FocusEvent e)   { if (txtSearch.getText().isEmpty())  { txtSearch.setText(ph);  txtSearch.setForeground(Color.GRAY); } }
        });
        txtSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override public void insertUpdate(javax.swing.event.DocumentEvent e)  { doSearch(); }
            @Override public void removeUpdate(javax.swing.event.DocumentEvent e)  { doSearch(); }
            @Override public void changedUpdate(javax.swing.event.DocumentEvent e) { doSearch(); }
        });
        
        searchPanel.add(txtSearch);
        searchPanel.add(cbFilterVaiTro);
        searchPanel.add(cbFilterTrangThai);

        JPanel rowActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        rowActions.setOpaque(false);
        
        JButton btnXem = buildSolidBtn("Xem chi tiết", new Color(46, 204, 113), new Color(39, 174, 96), WHITE);
        JButton btnSua = buildSolidBtn("Sửa", PRIMARY_BLUE, new Color(21, 101, 192), WHITE);
        JButton btnXoa = buildSolidBtn("Xóa", RED_BTN, RED_DARK, WHITE);

        btnXem.addActionListener(e -> actionViewSelected());
        btnSua.addActionListener(e -> actionEditSelected());
        btnXoa.addActionListener(e -> actionDeleteSelected());

        rowActions.add(btnXem);
        rowActions.add(btnSua);
        rowActions.add(btnXoa);

        toolBar.add(searchPanel, BorderLayout.WEST);
        toolBar.add(rowActions, BorderLayout.EAST);

        String[] cols = {"Mã NV", "Tên nhân viên", "Giới tính", "SĐT", "Vai trò", "Tình trạng"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(tableModel);
        table.setFont(FONT_TABLE);
        table.setRowHeight(45); 
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(BORDER_COLOR);
        table.setSelectionBackground(ROW_SELECTED);
        table.setSelectionForeground(TEXT_PRIMARY);
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t, Object val, boolean isSel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, val, isSel, foc, row, col);
                if (!isSel) c.setBackground(row % 2 == 0 ? WHITE : ROW_ALT);
                setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15)); 
                return c;
            }
        });

        table.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean foc, int row, int col) {
                String status = val == null ? "" : val.toString();
                boolean active = status.equals("Đang làm việc"); 
                
                JLabel badge = new JLabel(status, SwingConstants.CENTER) {
                    @Override protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(active ? new Color(46, 204, 113) : RED_BTN);
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                        g2.dispose(); super.paintComponent(g);
                    }
                };
                badge.setForeground(Color.WHITE);
                badge.setFont(new Font("Segoe UI", Font.BOLD, 12));
                badge.setOpaque(false);
                
                JPanel wrap = new JPanel(new GridBagLayout());
                wrap.setBackground(sel ? t.getSelectionBackground() : (row % 2 == 0 ? WHITE : ROW_ALT));
                badge.setPreferredSize(new Dimension(110, 26));
                wrap.add(badge);
                return wrap;
            }
        });

        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_HEADER);
        header.setBackground(TABLE_HEADER_BG);
        header.setForeground(TEXT_PRIMARY);
        header.setPreferredSize(new Dimension(0, 50));
        header.setBorder(new MatteBorder(0, 0, 1, 0, BORDER_COLOR));

        table.getColumnModel().getColumn(0).setPreferredWidth(100); 
        table.getColumnModel().getColumn(1).setPreferredWidth(250); 
        table.getColumnModel().getColumn(2).setPreferredWidth(100); 
        table.getColumnModel().getColumn(3).setPreferredWidth(150); 
        table.getColumnModel().getColumn(4).setPreferredWidth(180); 
        table.getColumnModel().getColumn(5).setPreferredWidth(150); 

        // Sửa lại: Nháy đúp 2 lần mở form Sửa
        table.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    actionEditSelected();
                }
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new MatteBorder(0, 1, 1, 1, BORDER_COLOR));
        scroll.getViewport().setBackground(WHITE);

        centerPanel.add(toolBar, BorderLayout.NORTH);
        centerPanel.add(scroll, BorderLayout.CENTER);

        add(topHeader, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }

    // =========================================================================
    // ACTION METHODS
    // =========================================================================
    private NhanVienDTO getSelectedEmployee() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng click chọn một nhân viên trên bảng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        return employeeList.get(row);
    }

    private void actionViewSelected() {
        NhanVienDTO nv = getSelectedEmployee();
        if (nv != null) showXemNhanVienDialog(nv);
    }

    private void actionEditSelected() {
        NhanVienDTO nv = getSelectedEmployee();
        if (nv != null) showSuaNhanVienDialog(nv);
    }

    private void actionDeleteSelected() {
        NhanVienDTO nv = getSelectedEmployee();
        if (nv != null) {
            int ok = JOptionPane.showConfirmDialog(this, 
                "Bạn có chắc muốn xóa nhân viên: " + nv.getTenNV() + "?\nDữ liệu sẽ không thể khôi phục.", 
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (ok == JOptionPane.YES_OPTION) {
                if (dao.delete(nv.getMaNV())) { 
                    loadDataFromDB(); 
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa thất bại do lỗi dữ liệu ràng buộc!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (NhanVienDTO nv : employeeList) {
            tableModel.addRow(new Object[]{
                nv.getMaNV(),
                nv.getTenNV(),
                nv.getGioiTinh(),
                nv.getSoDienThoai(),
                formatVaiTro(nv.getVaiTro()), 
                formatTrangThai(nv.getTrangThai()) 
            });
        }
    }

    private void doSearch() {
        if (cbFilterVaiTro == null || cbFilterTrangThai == null) return; 
        
        String kw = txtSearch.getText().trim().toLowerCase();
        String ph = "tìm kiếm nhân viên...";
        if (kw.equals(ph)) kw = ""; 

        String selectedRole = cbFilterVaiTro.getSelectedItem().toString();
        String selectedStatus = cbFilterTrangThai.getSelectedItem().toString();

        List<NhanVienDTO> allData = dao.getAll();
        employeeList.clear();

        if (allData != null) {
            for (NhanVienDTO nv : allData) {
                boolean matchKeyword = kw.isEmpty() || 
                    (nv.getTenNV() != null && nv.getTenNV().toLowerCase().contains(kw)) || 
                    String.valueOf(nv.getMaNV()).contains(kw) ||
                    (nv.getSoDienThoai() != null && nv.getSoDienThoai().contains(kw));

                boolean matchRole = selectedRole.equals("Tất cả vai trò") || 
                    formatVaiTro(nv.getVaiTro()).equals(selectedRole);

                boolean matchStatus = selectedStatus.equals("Tất cả trạng thái") || 
                    formatTrangThai(nv.getTrangThai()).equals(selectedStatus);

                if (matchKeyword && matchRole && matchStatus) {
                    employeeList.add(nv);
                }
            }
        }
        refreshTable();
    }

    // =========================================================================
    // VALIDATION (HÀM KIỂM TRA LỖI LOGIC)
    // =========================================================================
    
    private void setNumericOnly(JTextField textField) {
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text.matches("[0-9]*")) { 
                    super.replace(fb, offset, length, text, attrs);
                }
            }
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string.matches("[0-9]*")) {
                    super.insertString(fb, offset, string, attr);
                }
            }
        });
    }

    private boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) return true; 
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    private boolean isValidPhone(String phone) {
        if (phone == null || phone.isEmpty()) return true;
        return phone.matches("^0\\d{9}$");
    }

    // =========================================================================
    // DIALOGS 
    // =========================================================================
    
    private void showXemNhanVienDialog(NhanVienDTO nv) {
        JDialog dlg = new JDialog(getParentFrame(), true);
        dlg.setUndecorated(true);
        dlg.setSize(800, 400);
        dlg.setLocationRelativeTo(this);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(WHITE);
        root.setBorder(new LineBorder(new Color(200, 200, 200), 1));

        JPanel header = createCustomHeader(dlg, "Thông tin nhân viên: " + nv.getTenNV());

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(15, 10, 15, 10);

        String nSinh = nv.getNgaySinh() != null ? SDF.format(nv.getNgaySinh()) : "—";
        String nVao = nv.getNgayVaoLam() != null ? SDF.format(nv.getNgayVaoLam()) : "—";
        String vTro = formatVaiTro(nv.getVaiTro());
        String tThai = formatTrangThai(nv.getTrangThai());

        addMaterialRowLabel(form, gc, 0, "Mã NV:", String.valueOf(nv.getMaNV()), "Vai trò:", vTro);
        addMaterialRowLabel(form, gc, 1, "Họ và Tên:", nv.getTenNV(), "Tình trạng:", tThai);
        addMaterialRowLabel(form, gc, 2, "Giới tính:", nv.getGioiTinh(), "SĐT:", nv.getSoDienThoai());
        addMaterialRowLabel(form, gc, 3, "Ngày sinh:", nSinh, "Email:", nv.getEmail());
        addMaterialRowLabel(form, gc, 4, "CCCD:", nv.getCccd(), "Ngày vào làm:", nVao);
        addMaterialRowLabel(form, gc, 5, "Địa chỉ:", nv.getDiaChi(), null, null);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        footer.setBackground(WHITE);
        JButton btnBQ = buildSolidBtn("Đóng", GRAY_BTN, GRAY_DARK, WHITE);
        btnBQ.addActionListener(e -> dlg.dispose());
        footer.add(btnBQ);

        root.add(header, BorderLayout.NORTH);
        root.add(form, BorderLayout.CENTER);
        root.add(footer, BorderLayout.SOUTH);
        dlg.setContentPane(root);
        dlg.setVisible(true);
    }

    private void showThemNhanVienDialog() {
        JDialog dlg = new JDialog(getParentFrame(), true);
        dlg.setUndecorated(true);
        dlg.setSize(850, 480);
        dlg.setLocationRelativeTo(this);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(WHITE);
        root.setBorder(new LineBorder(new Color(200, 200, 200), 1));

        JPanel header = createCustomHeader(dlg, "Thêm Nhân Viên Mới");

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(15, 10, 15, 10);

        JTextField tfTen    = makeMaterialField("");
        JComboBox<String> cbGioiTinh = makeMaterialCombo(new String[]{"Nam", "Nữ"});
        
        JTextField tfCCCD   = makeMaterialField("");
        setNumericOnly(tfCCCD); 
        
        JTextField tfSDT    = makeMaterialField("");
        setNumericOnly(tfSDT); 
        
        JTextField tfEmail  = makeMaterialField("");
        JTextField tfDiaChi = makeMaterialField("");
        JTextField tfNgSinh = makeMaterialField("dd/MM/yyyy");
        JTextField tfNgVao  = makeMaterialField("dd/MM/yyyy");
        
        JComboBox<String> cbVaiTro   = makeMaterialCombo(new String[]{"Nhân viên bán hàng", "Quản lý"});
        JComboBox<String> cbTThai    = makeMaterialCombo(new String[]{"Đang làm việc", "Đã nghỉ việc"});

        addMaterialRow(form, gc, 0, "Họ và Tên *", tfTen, "Giới Tính", cbGioiTinh);
        addMaterialRow(form, gc, 1, "Số CCCD", tfCCCD, "Số Điện Thoại", tfSDT);
        addMaterialRow(form, gc, 2, "Ngày Sinh", tfNgSinh, "Email", tfEmail);
        addMaterialRow(form, gc, 3, "Ngày Vào Làm", tfNgVao, "Địa Chỉ", tfDiaChi);
        addMaterialRow(form, gc, 4, "Vai Trò", cbVaiTro, "Tình Trạng", cbTThai);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        footer.setBackground(WHITE);
        
        JButton btnLuu = buildSolidBtn("Lưu thông tin", PRIMARY_BLUE, new Color(21, 101, 192), WHITE);
        JButton btnBQ  = buildSolidBtn("Hủy bỏ", GRAY_BTN, GRAY_DARK, WHITE);
        btnBQ.addActionListener(e -> dlg.dispose());
        
        btnLuu.addActionListener(e -> {
            String ten = tfTen.getText().trim();
            if (ten.isEmpty()) { JOptionPane.showMessageDialog(dlg, "Vui lòng nhập Họ và Tên!"); return; }
            
            String email = tfEmail.getText().trim();
            if (!isValidEmail(email)) { JOptionPane.showMessageDialog(dlg, "Email không đúng định dạng!"); return; }
            
            String sdt = tfSDT.getText().trim();
            if (!sdt.isEmpty() && !isValidPhone(sdt)) { JOptionPane.showMessageDialog(dlg, "SĐT phải có 10 chữ số và bắt đầu bằng số 0!"); return; }

            Date ngaySinh = parseDate(tfNgSinh.getText());
            if (!tfNgSinh.getText().equals("dd/MM/yyyy") && ngaySinh == null) {
                JOptionPane.showMessageDialog(dlg, "Ngày sinh không đúng định dạng dd/MM/yyyy hoặc ngày không tồn tại!"); return;
            }
            Date ngayVaoLam = parseDate(tfNgVao.getText());
            if (!tfNgVao.getText().equals("dd/MM/yyyy") && ngayVaoLam == null) {
                JOptionPane.showMessageDialog(dlg, "Ngày vào làm không đúng định dạng dd/MM/yyyy hoặc ngày không tồn tại!"); return;
            }

            String vTro = parseVaiTro(cbVaiTro.getSelectedItem().toString());
            String tThai = parseTrangThai(cbTThai.getSelectedItem().toString());

            NhanVienDTO newNV = new NhanVienDTO(0, ten, cbGioiTinh.getSelectedItem().toString(), 
                sdt, email, tfDiaChi.getText().trim(), 
                ngaySinh, ngayVaoLam, vTro, tThai, tfCCCD.getText().trim());
            
            if (dao.insert(newNV)) {
                loadDataFromDB(); 
                dlg.dispose();
                JOptionPane.showMessageDialog(this, "Thêm thành công!");
            } else {
                JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi lưu vào CSDL.");
            }
        });
        
        footer.add(btnBQ);
        footer.add(btnLuu);
        
        root.add(header, BorderLayout.NORTH);
        root.add(form, BorderLayout.CENTER);
        root.add(footer, BorderLayout.SOUTH);
        dlg.setContentPane(root);
        dlg.setVisible(true);
    }

    private void showSuaNhanVienDialog(NhanVienDTO nv) {
        JDialog dlg = new JDialog(getParentFrame(), true);
        dlg.setUndecorated(true);
        dlg.setSize(850, 480);
        dlg.setLocationRelativeTo(this);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(WHITE);
        root.setBorder(new LineBorder(new Color(200, 200, 200), 1));

        JPanel header = createCustomHeader(dlg, "Cập nhật nhân viên - Mã: " + nv.getMaNV());

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(15, 10, 15, 10);

        JTextField tfTen    = makeMaterialField(nv.getTenNV());
        JComboBox<String> cbGioiTinh = makeMaterialCombo(new String[]{"Nam", "Nữ"});
        if (nv.getGioiTinh() != null) cbGioiTinh.setSelectedItem(nv.getGioiTinh());

        JTextField tfCCCD   = makeMaterialField(nv.getCccd());
        setNumericOnly(tfCCCD);

        JTextField tfSDT    = makeMaterialField(nv.getSoDienThoai());
        setNumericOnly(tfSDT);

        JTextField tfEmail  = makeMaterialField(nv.getEmail());
        JTextField tfDiaChi = makeMaterialField(nv.getDiaChi());
        JTextField tfNgSinh = makeMaterialField(nv.getNgaySinh() != null ? SDF.format(nv.getNgaySinh()) : "dd/MM/yyyy");
        JTextField tfNgVao  = makeMaterialField(nv.getNgayVaoLam() != null ? SDF.format(nv.getNgayVaoLam()) : "dd/MM/yyyy");
        
        JComboBox<String> cbVaiTro = makeMaterialCombo(new String[]{"Nhân viên bán hàng", "Quản lý"});
        if (nv.getVaiTro() != null) cbVaiTro.setSelectedItem(formatVaiTro(nv.getVaiTro()));
        
        JComboBox<String> cbTThai = makeMaterialCombo(new String[]{"Đang làm việc", "Đã nghỉ việc"});
        if (nv.getTrangThai() != null) cbTThai.setSelectedItem(formatTrangThai(nv.getTrangThai()));

        addMaterialRow(form, gc, 0, "Họ và Tên *", tfTen, "Giới Tính", cbGioiTinh);
        addMaterialRow(form, gc, 1, "Số CCCD", tfCCCD, "Số Điện Thoại", tfSDT);
        addMaterialRow(form, gc, 2, "Ngày Sinh", tfNgSinh, "Email", tfEmail);
        addMaterialRow(form, gc, 3, "Ngày Vào Làm", tfNgVao, "Địa Chỉ", tfDiaChi);
        addMaterialRow(form, gc, 4, "Vai Trò", cbVaiTro, "Tình Trạng", cbTThai);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        footer.setBackground(WHITE);
        
        JButton btnLuu = buildSolidBtn("Cập nhật", PRIMARY_BLUE, new Color(21, 101, 192), WHITE);
        JButton btnBQ  = buildSolidBtn("Hủy bỏ", GRAY_BTN, GRAY_DARK, WHITE);
        btnBQ.addActionListener(e -> dlg.dispose());
        
        btnLuu.addActionListener(e -> {
            String ten = tfTen.getText().trim();
            if (ten.isEmpty()) { JOptionPane.showMessageDialog(dlg, "Vui lòng nhập Họ và Tên!"); return; }
            
            String email = tfEmail.getText().trim();
            if (!isValidEmail(email)) { JOptionPane.showMessageDialog(dlg, "Email không đúng định dạng!"); return; }
            
            String sdt = tfSDT.getText().trim();
            if (!sdt.isEmpty() && !isValidPhone(sdt)) { JOptionPane.showMessageDialog(dlg, "SĐT phải có 10 chữ số và bắt đầu bằng số 0!"); return; }

            Date ngaySinh = parseDate(tfNgSinh.getText());
            if (!tfNgSinh.getText().equals("dd/MM/yyyy") && ngaySinh == null) {
                JOptionPane.showMessageDialog(dlg, "Ngày sinh không đúng định dạng dd/MM/yyyy hoặc ngày không tồn tại!"); return;
            }
            Date ngayVaoLam = parseDate(tfNgVao.getText());
            if (!tfNgVao.getText().equals("dd/MM/yyyy") && ngayVaoLam == null) {
                JOptionPane.showMessageDialog(dlg, "Ngày vào làm không đúng định dạng dd/MM/yyyy hoặc ngày không tồn tại!"); return;
            }

            nv.setTenNV(ten);
            nv.setGioiTinh(cbGioiTinh.getSelectedItem().toString());
            nv.setCccd(tfCCCD.getText().trim());
            nv.setSoDienThoai(sdt);
            nv.setEmail(email);
            nv.setDiaChi(tfDiaChi.getText().trim());
            nv.setNgaySinh(ngaySinh);
            nv.setNgayVaoLam(ngayVaoLam);
            nv.setVaiTro(parseVaiTro(cbVaiTro.getSelectedItem().toString()));
            nv.setTrangThai(parseTrangThai(cbTThai.getSelectedItem().toString()));
            
            if (dao.update(nv)) {
                loadDataFromDB(); 
                dlg.dispose();
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            } else {
                JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi cập nhật CSDL.");
            }
        });
        
        footer.add(btnBQ);
        footer.add(btnLuu);
        
        root.add(header, BorderLayout.NORTH);
        root.add(form, BorderLayout.CENTER);
        root.add(footer, BorderLayout.SOUTH);
        dlg.setContentPane(root);
        dlg.setVisible(true);
    }

    // =========================================================================
    // UI HELPERS & UTILS
    // =========================================================================
    
    private Date parseDate(String s) {
        if (s == null || s.trim().isEmpty() || s.equals("dd/MM/yyyy")) return null;
        try { return new Date(SDF.parse(s.trim()).getTime()); } catch (ParseException e) { return null; }
    }

    // FIX LỖI: Chống NullPointerException và chống biến mất chữ thật
    private JTextField makeMaterialField(String text) {
        if (text == null) text = ""; 

        JTextField tf = new JTextField(text); 
        tf.setFont(FONT_NORMAL);
        tf.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(0, 0, 1, 0, new Color(200, 200, 200)), 
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
            
        // Chỉ xử lý hiệu ứng placeholder nếu chữ là "dd/MM/yyyy"
        if (text.equals("dd/MM/yyyy")) {
            tf.setForeground(Color.GRAY);
            tf.addFocusListener(new FocusAdapter() {
                @Override public void focusGained(FocusEvent e) { 
                    if (tf.getText().equals("dd/MM/yyyy")) { 
                        tf.setText(""); tf.setForeground(TEXT_PRIMARY); 
                    } 
                }
                @Override public void focusLost(FocusEvent e)   { 
                    if (tf.getText().isEmpty()) { 
                        tf.setText("dd/MM/yyyy"); tf.setForeground(Color.GRAY); 
                    } 
                }
            });
        } else {
            tf.setForeground(TEXT_PRIMARY);
        }
        return tf;
    }

    private JComboBox<String> makeMaterialCombo(String[] items) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setFont(FONT_NORMAL);
        cb.setBackground(WHITE);
        cb.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(0, 0, 1, 0, new Color(200, 200, 200)), 
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        return cb;
    }

    private void addMaterialRow(JPanel p, GridBagConstraints gc, int row, String l1, JComponent f1, String l2, JComponent f2) {
        gc.gridy = row;
        gc.gridx = 0; gc.weightx = 0.05; 
        JLabel lbl1 = new JLabel(l1); lbl1.setFont(new Font("Segoe UI", Font.BOLD, 13)); p.add(lbl1, gc);
        gc.gridx = 1; gc.weightx = 0.40; p.add(f1, gc);
        gc.gridx = 2; gc.weightx = 0.10; p.add(Box.createHorizontalStrut(30), gc);
        
        if (l2 != null && f2 != null) {
            gc.gridx = 3; gc.weightx = 0.05; 
            JLabel lbl2 = new JLabel(l2); lbl2.setFont(new Font("Segoe UI", Font.BOLD, 13)); p.add(lbl2, gc);
            gc.gridx = 4; gc.weightx = 0.40; p.add(f2, gc);
        }
    }

    private void addMaterialRowLabel(JPanel p, GridBagConstraints gc, int row, String l1, String v1, String l2, String v2) {
        gc.gridy = row;
        gc.gridx = 0; gc.weightx = 0.1; 
        JLabel lbl1 = new JLabel(l1); lbl1.setFont(new Font("Segoe UI", Font.BOLD, 13)); lbl1.setForeground(Color.GRAY); p.add(lbl1, gc);
        gc.gridx = 1; gc.weightx = 0.4; 
        JLabel val1 = new JLabel(v1 != null && !v1.isEmpty() ? v1 : "—"); val1.setFont(FONT_NORMAL); p.add(val1, gc);
        
        if (l2 != null) {
            gc.gridx = 2; gc.weightx = 0.1; 
            JLabel lbl2 = new JLabel(l2); lbl2.setFont(new Font("Segoe UI", Font.BOLD, 13)); lbl2.setForeground(Color.GRAY); p.add(lbl2, gc);
            gc.gridx = 3; gc.weightx = 0.4; 
            JLabel val2 = new JLabel(v2 != null && !v2.isEmpty() ? v2 : "—"); val2.setFont(FONT_NORMAL); p.add(val2, gc);
        }
    }

    private JPanel createCustomHeader(JDialog dlg, String title) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(TABLE_HEADER_BG);
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(PRIMARY_BLUE);
        
        JLabel lblClose = new JLabel("X");
        lblClose.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblClose.setForeground(Color.GRAY);
        lblClose.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblClose.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { dlg.dispose(); }
            @Override public void mouseEntered(MouseEvent e) { lblClose.setForeground(RED_BTN); }
            @Override public void mouseExited(MouseEvent e)  { lblClose.setForeground(Color.GRAY); }
        });
        
        header.add(lblTitle, BorderLayout.WEST);
        header.add(lblClose, BorderLayout.EAST);

        Point[] initialClick = new Point[1];
        header.addMouseListener(new MouseAdapter() { 
            @Override public void mousePressed(MouseEvent e) { initialClick[0] = e.getPoint(); }
        });
        header.addMouseMotionListener(new MouseMotionAdapter() {
            @Override public void mouseDragged(MouseEvent e) {
                int thisX = dlg.getLocation().x; int thisY = dlg.getLocation().y;
                int xMoved = e.getX() - initialClick[0].x; int yMoved = e.getY() - initialClick[0].y;
                dlg.setLocation(thisX + xMoved, thisY + yMoved);
            }
        });
        return header;
    }

    private JFrame getParentFrame() {
        Window w = SwingUtilities.getWindowAncestor(this);
        return (w instanceof JFrame) ? (JFrame) w : null;
    }

    private JButton buildSolidBtn(String text, Color bg, Color hover, Color fg) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? hover : bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose(); super.paintComponent(g);
            }
        };
        btn.setForeground(fg); btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setContentAreaFilled(false); btn.setBorderPainted(false); btn.setFocusPainted(false); 
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        return btn;
    }

    private JButton buildOutlineBtn(String text) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2.setColor(new Color(255, 255, 255, 40)); 
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                }
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 8, 8); 
                g2.dispose(); super.paintComponent(g);
            }
        };
        btn.setForeground(Color.WHITE); btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setContentAreaFilled(false); btn.setBorderPainted(false); btn.setFocusPainted(false); 
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        return btn;
    }
}