package lk.educore.schoolms.gui;

import javaswingdev.chart.ModelPieChart;
import javaswingdev.chart.PieChart;
import lk.educore.schoolms.component.RoundPanel;
import lk.educore.schoolms.model.MySQL;

import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.io.InputStream;
import java.sql.ResultSet;
import java.util.logging.Level;

/**
 * Dashboard Panel Modern home panel displaying statistics cards with gradient
 * backgrounds and interactive pie charts for the EduCore School Management
 * System.
 *
 * @author ASUS™
 */
public class DashboardPanel extends JPanel {

    // Stat card value labels
    private JLabel lblTotalStudents;
    private JLabel lblTotalTeachers;
    private JLabel lblAttendanceRate;
    private JLabel lblFeeCollection;

    // Chart holder panels
    private JPanel teacherChartHolder;
    private JPanel feeChartHolder;

    public DashboardPanel() {
        initComponents();
        loadStatistics();
        loadCharts();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(900, 600));
        
        add(buildHeader(), BorderLayout.NORTH);
        
        JPanel content = new JPanel(new BorderLayout(0, 20));
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        content.add(buildStatCards(), BorderLayout.NORTH);
        content.add(buildChartsRow(), BorderLayout.CENTER);

        add(content, BorderLayout.CENTER);
    }

    /**
     * Light-gray header strip with "Dashboard" title.
     */
    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(243, 242, 242));
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(225, 225, 225)),
                BorderFactory.createEmptyBorder(15, 25, 15, 25)));

        JLabel title = new JLabel("Dashboard Overview");
        title.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        title.setForeground(new Color(60, 60, 60));
        header.add(title, BorderLayout.WEST);

        return header;
    }

    /**
     * Four gradient stat cards in a 1×4 grid. Each card shows a big number, a
     * label, and an icon.
     */
    private JPanel buildStatCards() {
        JPanel cards = new JPanel(new GridLayout(1, 4, 12, 0));
        cards.setOpaque(false);
        cards.setPreferredSize(new Dimension(0, 155));

        // Card 1 : Students
        lblTotalStudents = new JLabel("0");
        cards.add(createStatCard(
                lblTotalStudents, "Students",
                Color.decode("#183d8a"), Color.decode("#719bf0"),
                "/lk/educore/schoolms/resources/other/icons8-maximum-order-1001.png"));

        // Card 2 : Teachers
        lblTotalTeachers = new JLabel("0");
        cards.add(createStatCard(
                lblTotalTeachers, "Teachers",
                Color.decode("#0D5917"), Color.decode("#51F815"),
                "/lk/educore/schoolms/resources/other/icons8-add-user-male-100.png"));

        // Card 3 : Attendance
        lblAttendanceRate = new JLabel("0%");
        cards.add(createStatCard(
                lblAttendanceRate, "Attendance",
                Color.decode("#C55404"), Color.decode("#F8D32A"),
                "/lk/educore/schoolms/resources/other/icons8-fingerprint-accepted-100.png"));

        // Card 4 : Fees / Earnings 
        lblFeeCollection = new JLabel("Rs.0");
        cards.add(createStatCard(
                lblFeeCollection, "Earnings",
                Color.decode("#931835"), Color.decode("#F18352"),
                "/lk/educore/schoolms/resources/other/icons8-money-100.png"));

        return cards;
    }

    /**
     * Build a single gradient stat card.
     *
     * @param valueLabel JLabel whose text will be updated with the stat value
     * @param subtitle Descriptive text below the number (e.g. "Students")
     * @param startColor Gradient start color
     * @param endColor Gradient end color
     * @param iconPath Classpath resource path for the card icon
     * @return configured RoundPanel
     */
    private RoundPanel createStatCard(JLabel valueLabel, String subtitle,
            Color startColor, Color endColor, String iconPath) {

        RoundPanel card = new RoundPanel();
        card.setGradientColors(startColor, endColor);
        card.setLayout(new BorderLayout(10, 0));
        card.setBorder(BorderFactory.createEmptyBorder(25, 28, 25, 20));

        // ── Left: big number + subtitle ──
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 44));
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sub = new JLabel(subtitle);
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        sub.setForeground(new Color(255, 255, 255, 220));
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);

        textPanel.add(valueLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        textPanel.add(sub);
        card.add(textPanel, BorderLayout.CENTER);

        // ── Right: icon ──
        loadAndSetIcon(card, iconPath, 75, 75);

        return card;
    }

    /**
     * Load a resource icon, scale it, and place it at BorderLayout.EAST on the
     * card.
     */
    private void loadAndSetIcon(JPanel card, String iconPath, int w, int h) {
        try {
            InputStream stream = getClass().getResourceAsStream(iconPath);
            if (stream != null) {
                ImageIcon raw = new ImageIcon(ImageIO.read(stream));
                Image scaled = raw.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
                JLabel icon = new JLabel(new ImageIcon(scaled));
                icon.setVerticalAlignment(SwingConstants.CENTER);
                card.add(icon, BorderLayout.EAST);
                stream.close();
            }
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING,
                    "Dashboard icon load failed: " + iconPath, e);
        }
    }

    /**
     * Two side-by-side chart sections with titled panels.
     */
    private JPanel buildChartsRow() {
        JPanel row = new JPanel(new GridLayout(1, 2, 20, 0));
        row.setOpaque(false);
        row.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        // ── Teacher Types ──
        teacherChartHolder = new JPanel(new BorderLayout());
        teacherChartHolder.setOpaque(false);
        row.add(wrapChart("Teacher Types", teacherChartHolder));

        // ── Fee Collection Status ──
        feeChartHolder = new JPanel(new BorderLayout());
        feeChartHolder.setOpaque(false);
        row.add(wrapChart("Fee Collection Status", feeChartHolder));

        return row;
    }

    /**
     * Wrap a chart holder panel with a bold title label above it.
     */
    private JPanel wrapChart(String title, JPanel holder) {
        JPanel wrapper = new JPanel(new BorderLayout(0, 10));
        wrapper.setOpaque(false);
        wrapper.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lbl.setForeground(new Color(51, 51, 51));
        lbl.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));

        wrapper.add(lbl, BorderLayout.NORTH);
        wrapper.add(holder, BorderLayout.CENTER);
        return wrapper;
    }
    
    /**
     * Load the four numeric statistics into the card labels.
     */
    private void loadStatistics() {
        try {
            // ── Total Students 
            ResultSet studentRs = MySQL.executeSearch(
                    "SELECT COUNT(*) FROM student WHERE is_active = 1");
            if (studentRs.next()) {
                lblTotalStudents.setText(String.valueOf(studentRs.getInt(1)));
            }

            // ── Total Teachers 
            ResultSet teacherRs = MySQL.executeSearch(
                    "SELECT COUNT(*) FROM teacher WHERE status_id = 1");
            if (teacherRs.next()) {
                lblTotalTeachers.setText(String.valueOf(teacherRs.getInt(1)));
            }

            // ── Attendance Rate (last 30 days) 
            ResultSet attendanceRs = MySQL.executeSearch(
                    "SELECT ROUND("
                    + "(SELECT COUNT(*) FROM student_attendance "
                    + " WHERE status = 'Present' "
                    + "   AND date_time >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)) * 100.0 / "
                    + "NULLIF((SELECT COUNT(*) FROM student_attendance "
                    + " WHERE date_time >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)), 0)"
                    + ", 1)");
            if (attendanceRs.next()) {
                String rate = attendanceRs.getString(1);
                lblAttendanceRate.setText(rate != null ? rate + "%" : "N/A");
            }

            // ── Fee Collection (current month) 
            ResultSet feeRs = MySQL.executeSearch(
                    "SELECT COALESCE(SUM(amount), 0) FROM fee "
                    + "WHERE status = 'Paid' "
                    + "  AND paid_date >= DATE_FORMAT(CURDATE(), '%Y-%m-01')");
            if (feeRs.next()) {
                lblFeeCollection.setText(formatNumber(feeRs.getDouble(1)));
            }

        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING,
                    "Dashboard statistics loading failed", e);
            lblTotalStudents.setText("--");
            lblTotalTeachers.setText("--");
            lblAttendanceRate.setText("--%");
            lblFeeCollection.setText("Rs.0");
        }
    }

    /**
     * Load pie-chart data into the two chart holders.
     */
    private void loadCharts() {
        loadTeacherTypesChart();
        loadFeeStatusChart();
    }

    // ── Teacher Types Pie Chart 
    private void loadTeacherTypesChart() {
        PieChart chart = new PieChart();
        chart.setChartType(PieChart.PeiChartType.DONUT_CHART);

        Color[] palette = {
            new Color(23, 126, 238), // Blue
            new Color(221, 65, 65), // Red
            new Color(47, 157, 64), // Green
            new Color(196, 151, 58), // Gold
            new Color(156, 39, 176), // Purple
            new Color(244, 67, 54), // Bright Red
            new Color(33, 150, 243), // Light Blue
            new Color(76, 175, 80), // Light Green
            new Color(255, 152, 0), // Orange
            new Color(121, 85, 72), // Brown
            new Color(96, 125, 139), // Blue Gray
            new Color(233, 30, 99) // Pink
        };

        boolean hasData = false;
        try {
            ResultSet rs = MySQL.executeSearch(
                    "SELECT tt.type_name, COUNT(t.teacher_id) AS count "
                    + "FROM teacher_type tt "
                    + "LEFT JOIN teacher t "
                    + "  ON tt.teacher_type_id = t.teacher_type_id "
                    + "  AND t.status_id = 1 "
                    + "GROUP BY tt.teacher_type_id, tt.type_name");

            int idx = 0;
            while (rs.next()) {
                String name = rs.getString("type_name");
                int count = rs.getInt("count");
                if (count > 0) {
                    chart.addData(new ModelPieChart(name, count, palette[idx % palette.length]));
                    hasData = true;
                }
                idx++;
            }
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING,
                    "Teacher chart data load failed", e);
        }

        if (!hasData) {
            // Fallback sample data
            chart.addData(new ModelPieChart("Teacher", 8, palette[0]));
            chart.addData(new ModelPieChart("Principal", 1, palette[1]));
            chart.addData(new ModelPieChart("Office Staff", 3, palette[2]));
            chart.addData(new ModelPieChart("Support Staff", 2, palette[3]));
        }

        teacherChartHolder.add(chart, BorderLayout.CENTER);
        teacherChartHolder.revalidate();
        teacherChartHolder.repaint();
    }

    // ── Fee Collection Status Pie Chart 
    private void loadFeeStatusChart() {
        PieChart chart = new PieChart();
        chart.setChartType(PieChart.PeiChartType.DONUT_CHART);

        boolean hasData = false;
        try {
            ResultSet rs = MySQL.executeSearch(
                    "SELECT status, COUNT(*) AS count FROM fee GROUP BY status");

            while (rs.next()) {
                String status = rs.getString("status");
                int count = rs.getInt("count");
                if (count > 0) {
                    Color color;
                    switch (status) {
                        case "Paid":
                            color = new Color(76, 175, 80);   // Green
                            break;
                        case "Pending":
                            color = new Color(33, 150, 243);  // Blue
                            break;
                        case "Overdue":
                            color = new Color(244, 67, 54);   // Red
                            break;
                        default:
                            color = new Color(158, 158, 158); // Gray
                            break;
                    }
                    chart.addData(new ModelPieChart(status, count, color));
                    hasData = true;
                }
            }
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING,
                    "Fee chart data load failed", e);
        }

        if (!hasData) {
            // Fallback sample data
            chart.addData(new ModelPieChart("Paid", 52, new Color(76, 175, 80)));
            chart.addData(new ModelPieChart("Pending", 24, new Color(33, 150, 243)));
            chart.addData(new ModelPieChart("Overdue", 24, new Color(244, 67, 54)));
        }

        feeChartHolder.add(chart, BorderLayout.CENTER);
        feeChartHolder.revalidate();
        feeChartHolder.repaint();
    }
    
    /**
     * Format a number with k/M suffix for compact display. e.g. 1500 → "1.5k",
     * 2000000 → "2.0M"
     */
    public static String formatNumber(double number) {
        if (number < 1000) {
            return String.format("%.0f", number);
        } else if (number < 1_000_000) {
            return String.format("%.1fk", number / 1000);
        } else {
            return String.format("%.1fM", number / 1_000_000);
        }
    }
}
