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
 * Role-aware: Admin sees full overview (students, teachers, earnings, fee charts).
 * Teacher sees a tailored view (students, attendance, upcoming exams, events).
 *
 * @author ASUS™
 */
public class DashboardPanel extends JPanel {

    private final String role;

    // Stat card value labels (shared)
    private JLabel lblTotalStudents;
    private JLabel lblAttendanceRate;

    // Admin-only stat labels
    private JLabel lblTotalTeachers;
    private JLabel lblFeeCollection;

    // Teacher-only stat labels
    private JLabel lblUpcomingExams;
    private JLabel lblActiveEvents;

    // Chart holder panels
    private JPanel chartHolder1;
    private JPanel chartHolder2;

    /**
     * Creates a DashboardPanel with role-specific content.
     *
     * @param role the user's role ("Admin" or "Teacher")
     */
    public DashboardPanel(String role) {
        this.role = role;
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

        String titleText = "Admin".equals(role)
                ? "Dashboard Overview"
                : "Teacher Dashboard";

        JLabel title = new JLabel(titleText);
        title.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        title.setForeground(new Color(60, 60, 60));
        header.add(title, BorderLayout.WEST);

        return header;
    }

    /**
     * Four gradient stat cards in a 1×4 grid. Cards shown depend on role.
     */
    private JPanel buildStatCards() {
        JPanel cards = new JPanel(new GridLayout(1, 4, 12, 0));
        cards.setOpaque(false);
        cards.setPreferredSize(new Dimension(0, 155));

        if ("Admin".equals(role)) {
            // ── Admin Cards ──

            // Card 1 : Students
            lblTotalStudents = new JLabel("0");
            cards.add(createStatCard(
                    lblTotalStudents, "Students",
                    Color.decode("#183d8a"), Color.decode("#719bf0"),
                    "/lk/educore/schoolms/resources/other/student.png"));

            // Card 2 : Teachers
            lblTotalTeachers = new JLabel("0");
            cards.add(createStatCard(
                    lblTotalTeachers, "Teachers",
                    Color.decode("#0D5917"), Color.decode("#51F815"),
                    "/lk/educore/schoolms/resources/other/teacher.png"));

            // Card 3 : Attendance
            lblAttendanceRate = new JLabel("0%");
            cards.add(createStatCard(
                    lblAttendanceRate, "Attendance",
                    Color.decode("#C55404"), Color.decode("#F8D32A"),
                    "/lk/educore/schoolms/resources/other/attendance.png"));

            // Card 4 : Fees / Earnings
            lblFeeCollection = new JLabel("Rs.0");
            cards.add(createStatCard(
                    lblFeeCollection, "Earnings",
                    Color.decode("#931835"), Color.decode("#F18352"),
                    "/lk/educore/schoolms/resources/other/fee.png"));

        } else {
            // ── Teacher Cards ──

            // Card 1 : Total Students
            lblTotalStudents = new JLabel("0");
            cards.add(createStatCard(
                    lblTotalStudents, "Students",
                    Color.decode("#183d8a"), Color.decode("#719bf0"),
                    "/lk/educore/schoolms/resources/other/student.png"));

            // Card 2 : Attendance Rate
            lblAttendanceRate = new JLabel("0%");
            cards.add(createStatCard(
                    lblAttendanceRate, "Attendance",
                    Color.decode("#0D5917"), Color.decode("#51F815"),
                    "/lk/educore/schoolms/resources/other/attendance.png"));

            // Card 3 : Upcoming Exams
            lblUpcomingExams = new JLabel("0");
            cards.add(createStatCard(
                    lblUpcomingExams, "Exams",
                    Color.decode("#C55404"), Color.decode("#F8D32A"),
                    "/lk/educore/schoolms/resources/other/exam.png"));

            // Card 4 : Active Events
            lblActiveEvents = new JLabel("0");
            cards.add(createStatCard(
                    lblActiveEvents, "Events",
                    Color.decode("#4A148C"), Color.decode("#CE93D8"),
                    "/lk/educore/schoolms/resources/other/event.png"));
        }

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
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 25));
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
     * Two side-by-side chart sections with titled panels. Content depends on role.
     */
    private JPanel buildChartsRow() {
        JPanel row = new JPanel(new GridLayout(1, 2, 20, 0));
        row.setOpaque(false);
        row.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        chartHolder1 = new JPanel(new BorderLayout());
        chartHolder1.setOpaque(false);

        chartHolder2 = new JPanel(new BorderLayout());
        chartHolder2.setOpaque(false);

        if ("Admin".equals(role)) {
            row.add(wrapChart("Teacher Types", chartHolder1));
            row.add(wrapChart("Fee Collection Status", chartHolder2));
        } else {
            row.add(wrapChart("Student Attendance (30 Days)", chartHolder1));
            row.add(wrapChart("Upcoming Exams by Subject", chartHolder2));
        }

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

    // ================================================================
    //  STATISTICS LOADING
    // ================================================================

    /**
     * Load the numeric statistics into the card labels (role-dependent).
     */
    private void loadStatistics() {
        if ("Admin".equals(role)) {
            loadAdminStatistics();
        } else {
            loadTeacherStatistics();
        }
    }

    private void loadAdminStatistics() {
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
                    "Admin dashboard statistics loading failed", e);
            lblTotalStudents.setText("--");
            lblTotalTeachers.setText("--");
            lblAttendanceRate.setText("--%");
            lblFeeCollection.setText("Rs.0");
        }
    }

    private void loadTeacherStatistics() {
        try {
            // ── Total Active Students
            ResultSet studentRs = MySQL.executeSearch(
                    "SELECT COUNT(*) FROM student WHERE is_active = 1");
            if (studentRs.next()) {
                lblTotalStudents.setText(String.valueOf(studentRs.getInt(1)));
            }

            // ── Student Attendance Rate (last 30 days)
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

            // ── Upcoming Exams (from today onwards)
            ResultSet examRs = MySQL.executeSearch(
                    "SELECT COUNT(*) FROM exam WHERE exam_date >= CURDATE()");
            if (examRs.next()) {
                lblUpcomingExams.setText(String.valueOf(examRs.getInt(1)));
            }

            // ── Active Events
            ResultSet eventRs = MySQL.executeSearch(
                    "SELECT COUNT(*) FROM event e "
                    + "JOIN event_status es ON e.event_status_id = es.event_status_id "
                    + "WHERE es.status_name = 'Active'");
            if (eventRs.next()) {
                lblActiveEvents.setText(String.valueOf(eventRs.getInt(1)));
            }

        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING,
                    "Teacher dashboard statistics loading failed", e);
            lblTotalStudents.setText("--");
            lblAttendanceRate.setText("--%");
            lblUpcomingExams.setText("--");
            lblActiveEvents.setText("--");
        }
    }

    // ================================================================
    //  CHART LOADING
    // ================================================================

    /**
     * Load pie-chart data into the two chart holders (role-dependent).
     */
    private void loadCharts() {
        if ("Admin".equals(role)) {
            loadTeacherTypesChart();
            loadFeeStatusChart();
        } else {
            loadStudentAttendanceChart();
            loadUpcomingExamsBySubjectChart();
        }
    }

    // ── Admin: Teacher Types Pie Chart ──
    private void loadTeacherTypesChart() {
        PieChart chart = new PieChart();
        chart.setChartType(PieChart.PeiChartType.DONUT_CHART);

        JPanel legendPanel = new JPanel();
        legendPanel.setLayout(new BoxLayout(legendPanel, BoxLayout.Y_AXIS));
        legendPanel.setOpaque(false);
        legendPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));

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
                    Color c = palette[idx % palette.length];
                    chart.addData(new ModelPieChart(name, count, c));
                    legendPanel.add(createLegendItem(name, count, c));
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
            String[] names = {"Teacher", "Principal", "Office Staff", "Support Staff"};
            int[] counts = {8, 1, 3, 2};
            for(int i=0; i<4; i++) {
                chart.addData(new ModelPieChart(names[i], counts[i], palette[i]));
                legendPanel.add(createLegendItem(names[i], counts[i], palette[i]));
            }
        }

        // Use a JScrollPane for the legend in case there are many types
        JScrollPane scrollLegend = new JScrollPane(legendPanel);
        scrollLegend.setBorder(null);
        scrollLegend.setOpaque(false);
        scrollLegend.getViewport().setOpaque(false);
        scrollLegend.setPreferredSize(new Dimension(140, 0));

        chartHolder1.add(chart, BorderLayout.CENTER);
        chartHolder1.add(scrollLegend, BorderLayout.EAST);
        chartHolder1.revalidate();
        chartHolder1.repaint();
    }

    // ── Admin: Fee Collection Status Pie Chart ──
    private void loadFeeStatusChart() {
        PieChart chart = new PieChart();
        chart.setChartType(PieChart.PeiChartType.DONUT_CHART);

        JPanel legendPanel = new JPanel();
        legendPanel.setLayout(new BoxLayout(legendPanel, BoxLayout.Y_AXIS));
        legendPanel.setOpaque(false);
        legendPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 10));

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
                    legendPanel.add(createLegendItem(status, count, color));
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
            legendPanel.add(createLegendItem("Paid", 52, new Color(76, 175, 80)));
            chart.addData(new ModelPieChart("Pending", 24, new Color(33, 150, 243)));
            legendPanel.add(createLegendItem("Pending", 24, new Color(33, 150, 243)));
            chart.addData(new ModelPieChart("Overdue", 24, new Color(244, 67, 54)));
            legendPanel.add(createLegendItem("Overdue", 24, new Color(244, 67, 54)));
        }

        chartHolder2.add(chart, BorderLayout.CENTER);
        chartHolder2.add(legendPanel, BorderLayout.EAST);
        chartHolder2.revalidate();
        chartHolder2.repaint();
    }

    // ── Teacher: Student Attendance Breakdown Pie Chart ──
    private void loadStudentAttendanceChart() {
        PieChart chart = new PieChart();
        chart.setChartType(PieChart.PeiChartType.DONUT_CHART);

        JPanel legendPanel = new JPanel();
        legendPanel.setLayout(new BoxLayout(legendPanel, BoxLayout.Y_AXIS));
        legendPanel.setOpaque(false);
        legendPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));

        boolean hasData = false;
        try {
            ResultSet rs = MySQL.executeSearch(
                    "SELECT status, COUNT(*) AS count FROM student_attendance "
                    + "WHERE date_time >= DATE_SUB(CURDATE(), INTERVAL 30 DAY) "
                    + "GROUP BY status");

            while (rs.next()) {
                String status = rs.getString("status");
                int count = rs.getInt("count");
                if (count > 0) {
                    Color color;
                    switch (status) {
                        case "Present":
                            color = new Color(76, 175, 80);   // Green
                            break;
                        case "Absent":
                            color = new Color(244, 67, 54);   // Red
                            break;
                        case "Late":
                            color = new Color(255, 152, 0);   // Orange
                            break;
                        default:
                            color = new Color(158, 158, 158); // Gray
                            break;
                    }
                    chart.addData(new ModelPieChart(status, count, color));
                    legendPanel.add(createLegendItem(status, count, color));
                    hasData = true;
                }
            }
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING,
                    "Student attendance chart data load failed", e);
        }

        if (!hasData) {
            // Fallback sample data
            chart.addData(new ModelPieChart("Present", 85, new Color(76, 175, 80)));
            legendPanel.add(createLegendItem("Present", 85, new Color(76, 175, 80)));
            chart.addData(new ModelPieChart("Absent", 10, new Color(244, 67, 54)));
            legendPanel.add(createLegendItem("Absent", 10, new Color(244, 67, 54)));
            chart.addData(new ModelPieChart("Late", 5, new Color(255, 152, 0)));
            legendPanel.add(createLegendItem("Late", 5, new Color(255, 152, 0)));
        }

        chartHolder1.add(chart, BorderLayout.CENTER);
        chartHolder1.add(legendPanel, BorderLayout.EAST);
        chartHolder1.revalidate();
        chartHolder1.repaint();
    }

    // ── Teacher: Upcoming Exams by Subject Pie Chart ──
    private void loadUpcomingExamsBySubjectChart() {
        PieChart chart = new PieChart();
        chart.setChartType(PieChart.PeiChartType.DONUT_CHART);

        JPanel legendPanel = new JPanel();
        legendPanel.setLayout(new BoxLayout(legendPanel, BoxLayout.Y_AXIS));
        legendPanel.setOpaque(false);
        legendPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));

        Color[] palette = {
            new Color(23, 126, 238),  // Blue
            new Color(76, 175, 80),   // Green
            new Color(255, 152, 0),   // Orange
            new Color(156, 39, 176),  // Purple
            new Color(244, 67, 54),   // Red
            new Color(0, 150, 136),   // Teal
            new Color(233, 30, 99),   // Pink
            new Color(121, 85, 72),   // Brown
            new Color(33, 150, 243),  // Light Blue
            new Color(196, 151, 58),  // Gold
            new Color(96, 125, 139),  // Blue Gray
            new Color(139, 195, 74)   // Light Green
        };

        boolean hasData = false;
        try {
            ResultSet rs = MySQL.executeSearch(
                    "SELECT s.subject_name, COUNT(e.exam_id) AS count "
                    + "FROM exam e "
                    + "JOIN subject s ON e.subject_id = s.subject_id "
                    + "WHERE e.exam_date >= CURDATE() "
                    + "GROUP BY s.subject_id, s.subject_name "
                    + "ORDER BY count DESC");

            int idx = 0;
            while (rs.next()) {
                String name = rs.getString("subject_name");
                int count = rs.getInt("count");
                if (count > 0) {
                    Color c = palette[idx % palette.length];
                    chart.addData(new ModelPieChart(name, count, c));
                    legendPanel.add(createLegendItem(name, count, c));
                    hasData = true;
                }
                idx++;
            }
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING,
                    "Upcoming exams chart data load failed", e);
        }

        if (!hasData) {
            // Fallback sample data
            String[] names = {"Mathematics", "Science", "English", "History"};
            int[] counts = {3, 2, 2, 1};
            for (int i = 0; i < 4; i++) {
                chart.addData(new ModelPieChart(names[i], counts[i], palette[i]));
                legendPanel.add(createLegendItem(names[i], counts[i], palette[i]));
            }
        }

        // Use a JScrollPane for the legend in case there are many subjects
        JScrollPane scrollLegend = new JScrollPane(legendPanel);
        scrollLegend.setBorder(null);
        scrollLegend.setOpaque(false);
        scrollLegend.getViewport().setOpaque(false);
        scrollLegend.setPreferredSize(new Dimension(140, 0));

        chartHolder2.add(chart, BorderLayout.CENTER);
        chartHolder2.add(scrollLegend, BorderLayout.EAST);
        chartHolder2.revalidate();
        chartHolder2.repaint();
    }

    // ================================================================
    //  SHARED HELPERS
    // ================================================================

    /**
     * Create a single row for the chart legend containing a color box and label.
     */
    private JPanel createLegendItem(String label, int value, Color color) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 2));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel colorBox = new JPanel();
        colorBox.setBackground(color);
        colorBox.setPreferredSize(new Dimension(14, 14));

        JLabel lbl = new JLabel(label + " (" + value + ")");
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(new Color(80, 80, 80));

        row.add(colorBox);
        row.add(lbl);
        return row;
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
