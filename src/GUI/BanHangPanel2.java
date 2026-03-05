package GUI;

import BUS.SanPhamBUS;
import DTO.SanPhamDTO;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.text.NumberFormat;
import java.util.Locale;

public class BanHangPanel2 extends JPanel {

    // ===== COLORS =====
    private static final Color PRIMARY      = new Color(21, 101, 192);
    private static final Color PRIMARY_DARK = new Color(10, 60, 130);
    private static final Color CONTENT_BG   = new Color(236, 242, 250);
    private static final Color WHITE        = Color.WHITE;
    private static final Color ROW_ALT      = new Color(245, 250, 255);
    private static final Color TABLE_HEADER = new Color(21, 101, 192);
    private static final Color SUCCESS      = new Color(46, 125, 50);
    private static final Color DANGER       = new Color(198, 40, 40);
    private static final Color TAB_ACTIVE   = new Color(0, 188, 212);
    private static final Color TAB_INACTIVE = new Color(180, 210, 240);
    private static final Color CARD_BG      = Color.WHITE;
    private static final Color CARD_HOVER   = new Color(227, 242, 253);
    private static final Color CARD_BORDER  = new Color(187, 222, 251);
    private static final Color PRICE_COLOR  = new Color(198, 40, 40);

    // ===== FONTS =====
    private static final Font FONT_TITLE      = new Font("Segoe UI", Font.BOLD, 20);
    private static final Font FONT_LABEL      = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font FONT_NORMAL     = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font FONT_SMALL      = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Font FONT_TOTAL      = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font FONT_TAB        = new Font("Segoe UI", Font.BOLD, 12);
    private static final Font FONT_CARD_NAME  = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font FONT_CARD_PRICE = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font FONT_CARD_BRAND = new Font("Segoe UI", Font.PLAIN, 11);

    // ===== STATE =====
    private JTextField txtSearch;
    private JTextField txtMaHD, txtNgayLap;
    private JPanel productGrid;
    private JScrollPane gridScroll;
    private String activeHangKey = "hang_Tat ca";
    private String activePkKey   = "";
    private ArrayList<JButton> allHangBtns = new ArrayList<>();
    private ArrayList<JButton> allPkBtns   = new ArrayList<>();

    // Keys ASCII (dung de so sanh) va nhan hien thi tieng Viet UTF-8
    private static final String[] HANG_KEYS    = {"Tat ca","ASUS","DELL","ACER","HP","MSI","GIGABYTE","LENOVO","APPLE"};
    private static final String[] HANG_DISPLAY = {"Tất cả","ASUS","DELL","ACER","HP","MSI","GIGABYTE","LENOVO","APPLE"};
    private static final String[] PK_KEYS      = {"Chuot","Ban phim","Tai nghe","Man hinh","RAM"};
    private static final String[] PK_DISPLAY   = {"Chuột","Bàn phím","Tai nghe","Màn hình","RAM"};

    private SanPhamBUS sanPhamBUS = new SanPhamBUS();
    private ArrayList<SanPhamDTO> allProducts = new ArrayList<>();
    private ArrayList<Object[]>   cartItems   = new ArrayList<>();

    public BanHangPanel2() {
        setLayout(new BorderLayout(8, 8));
        setBackground(CONTENT_BG);
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        add(createTitlePanel(), BorderLayout.NORTH);
        add(createMainPanel(),  BorderLayout.CENTER);
        loadProductData();
    }

    private void loadProductData() {
        allProducts = sanPhamBUS.getAll();
        renderProductGrid(allProducts);
    }

    private void renderProductGrid(ArrayList<SanPhamDTO> list) {
        productGrid.removeAll();
        for (SanPhamDTO sp : list) {
            if (!sp.getTrangThai().equals("DangBan")) continue;
            productGrid.add(createProductCard(sp));
        }
        productGrid.revalidate();
        productGrid.repaint();
    }

