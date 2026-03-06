package GUI;

import DAO.NhaCungCapDAO;
import DTO.NhaCungCapDTO;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class NhaCungCapPanel extends JPanel {

    // =========================================================================
    // MÃ MÀU ĐỒNG BỘ GIAO DIỆN BÁN HÀNG
    // =========================================================================
    private static final Color PRIMARY_BLUE  = new Color(25, 118, 210); // Xanh dương đậm (Header)
    private static final Color ACCENT_YELLOW = new Color(253, 216, 53); // Vàng nổi bật (Nút Thêm)
    private static final Color BG_LIGHT      = new Color(245, 247, 250); // Nền xám nhạt toàn trang
    
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

    // =========================================================================
    // DATA
    // =========================================================================
    private final List<NhaCungCapDTO> supplierList = new ArrayList<>();
    private final List<NhaCungCapDTO> filteredList = new ArrayList<>();
    private final NhaCungCapDAO nccDAO = new NhaCungCapDAO();

    // =========================================================================
    // UI COMPONENTS
    // =========================================================================
    private DefaultTableModel tableModel;
    private JTable            table;
    private JTextField        txtSearch;

    public NhaCungCapPanel() {
        setLayout(new BorderLayout(0, 15)); // Khoảng cách giữa các thành phần
        setBackground(BG_LIGHT); 
        setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0)); // Bỏ margin top để header sát lề trên
        
        buildUI();       
        loadDataFromDB(); 
    }

    private void loadDataFromDB() {
        supplierList.clear();
        List<NhaCungCapDTO> data = nccDAO.selectAll();
        
        if (data != null && !data.isEmpty()) {
            supplierList.addAll(data);
        }
        
        filteredList.clear();
        filteredList.addAll(supplierList);
        if (tableModel != null) {
            refreshTable();
        }
    }

    private String formatMa(int id) {
        if (id < 10)   return "NCC00000" + id;
        if (id < 100)  return "NCC0000"  + id;
        if (id < 1000) return "NCC000"   + id;
        return "NCC" + id;
    }

    // =========================================================================
    // BUILD UI
    // =========================================================================
    private void buildUI() {
        // 1. THANH HEADER (MÀU XANH DƯƠNG ĐẬM NHƯ QUẢN LÝ BÁN HÀNG)
        JPanel topHeader = new JPanel(new BorderLayout());
        topHeader.setBackground(PRIMARY_BLUE);
        topHeader.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // Nếu bạn có icon màu trắng, hãy bỏ comment dòng dưới và thêm đường dẫn icon
        // JLabel lblTitle = new JLabel(" QUẢN LÝ NHÀ CUNG CẤP", new ImageIcon(getClass().getResource("/icons/supplier_white.png")), JLabel.LEFT);
        JLabel lblTitle = new JLabel("QUẢN LÝ NHÀ CUNG CẤP");
        lblTitle.setFont(FONT_TITLE);
        lblTitle.setForeground(WHITE);
        topHeader.add(lblTitle, BorderLayout.WEST);

        // Các nút bên phải trên nền xanh
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        actionPanel.setOpaque(false);

        // Nút Thêm (Màu Vàng chữ đen)
        JButton btnThem = buildSolidBtn("+ Nhà cung cấp", ACCENT_YELLOW, new Color(251, 192, 45), Color.BLACK);
        
        // Nút Import / Xuất file (Viền trắng, nền trong suốt)
        JButton btnImport = buildOutlineBtn("Import");
        JButton btnExport = buildOutlineBtn("Xuất file");
        
        btnThem.addActionListener(e -> showThemDialog());

        actionPanel.add(btnImport);
        actionPanel.add(btnExport);
        actionPanel.add(btnThem);
        topHeader.add(actionPanel, BorderLayout.EAST);

        // 2. PHẦN CENTER (TÌM KIẾM + BẢNG)
        JPanel centerPanel = new JPanel(new BorderLayout(0, 15));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20)); // Margin hai bên cho phần ruột

        // --- Thanh tìm kiếm ---
        JPanel searchBarPanel = new JPanel(new BorderLayout(10, 0));
        searchBarPanel.setBackground(WHITE);
        searchBarPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_COLOR, 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 5)));

        txtSearch = new JTextField();
        txtSearch.setFont(FONT_NORMAL);
        txtSearch.setBorder(BorderFactory.createEmptyBorder()); // Bỏ viền mặc định của Textfield
        String ph = "Tìm kiếm theo mã, tên, điện thoại nhà cung cấp...";
        txtSearch.setText(ph);
        txtSearch.setForeground(Color.GRAY);
            
        txtSearch.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { if (txtSearch.getText().equals(ph)) { txtSearch.setText(""); txtSearch.setForeground(TEXT_PRIMARY); } }
            public void focusLost(FocusEvent e)   { if (txtSearch.getText().isEmpty())  { txtSearch.setText(ph);  txtSearch.setForeground(Color.GRAY); } }
        });
        txtSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e)  { doSearch(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e)  { doSearch(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { doSearch(); }
        });

        JButton btnTimKiem = buildSolidBtn("Tìm kiếm", PRIMARY_BLUE, new Color(21, 101, 192), WHITE);
        btnTimKiem.setPreferredSize(new Dimension(120, 35));

        searchBarPanel.add(txtSearch, BorderLayout.CENTER);
        searchBarPanel.add(btnTimKiem, BorderLayout.EAST);

        // --- Bảng Dữ Liệu ---
        JPanel tableWrapper = new JPanel(new BorderLayout());
        tableWrapper.setBackground(WHITE);
        tableWrapper.setBorder(new LineBorder(BORDER_COLOR, 1, true));

        String[] cols = {"Mã nhà cung cấp", "Tên nhà cung cấp", "Điện thoại", "Email", "Địa chỉ", "Trạng thái"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public Class<?> getColumnClass(int columnIndex) { return String.class; }
            @Override public boolean isCellEditable(int rowIndex, int columnIndex) { return false; }
        };

        table = new JTable(tableModel);
        table.setFont(FONT_TABLE);
        table.setRowHeight(45); // Tăng chiều cao dòng cho thoáng giống trang Bán hàng
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(BORDER_COLOR);
        table.setSelectionBackground(ROW_SELECTED);
        table.setSelectionForeground(TEXT_PRIMARY);
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table.setDefaultRenderer(String.class, new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t, Object val, boolean isSel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, val, isSel, foc, row, col);
                if (!isSel) c.setBackground(row % 2 == 0 ? WHITE : ROW_ALT);
                setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15)); // Padding chữ trong bảng
                return c;
            }
        });

        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_HEADER);
        header.setBackground(TABLE_HEADER_BG);
        header.setForeground(TEXT_PRIMARY);
        header.setPreferredSize(new Dimension(0, 50));
        header.setBorder(new MatteBorder(0, 0, 1, 0, BORDER_COLOR));
        ((DefaultTableCellRenderer)header.getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);

        table.getColumnModel().getColumn(0).setPreferredWidth(130); 
        table.getColumnModel().getColumn(1).setPreferredWidth(250); 
        table.getColumnModel().getColumn(2).setPreferredWidth(120); 
        table.getColumnModel().getColumn(3).setPreferredWidth(180); 
        table.getColumnModel().getColumn(4).setPreferredWidth(250); 
        table.getColumnModel().getColumn(5).setPreferredWidth(120); 

        // NHÁY ĐÚP CHUỘT MỞ BẢNG CHI TIẾT
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    actionEditSelected();
                }
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(WHITE);

        tableWrapper.add(scroll, BorderLayout.CENTER);

        // Ráp vào Center Panel
        centerPanel.add(searchBarPanel, BorderLayout.NORTH);
        centerPanel.add(tableWrapper, BorderLayout.CENTER);

        // Gắn vào Main Panel
        add(topHeader, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }

    private NhaCungCapDTO getSelectedSupplier() {
        int row = table.getSelectedRow();
        if (row == -1) return null;
        return filteredList.get(row);
    }

    private void actionEditSelected() {
        NhaCungCapDTO ncc = getSelectedSupplier();
        if (ncc != null) showCapNhatDialog(ncc);
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (NhaCungCapDTO ncc : filteredList) {
            tableModel.addRow(new Object[]{
                formatMa(ncc.getMaNCC()),
                ncc.getTenNCC(),
                ncc.getSDT(),
                ncc.getEmail(),
                ncc.getDiaChi(),
                ncc.getTrangThai() == 1 ? "Đang hợp tác" : "Ngừng hợp tác"
            });
        }
    }

    private void doSearch() {
        String kw = txtSearch.getText().trim().toLowerCase();
        String ph = "tìm kiếm theo mã, tên, điện thoại nhà cung cấp...";
        filteredList.clear();
        if (kw.isEmpty() || kw.equals(ph)) {
            filteredList.addAll(supplierList);
        } else {
            for (NhaCungCapDTO ncc : supplierList) {
                String maStr = formatMa(ncc.getMaNCC()).toLowerCase();
                String tenStr = ncc.getTenNCC() != null ? ncc.getTenNCC().toLowerCase() : "";
                String sdtStr = ncc.getSDT() != null ? ncc.getSDT().toLowerCase() : "";
                
                if (maStr.contains(kw) || tenStr.contains(kw) || sdtStr.contains(kw)) {
                    filteredList.add(ncc);
                }
            }
        }
        refreshTable();
    }

    // =========================================================================
    // DIALOGS KHÔNG CÓ KÝ TỰ LỖI
    // =========================================================================
    
    private void showThemDialog() {
        JDialog dlg = new JDialog(getParentFrame(), true);
        dlg.setUndecorated(true);
        dlg.setSize(800, 300);
        dlg.setLocationRelativeTo(this);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(WHITE);
        root.setBorder(new LineBorder(new Color(200, 200, 200), 1));

        JPanel header = createCustomHeader(dlg, "Thêm nhà cung cấp");

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(15, 10, 15, 10);

        JTextField tfMa     = makeMaterialField("Mã mặc định (Tự động)"); tfMa.setEditable(false); tfMa.setBackground(WHITE);
        JTextField tfTen    = makeMaterialField("");
        JTextField tfSDT    = makeMaterialField("");
        JTextField tfEmail  = makeMaterialField("");
        JTextField tfDiaChi = makeMaterialField("");

        addMaterialRow(form, gc, 0, "Mã nhà cung cấp", tfMa, "Email", tfEmail);
        addMaterialRow(form, gc, 1, "Tên nhà cung cấp", tfTen, "Điện thoại", tfSDT);
        addMaterialRow(form, gc, 2, "Địa chỉ", tfDiaChi, null, null);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        footer.setBackground(WHITE);
        
        JButton btnLuu = buildSolidBtn("Lưu thông tin", PRIMARY_BLUE, new Color(21, 101, 192), WHITE);
        JButton btnBQ  = buildSolidBtn("Hủy bỏ", GRAY_BTN, GRAY_DARK, WHITE);
        btnBQ.addActionListener(e -> dlg.dispose());
        
        btnLuu.addActionListener(e -> {
            String ten = tfTen.getText().trim();
            if (ten.isEmpty()) { JOptionPane.showMessageDialog(dlg, "Vui lòng nhập tên nhà cung cấp!"); return; }
            
            int newId = supplierList.stream().mapToInt(NhaCungCapDTO::getMaNCC).max().orElse(0) + 1;
            NhaCungCapDTO newNCC = new NhaCungCapDTO(newId, ten, tfSDT.getText().trim(), tfEmail.getText().trim(), tfDiaChi.getText().trim(), 1);
            
            if (nccDAO.insert(newNCC)) {
                loadDataFromDB(); 
                dlg.dispose();
                JOptionPane.showMessageDialog(this, "Thêm nhà cung cấp thành công!");
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

    private void showCapNhatDialog(NhaCungCapDTO ncc) {
        JDialog dlg = new JDialog(getParentFrame(), true);
        dlg.setUndecorated(true);
        dlg.setSize(800, 300); 
        dlg.setLocationRelativeTo(this);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(WHITE);
        root.setBorder(new LineBorder(new Color(200, 200, 200), 1));

        JPanel header = createCustomHeader(dlg, "Thông tin chi tiết nhà cung cấp");

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(15, 10, 15, 10);

        JTextField tfMa     = makeMaterialField(formatMa(ncc.getMaNCC())); tfMa.setEditable(false); tfMa.setBackground(WHITE);
        JTextField tfTen    = makeMaterialField(ncc.getTenNCC());
        JTextField tfSDT    = makeMaterialField(ncc.getSDT());
        JTextField tfEmail  = makeMaterialField(ncc.getEmail());
        JTextField tfDiaChi = makeMaterialField(ncc.getDiaChi());

        addMaterialRow(form, gc, 0, "Mã nhà cung cấp", tfMa, "Email", tfEmail);
        addMaterialRow(form, gc, 1, "Tên nhà cung cấp", tfTen, "Điện thoại", tfSDT);
        addMaterialRow(form, gc, 2, "Địa chỉ", tfDiaChi, null, null);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        footer.setBackground(WHITE);
        
        JButton btnSua       = buildSolidBtn("Cập nhật", PRIMARY_BLUE, new Color(21, 101, 192), WHITE);
        JButton btnTrangThai = buildSolidBtn("Đổi Trạng Thái", ORANGE_BTN, ORANGE_DARK, WHITE);
        JButton btnXoa       = buildSolidBtn("Xóa", RED_BTN, RED_DARK, WHITE);
        JButton btnBQ        = buildSolidBtn("Đóng", GRAY_BTN, GRAY_DARK, WHITE);
        
        btnSua.addActionListener(e -> {
            ncc.setTenNCC(tfTen.getText().trim());
            ncc.setSDT(tfSDT.getText().trim());
            ncc.setEmail(tfEmail.getText().trim());
            ncc.setDiaChi(tfDiaChi.getText().trim());
            
            if (nccDAO.update(ncc)) {
                loadDataFromDB();
                dlg.dispose();
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            } else {
                JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi cập nhật CSDL!");
            }
        });

        btnTrangThai.addActionListener(e -> {
            boolean isActive = ncc.getTrangThai() == 1;
            String msg = isActive
                ? "Đổi trạng thái thành Ngừng hoạt động cho nhà cung cấp này?"
                : "Cho phép nhà cung cấp này Hoạt động trở lại?";
            
            int confirm = JOptionPane.showConfirmDialog(dlg, msg, "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                int trangThaiMoi = isActive ? 2 : 1;
                if (nccDAO.updateTrangThai(ncc.getMaNCC(), trangThaiMoi)) {
                    loadDataFromDB();
                    dlg.dispose();
                }
            }
        });

        btnXoa.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(dlg, "Xóa hoàn toàn nhà cung cấp này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                if (nccDAO.delete(ncc.getMaNCC())) {
                    loadDataFromDB();
                    dlg.dispose();
                } else {
                    JOptionPane.showMessageDialog(dlg, "Xóa thất bại do vướng dữ liệu khóa ngoại.");
                }
            }
        });

        btnBQ.addActionListener(e -> dlg.dispose());
        
        footer.add(btnSua);
        footer.add(btnTrangThai);
        footer.add(btnXoa);
        footer.add(btnBQ);
        
        root.add(header, BorderLayout.NORTH);
        root.add(form, BorderLayout.CENTER);
        root.add(footer, BorderLayout.SOUTH);
        
        dlg.setContentPane(root);
        dlg.setVisible(true);
    }

    // =========================================================================
    // UI HELPERS (XÂY DỰNG GIAO DIỆN)
    // =========================================================================
    
    private JTextField makeMaterialField(String textOrPlaceholder) {
        JTextField tf = new JTextField(); 
        tf.setFont(FONT_NORMAL);
        tf.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(0, 0, 1, 0, new Color(200, 200, 200)), 
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
            
        boolean isPlaceholder = textOrPlaceholder.startsWith("Mã mặc định") || textOrPlaceholder.isEmpty();
        
        if (textOrPlaceholder != null && !textOrPlaceholder.isEmpty()) {
            tf.setText(textOrPlaceholder); 
            if (isPlaceholder) tf.setForeground(Color.GRAY);
            else tf.setForeground(TEXT_PRIMARY);
            
            if (isPlaceholder) {
                tf.addFocusListener(new FocusAdapter() {
                    public void focusGained(FocusEvent e) { if (tf.getText().equals(textOrPlaceholder)) { tf.setText(""); tf.setForeground(TEXT_PRIMARY); } }
                    public void focusLost(FocusEvent e)   { if (tf.getText().isEmpty())  { tf.setText(textOrPlaceholder); tf.setForeground(Color.GRAY); } }
                });
            }
        }
        return tf;
    }

    private void addMaterialRow(JPanel p, GridBagConstraints gc, int row, String l1, JComponent f1, String l2, JComponent f2) {
        gc.gridy = row;
        
        gc.gridx = 0; gc.weightx = 0.05; 
        JLabel lbl1 = new JLabel(l1); 
        lbl1.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl1.setForeground(TEXT_PRIMARY);
        p.add(lbl1, gc);
        
        gc.gridx = 1; gc.weightx = 0.40;
        p.add(f1, gc);
        
        gc.gridx = 2; gc.weightx = 0.10;
        p.add(Box.createHorizontalStrut(40), gc);
        
        if (l2 != null && f2 != null) {
            gc.gridx = 3; gc.weightx = 0.05;
            JLabel lbl2 = new JLabel(l2); 
            lbl2.setFont(new Font("Segoe UI", Font.BOLD, 13));
            lbl2.setForeground(TEXT_PRIMARY);
            p.add(lbl2, gc);
            
            gc.gridx = 4; gc.weightx = 0.40;
            p.add(f2, gc);
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
            public void mouseClicked(MouseEvent e) { dlg.dispose(); }
            public void mouseEntered(MouseEvent e) { lblClose.setForeground(RED_BTN); }
            public void mouseExited(MouseEvent e)  { lblClose.setForeground(Color.GRAY); }
        });
        
        header.add(lblTitle, BorderLayout.WEST);
        header.add(lblClose, BorderLayout.EAST);

        Point[] initialClick = new Point[1];
        header.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) { initialClick[0] = e.getPoint(); }
        });
        header.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                int thisX = dlg.getLocation().x;
                int thisY = dlg.getLocation().y;
                int xMoved = e.getX() - initialClick[0].x;
                int yMoved = e.getY() - initialClick[0].y;
                dlg.setLocation(thisX + xMoved, thisY + yMoved);
            }
        });
        
        return header;
    }

    private JFrame getParentFrame() {
        Window w = SwingUtilities.getWindowAncestor(this);
        return (w instanceof JFrame) ? (JFrame) w : null;
    }

    // Nút có nền màu (Solid Button)
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
        btn.setForeground(fg); 
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setContentAreaFilled(false); 
        btn.setBorderPainted(false);
        btn.setFocusPainted(false); 
        btn.setOpaque(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        return btn;
    }

    // Nút có viền, nền trong suốt (Outline Button dùng cho Header)
    private JButton buildOutlineBtn(String text) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2.setColor(new Color(255, 255, 255, 40)); // Nền trắng mờ khi hover
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                }
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 8, 8); // Vẽ viền trắng
                g2.dispose(); super.paintComponent(g);
            }
        };
        btn.setForeground(Color.WHITE); 
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setContentAreaFilled(false); 
        btn.setBorderPainted(false);
        btn.setFocusPainted(false); 
        btn.setOpaque(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        return btn;
    }
}