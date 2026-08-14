package lk.educore.schoolms.gui;

import lk.educore.schoolms.component.RoundPanel;
import lk.educore.schoolms.model.MySQL;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

/**
 * Dashboard Panel
 * Home panel displaying statistics and charts for the school management system
 */
public class DashboardPanel extends JPanel {

    private JLabel lblTotalStudents;
    private JLabel lblTotalTeachers;
    private JLabel lblAttendanceRate;
    private JLabel lblFeeCollection;

    public DashboardPanel() {
        initComponents();
        loadStatistics();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel titleLabel = new JLabel("Dashboard Overview");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // Stats panel
        JPanel statsPanel = createStatsPanel();
        add(statsPanel, BorderLayout.CENTER);

        // Charts panel
        JPanel chartsPanel = createChartsPanel();
        add(chartsPanel, BorderLayout.SOUTH);
    }

    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Total Students Card
        RoundPanel studentsCard = RoundPanel.createBlueGradientPanel();
        studentsCard.setLayout(new BorderLayout());
        studentsCard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel studentsLabel = new JLabel("Total Students");
        studentsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        studentsLabel.setForeground(Color.WHITE);
        
        lblTotalStudents = new JLabel("0");
        lblTotalStudents.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTotalStudents.setForeground(Color.WHITE);
        lblTotalStudents.setHorizontalAlignment(SwingConstants.CENTER);
        
        studentsCard.add(studentsLabel, BorderLayout.NORTH);
        studentsCard.add(lblTotalStudents, BorderLayout.CENTER);
        panel.add(studentsCard);

        // Total Teachers Card
        RoundPanel teachersCard = RoundPanel.createGreenGradientPanel();
        teachersCard.setLayout(new BorderLayout());
        teachersCard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel teachersLabel = new JLabel("Total Teachers");
        teachersLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        teachersLabel.setForeground(Color.WHITE);
        
        lblTotalTeachers = new JLabel("0");
        lblTotalTeachers.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTotalTeachers.setForeground(Color.WHITE);
        lblTotalTeachers.setHorizontalAlignment(SwingConstants.CENTER);
        
        teachersCard.add(teachersLabel, BorderLayout.NORTH);
        teachersCard.add(lblTotalTeachers, BorderLayout.CENTER);
        panel.add(teachersCard);

        // Attendance Rate Card
        RoundPanel attendanceCard = RoundPanel.createYellowGradientPanel();
        attendanceCard.setLayout(new BorderLayout());
        attendanceCard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel attendanceLabel = new JLabel("Attendance Rate");
        attendanceLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        attendanceLabel.setForeground(Color.WHITE);
        
        lblAttendanceRate = new JLabel("0%");
        lblAttendanceRate.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblAttendanceRate.setForeground(Color.WHITE);
        lblAttendanceRate.setHorizontalAlignment(SwingConstants.CENTER);
        
        attendanceCard.add(attendanceLabel, BorderLayout.NORTH);
        attendanceCard.add(lblAttendanceRate, BorderLayout.CENTER);
        panel.add(attendanceCard);

        // Fee Collection Card
        RoundPanel feeCard = RoundPanel.createPurpleGradientPanel();
        feeCard.setLayout(new BorderLayout());
        feeCard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel feeLabel = new JLabel("Fee Collection");
        feeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        feeLabel.setForeground(Color.WHITE);
        
        lblFeeCollection = new JLabel("Rs. 0");
        lblFeeCollection.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblFeeCollection.setForeground(Color.WHITE);
        lblFeeCollection.setHorizontalAlignment(SwingConstants.CENTER);
        
        feeCard.add(feeLabel, BorderLayout.NORTH);
        feeCard.add(lblFeeCollection, BorderLayout.CENTER);
        panel.add(feeCard);

        return panel;
    }

    private JPanel createChartsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        // Pie Chart Panel
        RoundPanel pieChartPanel = RoundPanel.createWhitePanel();
        pieChartPanel.setLayout(new BorderLayout());
        pieChartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel pieChartLabel = new JLabel("Fee Collection Status");
        pieChartLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pieChartPanel.add(pieChartLabel, BorderLayout.NORTH);
        
        // Add pie chart (placeholder for now)
        JPanel pieChartPlaceholder = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pieChartPlaceholder.add(new JLabel("Pie Chart - Coming Soon"));
        pieChartPanel.add(pieChartPlaceholder, BorderLayout.CENTER);
        
        panel.add(pieChartPanel);

        // Line Chart Panel
        RoundPanel lineChartPanel = RoundPanel.createWhitePanel();
        lineChartPanel.setLayout(new BorderLayout());
        lineChartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel lineChartLabel = new JLabel("Attendance Trends");
        lineChartLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lineChartPanel.add(lineChartLabel, BorderLayout.NORTH);
        
        // Add line chart (placeholder for now)
        JPanel lineChartPlaceholder = new JPanel(new FlowLayout(FlowLayout.CENTER));
        lineChartPlaceholder.add(new JLabel("Line Chart - Coming Soon"));
        lineChartPanel.add(lineChartPlaceholder, BorderLayout.CENTER);
        
        panel.add(lineChartPanel);

        return panel;
    }

    private void loadStatistics() {
        try {
            Connection conn = MySQL.getConnection();

            // Total Students
            String studentQuery = "SELECT COUNT(*) FROM student WHERE is_active = 1";
            PreparedStatement studentStmt = conn.prepareStatement(studentQuery);
            ResultSet studentRs = studentStmt.executeQuery();
            if (studentRs.next()) {
                lblTotalStudents.setText(String.valueOf(studentRs.getInt(1)));
            }
            studentRs.close();
            studentStmt.close();

            // Total Teachers
            String teacherQuery = "SELECT COUNT(*) FROM teacher WHERE status_id = 1";
            PreparedStatement teacherStmt = conn.prepareStatement(teacherQuery);
            ResultSet teacherRs = teacherStmt.executeQuery();
            if (teacherRs.next()) {
                lblTotalTeachers.setText(String.valueOf(teacherRs.getInt(1)));
            }
            teacherRs.close();
            teacherStmt.close();

            // Attendance Rate (last 30 days)
            String attendanceQuery = "SELECT " +
                "ROUND((SELECT COUNT(*) FROM student_attendance " +
                "WHERE status = 'Present' AND date_time >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)) * 100.0 / " +
                "(SELECT COUNT(*) FROM student_attendance " +
                "WHERE date_time >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)), 1)";
            PreparedStatement attendanceStmt = conn.prepareStatement(attendanceQuery);
            ResultSet attendanceRs = attendanceStmt.executeQuery();
            if (attendanceRs.next()) {
                lblAttendanceRate.setText(attendanceRs.getString(1) + "%");
            }
            attendanceRs.close();
            attendanceStmt.close();

            // Fee Collection (current month)
            String feeQuery = "SELECT COALESCE(SUM(amount), 0) FROM fee " +
                "WHERE status = 'Paid' AND paid_date >= DATE_FORMAT(CURDATE(), '%Y-%m-01')";
            PreparedStatement feeStmt = conn.prepareStatement(feeQuery);
            ResultSet feeRs = feeStmt.executeQuery();
            if (feeRs.next()) {
                lblFeeCollection.setText("Rs. " + String.format("%,.2f", feeRs.getDouble(1)));
            }
            feeRs.close();
            feeStmt.close();

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
            // Set default values on error
            lblTotalStudents.setText("0");
            lblTotalTeachers.setText("0");
            lblAttendanceRate.setText("0%");
            lblFeeCollection.setText("Rs. 0");
        }
    }
}