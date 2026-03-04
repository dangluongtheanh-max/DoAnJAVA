
package GUI;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

/**
 * ThongKeGUI: phiên bản tối giản không phụ thuộc JFreeChart/FlatLaf
 */
public class ThongKeGUI extends JFrame {

    public ThongKeGUI() {
        setTitle("Thống kê");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(createHeader(), BorderLayout.NORTH);
        add(createContent(), BorderLayout.CENTER);
    }

    /* ================= HEADER ================= */
    private JPanel createHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 15, 10, 15));

        JLabel title = new JLabel("Thống kê");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        panel.add(title, BorderLayout.WEST);

        return panel;
    }

    /* ================= CONTENT ================= */
    private JPanel createContent() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBorder(new EmptyBorder(10, 15, 15, 15));

        panel.add(createTabs(), BorderLayout.NORTH);
        panel.add(createCenterPanel(), BorderLayout.CENTER);

        return panel;
    }

    /* ================= TABS ================= */
    private JTabbedPane createTabs() {
        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("Doanh thu", new JPanel());
        tabs.addTab("Bán hàng", new JPanel());
        tabs.addTab("Khách mua hàng theo thời gian", new JPanel());

        return tabs;
    }

    /* ================= CENTER ================= */
    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));

        panel.add(createFilterPanel(), BorderLayout.NORTH);
        panel.add(createTablePanel(), BorderLayout.CENTER);
        panel.add(createChartPanel(), BorderLayout.SOUTH);

        return panel;
    }

    /* ================= FILTER ================= */
    private JPanel createFilterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));

        panel.add(new JLabel("Từ năm:"));
        JComboBox<String> cbFrom = new JComboBox<>(new String[]{"2024", "2023", "2022"});
        panel.add(cbFrom);

        panel.add(new JLabel("Đến năm:"));
        JComboBox<String> cbTo = new JComboBox<>(new String[]{"2024", "2023", "2022"});
        panel.add(cbTo);

        JButton btnExcel = new JButton("Xuất Excel");
        JButton btnPdf = new JButton("In PDF");

        panel.add(btnExcel);
        panel.add(btnPdf);

        return panel;
    }

    /* ================= TABLE ================= */
    private JScrollPane createTablePanel() {

        String[] columns = {"Năm", "Vốn", "Doanh thu", "Lợi nhuận"};

        Object[][] data = {
                {"2024", "39.374.000 đ", "1.592.050 đ", "-37.781.950 đ"}
        };

        JTable table = new JTable(new DefaultTableModel(data, columns));
        table.setRowHeight(28);

        return new JScrollPane(table);
    }

    /* ================= CHART ================= */
    private JPanel createChartPanel() {
        // Improved Java2D bar chart: scaled, labeled, and handles negative values
        return new JPanel() {
            final long[] values = new long[]{39374000L, 1592050L, -37781950L};
            final String[] labels = new String[]{"Vốn", "Doanh thu", "Lợi nhuận"};
            final Color[] colors = new Color[]{new Color(70,130,180), new Color(60,179,113), new Color(178,34,34)};

            private String fmt(long v) {
                java.text.NumberFormat nf = java.text.NumberFormat.getInstance(new java.util.Locale("vi", "VN"));
                return nf.format(v) + " đ";
            }

            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();
                int padLeft = 60, padRight = 40, padTop = 20, padBottom = 50;
                int chartW = w - padLeft - padRight;
                int chartH = h - padTop - padBottom;

                // background
                g2.setColor(getBackground());
                g2.fillRect(0, 0, w, h);

                // find min/max
                long min = Long.MAX_VALUE, max = Long.MIN_VALUE;
                for (long v : values) { min = Math.min(min, v); max = Math.max(max, v); }
                if (min == Long.MAX_VALUE) { min = 0; max = 0; }
                if (min == max) { // avoid division by zero
                    max = Math.max(max, 1);
                    min = Math.min(min, -1);
                }

                // scale
                double scale = (double) chartH / (max - min);

                // draw horizontal grid lines and axis labels (5 steps)
                g2.setColor(new Color(230,230,230));
                g2.setStroke(new BasicStroke(1f));
                int steps = 4;
                java.text.NumberFormat nf = java.text.NumberFormat.getInstance(new java.util.Locale("vi", "VN"));
                for (int i = 0; i <= steps; i++) {
                    double t = (double) i / steps;
                    long valueAt = (long) Math.round(max - t * (max - min));
                    int y = padTop + (int) Math.round((max - valueAt) * scale);
                    g2.setColor(new Color(240,240,240));
                    g2.drawLine(padLeft, y, w - padRight, y);
                    g2.setColor(Color.DARK_GRAY);
                    String lbl = nf.format(valueAt);
                    g2.drawString(lbl, 6, y + 4);
                }

                // baseline for zero
                int yZero = padTop + (int) Math.round((max - 0d) * scale);
                g2.setColor(Color.GRAY);
                g2.setStroke(new BasicStroke(2f));
                g2.drawLine(padLeft, yZero, w - padRight, yZero);

                // bars
                int n = values.length;
                int gap = 24;
                int barW = (chartW - gap * (n + 1)) / n;
                for (int i = 0; i < n; i++) {
                    long v = values[i];
                    int x = padLeft + gap + i * (barW + gap);
                    int y = padTop + (int) Math.round((max - Math.max(v, 0)) * scale);
                    int yTop = padTop + (int) Math.round((max - Math.max(v, (long)min)) * scale);
                    int height = Math.abs((int) Math.round(v * scale));

                    // compute rect for positive/negative
                    int rectTop, rectHeight;
                    if (v >= 0) {
                        rectTop = padTop + (int) Math.round((max - v) * scale);
                        rectHeight = (int) Math.round(v * scale);
                    } else {
                        rectTop = yZero;
                        rectHeight = (int) Math.round(Math.abs(v) * scale);
                    }

                    // ensure minimum visible height
                    if (rectHeight < 4) rectHeight = 4;

                    // draw bar
                    g2.setColor(colors[i % colors.length]);
                    g2.fillRoundRect(x, rectTop, barW, rectHeight, 6, 6);
                    g2.setColor(colors[i % colors.length].darker());
                    g2.setStroke(new BasicStroke(1f));
                    g2.drawRoundRect(x, rectTop, barW, rectHeight, 6, 6);

                    // value label above/below bar
                    String valLabel = fmt(v);
                    FontMetrics fm = g2.getFontMetrics();
                    int labelW = fm.stringWidth(valLabel);
                    int lx = x + (barW - labelW) / 2;
                    int ly = (v >= 0) ? rectTop - 6 : rectTop + rectHeight + fm.getAscent() + 2;
                    g2.setColor(Color.DARK_GRAY);
                    g2.drawString(valLabel, lx, ly);

                    // category label under chart
                    String cat = labels[i];
                    int cw = fm.stringWidth(cat);
                    int cx = x + (barW - cw) / 2;
                    g2.drawString(cat, cx, h - 18);
                }

                g2.dispose();
            }

            @Override public Dimension getPreferredSize() {
                return new Dimension(900, 300);
            }
        };
    }

    /* ================= MAIN ================= */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ThongKeGUI().setVisible(true));
    }
}
// ...existing code...