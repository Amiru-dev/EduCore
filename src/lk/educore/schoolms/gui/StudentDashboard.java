package lk.educore.schoolms.gui;

import lk.educore.schoolms.model.MySQL;
import lk.educore.schoolms.validate.JOptionIcon;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;

/**
 * Student Dashboard
 * Student-specific dashboard showing personal information, attendance, fees, and results
 */
public class StudentDashboard extends JFrame {

    private String username;
    private String studentId;
    private JLabel lblStudentId, lblName, lblGrade, lblEmail, lblPhone;
    private JLabel lblAttendanceRate, lblTotalFees, lblPaidFees, lblPendingFees;
    private JTable resultsTable;
    private DefaultTableModel tableModel;

    public StudentDashboard(String username) {
        this.username = username;
        initComponents();
        loadStudentInfo();
    }

    private void initComponents() {
        setTitle("Student Dashboard - EduCore School Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        // Apply FlatLaf theme
        try {
            UIManager.setLookAndFeel("com.formdev.flatlaf.FlatLightLaf");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Main panel with tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Profile Tab
        JPanel profilePanel = createProfilePanel();
        tabbedPane.addTab("My Profile", profilePanel);
        
        // Attendance Tab
        JPanel attendancePanel = createAttendancePanel();
        tabbedPane.addTab("Attendance", attendancePanel);
        
        // Fees Tab
        JPanel feesPanel = createFeesPanel();
        tabbedPane.addTab("Fees", feesPanel);
        
        // Results Tab
        JPanel resultsPanel = createResultsPanel();
        tabbedPane.addTab("Results", resultsPanel);

        add(tabbedPane);

        // Logout button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton btnLogout = new JButton("Logout");
        btnLogout.addActionListener(e -> handleLogout());
        
        buttonPanel.add(btnLogout);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("My Profile");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titlePanel.add(titlePanel);
        panel.add(titlePanel, BorderLayout.NORTH);

        // Info panel
        JPanel infoPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Student ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        infoPanel.add(new JLabel("Student ID:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        lblStudentId = new JLabel();
        lblStudentId.setFont(new Font("Segoe UI", Font.BOLD, 12));
        infoPanel.add(lblStudentId, gbc);

        // Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        infoPanel.add(new JLabel("Full Name:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        lblName = new JLabel();
        infoPanel.add(lblName, gbc);

        // Grade
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        infoPanel.add(new JLabel("Grade:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        lblGrade = new JLabel();
        infoPanel.add(lblGrade, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        infoPanel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        lblEmail = new JLabel();
        infoPanel.add(lblEmail, gbc);

        // Phone
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.0;
        infoPanel.add(new JLabel("Phone:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        lblPhone = new JLabel();
        infoPanel.add(lblPhone, gbc);

        panel.add(infoPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createAttendancePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Attendance Overview");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titlePanel.add(titlePanel);
        panel.add(titlePanel, BorderLayout.NORTH);

        // Stats panel
        JPanel statsPanel = new JPanel(new GridLayout(1, 1, 10, 10));
        
        JPanel attendanceCard = new JPanel(new BorderLayout(10, 10));
        attendanceCard.setBorder(BorderFactory.createTitledBorder("Attendance Rate"));
        attendanceCard.setBackground(new Color(240, 240, 240));
        
        lblAttendanceRate = new JLabel("0%", SwingConstants.CENTER);
        lblAttendanceRate.setFont(new Font("Segoe UI", Font.BOLD, 36));
        attendanceCard.add(lblAttendanceRate, BorderLayout.CENTER);
        
        statsPanel.add(attendanceCard);
        panel.add(statsPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createFeesPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Fee Summary");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titlePanel.add(titlePanel);
        panel.add(titlePanel, BorderLayout.NORTH);

        // Stats panel
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        
        // Total Fees
        JPanel totalPanel = new JPanel(new BorderLayout(10, 10));
        totalPanel.setBorder(BorderFactory.createTitledBorder("Total Fees"));
        totalPanel.setBackground(new Color(240, 240, 240));
        lblTotalFees = new JLabel("Rs. 0", SwingConstants.CENTER);
        lblTotalFees.setFont(new Font("Segoe UI", Font.BOLD, 24));
        totalPanel.add(lblTotalFees, BorderLayout.CENTER);
        
        // Paid Fees
        JPanel paidPanel = new JPanel(new BorderLayout(10, 10));
        paidPanel.setBorder(BorderFactory.createTitledBorder("Paid"));
        paidPanel.setBackground(new Color(220, 255, 220));
        lblPaidFees = new JLabel("Rs. 0", SwingConstants.CENTER);
        lblPaidFees.setFont(new Font("Segoe UI", Font.BOLD, 24));
        paidPanel.add(lblPaidFees, BorderLayout.CENTER);
        
        // Pending Fees
        JPanel pendingPanel = new JPanel(new BorderLayout(10, 10));
        pendingPanel.setBorder(BorderFactory.createTitledBorder("Pending"));
        pendingPanel.setBackground(new Color(255, 220, 220));
        lblPendingFees = new JLabel("Rs. 0", SwingConstants.CENTER);
        lblPendingFees.setFont(new Font("Segoe UI", Font.BOLD, 24));
        pendingPanel.add(lblPendingFees, BorderLayout.CENTER);
        
        statsPanel.add(totalPanel);
        statsPanel.add(paidPanel);
        statsPanel.add(pendingPanel);
        
        panel.add(statsPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createResultsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("My Results");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titlePanel.add(titlePanel);
        panel.add(titlePanel, BorderLayout.NORTH);

        // Table
        tableModel = new DefaultTableModel(
            new Object[]{"Exam", "Subject", "Marks", "Grade", "Date"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        resultsTable = new JTable(tableModel);
        resultsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resultsTable.setRowHeight(25);
        resultsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        JScrollPane scrollPane = new JScrollPane(resultsTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void loadStudentInfo() {
        try {
            Connection conn = MySQL.getConnection();
            
            // Get student info from username
            String studentQuery = "SELECT s.student_id, s.first_name, s.last_name, g.grade_name, s.email, s.phone " +
                                 "FROM student s " +
                                 "JOIN grade g ON s.grade_id = g.grade_id " +
                                 "JOIN user u ON s.user_id = u.user_id " +
                                 "WHERE u.username = ?";
            
            PreparedStatement studentStmt = conn.prepareStatement(studentQuery);
            studentStmt.setString(1, username);
            ResultSet studentRs = studentStmt.executeQuery();
            
            if (studentRs.next()) {
                studentId = studentRs.getString("student_id");
                lblStudentId.setText(studentId);
                lblName.setText(studentRs.getString("first_name") + " " + studentRs.getString("last_name"));
                lblGrade.setText(studentRs.getString("grade_name"));
                lblEmail.setText(studentRs.getString("email"));
                lblPhone.setText(studentRs.getString("phone"));
            }
            
            studentRs.close();
            studentStmt.close();
            
            // Load attendance rate
            loadAttendance(conn);
            
            // Load fee summary
            loadFeeSummary(conn);
            
            // Load results
            loadResults(conn);
            
            conn.close();
            
        } catch (SQLException e) {
            JOptionIcon.showErrorDialog(this, "Error loading student information: " + e.getMessage());
        }
    }

    private void loadAttendance(Connection conn) throws SQLException {
        String query = "SELECT " +
                       "ROUND((SELECT COUNT(*) FROM student_attendance " +
                       "WHERE student_id = ? AND status = 'Present' AND date_time >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)) * 100.0 / " +
                       "(SELECT COUNT(*) FROM student_attendance " +
                       "WHERE student_id = ? AND date_time >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)), 1)";
        
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, studentId);
        stmt.setString(2, studentId);
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            lblAttendanceRate.setText(rs.getString(1) + "%");
        }
        
        rs.close();
        stmt.close();
    }

    private void loadFeeSummary(Connection conn) throws SQLException {
        // Total fees
        String totalQuery = "SELECT COALESCE(SUM(amount), 0) FROM fee WHERE student_id = ?";
        PreparedStatement totalStmt = conn.prepareStatement(totalQuery);
        totalStmt.setString(1, studentId);
        ResultSet totalRs = totalStmt.executeQuery();
        
        if (totalRs.next()) {
            lblTotalFees.setText("Rs. " + String.format("%,.2f", totalRs.getDouble(1)));
        }
        
        totalRs.close();
        totalStmt.close();
        
        // Paid fees
        String paidQuery = "SELECT COALESCE(SUM(amount), 0) FROM fee WHERE student_id = ? AND status = 'Paid'";
        PreparedStatement paidStmt = conn.prepareStatement(paidQuery);
        paidStmt.setString(1, studentId);
        ResultSet paidRs = paidStmt.executeQuery();
        
        if (paidRs.next()) {
            lblPaidFees.setText("Rs. " + String.format("%,.2f", paidRs.getDouble(1)));
        }
        
        paidRs.close();
        paidStmt.close();
        
        // Pending fees
        String pendingQuery = "SELECT COALESCE(SUM(amount), 0) FROM fee WHERE student_id = ? AND status != 'Paid'";
        PreparedStatement pendingStmt = conn.prepareStatement(pendingQuery);
        pendingStmt.setString(1, studentId);
        ResultSet pendingRs = pendingStmt.executeQuery();
        
        if (pendingRs.next()) {
            lblPendingFees.setText("Rs. " + String.format("%,.2f", pendingRs.getDouble(1)));
        }
        
        pendingRs.close();
        pendingStmt.close();
    }

    private void loadResults(Connection conn) throws SQLException {
        tableModel.setRowCount(0);
        
        String query = "SELECT e.exam_name, sub.subject_name, r.marks_obtained, r.grade_letter, e.exam_date " +
                       "FROM result r " +
                       "JOIN exam e ON r.exam_id = e.exam_id " +
                       "JOIN subject sub ON e.subject_id = sub.subject_id " +
                       "WHERE r.student_id = ? " +
                       "ORDER BY e.exam_date DESC";
        
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, studentId);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            String exam = rs.getString("exam_name");
            String subject = rs.getString("subject_name");
            double marks = rs.getDouble("marks_obtained");
            String grade = rs.getString("grade_letter");
            String date = rs.getString("exam_date");
            
            tableModel.addRow(new Object[]{exam, subject, marks, grade, date});
        }
        
        rs.close();
        stmt.close();
    }

    private void handleLogout() {
        dispose();
        new SignIn().setVisible(true);
    }
}