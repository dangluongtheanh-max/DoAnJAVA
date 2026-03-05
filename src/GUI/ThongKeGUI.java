package GUI;

import BUS.ThongKeBUS;
import DTO.ThongKe.ThongKeDoanhThuDTO;
import DTO.ThongKe.ThongKeHoaDonBanDTO;
import DTO.ThongKe.ThongKeSanPhamBanDTO;
import DTO.ThongKe.ThongKeTheLoaiBanDTO;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ThongKeGUI extends JPanel {

    // ─── BUS ────────────────────────────────────────────────────────────────
    private final ThongKeBUS tkBUS = new ThongKeBUS();

    // ─── Tab: Doanh thu theo tháng ──────────────────────────────────────────
    private JComboBox<String> cbNam;
    private JTable tableTheoThang;
    private DefaultTableModel modelTheoThang;
    private RevenueBarChartPanel chartPanelTheoThang;

    // ─── Tab: Doanh thu từ ngày đến ngày ────────────────────────────────────
    private JComboBox<String> DoanhThuCbThoiGian;
    private JSpinner DoanhThuSpinnerBatDau;
    private JSpinner DoanhThuSpinnerKetThuc;
    private JTable TableTuNgayDenNgay;
    private DefaultTableModel modelTuNgayDenNgay;

    // ─── Tab: Sản phẩm bán chạy ─────────────────────────────────────────────
    private JComboBox<String> CbThoiGianSanPham;
    private JSpinner spSpinnerBatDau;
    private JSpinner spSpinnerKetThuc;
    private JComboBox<String> cbTopSanPham;
    private JTable TableSanPham;
    private DefaultTableModel modelSanPham;

    // ─── Tab: Thể loại bán chạy ─────────────────────────────────────────────
    private JComboBox<String> CbThoiGianTheLoai;
    private JSpinner tlSpinnerBatDau;
    private JSpinner tlSpinnerKetThuc;
    private JComboBox<String> cbTopTheLoai;
    private JTable TableTheLoai;
    private DefaultTableModel modelTheLoai;

    // ─── Tab: Hóa đơn theo thời gian ────────────────────────────────────────
    private JComboBox<String> CbThoiGian;
    private JSpinner tgSpinnerBatDau;
    private JSpinner tgSpinnerKetThuc;
    private JTable TableThoiGian;
    private DefaultTableModel modelThoiGian;

    // ─── Spinner date format ─────────────────────────────────────────────────
    private static final String DATE_FORMAT = "dd/MM/yyyy";

    // ════════════════════════════════════════════════════════════════════════
    public ThongKeGUI() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1020, 750));
        setBackground(Color.WHITE);

        // Tiêu đề
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        titlePanel.setBackground(Color.WHITE);
        JLabel lblTitle = new JLabel("Thống kê");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(new Color(253, 183, 58));
        titlePanel.add(lblTitle);
        add(titlePanel, BorderLayout.NORTH);

        // Tab chính
        JTabbedPane mainTabs = new JTabbedPane();
        mainTabs.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        mainTabs.addTab("Doanh thu",        buildDoanhThuPanel());
        mainTabs.addTab("Bán hàng",         buildBanHangPanel());
        mainTabs.addTab("Hóa đơn theo TG",  buildHoaDonPanel());

        add(mainTabs, BorderLayout.CENTER);

        // Load dữ liệu mặc định
        loadTheoThang();
        loadTuNgayDenNgay();
        loadSanPham();
        loadTheLoai();
        loadHoaDon();
    }

    // ════════════════════════════════════════════════════════════════════════
    //  PANEL: DOANH THU
    // ════════════════════════════════════════════════════════════════════════
    private JPanel buildDoanhThuPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JTabbedPane subTabs = new JTabbedPane();
        subTabs.addTab("Theo tháng trong năm", buildTheoThangPanel());
        subTabs.addTab("Từ ngày đến ngày",      buildTuNgayDenNgayPanel());
        panel.add(subTabs, BorderLayout.CENTER);
        return panel;
    }

    // ── Sub-tab: Doanh thu theo tháng ───────────────────────────────────────
    private JPanel buildTheoThangPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        // Toolbar
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        toolbar.add(new JLabel("Chọn năm:"));
        cbNam = new JComboBox<>(buildYearItems());
        cbNam.addActionListener(e -> loadTheoThang());
        toolbar.add(cbNam);

        JButton btnExcel = makeButton("Xuất Excel");
        JButton btnPdf   = makeButton("In PDF");
        btnExcel.addActionListener(e -> exportExcel(tableTheoThang, "Doanh thu theo tháng"));
        btnPdf.addActionListener(e   -> exportPdf(tableTheoThang,   "Doanh thu theo tháng"));
        toolbar.add(Box.createHorizontalStrut(20));
        toolbar.add(btnExcel);
        toolbar.add(btnPdf);

        // Bảng
        modelTheoThang = new DefaultTableModel(
                new String[]{"Tháng", "Vốn", "Doanh thu", "Lợi nhuận"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tableTheoThang = new JTable(modelTheoThang);
        styleTable(tableTheoThang);

        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(new JScrollPane(tableTheoThang), BorderLayout.CENTER);
        // Thêm biểu đồ doanh thu theo tháng
        ArrayList<ThongKeDoanhThuDTO> data = tkBUS.thongKeDoanhThuTheoThang(getSelectedYear());
        chartPanelTheoThang = new RevenueBarChartPanel();
        chartPanelTheoThang.setData(data);
        panel.add(chartPanelTheoThang, BorderLayout.SOUTH);
        return panel;
    }

    private int getSelectedYear() {
        if (cbNam != null && cbNam.getSelectedItem() != null) {
            return Integer.parseInt(cbNam.getSelectedItem().toString());
        }
        return java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
    }

    private static class RevenueBarChartPanel extends JPanel {
        private final Color colorVon = new Color(223, 190, 144);
        private final Color colorDoanhThu = new Color(98, 185, 236);
        private final Color colorLoiNhuan = new Color(154, 128, 230);
        private java.util.List<ThongKeDoanhThuDTO> data = new ArrayList<>();

        RevenueBarChartPanel() {
            setBackground(new Color(245, 245, 245));
            setPreferredSize(new Dimension(900, 320));
        }

        void setData(java.util.List<ThongKeDoanhThuDTO> data) {
            this.data = (data == null) ? new ArrayList<>() : data;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();
            int left = 58, right = 18, top = 18, bottom = 48;
            int chartW = Math.max(100, w - left - right);
            int chartH = Math.max(80, h - top - bottom);

            long max = 1;
            for (ThongKeDoanhThuDTO d : data) {
                max = Math.max(max, Math.max(d.getTongVon(), Math.max(d.getTongDoanhThu(), d.getLoiNhuan())));
            }

            // Grid + trục Y
            g2.setColor(new Color(205, 205, 205));
            for (int i = 0; i <= 10; i++) {
                int y = top + i * chartH / 10;
                g2.draw(new Line2D.Double(left, y, left + chartW, y));
                long val = (long) ((10 - i) * (double) max / 10);
                g2.setColor(new Color(120, 120, 120));
                g2.drawString(String.format("%,d", val), 10, y + 5);
                g2.setColor(new Color(205, 205, 205));
            }

            g2.setColor(new Color(130, 130, 130));
            g2.drawLine(left, top + chartH, left + chartW, top + chartH);

            if (!data.isEmpty()) {
                int groups = data.size();
                int groupW = Math.max(16, chartW / groups);
                int barW = Math.max(4, Math.min(14, groupW / 5));

                for (int i = 0; i < groups; i++) {
                    ThongKeDoanhThuDTO d = data.get(i);
                    int gx = left + i * groupW + (groupW - 3 * barW) / 2;

                    int hVon = (int) (chartH * (double) d.getTongVon() / max);
                    int hDoanhThu = (int) (chartH * (double) d.getTongDoanhThu() / max);
                    int hLoiNhuan = (int) (chartH * (double) d.getLoiNhuan() / max);

                    g2.setColor(colorVon);
                    g2.fillRect(gx, top + chartH - hVon, barW, hVon);

                    g2.setColor(colorDoanhThu);
                    g2.fillRect(gx + barW, top + chartH - hDoanhThu, barW, hDoanhThu);

                    g2.setColor(colorLoiNhuan);
                    g2.fillRect(gx + 2 * barW, top + chartH - hLoiNhuan, barW, hLoiNhuan);

                    if (groups <= 12) {
                        g2.setColor(new Color(110, 110, 110));
                        String label = d.getThoiGian();
                        if (label.length() > 8) label = label.substring(0, 8);
                        g2.drawString(label, gx - 2, top + chartH + 16);
                    }
                }
            }

            // Legend
            int ly = h - 16;
            drawLegend(g2, left + chartW / 2 - 120, ly, colorVon, "Vốn");
            drawLegend(g2, left + chartW / 2 - 30, ly, colorDoanhThu, "Doanh thu");
            drawLegend(g2, left + chartW / 2 + 80, ly, colorLoiNhuan, "Lợi nhuận");

            g2.dispose();
        }

        private void drawLegend(Graphics2D g2, int x, int y, Color c, String text) {
            g2.setColor(c);
            g2.fillOval(x, y - 8, 8, 8);
            g2.setColor(new Color(100, 100, 100));
            g2.drawString(text, x + 12, y);
        }
    }

    // ── Sub-tab: Từ ngày đến ngày ────────────────────────────────────────────
    private JPanel buildTuNgayDenNgayPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        toolbar.add(new JLabel("Khoảng thời gian:"));
        DoanhThuCbThoiGian = buildThoiGianComboBox();
        toolbar.add(DoanhThuCbThoiGian);

        DoanhThuSpinnerBatDau  = buildDateSpinner();
        DoanhThuSpinnerKetThuc = buildDateSpinner();
        toolbar.add(new JLabel("Từ:"));
        toolbar.add(DoanhThuSpinnerBatDau);
        toolbar.add(new JLabel("Đến:"));
        toolbar.add(DoanhThuSpinnerKetThuc);

        JButton btnExcel = makeButton("Xuất Excel");
        JButton btnPdf   = makeButton("In PDF");
        btnExcel.addActionListener(e -> exportExcel(TableTuNgayDenNgay, "Doanh thu từ ngày đến ngày"));
        btnPdf.addActionListener(e   -> exportPdf(TableTuNgayDenNgay,   "Doanh thu từ ngày đến ngày"));
        toolbar.add(btnExcel);
        toolbar.add(btnPdf);

        // Sự kiện
        DoanhThuCbThoiGian.addActionListener(e -> {
            applyDateRange(DoanhThuSpinnerBatDau, DoanhThuSpinnerKetThuc, DoanhThuCbThoiGian);
            loadTuNgayDenNgay();
        });
        addSpinnerChangeListener(DoanhThuSpinnerBatDau,  this::loadTuNgayDenNgay);
        addSpinnerChangeListener(DoanhThuSpinnerKetThuc, this::loadTuNgayDenNgay);

        modelTuNgayDenNgay = new DefaultTableModel(
                new String[]{"Ngày", "Vốn", "Doanh thu", "Lợi nhuận"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        TableTuNgayDenNgay = new JTable(modelTuNgayDenNgay);
        styleTable(TableTuNgayDenNgay);

        // Đặt mặc định "7 ngày qua"
        DoanhThuCbThoiGian.setSelectedItem("7 ngày qua");

        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(new JScrollPane(TableTuNgayDenNgay), BorderLayout.CENTER);
        return panel;
    }

    // ════════════════════════════════════════════════════════════════════════
    //  PANEL: BÁN HÀNG
    // ════════════════════════════════════════════════════════════════════════
    private JPanel buildBanHangPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTabbedPane subTabs = new JTabbedPane();
        subTabs.addTab("Sản phẩm",  buildSanPhamPanel());
        subTabs.addTab("Thể loại",  buildTheLoaiPanel());
        panel.add(subTabs, BorderLayout.CENTER);
        return panel;
    }

    // ── Sub-tab: Sản phẩm bán chạy ─────────────────────────────────────────
    private JPanel buildSanPhamPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        toolbar.add(new JLabel("Khoảng thời gian:"));
        CbThoiGianSanPham = buildThoiGianComboBox();
        toolbar.add(CbThoiGianSanPham);

        spSpinnerBatDau  = buildDateSpinner();
        spSpinnerKetThuc = buildDateSpinner();
        toolbar.add(new JLabel("Từ:"));
        toolbar.add(spSpinnerBatDau);
        toolbar.add(new JLabel("Đến:"));
        toolbar.add(spSpinnerKetThuc);

        toolbar.add(new JLabel("Hiển thị:"));
        cbTopSanPham = new JComboBox<>(new String[]{"Tất cả", "Top 3", "Top 5", "Top 10", "Top 20"});
        toolbar.add(cbTopSanPham);

        JButton btnExcel = makeButton("Xuất Excel");
        JButton btnPdf   = makeButton("In PDF");
        btnExcel.addActionListener(e -> exportExcel(TableSanPham, "Sản phẩm bán chạy"));
        btnPdf.addActionListener(e   -> exportPdf(TableSanPham,   "Sản phẩm bán chạy"));
        toolbar.add(btnExcel);
        toolbar.add(btnPdf);

        // Sự kiện
        CbThoiGianSanPham.addActionListener(e -> {
            applyDateRange(spSpinnerBatDau, spSpinnerKetThuc, CbThoiGianSanPham);
            loadSanPham();
        });
        addSpinnerChangeListener(spSpinnerBatDau,  this::loadSanPham);
        addSpinnerChangeListener(spSpinnerKetThuc, this::loadSanPham);
        cbTopSanPham.addActionListener(e -> loadSanPham());

        modelSanPham = new DefaultTableModel(
                new String[]{"STT", "Mã SP", "Tên SP", "SL bán", "SL đơn", "Doanh thu"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        TableSanPham = new JTable(modelSanPham);
        styleTable(TableSanPham);

        // Đặt mặc định "7 ngày qua"
        CbThoiGianSanPham.setSelectedItem("7 ngày qua");

        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(new JScrollPane(TableSanPham), BorderLayout.CENTER);
        return panel;
    }

    // ── Sub-tab: Thể loại bán chạy ─────────────────────────────────────────
    private JPanel buildTheLoaiPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        toolbar.add(new JLabel("Khoảng thời gian:"));
        CbThoiGianTheLoai = buildThoiGianComboBox();
        toolbar.add(CbThoiGianTheLoai);

        tlSpinnerBatDau  = buildDateSpinner();
        tlSpinnerKetThuc = buildDateSpinner();
        toolbar.add(new JLabel("Từ:"));
        toolbar.add(tlSpinnerBatDau);
        toolbar.add(new JLabel("Đến:"));
        toolbar.add(tlSpinnerKetThuc);

        toolbar.add(new JLabel("Hiển thị:"));
        cbTopTheLoai = new JComboBox<>(new String[]{"Tất cả", "Top 3", "Top 5", "Top 10", "Top 20"});
        toolbar.add(cbTopTheLoai);

        JButton btnExcel = makeButton("Xuất Excel");
        JButton btnPdf   = makeButton("In PDF");
        btnExcel.addActionListener(e -> exportExcel(TableTheLoai, "Thể loại bán chạy"));
        btnPdf.addActionListener(e   -> exportPdf(TableTheLoai,   "Thể loại bán chạy"));
        toolbar.add(btnExcel);
        toolbar.add(btnPdf);

        // Sự kiện
        CbThoiGianTheLoai.addActionListener(e -> {
            applyDateRange(tlSpinnerBatDau, tlSpinnerKetThuc, CbThoiGianTheLoai);
            loadTheLoai();
        });
        addSpinnerChangeListener(tlSpinnerBatDau,  this::loadTheLoai);
        addSpinnerChangeListener(tlSpinnerKetThuc, this::loadTheLoai);
        cbTopTheLoai.addActionListener(e -> loadTheLoai());

        modelTheLoai = new DefaultTableModel(
                new String[]{"STT", "Mã TL", "Tên TL", "SL bán", "Số loại SP", "SL đơn", "Doanh thu"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        TableTheLoai = new JTable(modelTheLoai);
        styleTable(TableTheLoai);

        // Đặt mặc định "7 ngày qua"
        CbThoiGianTheLoai.setSelectedItem("7 ngày qua");

        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(new JScrollPane(TableTheLoai), BorderLayout.CENTER);
        return panel;
    }

    // ════════════════════════════════════════════════════════════════════════
    //  PANEL: HÓA ĐƠN THEO THỜI GIAN
    // ════════════════════════════════════════════════════════════════════════
    private JPanel buildHoaDonPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        toolbar.add(new JLabel("Khoảng thời gian:"));
        CbThoiGian = buildThoiGianComboBox();
        toolbar.add(CbThoiGian);

        tgSpinnerBatDau  = buildDateSpinner();
        tgSpinnerKetThuc = buildDateSpinner();
        toolbar.add(new JLabel("Từ:"));
        toolbar.add(tgSpinnerBatDau);
        toolbar.add(new JLabel("Đến:"));
        toolbar.add(tgSpinnerKetThuc);

        JButton btnExcel = makeButton("Xuất Excel");
        JButton btnPdf   = makeButton("In PDF");
        btnExcel.addActionListener(e -> exportExcel(TableThoiGian, "Hóa đơn theo thời gian"));
        btnPdf.addActionListener(e   -> exportPdf(TableThoiGian,   "Hóa đơn theo thời gian"));
        toolbar.add(btnExcel);
        toolbar.add(btnPdf);

        // Sự kiện
        CbThoiGian.addActionListener(e -> {
            applyDateRange(tgSpinnerBatDau, tgSpinnerKetThuc, CbThoiGian);
            loadHoaDon();
        });
        addSpinnerChangeListener(tgSpinnerBatDau,  this::loadHoaDon);
        addSpinnerChangeListener(tgSpinnerKetThuc, this::loadHoaDon);

        modelThoiGian = new DefaultTableModel(
                new String[]{"STT", "Thời gian", "Số lượng đơn", "Số lượng SP", "Số loại SP", "Doanh thu"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        TableThoiGian = new JTable(modelThoiGian);
        styleTable(TableThoiGian);

        // Đặt mặc định "7 ngày qua"
        CbThoiGian.setSelectedItem("7 ngày qua");

        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(new JScrollPane(TableThoiGian), BorderLayout.CENTER);
        return panel;
    }

    // ════════════════════════════════════════════════════════════════════════
    //  LOAD DỮ LIỆU
    // ════════════════════════════════════════════════════════════════════════

    private void loadTheoThang() {
        if (cbNam == null || cbNam.getSelectedItem() == null) return;
        int nam = Integer.parseInt(cbNam.getSelectedItem().toString());
        ArrayList<ThongKeDoanhThuDTO> result = tkBUS.thongKeDoanhThuTheoThang(nam);
        modelTheoThang.setRowCount(0);
        for (ThongKeDoanhThuDTO dto : result) {
            modelTheoThang.addRow(new Object[]{
                dto.getThoiGian(),
                formatVND(dto.getTongVon()),
                formatVND(dto.getTongDoanhThu()),
                formatVND(dto.getLoiNhuan())
            });
        }
        if (chartPanelTheoThang != null) {
            chartPanelTheoThang.setData(result);
        }
    }

    private void loadTuNgayDenNgay() {
        if (DoanhThuSpinnerBatDau == null) return;
        Date startDate = getDateFromSpinner(DoanhThuSpinnerBatDau);
        Date endDate   = getDateFromSpinner(DoanhThuSpinnerKetThuc);
        if (!validateDateRange(startDate, endDate)) return;

        ArrayList<ThongKeDoanhThuDTO> result = tkBUS.thongKeDoanhThuTuNgayDenNgay(startDate, endDate);
        modelTuNgayDenNgay.setRowCount(0);
        for (ThongKeDoanhThuDTO dto : result) {
            modelTuNgayDenNgay.addRow(new Object[]{
                dto.getThoiGian(),
                formatVND(dto.getTongVon()),
                formatVND(dto.getTongDoanhThu()),
                formatVND(dto.getLoiNhuan())
            });
        }
    }

    private void loadSanPham() {
        if (spSpinnerBatDau == null) return;
        Date startDate = getDateFromSpinner(spSpinnerBatDau);
        Date endDate   = getDateFromSpinner(spSpinnerKetThuc);
        if (!validateDateRange(startDate, endDate)) return;

        ArrayList<ThongKeSanPhamBanDTO> result = tkBUS.thongKeSanPhamBanTrongKhoangThoiGian(startDate, endDate);
        int limit = getRowLimit(cbTopSanPham);
        modelSanPham.setRowCount(0);
        int stt = 1;
        for (ThongKeSanPhamBanDTO dto : result) {
            if (stt > limit) break;
            modelSanPham.addRow(new Object[]{
                stt++,
                formatID("SP", dto.getMaSP()),
                dto.getTenSP(),
                dto.getTongSoLuongBan(),
                dto.getSoHoaDon(),
                formatVND(dto.getDoanhThu())
            });
        }
    }

    private void loadTheLoai() {
        if (tlSpinnerBatDau == null) return;
        Date startDate = getDateFromSpinner(tlSpinnerBatDau);
        Date endDate   = getDateFromSpinner(tlSpinnerKetThuc);
        if (!validateDateRange(startDate, endDate)) return;

        // BUS mới: thongKeLoaiSanPhamTrongKhoangThoiGian
        ArrayList<ThongKeTheLoaiBanDTO> result = tkBUS.thongKeLoaiSanPhamTrongKhoangThoiGian(startDate, endDate);
        int limit = getRowLimit(cbTopTheLoai);
        modelTheLoai.setRowCount(0);
        int stt = 1;
        for (ThongKeTheLoaiBanDTO dto : result) {
            if (stt > limit) break;
            modelTheLoai.addRow(new Object[]{
                stt++,
                dto.getLoaiSP(),
                dto.getTongSoLuongBan(),
                dto.getSoHoaDon(),
                dto.getSoSanPham(),
                formatVND(dto.getDoanhThu())
            });
        }
    }

    private void loadHoaDon() {
        if (tgSpinnerBatDau == null) return;
        Date startDate = getDateFromSpinner(tgSpinnerBatDau);
        Date endDate   = getDateFromSpinner(tgSpinnerKetThuc);
        if (!validateDateRange(startDate, endDate)) return;

        ArrayList<ThongKeHoaDonBanDTO> result = tkBUS.thongKeHoaDonTrongKhoangThoiGian(startDate, endDate);
        modelThoiGian.setRowCount(0);
        int stt = 1;
        for (ThongKeHoaDonBanDTO dto : result) {
            modelThoiGian.addRow(new Object[]{
                stt++,
                dto.getThoiGian(),
                dto.getSoHoaDon(),
                dto.getTongSoSanPham(),
                dto.getTongLoaiSanPham(),
                formatVND(dto.getTongDoanhThu())
            });
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  HELPERS: DATE / SPINNER
    // ════════════════════════════════════════════════════════════════════════

    /** Tạo JSpinner chọn ngày dd/MM/yyyy */
    private JSpinner buildDateSpinner() {
        SpinnerDateModel model = new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH);
        JSpinner spinner = new JSpinner(model);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, DATE_FORMAT);
        spinner.setEditor(editor);
        spinner.setPreferredSize(new Dimension(110, 28));
        return spinner;
    }

    private Date getDateFromSpinner(JSpinner spinner) {
        return (Date) spinner.getValue();
    }

    private void addSpinnerChangeListener(JSpinner spinner, Runnable action) {
        spinner.addChangeListener(e -> action.run());
    }

    /** Tạo ComboBox khoảng thời gian nhanh */
    private JComboBox<String> buildThoiGianComboBox() {
        return new JComboBox<>(new String[]{
            "Hôm nay", "Hôm qua", "7 ngày qua", "30 ngày qua",
            "Tháng trước", "Tháng này", "Năm trước", "Năm nay", "Tùy chọn khác"
        });
    }

    /** Áp dụng khoảng thời gian nhanh vào hai spinner */
    private void applyDateRange(JSpinner from, JSpinner to, JComboBox<String> cb) {
        String sel = (String) cb.getSelectedItem();
        if (sel == null) return;
        Calendar cal = Calendar.getInstance();
        Date start = null, end = null;

        switch (sel) {
            case "Hôm nay" -> { start = cal.getTime(); end = cal.getTime(); }
            case "Hôm qua" -> {
                cal.add(Calendar.DATE, -1);
                start = cal.getTime(); end = cal.getTime();
            }
            case "7 ngày qua" -> {
                end = cal.getTime();
                cal.add(Calendar.DATE, -6);
                start = cal.getTime();
            }
            case "30 ngày qua" -> {
                end = cal.getTime();
                cal.add(Calendar.DATE, -29);
                start = cal.getTime();
            }
            case "Tháng trước" -> {
                cal.add(Calendar.MONTH, -1);
                cal.set(Calendar.DAY_OF_MONTH, 1);
                start = cal.getTime();
                cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                end = cal.getTime();
            }
            case "Tháng này" -> {
                cal.set(Calendar.DAY_OF_MONTH, 1);
                start = cal.getTime();
                cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                end = cal.getTime();
            }
            case "Năm trước" -> {
                cal.add(Calendar.YEAR, -1);
                cal.set(Calendar.MONTH, 0); cal.set(Calendar.DAY_OF_MONTH, 1);
                start = cal.getTime();
                cal.set(Calendar.MONTH, 11); cal.set(Calendar.DAY_OF_MONTH, 31);
                end = cal.getTime();
            }
            case "Năm nay" -> {
                cal.set(Calendar.MONTH, 0); cal.set(Calendar.DAY_OF_MONTH, 1);
                start = cal.getTime();
                cal.set(Calendar.MONTH, 11); cal.set(Calendar.DAY_OF_MONTH, 31);
                end = cal.getTime();
            }
            default -> { /* Tùy chọn khác - không thay đổi spinner */ return; }
        }
        if (start != null) from.setValue(start);
        if (end   != null) to.setValue(end);
    }

    /** Kiểm tra ngày hợp lệ, hiển thị lỗi nếu không */
    private boolean validateDateRange(Date start, Date end) {
        if (start == null || end == null) return false;
        if (start.after(end)) {
            JOptionPane.showMessageDialog(this,
                "Ngày bắt đầu không được lớn hơn ngày kết thúc", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    // ════════════════════════════════════════════════════════════════════════
    //  HELPERS: EXPORT (stub — kết nối ExcelExporter / PdfExporter của bạn)
    // ════════════════════════════════════════════════════════════════════════

    @SuppressWarnings("unused")
    private void exportExcel(JTable table, String title) {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Lưu file Excel");
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            String path = fc.getSelectedFile().getAbsolutePath();
            if (!path.endsWith(".xlsx")) path += ".xlsx";
            // TODO: ExcelExporter.exportToExcel(table, path, ...);
            JOptionPane.showMessageDialog(this, "Đã xuất Excel: " + path);
        }
    }

    @SuppressWarnings("unused")
    private void exportPdf(JTable table, String title) {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Lưu file PDF");
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            String path = fc.getSelectedFile().getAbsolutePath();
            if (!path.endsWith(".pdf")) path += ".pdf";
            // TODO: PdfExporter.exportToPdfReport(..., table, title, path);
            JOptionPane.showMessageDialog(this, "Đã xuất PDF: " + path);
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  HELPERS: UI
    // ════════════════════════════════════════════════════════════════════════

    private void styleTable(JTable table) {
        table.setRowHeight(24);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(135, 172, 217));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setGridColor(new Color(220, 220, 220));
        table.setSelectionBackground(new Color(200, 220, 255));
        table.setFillsViewportHeight(true);
    }

    private JButton makeButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setForeground(new Color(135, 172, 217));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    /** Tạo danh sách năm từ năm hiện tại trở về 10 năm trước */
    private String[] buildYearItems() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        String[] years = new String[11];
        for (int i = 0; i < 11; i++) {
            years[i] = String.valueOf(currentYear - i);
        }
        return years;
    }

    private int getRowLimit(JComboBox<String> cb) {
        if (cb == null) return Integer.MAX_VALUE;
        return switch ((String) cb.getSelectedItem()) {
            case "Top 3"  -> 3;
            case "Top 5"  -> 5;
            case "Top 10" -> 10;
            case "Top 20" -> 20;
            default       -> Integer.MAX_VALUE;
        };
    }

    // ════════════════════════════════════════════════════════════════════════
    //  HELPERS: FORMAT
    // ════════════════════════════════════════════════════════════════════════

    private String formatVND(double amount) {
        return String.format("%,.0f đ", amount);
    }

    private String formatID(String prefix, int id) {
        return String.format("%s%05d", prefix, id);
    }

    // ════════════════════════════════════════════════════════════════════════
    //  MAIN (để test độc lập)
    // ════════════════════════════════════════════════════════════════════════
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Thống kê");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new ThongKeGUI());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}