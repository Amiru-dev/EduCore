package lk.educore.schoolms.gui;

import lk.educore.schoolms.model.MySQL;
import lk.educore.schoolms.model.UserSession;
import lk.educore.schoolms.validate.JOptionIcon;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;

/**
 * My Attendance Panel (Teacher Self-Service)
 * Displays the logged-in teacher's own attendance records and summary statistics.
 * Read-only — teachers cannot modify their attendance from here.
 */
public class MyAttendancePanel extends JPanel {

    private DefaultTableModel tableModel;
    private JLabel lblTotalDays, lblPresentDays, lblAttendancePct;

    public MyAttendancePanel() {
        initComponents();
        loadAttendanceData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(Color.WHITE);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("My Attendance");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(44, 62, 80));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> loadAttendanceData());
        applySecondaryButtonEffect(btnRefresh);
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 10));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        btnPanel.add(btnRefresh);
        headerPanel.add(btnPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Summary cards
        JPanel summaryPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        summaryPanel.setBackground(Color.WHITE);
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));

        lblTotalDays = new JLabel("0", SwingConstants.CENTER);
        summaryPanel.add(createSummaryCard("Total Working Days", lblTotalDays, new Color(37, 99, 235)));

        lblPresentDays = new JLabel("0", SwingConstants.CENTER);
        summaryPanel.add(createSummaryCard("Days Present", lblPresentDays, new Color(22, 163, 74)));

        lblAttendancePct = new JLabel("0%", SwingConstants.CENTER);
        summaryPanel.add(createSummaryCard("Attendance Rate", lblAttendancePct, new Color(234, 88, 12)));

        // Combine summary + table
        JPanel centerPanel = new JPanel(new BorderLayout(0, 10));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(summaryPanel, BorderLayout.NORTH);

        // Attendance table
        tableModel = new DefaultTableModel(
            new Object[]{"Date", "Check In", "Check Out", "Duration (min)"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable attendanceTable = new JTable(tableModel);
        attendanceTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        attendanceTable.setRowHeight(30);
        attendanceTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        attendanceTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(attendanceTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true));
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
    }

    private JPanel createSummaryCard(String title, JLabel valueLabel, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(accentColor.brighter(), 1, true),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTitle.setForeground(new Color(100, 116, 139));
        card.add(lblTitle, BorderLayout.NORTH);

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        valueLabel.setForeground(accentColor);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private void loadAttendanceData() {
        UserSession session = UserSession.get();
        if (session == null || session.getTeacherNIC() == null) {
            return;
        }

        try {
            tableModel.setRowCount(0);

            ResultSet rs = MySQL.executeSearch(
                "SELECT attendance_date, check_in, check_out, duration "
                + "FROM teacher_attendance "
                + "WHERE teacher_NIC = '" + session.getTeacherNIC() + "' "
                + "ORDER BY attendance_date DESC "
                + "LIMIT 200");

            int totalDays = 0;
            while (rs.next()) {
                totalDays++;
                String date = rs.getString("attendance_date");
                String checkIn = rs.getString("check_in");
                String checkOut = rs.getString("check_out");
                String duration = rs.getString("duration");

                tableModel.addRow(new Object[]{
                    date,
                    checkIn != null ? checkIn : "—",
                    checkOut != null ? checkOut : "—",
                    duration != null ? duration : "—"
                });
            }
            rs.close();

            // Summary stats (current academic year)
            ResultSet statsRs = MySQL.executeSearch(
                "SELECT COUNT(*) as present_days FROM teacher_attendance "
                + "WHERE teacher_NIC = '" + session.getTeacherNIC() + "' "
                + "AND YEAR(attendance_date) = YEAR(CURDATE())");

            int presentDays = 0;
            if (statsRs.next()) {
                presentDays = statsRs.getInt("present_days");
            }
            statsRs.close();

            // Estimate total working days (weekdays from Jan 1 to today this year)
            ResultSet wdRs = MySQL.executeSearch(
                "SELECT (DATEDIFF(CURDATE(), MAKEDATE(YEAR(CURDATE()),1)) - "
                + "(FLOOR(DATEDIFF(CURDATE(), MAKEDATE(YEAR(CURDATE()),1)) / 7) * 2)) as working_days");
            int totalWorkingDays = 0;
            if (wdRs.next()) {
                totalWorkingDays = wdRs.getInt("working_days");
                if (totalWorkingDays <= 0) totalWorkingDays = 1;
            }
            wdRs.close();

            lblTotalDays.setText(String.valueOf(totalWorkingDays));
            lblPresentDays.setText(String.valueOf(presentDays));
            double pct = totalWorkingDays > 0 ? (presentDays * 100.0 / totalWorkingDays) : 0;
            lblAttendancePct.setText(String.format("%.1f%%", pct));

        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error loading attendance: " + e.getMessage());
        }
    }

    private void applySecondaryButtonEffect(JButton button) {
        button.setBackground(Color.WHITE);
        button.setForeground(new Color(71, 85, 105));
        button.setFocusPainted(false);
        button.setBorderPainted(true);
        button.setBorder(BorderFactory.createLineBorder(new Color(203, 213, 225), 1));
        button.setOpaque(true);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(203, 213, 225), 1),
            BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(new Color(248, 250, 252));
                    button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(148, 163, 184), 1),
                        BorderFactory.createEmptyBorder(8, 16, 8, 16)
                    ));
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(Color.WHITE);
                    button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(203, 213, 225), 1),
                        BorderFactory.createEmptyBorder(8, 16, 8, 16)
                    ));
                }
            }
        });
    }
}
