
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
        // Phiên bản đơn giản vẽ bằng Java2D — không cần JFreeChart
        return new JPanel() {
            int[] values = new int[]{39374000, 1592050, -37781950};
            String[] labels = new String[]{"Vốn", "Doanh thu", "Lợi nhuận"};
            Color[] colors = new Color[]{new Color(70,130,180), new Color(60,179,113), new Color(220,20,60)};

            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                int w = getWidth();
                int h = getHeight();
                g2.setColor(Color.WHITE);
                g2.fillRect(0,0,w,h);

                int maxAbs = 1;
                for (int v : values) maxAbs = Math.max(maxAbs, Math.abs(v));
                int barAreaW = w - 80;
                int barW = barAreaW / values.length - 20;
                int baseY = h/2;

                for (int i = 0; i < values.length; i++) {
                    int x = 40 + i * (barW + 20);
                    int barH = (int) ((h/2 - 20) * ((double)Math.abs(values[i]) / maxAbs));
                    if (values[i] >= 0) {
                        g2.setColor(colors[i]);
                        g2.fillRect(x, baseY - barH, barW, barH);
                    } else {
                        g2.setColor(colors[i].darker());
                        g2.fillRect(x, baseY, barW, barH);
                    }
                    g2.setColor(Color.DARK_GRAY);
                    g2.drawString(labels[i], x + 6, baseY + (values[i] < 0 ? barH + 16 : -barH - 6));
                }

                g2.dispose();
            }

            @Override public Dimension getPreferredSize() {
                return new Dimension(900, 250);
            }
        };
    }

    /* ================= MAIN ================= */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ThongKeGUI().setVisible(true));
    }
}
// ...existing code...