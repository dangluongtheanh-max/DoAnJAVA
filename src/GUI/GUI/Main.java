package GUI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;



public class Main extends JFrame {

    private static final Color PRIMARY      = new Color(21, 101, 192);
    private static final Color PRIMARY_DARK = new Color(10, 60, 130);
    private static final Color ACCENT       = new Color(0, 188, 212);
    private static final Color CONTENT_BG   = new Color(236, 242, 250);
    private static final Color SIDEBAR_TEXT = Color.WHITE;

    private static final Font MENU_FONT   = new Font("Segoe UI", Font.PLAIN, 16);
    private static final Font MENU_BOLD   = new Font("Segoe UI", Font.BOLD, 17);

    private JPanel activeItem = null;
    private JPanel contentPanel;
    private CardLayout cardLayout;

    public Main() {
        setTitle("Phần mềm quản lý cửa hàng Laptop");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1150, 700); 
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        

        JPanel body = new JPanel(new BorderLayout());
        body.add(createSidebar(), BorderLayout.WEST);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(CONTENT_BG);

        //contentPanel.add(new BanHangPanel2(), "Bán hàng");

        contentPanel.add(createPlaceholder("Nhập hàng", "🚚"), "Nhập hàng");
        contentPanel.add(createPlaceholder("Sản phẩm", "💻"), "Sản phẩm");
        contentPanel.add(createPlaceholder("Nhân viên", "👤"), "Nhân viên");
        contentPanel.add(createPlaceholder("Khách hàng", "🤝"), "Khách hàng");
        contentPanel.add(createPlaceholder("Nhà cung cấp", "🏭"), "Nhà cung cấp");
        contentPanel.add(new BaoHanh(), "Bảo hành");
        contentPanel.add(createPlaceholder("Đổi trả", "🔄"), "Đổi trả");
        contentPanel.add(createPlaceholder("Thống kê", "📊"), "Thống kê");

        body.add(contentPanel, BorderLayout.CENTER);
        add(body, BorderLayout.CENTER);
        setVisible(true);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setPaint(new GradientPaint(0, 0, PRIMARY_DARK, 0, getHeight(), new Color(15, 80, 160)));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        sidebar.setOpaque(false);
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBorder(new MatteBorder(0, 0, 0, 1, ACCENT));

        sidebar.add(createLogoPanel(), BorderLayout.NORTH);