    // ============================
    // CARD SAN PHAM
    // ============================
    private JPanel createProductCard(SanPhamDTO sp) {
        JPanel card = new JPanel(new BorderLayout(0, 0)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.dispose();
            }
            @Override protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BORDER);
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 14, 14);
                g2.dispose();
            }
        };
        card.setBackground(CARD_BG); card.setOpaque(false);
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JPanel imgPanel = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(240, 245, 255));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose(); super.paintComponent(g);
            }
        };
        imgPanel.setOpaque(false); imgPanel.setPreferredSize(new Dimension(0, 130));
        JLabel imgLabel = new JLabel(createProductIcon(sp), SwingConstants.CENTER);
        imgLabel.setOpaque(false); imgPanel.add(imgLabel, BorderLayout.CENTER);

        JLabel badge = new JLabel("  Còn " + sp.getSoLuongTon() + "  ");
        badge.setFont(new Font("Segoe UI", Font.BOLD, 10)); badge.setForeground(WHITE);
        badge.setBackground(sp.getSoLuongTon() > 0 ? new Color(46, 125, 50) : DANGER);
        badge.setOpaque(true); badge.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
        JPanel badgeWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 4));
        badgeWrap.setOpaque(false); badgeWrap.add(badge);
        imgPanel.add(badgeWrap, BorderLayout.SOUTH);
        card.add(imgPanel, BorderLayout.CENTER);

        JPanel info = new JPanel(); info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setOpaque(false); info.setBorder(BorderFactory.createEmptyBorder(8, 2, 4, 2));
        JLabel lblBrand = new JLabel(sp.getThuongHieu().toUpperCase());
        lblBrand.setFont(FONT_CARD_BRAND); lblBrand.setForeground(new Color(100, 130, 170));
        lblBrand.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lblName = new JLabel("<html><body style='width:130px'>" + sp.getTenSP() + "</body></html>");
        lblName.setFont(FONT_CARD_NAME); lblName.setForeground(PRIMARY_DARK);
        lblName.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lblPrice = new JLabel(formatMoney(sp.getGia().doubleValue()) + " đ");
        lblPrice.setFont(FONT_CARD_PRICE); lblPrice.setForeground(PRICE_COLOR);
        lblPrice.setAlignmentX(Component.LEFT_ALIGNMENT);
        info.add(lblBrand); info.add(Box.createVerticalStrut(3));
        info.add(lblName);  info.add(Box.createVerticalStrut(5)); info.add(lblPrice);
        card.add(info, BorderLayout.SOUTH);

        MouseAdapter ma = new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { card.setBackground(CARD_HOVER); card.repaint(); }
            @Override public void mouseExited(MouseEvent e)  { card.setBackground(CARD_BG);   card.repaint(); }
            @Override public void mouseClicked(MouseEvent e) { addToCart(sp); }
        };
        for (Component c : new Component[]{card, imgPanel, imgLabel, badgeWrap, badge, info, lblBrand, lblName, lblPrice})
            c.addMouseListener(ma);
        return card;
    }

    private ImageIcon createProductIcon(SanPhamDTO sp) {
        int w = 100, h = 85;
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        Color ic = getBrandColor(sp.getThuongHieu());
        g2.setColor(new Color(210, 220, 238)); g2.fillRoundRect(4, 5, w-8, h-22, 8, 8);
        g2.setColor(ic); g2.fillRoundRect(8, 9, w-16, h-32, 6, 6);
        g2.setColor(new Color(235, 245, 255)); g2.fillRect(12, 13, w-24, h-42);
        g2.setColor(ic.darker()); g2.setFont(new Font("Arial", Font.BOLD, 11));
        String bt = sp.getThuongHieu().length() > 5 ? sp.getThuongHieu().substring(0, 5) : sp.getThuongHieu();
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(bt, (w - fm.stringWidth(bt)) / 2, 13 + (h - 42 - fm.getHeight()) / 2 + fm.getAscent());
        g2.setColor(new Color(185, 198, 218)); g2.fillRoundRect(1, h-18, w-2, 13, 5, 5);
        g2.setColor(new Color(160, 172, 192)); g2.fillRect(w/2-12, h-18, 24, 3);
        g2.dispose(); return new ImageIcon(img);
    }

    private Color getBrandColor(String brand) {
        switch (brand.toUpperCase()) {
            case "ASUS":     return new Color(0, 100, 180);
            case "DELL":     return new Color(0, 116, 214);
            case "HP":       return new Color(0, 150, 57);
            case "ACER":     return new Color(131, 0, 0);
            case "MSI":      return new Color(180, 0, 0);
            case "MACBOOK":    return new Color(80, 80, 80);
            case "LENOVO":   return new Color(200, 0, 18);
            case "GIGABYTE": return new Color(210, 90, 0);
            default:         return PRIMARY;
        }
    }

    private void addToCart(SanPhamDTO sp) {
        if (sp.getSoLuongTon() <= 0) {
            JOptionPane.showMessageDialog(this, "Sản phẩm đã hết hàng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String maSP = String.valueOf(sp.getMaSP());
        for (Object[] item : cartItems) {
            if (String.valueOf(item[0]).equals(maSP)) {
                int sl = (int) item[3]; item[3] = sl + 1;
                item[4] = sp.getGia().doubleValue() * (sl + 1);
                showToast("Tăng SL: " + sp.getTenSP() + " → " + (sl + 1));
                return;
            }
        }
        cartItems.add(new Object[]{ maSP, sp.getTenSP(), sp.getGia().doubleValue(), 1, sp.getGia().doubleValue() });
        showToast("Đã thêm: " + sp.getTenSP());
    }

    private void showToast(String msg) {
        try {
            Window owner = SwingUtilities.getWindowAncestor(this);
            JWindow toast = new JWindow(owner);
            JLabel lbl = new JLabel("  " + msg + "  ");
            lbl.setFont(FONT_LABEL); lbl.setForeground(WHITE); lbl.setOpaque(true);
            lbl.setBackground(new Color(46, 125, 50));
            lbl.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(30, 100, 40), 1, true),
                BorderFactory.createEmptyBorder(7, 12, 7, 12)));
            toast.add(lbl); toast.pack();
            Point loc = getLocationOnScreen();
            toast.setLocation(loc.x + getWidth() - toast.getWidth() - 20, loc.y + getHeight() - toast.getHeight() - 20);
            toast.setVisible(true);
            new Timer(1400, e -> toast.dispose()) {{ setRepeats(false); start(); }};
        } catch (Exception ignored) {}
    }

    // ============================
    // TITLE PANEL
    // ============================
    private JPanel createTitlePanel() {
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, PRIMARY_DARK, getWidth(), 0, PRIMARY));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
            }
        };
        panel.setOpaque(false); panel.setPreferredSize(new Dimension(0, 58));

        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setOpaque(false); leftPanel.setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 0));
        JComponent cartIcon = new JComponent() {
            { setPreferredSize(new Dimension(28, 58)); setOpaque(false); }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(WHITE);
                g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                int cy = getHeight() / 2 - 4;
                g2.drawLine(2, cy-6, 6, cy-6); g2.drawLine(6, cy-6, 9, cy+6);
                g2.drawLine(9, cy+6, 22, cy+6); g2.drawLine(22, cy+6, 25, cy-1);
                g2.drawLine(25, cy-1, 9, cy-1);
                g2.fillOval(11, cy+8, 4, 4); g2.fillOval(19, cy+8, 4, 4);
                g2.dispose();
            }
        };
        JLabel title = new JLabel("  QUẢN LÝ BÁN HÀNG");
        title.setFont(FONT_TITLE); title.setForeground(WHITE);
        GridBagConstraints lgc = new GridBagConstraints();
        lgc.anchor = GridBagConstraints.CENTER; lgc.insets = new Insets(0, 0, 0, 4);
        lgc.gridx = 0; leftPanel.add(cartIcon, lgc);
        lgc.gridx = 1; lgc.insets = new Insets(0, 0, 0, 0);
        leftPanel.add(title, lgc);
        panel.add(leftPanel, BorderLayout.WEST);

        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setOpaque(false); rightPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 16));
        txtMaHD    = createSmallField("HD001", 80);   txtMaHD.setEditable(false);
        txtNgayLap = createSmallField(java.time.LocalDate.now().toString(), 100); txtNgayLap.setEditable(false);

        // Nut Lap hoa don: ve toan bo trong paintComponent, khong dung inner components
        final String BTN_LABEL = "Lập hóa đơn";
        final Font   BTN_FONT  = new Font("Segoe UI", Font.BOLD, 13);
        Canvas _cv2 = new Canvas();
        FontMetrics _fm2 = _cv2.getFontMetrics(BTN_FONT);
        final int BTN_W = _fm2.stringWidth(BTN_LABEL) + 42;
        final int BTN_H = 36;

        JButton btnLapHD = new JButton() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                // Nen vang vua khit
                g2.setColor(getModel().isRollover() ? new Color(240, 180, 0) : new Color(255, 215, 40));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                // Icon clipboard
                int icx = 10, icy = getHeight() / 2;
                g2.setColor(PRIMARY_DARK);
                g2.fillRoundRect(icx, icy-8, 11, 13, 3, 3);
                g2.setColor(new Color(230, 240, 255));
                g2.fillRect(icx+2, icy-5, 7, 7);
                g2.setColor(PRIMARY_DARK);
                g2.fillRoundRect(icx+3, icy-10, 5, 4, 2, 2);
                g2.setColor(new Color(70, 120, 190));
                g2.setStroke(new BasicStroke(1f));
                g2.drawLine(icx+2, icy-2, icx+8, icy-2);
                g2.drawLine(icx+2, icy,   icx+8, icy);
                g2.drawLine(icx+2, icy+2, icx+6, icy+2);
                // Text
                g2.setFont(BTN_FONT);
                g2.setColor(PRIMARY_DARK);
                FontMetrics tfm = g2.getFontMetrics();
                int ty = (getHeight() - tfm.getHeight()) / 2 + tfm.getAscent();
                g2.drawString(BTN_LABEL, icx + 16, ty);
                g2.dispose();
            }
            @Override public Dimension getPreferredSize() { return new Dimension(BTN_W, BTN_H); }
            @Override public Dimension getMinimumSize()   { return getPreferredSize(); }
            @Override public Dimension getMaximumSize()   { return getPreferredSize(); }
        };
        btnLapHD.setText("");
        btnLapHD.setFocusPainted(false); btnLapHD.setBorderPainted(false);
        btnLapHD.setContentAreaFilled(false); btnLapHD.setOpaque(false);
        btnLapHD.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLapHD.addActionListener(e -> openInvoiceDialog());

        // Can giua doc bang GridBagLayout - anchor CENTER
        GridBagConstraints rgc = new GridBagConstraints();
        rgc.anchor = GridBagConstraints.CENTER;
        rgc.insets = new Insets(0, 6, 0, 0);
        rgc.gridx = 0; rightPanel.add(makeInlineLabel("Mã HD:"), rgc);
        rgc.gridx = 1; rightPanel.add(txtMaHD, rgc);
        rgc.gridx = 2; rightPanel.add(makeInlineLabel("Ngày:"), rgc);
        rgc.gridx = 3; rightPanel.add(txtNgayLap, rgc);
        rgc.gridx = 4; rgc.insets = new Insets(0, 12, 0, 0);
        rightPanel.add(btnLapHD, rgc);
        panel.add(rightPanel, BorderLayout.EAST);
        return panel;
    }

    // ============================
    // MAIN PANEL
    // ============================
    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(CONTENT_BG);
        panel.add(createFilterPanel(),      BorderLayout.NORTH);
        panel.add(createProductGridPanel(), BorderLayout.CENTER);
        return panel;
    }

    // ============================
    // FILTER PANEL
    // Tab button ve chu bang Graphics2D -> KHONG BAO GIO bi "..."
    // ============================
    private JPanel createFilterPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.setBackground(CONTENT_BG);

        // Search bar voi icon kinh lup ve tay ben trai
        JPanel searchBar = new JPanel(new BorderLayout(0, 0));
        searchBar.setBackground(WHITE);
        searchBar.setBorder(new CompoundBorder(
            new LineBorder(new Color(180, 210, 240), 1, true),
            BorderFactory.createEmptyBorder(0, 0, 0, 0)));
        searchBar.setPreferredSize(new Dimension(0, 36));

        // Icon kinh lup ve bang Graphics
        JComponent searchIcon = new JComponent() {
            { setPreferredSize(new Dimension(38, 36)); setOpaque(false); }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int cx = 14, cy = getHeight()/2 - 1, r = 7;
                g2.setColor(new Color(160, 185, 220));
                g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawOval(cx - r, cy - r, r*2, r*2);
                g2.drawLine(cx + r - 2, cy + r - 2, cx + r + 4, cy + r + 4);
                g2.dispose();
            }
        };

        txtSearch = new JTextField();
        txtSearch.setFont(FONT_NORMAL);
        txtSearch.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        txtSearch.setOpaque(false);

        JButton btnSearch = createActionButton("Tìm kiếm", PRIMARY, WHITE);
        btnSearch.setPreferredSize(new Dimension(105, 36));
        btnSearch.addActionListener(e -> filterBySearch());
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) { if (e.getKeyCode() == KeyEvent.VK_ENTER) filterBySearch(); }
        });
        searchBar.add(searchIcon, BorderLayout.WEST);
        searchBar.add(txtSearch, BorderLayout.CENTER);
        searchBar.add(btnSearch, BorderLayout.EAST);
        panel.add(searchBar, BorderLayout.NORTH);

        JPanel tabsContainer = new JPanel(new GridLayout(2, 1, 0, 4));
        tabsContainer.setBackground(CONTENT_BG);

        // Hang
        JPanel hangRow = new JPanel(new BorderLayout(8, 0));
        hangRow.setBackground(CONTENT_BG);
        JLabel lblHang = new JLabel("Hãng:");
        lblHang.setFont(new Font("Segoe UI", Font.BOLD, 12)); lblHang.setForeground(new Color(60, 80, 120));
        lblHang.setPreferredSize(new Dimension(60, 30));
        JPanel hangTabs = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        hangTabs.setBackground(CONTENT_BG); allHangBtns.clear();
        for (int i = 0; i < HANG_KEYS.length; i++) {
            final String key  = "hang_" + HANG_KEYS[i];
            final String disp = HANG_DISPLAY[i];
            final String val  = HANG_KEYS[i];
            JButton btn = buildTabButton(disp, key);
            btn.addActionListener(e -> { activeHangKey = key; activePkKey = ""; refreshTabColors(); filterByBrand(val); });
            hangTabs.add(btn); allHangBtns.add(btn);
        }
        hangRow.add(lblHang, BorderLayout.WEST); hangRow.add(hangTabs, BorderLayout.CENTER);
        tabsContainer.add(hangRow);

        // Phu kien
        JPanel pkRow = new JPanel(new BorderLayout(8, 0));
        pkRow.setBackground(CONTENT_BG);
        JLabel lblPK = new JLabel("Phụ kiện:");
        lblPK.setFont(new Font("Segoe UI", Font.BOLD, 12)); lblPK.setForeground(new Color(60, 80, 120));
        lblPK.setPreferredSize(new Dimension(60, 30));
        JPanel pkTabs = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        pkTabs.setBackground(CONTENT_BG); allPkBtns.clear();
        for (int i = 0; i < PK_KEYS.length; i++) {
            final String key  = "pk_" + PK_KEYS[i];
            final String disp = PK_DISPLAY[i];
            final String val  = PK_KEYS[i];
            JButton btn = buildTabButton(disp, key);
            btn.addActionListener(e -> { activePkKey = key; activeHangKey = ""; refreshTabColors(); filterByCategory(val); });
            pkTabs.add(btn); allPkBtns.add(btn);
        }
        pkRow.add(lblPK, BorderLayout.WEST); pkRow.add(pkTabs, BorderLayout.CENTER);
        tabsContainer.add(pkRow);

        panel.add(tabsContainer, BorderLayout.SOUTH);
        return panel;
    }

    /**
     * Tab button: setText("") - bo text mac dinh cua JButton.
     * Ve chu truc tiep bang g2.drawString() trong paintComponent
     * -> TUYET DOI khong bi cat thanh "..."
     */
    private JButton buildTabButton(String label, String key) {
        Canvas cv = new Canvas();
        FontMetrics fmCalc = cv.getFontMetrics(FONT_TAB);
        final int prefW = fmCalc.stringWidth(label) + 30;

        JButton btn = new JButton() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                boolean active = key.equals(activeHangKey) || key.equals(activePkKey);
                Color bg;
                if (active)                       bg = TAB_ACTIVE;
                else if (getModel().isRollover()) bg = new Color(210, 235, 255);
                else                              bg = WHITE;
                // Ve nen tron
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                // Ve vien
                g2.setColor(active ? TAB_ACTIVE : TAB_INACTIVE);
                g2.setStroke(new BasicStroke(1.3f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 16, 16);
                // Ve chu truc tiep - khong qua JButton text pipeline
                g2.setFont(FONT_TAB);
                g2.setColor(active ? WHITE : PRIMARY);
                FontMetrics tfm = g2.getFontMetrics();
                int tx = (getWidth()  - tfm.stringWidth(label)) / 2;
                int ty = (getHeight() - tfm.getHeight()) / 2 + tfm.getAscent();
                g2.drawString(label, tx, ty);
                g2.dispose();
            }
            @Override public Dimension getPreferredSize() { return new Dimension(prefW, 30); }
            @Override public Dimension getMinimumSize()   { return getPreferredSize(); }
            @Override public Dimension getMaximumSize()   { return getPreferredSize(); }
        };
        btn.setName(key);
        btn.setText("");       // QUAN TRONG: bo text mac dinh, chi ve bang Graphics
        btn.setToolTipText(label);
        btn.setFocusPainted(false); btn.setBorderPainted(false);
        btn.setContentAreaFilled(false); btn.setOpaque(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void refreshTabColors() {
        for (JButton b : allHangBtns) b.repaint();
        for (JButton b : allPkBtns)   b.repaint();
    }

    private void filterBySearch() {
        String kw = txtSearch.getText().trim().toLowerCase();
        if (kw.isEmpty()) { renderProductGrid(allProducts); return; }
        ArrayList<SanPhamDTO> out = new ArrayList<>();
        for (SanPhamDTO sp : allProducts)
            if (sp.getTenSP().toLowerCase().contains(kw) ||
                sp.getThuongHieu().toLowerCase().contains(kw) ||
                String.valueOf(sp.getMaSP()).contains(kw)) out.add(sp);
        renderProductGrid(out);
    }

    private void filterByBrand(String brand) {
        if (brand.equals("Tat ca")) { renderProductGrid(allProducts); return; }
        ArrayList<SanPhamDTO> out = new ArrayList<>();
        for (SanPhamDTO sp : allProducts)
            if (sp.getThuongHieu().equalsIgnoreCase(brand)) out.add(sp);
        renderProductGrid(out);
    }

    private void filterByCategory(String cat) { renderProductGrid(allProducts); }

    // ============================
    // PRODUCT GRID
    // ============================
    private JPanel createProductGridPanel() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(WHITE);
        wrapper.setBorder(new LineBorder(new Color(180, 210, 240), 1));
        productGrid = new JPanel(new GridLayout(0, 5, 10, 10));
        productGrid.setBackground(new Color(246, 249, 254));
        productGrid.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        gridScroll = new JScrollPane(productGrid);
        gridScroll.setBorder(null);
        gridScroll.getVerticalScrollBar().setUnitIncrement(16);
        gridScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        gridScroll.addComponentListener(new ComponentAdapter() {
            @Override public void componentResized(ComponentEvent e) {
                int w = gridScroll.getViewport().getWidth();
                int cols = Math.max(2, w / 210);
                GridLayout gl = (GridLayout) productGrid.getLayout();
                if (gl.getColumns() != cols) { gl.setColumns(cols); productGrid.revalidate(); }
            }
        });
        wrapper.add(gridScroll, BorderLayout.CENTER);
        return wrapper;
    }

    // ============================
    // OPEN INVOICE DIALOG
    // ============================
    private void openInvoiceDialog() {
        Window owner = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = (owner instanceof Frame)
            ? new JDialog((Frame) owner, "Lập hóa đơn bán hàng", true)
            : new JDialog((Dialog) owner, "Lập hóa đơn bán hàng", true);
        dialog.setSize(980, 660);
        dialog.setLocationRelativeTo(owner);
        dialog.setResizable(true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setContentPane(new InvoiceDialogPanel(cartItems, dialog));
        dialog.setVisible(true);
    }

    // ============================
    // INNER CLASS: INVOICE DIALOG
    // ============================
    class InvoiceDialogPanel extends JPanel {
        private JTable tableCart;
        private DefaultTableModel modelCart;
        private JTextField txtKhachHang, txtSDT, txtGiamGia, txtTienKhachDua;
        private JLabel lblTongTien, lblThanhTien, lblTienThua;
        private JRadioButton rbTienMat, rbChuyenKhoan;
        private double tongTien = 0;
        private final ArrayList<Object[]> cartRef;
        private final JDialog parentDialog;

        InvoiceDialogPanel(ArrayList<Object[]> cartRef, JDialog dialog) {
            this.cartRef = cartRef; this.parentDialog = dialog;
            setLayout(new BorderLayout(0, 0));
            setBackground(CONTENT_BG);
            add(buildHeader(), BorderLayout.NORTH);
            add(buildCenter(), BorderLayout.CENTER);
            add(buildFooter(), BorderLayout.SOUTH);
            loadCart();
        }

        private JPanel buildHeader() {
            JPanel p = new JPanel(new BorderLayout()) {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setPaint(new GradientPaint(0, 0, PRIMARY_DARK, getWidth(), 0, PRIMARY));
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    g2.dispose();
                }
            };
            p.setOpaque(false); p.setPreferredSize(new Dimension(0, 50));
            p.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 16));
            JLabel lbl = new JLabel("HÓA ĐƠN BÁN HÀNG  —  " + txtMaHD.getText());
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 17)); lbl.setForeground(WHITE);
            JLabel date = new JLabel("Ngày: " + txtNgayLap.getText());
            date.setFont(FONT_SMALL); date.setForeground(new Color(200, 230, 255));
            p.add(lbl, BorderLayout.WEST); p.add(date, BorderLayout.EAST);
            return p;
        }

        private JPanel buildCenter() {
            JPanel p = new JPanel(new BorderLayout(0, 6));
            p.setBackground(CONTENT_BG);
            p.setBorder(BorderFactory.createEmptyBorder(8, 10, 0, 10));
            p.add(buildCustomerPanel(), BorderLayout.NORTH);
            String[] cols = {"STT", "Tên sản phẩm", "Đơn giá (đ)", "SL", "Thành tiền (đ)"};
            modelCart = new DefaultTableModel(cols, 0) {
                @Override public boolean isCellEditable(int r, int c) { return c == 3; }
            };
            tableCart = buildStyledTable(modelCart);
            tableCart.getColumnModel().getColumn(0).setPreferredWidth(40);
            tableCart.getColumnModel().getColumn(1).setPreferredWidth(300);
            tableCart.getColumnModel().getColumn(2).setPreferredWidth(120);
            tableCart.getColumnModel().getColumn(3).setPreferredWidth(50);
            tableCart.getColumnModel().getColumn(4).setPreferredWidth(130);
            modelCart.addTableModelListener(e -> { if (e.getColumn() == 3) recalcTotal(); });
            JScrollPane sc = new JScrollPane(tableCart);
            sc.setBorder(new LineBorder(new Color(180, 210, 240), 1));
            p.add(sc, BorderLayout.CENTER);
            return p;
        }

        // Thong tin KH: 1 hang, bo chon nhan vien, them phuong thuc thanh toan
        private JPanel buildCustomerPanel() {
            JPanel p = new JPanel(new GridBagLayout());
            p.setBackground(WHITE);
            p.setBorder(new CompoundBorder(
                new LineBorder(new Color(180, 210, 240), 1),
                BorderFactory.createEmptyBorder(10, 14, 10, 14)));
            txtKhachHang = new JTextField(); txtSDT = new JTextField();
            styleField2(txtKhachHang); styleField2(txtSDT);
            GridBagConstraints gc = new GridBagConstraints();
            gc.insets = new Insets(0, 6, 0, 6); gc.fill = GridBagConstraints.HORIZONTAL; gc.anchor = GridBagConstraints.WEST;
            // Khach hang
            gc.gridx = 0; gc.weightx = 0; p.add(makeLabel("Khách hàng:"), gc);
            gc.gridx = 1; gc.weightx = 1.4; p.add(txtKhachHang, gc);
            // SDT
            gc.gridx = 2; gc.weightx = 0; p.add(makeLabel("SĐT:"), gc);
            gc.gridx = 3; gc.weightx = 0.8; p.add(txtSDT, gc);
            // Phuong thuc
            gc.gridx = 4; gc.weightx = 0; p.add(makeLabel("Thanh toán:"), gc);
            ButtonGroup bg = new ButtonGroup();
            rbTienMat     = makeRadio("Tiền mặt",     true);
            rbChuyenKhoan = makeRadio("Chuyển khoản", false);
            bg.add(rbTienMat); bg.add(rbChuyenKhoan);
            rbTienMat.addActionListener(e -> onPayChange());
            rbChuyenKhoan.addActionListener(e -> onPayChange());
            JPanel payRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
            payRow.setBackground(WHITE);
            payRow.add(makePayIcon(true));  payRow.add(rbTienMat);
            payRow.add(Box.createHorizontalStrut(8));
            payRow.add(makePayIcon(false)); payRow.add(rbChuyenKhoan);
            gc.gridx = 5; gc.weightx = 1.2; p.add(payRow, gc);
            return p;
        }

        private JComponent makePayIcon(boolean isCash) {
            return new JComponent() {
                { setPreferredSize(new Dimension(20, 22)); setOpaque(false); }
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    if (isCash) {
                        g2.setColor(new Color(46, 125, 50)); g2.fillRoundRect(1, 5, 18, 12, 4, 4);
                        g2.setColor(WHITE); g2.fillOval(7, 9, 6, 5);
                        g2.setColor(new Color(200, 240, 200)); g2.fillRect(2, 6, 2, 2); g2.fillRect(16, 14, 2, 2);
                    } else {
                        g2.setColor(PRIMARY); g2.fillRoundRect(1, 5, 18, 12, 4, 4);
                        g2.setColor(WHITE); g2.fillRect(1, 9, 18, 3);
                        g2.setColor(new Color(180, 210, 255)); g2.fillRect(3, 13, 5, 2);
                    }
                    g2.dispose();
                }
            };
        }

        private JRadioButton makeRadio(String text, boolean sel) {
            JRadioButton rb = new JRadioButton(text, sel);
            rb.setFont(FONT_NORMAL); rb.setForeground(PRIMARY_DARK);
            rb.setBackground(WHITE); rb.setFocusPainted(false);
            rb.setCursor(new Cursor(Cursor.HAND_CURSOR));
            return rb;
        }

        private void onPayChange() {
            boolean isMat = rbTienMat.isSelected();
            txtTienKhachDua.setEnabled(isMat);
            txtTienKhachDua.setBackground(isMat ? WHITE : new Color(240, 242, 248));
            if (!isMat) { txtTienKhachDua.setText("0"); recalcChange(); }
        }

        // Footer: input | summary 3 cot | buttons
        private JPanel buildFooter() {
            JPanel outer = new JPanel(new BorderLayout(0, 0));
            outer.setBackground(CONTENT_BG);

            // Input giam gia + tien khach
            JPanel inputRow = new JPanel(new GridBagLayout());
            inputRow.setBackground(WHITE);
            inputRow.setBorder(new CompoundBorder(
                new LineBorder(new Color(180, 210, 240), 1),
                BorderFactory.createEmptyBorder(8, 14, 8, 14)));
            txtGiamGia = new JTextField("0"); txtTienKhachDua = new JTextField("0");
            styleField2(txtGiamGia); styleField2(txtTienKhachDua);
            txtGiamGia.addKeyListener(new KeyAdapter()      { @Override public void keyReleased(KeyEvent e) { recalcTotal(); } });
            txtTienKhachDua.addKeyListener(new KeyAdapter() { @Override public void keyReleased(KeyEvent e) { recalcChange(); } });
            GridBagConstraints gc = new GridBagConstraints();
            gc.insets = new Insets(0, 6, 0, 6); gc.fill = GridBagConstraints.HORIZONTAL; gc.anchor = GridBagConstraints.WEST;
            gc.gridx = 0; gc.weightx = 0;   inputRow.add(makeLabel("Giảm giá (đ):"), gc);
            gc.gridx = 1; gc.weightx = 1.0; inputRow.add(txtGiamGia, gc);
            gc.gridx = 2; gc.weightx = 0;   inputRow.add(makeLabel("Tiền khách đưa:"), gc);
            gc.gridx = 3; gc.weightx = 1.0; inputRow.add(txtTienKhachDua, gc);
            outer.add(inputRow, BorderLayout.NORTH);

            // Summary bar 3 cot can bang
            JPanel summary = new JPanel(new GridLayout(1, 3, 1, 0));
            summary.setBackground(new Color(8, 50, 110));
            summary.setPreferredSize(new Dimension(0, 58));
            lblTongTien  = new JLabel("0 đ", SwingConstants.CENTER);
            lblThanhTien = new JLabel("0 đ", SwingConstants.CENTER);
            lblTienThua  = new JLabel("0 đ", SwingConstants.CENTER);
            lblTongTien.setFont(FONT_TOTAL);  lblTongTien.setForeground(new Color(255, 220, 100));
            lblThanhTien.setFont(FONT_TOTAL); lblThanhTien.setForeground(new Color(100, 255, 180));
            lblTienThua.setFont(FONT_TOTAL);  lblTienThua.setForeground(WHITE);
            summary.add(makeSummaryBlock("Tổng tiền hàng:",        lblTongTien));
            summary.add(makeSummaryBlock("Thành tiền (sau giảm):", lblThanhTien));
            summary.add(makeSummaryBlock("Tiền thừa:",                  lblTienThua));
            outer.add(summary, BorderLayout.CENTER);

            // Buttons
            JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 6));
            btnRow.setBackground(CONTENT_BG);
            JButton btnXoa   = createActionButton("Xóa dòng", DANGER, WHITE);
            JButton btnReset = createActionButton("Làm mới", new Color(90, 100, 115), WHITE);
            JButton btnIn    = createActionButton("In hóa đơn", PRIMARY, WHITE);
            JButton btnTT    = createActionButton("THANH TOÁN", SUCCESS, WHITE);
            btnTT.setPreferredSize(new Dimension(150, 42));
            btnTT.setFont(new Font("Segoe UI", Font.BOLD, 15));
            btnXoa.addActionListener(e -> removeRow());
            btnReset.addActionListener(e -> resetForm());
            btnIn.addActionListener(e -> JOptionPane.showMessageDialog(parentDialog, "Chức năng in đang phát triển!", "Thông báo", JOptionPane.INFORMATION_MESSAGE));
            btnTT.addActionListener(e -> doCheckout());
            btnRow.add(btnXoa); btnRow.add(btnReset); btnRow.add(btnIn); btnRow.add(btnTT);
            outer.add(btnRow, BorderLayout.SOUTH);
            return outer;
        }

        // Block label + value xep doc trong summary bar
        private JPanel makeSummaryBlock(String labelText, JLabel valueLabel) {
            JPanel block = new JPanel(new GridBagLayout());
            block.setBackground(PRIMARY_DARK);
            JLabel lbl = new JLabel(labelText, SwingConstants.CENTER);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
            lbl.setForeground(new Color(170, 205, 255));
            GridBagConstraints gc = new GridBagConstraints();
            gc.gridx = 0; gc.gridy = 0; gc.insets = new Insets(4, 8, 1, 8); gc.anchor = GridBagConstraints.CENTER;
            block.add(lbl, gc);
            gc.gridy = 1; gc.insets = new Insets(1, 8, 4, 8);
            block.add(valueLabel, gc);
            return block;
        }

        private void loadCart() {
            modelCart.setRowCount(0); int i = 1;
            for (Object[] item : cartRef)
                modelCart.addRow(new Object[]{ i++, item[1], formatMoney((double)item[2]), item[3], formatMoney((double)item[4]) });
            recalcTotal();
        }

        private void removeRow() {
            int row = tableCart.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE); return; }
            modelCart.removeRow(row);
            if (row < cartRef.size()) cartRef.remove(row);
            for (int i = 0; i < modelCart.getRowCount(); i++) modelCart.setValueAt(i+1, i, 0);
            recalcTotal();
        }

        private void recalcTotal() {
            tongTien = 0;
            for (int i = 0; i < modelCart.getRowCount(); i++) {
                try {
                    int sl = Integer.parseInt(modelCart.getValueAt(i, 3).toString());
                    double gia = Double.parseDouble(modelCart.getValueAt(i, 2).toString().replace(",", ""));
                    double thanh = sl * gia;
                    modelCart.setValueAt(formatMoney(thanh), i, 4);
                    tongTien += thanh;
                    if (i < cartRef.size()) { cartRef.get(i)[3] = sl; cartRef.get(i)[4] = thanh; }
                } catch (Exception ignored) {}
            }
            double giam = 0;
            try { giam = Double.parseDouble(txtGiamGia.getText().replace(",", "")); } catch (Exception ignored) {}
            lblTongTien.setText(formatMoney(tongTien) + " đ");
            lblThanhTien.setText(formatMoney(Math.max(0, tongTien - giam)) + " đ");
            recalcChange();
        }

        private void recalcChange() {
            double giam = 0, khach = 0;
            try { giam  = Double.parseDouble(txtGiamGia.getText().replace(",", "")); } catch (Exception ignored) {}
            try { khach = Double.parseDouble(txtTienKhachDua.getText().replace(",", "")); } catch (Exception ignored) {}
            double thanh = Math.max(0, tongTien - giam);
            double thua  = khach - thanh;
            lblTienThua.setText(formatMoney(Math.max(0, thua)) + " đ");
            lblTienThua.setForeground(thua >= 0 ? new Color(100, 255, 180) : new Color(255, 100, 100));
        }

        private void doCheckout() {
            if (modelCart.getRowCount() == 0) { JOptionPane.showMessageDialog(this, "Giỷ hàng trống!", "Lỗi", JOptionPane.WARNING_MESSAGE); return; }
            if (txtKhachHang.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this, "Vui lòng nhập tên khách hàng!", "Lỗi", JOptionPane.WARNING_MESSAGE); return; }
            String pt = rbTienMat.isSelected() ? "Tiền mặt" : "Chuyển khoản";
            int ok = JOptionPane.showConfirmDialog(this,
                "Xác nhận thanh toán hóa đơn " + txtMaHD.getText() + "\nPhương thức: " + pt + "?",
                "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (ok == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(this, "Thanh toán thành công!\nPhương thức: " + pt, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                cartItems.clear(); parentDialog.dispose();
            }
        }

        private void resetForm() {
            modelCart.setRowCount(0); cartRef.clear();
            txtKhachHang.setText(""); txtSDT.setText("");
            txtGiamGia.setText("0"); txtTienKhachDua.setText("0");
            rbTienMat.setSelected(true); onPayChange();
            tongTien = 0;
            lblTongTien.setText("0 đ"); lblThanhTien.setText("0 đ"); lblTienThua.setText("0 đ");
        }

        private void styleField2(JTextField f) {
            f.setFont(FONT_NORMAL); f.setPreferredSize(new Dimension(0, 30));
            f.setBorder(new CompoundBorder(new LineBorder(new Color(180,210,240),1), BorderFactory.createEmptyBorder(3,8,3,8)));
        }

        private JTable buildStyledTable(DefaultTableModel model) {
            JTable t = new JTable(model) {
                @Override public Component prepareRenderer(TableCellRenderer r, int row, int col) {
                    Component c = super.prepareRenderer(r, row, col);
                    if (!isRowSelected(row)) c.setBackground(row % 2 == 0 ? WHITE : ROW_ALT);
                    else c.setBackground(new Color(187, 222, 251));
                    c.setFont(FONT_NORMAL); return c;
                }
            };
            t.setRowHeight(34); t.setFont(FONT_NORMAL);
            t.setGridColor(new Color(220, 230, 245)); t.setShowVerticalLines(true);
            t.setSelectionBackground(new Color(187,222,251)); t.setSelectionForeground(PRIMARY_DARK);
            t.setIntercellSpacing(new Dimension(0, 1));
            t.getTableHeader().setBackground(TABLE_HEADER); t.getTableHeader().setForeground(WHITE);
            t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
            t.getTableHeader().setPreferredSize(new Dimension(0, 36));
            t.getTableHeader().setReorderingAllowed(false);
            return t;
        }
    }

    // ============================
    // HELPERS CHUNG
    // ============================
    private JButton createActionButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? bg.darker() : bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose(); super.paintComponent(g);
            }
        };
        btn.setForeground(fg); btn.setFont(FONT_LABEL);
        btn.setFocusPainted(false); btn.setBorderPainted(false);
        btn.setContentAreaFilled(false); btn.setOpaque(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(120, 38));
        return btn;
    }

    private JTextField createSmallField(String text, int w) {
        JTextField f = new JTextField(text); f.setFont(FONT_SMALL); f.setForeground(WHITE);
        f.setBackground(new Color(255,255,255,40));
        f.setBorder(new CompoundBorder(new LineBorder(new Color(255,255,255,80),1), BorderFactory.createEmptyBorder(2,6,2,6)));
        f.setPreferredSize(new Dimension(w, 26)); f.setCaretColor(WHITE);
        return f;
    }

    private JLabel makeLabel(String text) {
        JLabel l = new JLabel(text); l.setFont(FONT_LABEL); l.setForeground(PRIMARY); return l;
    }
    private JLabel makeInlineLabel(String text) {
        JLabel l = new JLabel(text); l.setFont(FONT_SMALL); l.setForeground(new Color(200,230,255)); return l;
    }
    private String formatMoney(double v) {
        return NumberFormat.getNumberInstance(Locale.US).format((long)v);
    }
}