        JPanel menuPanel = new JPanel();
        menuPanel.setOpaque(false);
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));

        String[][] items = {
            {"🛒","Bán hàng"}, {"🚚","Nhập hàng"}, {"💻","Sản phẩm"},
            {"👤","Nhân viên"}, {"🤝","Khách hàng"}, {"🏭","Nhà cung cấp"},
            {"🛡️","Bảo hành"}, {"🔄","Đổi trả"}, {"📊","Thống kê"}
        };
        for (String[] item : items) menuPanel.add(createMenuItem(item[0], item[1]));

        sidebar.add(menuPanel, BorderLayout.CENTER);

        JLabel version = new JLabel("v1.0.0", SwingConstants.CENTER);
        version.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        version.setForeground(new Color(150, 200, 255, 150));
        version.setBorder(BorderFactory.createEmptyBorder(6, 0, 8, 0));
        sidebar.add(version, BorderLayout.SOUTH);
        return sidebar;
    }

    private JPanel createLogoPanel() {
        JPanel panel = new JPanel(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int cx = getWidth() / 2, cy = getHeight() / 2 - 5, r = 52;

                // Glow rings
                for (int i = 5; i > 0; i--) {
                    g2.setColor(new Color(0, 188, 212, 12 * i));
                    g2.fillPolygon(hexagon(cx, cy, r + i * 4));
                }
                // Hex body
                g2.setPaint(new GradientPaint(cx - r, cy - r, new Color(30, 130, 220), cx + r, cy + r, new Color(10, 60, 140)));
                g2.fillPolygon(hexagon(cx, cy, r));
                g2.setColor(ACCENT);
                g2.setStroke(new BasicStroke(2.5f));
                g2.drawPolygon(hexagon(cx, cy, r));

                // Inner hex
                g2.setColor(new Color(10, 50, 120, 200));
                g2.fillPolygon(hexagon(cx, cy, r - 13));

                // Text
                g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
                g2.setColor(Color.WHITE);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString("LAPTOP", cx - fm.stringWidth("LAPTOP") / 2, cy - 4);
                g2.setColor(ACCENT);
                g2.drawString("STORE", cx - fm.stringWidth("STORE") / 2, cy + 12);

                // Laptop icon
                g2.setColor(new Color(200, 235, 255));
                g2.fillRoundRect(cx - 14, cy + 18, 28, 16, 3, 3);
                g2.setColor(new Color(10, 50, 120));
                g2.fillRect(cx - 11, cy + 20, 22, 11);
                g2.setColor(ACCENT);
                g2.fillRect(cx - 18, cy + 34, 36, 3);
                g2.dispose();
            }
            private Polygon hexagon(int cx, int cy, int r) {
                Polygon p = new Polygon();
                for (int i = 0; i < 6; i++) {
                    double a = Math.toRadians(60 * i - 30);
                    p.addPoint((int)(cx + r * Math.cos(a)), (int)(cy + r * Math.sin(a)));
                }
                return p;
            }
        };
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(220, 155));
        panel.setBorder(new MatteBorder(0, 0, 1, 0, new Color(0, 188, 212, 100)));
        return panel;
    }

    private JPanel createMenuItem(String iconText, String labelText) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setMaximumSize(new Dimension(220, 54));
        wrapper.setPreferredSize(new Dimension(220, 54));
        wrapper.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel stripe = new JPanel();
        stripe.setBackground(ACCENT);
        stripe.setPreferredSize(new Dimension(4, 54));
        stripe.setVisible(false);

        JPanel inner = new JPanel(new GridBagLayout());
        inner.setOpaque(false);

        JLabel icon = new JLabel(iconText);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        icon.setForeground(new Color(180, 215, 255));

        JLabel text = new JLabel(labelText);
        text.setFont(MENU_FONT);
        text.setForeground(SIDEBAR_TEXT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 16, 0, 0);
        gbc.gridy = 0;

        gbc.gridx = 0;
        inner.add(icon, gbc);
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 10, 0, 0);
        inner.add(text, gbc);

        // Filler to push content left
        gbc.gridx = 2;
        gbc.weightx = 1.0;
        inner.add(Box.createHorizontalGlue(), gbc);

        wrapper.add(stripe, BorderLayout.WEST);
        wrapper.add(inner, BorderLayout.CENTER);

        wrapper.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { // Hiệu ứng hover: sáng nhẹ lên khi di chuột vào, nhưng nếu nút đang được chọn rồi thì bỏ qua.
                if (wrapper != activeItem) {
                    inner.setOpaque(true);
                    inner.setBackground(new Color(255, 255, 255, 20));
                    text.setForeground(new Color(200, 235, 255));
                    wrapper.repaint();
                }
            }
            @Override public void mouseExited(MouseEvent e) { //Reset về ban đầu khi chuột rời đi.
                if (wrapper != activeItem) {
                    inner.setOpaque(false);
                    text.setForeground(SIDEBAR_TEXT);
                    wrapper.repaint();
                }
            }
            @Override public void mouseClicked(MouseEvent e) {
                if (activeItem != null) {
                    for (Component c : activeItem.getComponents()) {
                        if (c instanceof JPanel && ((JPanel)c).getPreferredSize().width == 4)
                            c.setVisible(false);
                        if (c instanceof JPanel) {
                            ((JPanel)c).setOpaque(false);
                            for (Component cc : ((JPanel)c).getComponents()) {
                                if (cc instanceof JLabel) {
                                    JLabel l = (JLabel)cc;
                                    if (l.getText().length() <= 3)
                                        l.setForeground(new Color(180, 215, 255));
                                    else { l.setFont(MENU_FONT); l.setForeground(SIDEBAR_TEXT); }
                                }
                            }
                        }
                    }
                    activeItem.repaint();
                }
                stripe.setVisible(true);
                inner.setOpaque(true);
                inner.setBackground(new Color(255, 255, 255, 35));
                icon.setForeground(ACCENT);
                text.setFont(MENU_BOLD);
                text.setForeground(Color.WHITE);
                activeItem = wrapper;
                cardLayout.show(contentPanel, labelText);
                wrapper.repaint();
            }
        });
        return wrapper;
    }

    private JPanel createPlaceholder(String name, String emoji) {
        JPanel outer = new JPanel(new GridBagLayout());
        outer.setBackground(CONTENT_BG);

        JPanel card = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Shadow
                g2.setColor(new Color(0, 0, 0, 18));
                g2.fillRoundRect(6, 6, getWidth() - 6, getHeight() - 6, 18, 18);
                // Card
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 6, getHeight() - 6, 18, 18);
                // Top stripe
                g2.setColor(PRIMARY);
                g2.fillRoundRect(0, 0, getWidth() - 6, 7, 10, 10);
                g2.fillRect(0, 4, getWidth() - 6, 3);
                // Cyan dot
                g2.setColor(ACCENT);
                g2.fillOval(getWidth() - 32, -2, 16, 16);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(480, 310));
        card.setBorder(BorderFactory.createEmptyBorder(40, 50, 35, 50));

        JLabel iconLabel = new JLabel(emoji, SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 62));
        iconLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel(name);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        titleLabel.setForeground(PRIMARY);
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 8, 0));

        JPanel line = new JPanel();
        line.setBackground(ACCENT);
        line.setMaximumSize(new Dimension(180, 3));
        line.setPreferredSize(new Dimension(180, 3));
        line.setAlignmentX(CENTER_ALIGNMENT);

        JLabel subLabel = new JLabel("Chức năng đang được phát triển...");
        subLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        subLabel.setForeground(new Color(150, 160, 175));
        subLabel.setAlignmentX(CENTER_ALIGNMENT);
        subLabel.setBorder(BorderFactory.createEmptyBorder(18, 0, 0, 0));

        card.add(iconLabel);
        card.add(titleLabel);
        card.add(line);
        card.add(subLabel);

        outer.add(card);
        return outer;
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); }
        catch (Exception ignored) {}
        SwingUtilities.invokeLater(Main::new);
    }   
}