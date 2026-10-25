package lk.educore.schoolms.gui;

import lk.educore.schoolms.model.MySQL;
import lk.educore.schoolms.model.Student;
import lk.educore.schoolms.model.UserSession;
import lk.educore.schoolms.util.ReportGenerator;
import lk.educore.schoolms.validate.JOptionIcon;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Student Management Panel Main CRUD interface for student management with tabs
 * for info, attendance, and charts
 */
public class StudentManagement extends JPanel {

    private JTable studentTable;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JComboBox<String> cmbSearchBy;
    private JButton btnAdd, btnEdit, btnDelete, btnView, btnRefresh, btnAttendance, btnQRCode, btnReport;
    private JTabbedPane tabbedPane;

    public StudentManagement() {
        initComponents();
        loadStudentData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Header panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Tabbed pane
        tabbedPane = new JTabbedPane();

        // Student Info Tab
        JPanel infoPanel = createInfoPanel();
        tabbedPane.addTab("Student Info", infoPanel);

        // Attendance Tab
        JPanel attendancePanel = createAttendancePanel();
        tabbedPane.addTab("Attendance", attendancePanel);

        // Charts Tab
        JPanel chartsPanel = createChartsPanel();
        tabbedPane.addTab("Analytics", chartsPanel);

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Title - Change to "My Classes" for teachers
        String titleText = "Student Management";
        if (UserSession.get() != null && UserSession.get().isTeacher()) {
            titleText = "My Classes";
        }
        JLabel titleLabel = new JLabel(titleText);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        panel.add(titleLabel, BorderLayout.WEST);

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        cmbSearchBy = new JComboBox<>(new String[]{"Student ID", "Name", "NIC", "Grade"});
        txtSearch = new JTextField(20);
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                searchStudents();
            }
        });

        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(e -> searchStudents());

        searchPanel.add(new JLabel("Search by:"));
        searchPanel.add(cmbSearchBy);
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);

        panel.add(searchPanel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createInfoPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        btnAdd = new JButton("Add Student");
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnAdd.addActionListener(e -> addStudent());
        applyPrimaryButtonEffect(btnAdd);

        btnEdit = new JButton("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(e -> editStudent());
        applySecondaryButtonEffect(btnEdit);

        btnDelete = new JButton("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(e -> deleteStudent());
        applyDestructiveButtonEffect(btnDelete);

        btnView = new JButton("View Details");
        btnView.setEnabled(false);
        btnView.addActionListener(e -> viewStudentDetails());
        applySecondaryButtonEffect(btnView);

        btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> loadStudentData());
        applySecondaryButtonEffect(btnRefresh);

        btnAttendance = new JButton("Mark Attendance");
        btnAttendance.addActionListener(e -> markAttendance());
        applySecondaryButtonEffect(btnAttendance);

        btnQRCode = new JButton("Generate QR");
        btnQRCode.setEnabled(false);
        btnQRCode.addActionListener(e -> generateQRCode());
        applySecondaryButtonEffect(btnQRCode);

        btnReport = new JButton("Generate Report");
        btnReport.addActionListener(e -> generateStudentReport());
        applySecondaryButtonEffect(btnReport);

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnView);
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnAttendance);
        buttonPanel.add(btnQRCode);
        buttonPanel.add(btnReport);

        if (UserSession.get() != null && UserSession.get().isTeacher()) {
            btnAdd.setVisible(false);
            btnEdit.setVisible(false);
            btnDelete.setVisible(false);
            btnQRCode.setVisible(false);
        }

        panel.add(buttonPanel, BorderLayout.NORTH);

        // Table
        tableModel = new DefaultTableModel(
                new Object[]{"Student ID", "Name", "Grade", "NIC", "Phone", "Email", "Status"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        studentTable = new JTable(tableModel);
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentTable.setRowHeight(30);
        studentTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        studentTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        // Add selection listener to enable/disable buttons
        studentTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                boolean hasSelection = studentTable.getSelectedRow() != -1;
                btnEdit.setEnabled(hasSelection);
                btnDelete.setEnabled(hasSelection);
                btnView.setEnabled(hasSelection);
                btnQRCode.setEnabled(hasSelection);
            }
        });

        // Add mouse listener for double-click
        studentTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    viewStudentDetails();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(studentTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createAttendancePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        
        // Top Panel for Refresh button
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnRefresh = new JButton("Refresh Attendance");
        topPanel.add(btnRefresh);
        panel.add(topPanel, BorderLayout.NORTH);

        // Table
        DefaultTableModel attendanceModel = new DefaultTableModel(
                new Object[]{"ID", "Student ID", "Name", "Date", "Status", "Check In", "Check Out"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable attendanceTable = new JTable(attendanceModel);
        attendanceTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        attendanceTable.setRowHeight(25);
        attendanceTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        JScrollPane scrollPane = new JScrollPane(attendanceTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        ActionListener loadAction = e -> {
            try {
                attendanceModel.setRowCount(0);
                String attQuery = "SELECT a.attendance_id, a.student_id, "
                        + "CONCAT(s.first_name, ' ', s.last_name) as name, "
                        + "a.attendance_date, a.status, a.check_in_time, a.check_out_time "
                        + "FROM student_attendance a "
                        + "INNER JOIN student s ON a.student_id = s.student_id ";
                if (UserSession.get() != null && UserSession.get().isTeacher()) {
                    attQuery += "INNER JOIN teacher_class tc ON s.grade_id = tc.grade_id "
                             + "WHERE tc.teacher_NIC = '" + UserSession.get().getTeacherNIC() + "' ";
                }
                attQuery += "ORDER BY a.attendance_date DESC LIMIT 100";

                ResultSet rs = MySQL.executeSearch(attQuery);

                while (rs.next()) {
                    attendanceModel.addRow(new Object[]{
                        rs.getString("attendance_id"), 
                        rs.getString("student_id"), 
                        rs.getString("name"),
                        rs.getString("attendance_date"), 
                        rs.getString("status"), 
                        rs.getString("check_in_time"), 
                        rs.getString("check_out_time")
                    });
                }
                rs.close();
            } catch (Exception ex) {
                JOptionIcon.showErrorDialog(this, "Error loading attendance data: " + ex.getMessage());
            }
        };

        btnRefresh.addActionListener(loadAction);
        
        // Load data initially
        SwingUtilities.invokeLater(() -> loadAction.actionPerformed(null));
        
        return panel;
    }

    private JPanel createChartsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        
        // Cards
        JPanel cardTotal = createStatCard("Total Students", "Loading...");
        JPanel cardActive = createStatCard("Active Students", "Loading...");
        JPanel cardMale = createStatCard("Male Students", "Loading...");
        JPanel cardFemale = createStatCard("Female Students", "Loading...");
        
        statsPanel.add(cardTotal);
        statsPanel.add(cardActive);
        statsPanel.add(cardMale);
        statsPanel.add(cardFemale);
        
        panel.add(statsPanel, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnRefresh = new JButton("Refresh Analytics");
        bottomPanel.add(btnRefresh);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        
        ActionListener loadAction = e -> {
            try {
                String chartQuery = "SELECT COUNT(*) as total, SUM(IF(s.is_active=1, 1, 0)) as active, SUM(IF(s.gender='Male', 1, 0)) as male, SUM(IF(s.gender='Female', 1, 0)) as female FROM student s";
                if (UserSession.get() != null && UserSession.get().isTeacher()) {
                    chartQuery += " JOIN teacher_class tc ON s.grade_id = tc.grade_id WHERE tc.teacher_NIC = '" + UserSession.get().getTeacherNIC() + "'";
                }
                ResultSet rs = MySQL.executeSearch(chartQuery);
                if (rs.next()) {
                    ((JLabel) cardTotal.getComponent(1)).setText(rs.getString("total") == null ? "0" : rs.getString("total"));
                    ((JLabel) cardActive.getComponent(1)).setText(rs.getString("active") == null ? "0" : rs.getString("active"));
                    ((JLabel) cardMale.getComponent(1)).setText(rs.getString("male") == null ? "0" : rs.getString("male"));
                    ((JLabel) cardFemale.getComponent(1)).setText(rs.getString("female") == null ? "0" : rs.getString("female"));
                }
                rs.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        };
        
        btnRefresh.addActionListener(loadAction);
        SwingUtilities.invokeLater(() -> loadAction.actionPerformed(null));
        
        return panel;
    }

    private JPanel createStatCard(String title, String initialValue) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        card.setBackground(Color.WHITE);
        
        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(new Color(100, 116, 139));
        
        JLabel lblValue = new JLabel(initialValue, SwingConstants.CENTER);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 48));
        lblValue.setForeground(new Color(37, 99, 235));
        
        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);
        
        return card;
    }

    private void loadStudentData() {
        try {
            tableModel.setRowCount(0);

            String query = "SELECT s.student_id, s.first_name, s.last_name, g.grade_name, s.nic, s.phone, s.email, s.is_active "
                    + "FROM student s "
                    + "JOIN grade g ON s.grade_id = g.grade_id ";
            if (UserSession.get() != null && UserSession.get().isTeacher()) {
                query += "JOIN teacher_class tc ON s.grade_id = tc.grade_id "
                       + "WHERE tc.teacher_NIC = '" + UserSession.get().getTeacherNIC() + "' ";
            }
            query += "ORDER BY s.student_id";

            ResultSet rs = MySQL.executeSearch(query);

            while (rs.next()) {
                Student student = new Student();
                student.setStudentId(rs.getString("student_id"));
                student.setFirstName(rs.getString("first_name"));
                student.setLastName(rs.getString("last_name"));
                student.setGrade(rs.getString("grade_name"));
                student.setNic(rs.getString("nic"));
                student.setPhone(rs.getString("phone"));
                student.setEmail(rs.getString("email"));
                student.setStatus(rs.getBoolean("is_active") ? "Active" : "Inactive");

                tableModel.addRow(new Object[]{
                    student.getPersonId(),
                    student.getFullName(),
                    student.getGrade(),
                    student.getNic(),
                    student.getPhone(),
                    student.getEmail(),
                    student.getStatus()
                });
            }

            rs.close();

        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error loading student data: " + e.getMessage());
        }
    }

    private void searchStudents() {
        try {
            String searchTerm = txtSearch.getText().trim();
            String searchBy = (String) cmbSearchBy.getSelectedItem();

            if (searchTerm.isEmpty()) {
                loadStudentData();
                return;
            }

            tableModel.setRowCount(0);

            boolean isTeacher = UserSession.get() != null && UserSession.get().isTeacher();
            String teacherJoin = isTeacher ? " JOIN teacher_class tc ON s.grade_id = tc.grade_id " : "";
            String teacherFilter = isTeacher ? " AND tc.teacher_NIC = '" + UserSession.get().getTeacherNIC() + "' " : "";
            String teacherWhere = isTeacher ? " WHERE tc.teacher_NIC = '" + UserSession.get().getTeacherNIC() + "' " : "";

            String query;

            switch (searchBy) {
                case "Student ID":
                    query = "SELECT s.student_id, s.first_name, s.last_name, g.grade_name, s.nic, s.phone, s.email, s.is_active "
                            + "FROM student s "
                            + "JOIN grade g ON s.grade_id = g.grade_id "
                            + teacherJoin
                            + "WHERE s.student_id LIKE '%" + searchTerm + "%' "
                            + teacherFilter
                            + "ORDER BY s.student_id";
                    break;
                case "Name":
                    query = "SELECT s.student_id, s.first_name, s.last_name, g.grade_name, s.nic, s.phone, s.email, s.is_active "
                            + "FROM student s "
                            + "JOIN grade g ON s.grade_id = g.grade_id "
                            + teacherJoin
                            + "WHERE (s.first_name LIKE '%" + searchTerm + "%' OR s.last_name LIKE '%" + searchTerm + "%') "
                            + teacherFilter
                            + "ORDER BY s.student_id";
                    break;
                case "NIC":
                    query = "SELECT s.student_id, s.first_name, s.last_name, g.grade_name, s.nic, s.phone, s.email, s.is_active "
                            + "FROM student s "
                            + "JOIN grade g ON s.grade_id = g.grade_id "
                            + teacherJoin
                            + "WHERE s.nic LIKE '%" + searchTerm + "%' "
                            + teacherFilter
                            + "ORDER BY s.student_id";
                    break;
                case "Grade":
                    query = "SELECT s.student_id, s.first_name, s.last_name, g.grade_name, s.nic, s.phone, s.email, s.is_active "
                            + "FROM student s "
                            + "JOIN grade g ON s.grade_id = g.grade_id "
                            + teacherJoin
                            + "WHERE g.grade_name LIKE '%" + searchTerm + "%' "
                            + teacherFilter
                            + "ORDER BY s.student_id";
                    break;
                default:
                    query = "SELECT s.student_id, s.first_name, s.last_name, g.grade_name, s.nic, s.phone, s.email, s.is_active "
                            + "FROM student s "
                            + "JOIN grade g ON s.grade_id = g.grade_id "
                            + teacherJoin
                            + teacherWhere
                            + "ORDER BY s.student_id";
            }

            ResultSet rs = MySQL.executeSearch(query);

            while (rs.next()) {
                Student student = new Student();
                student.setStudentId(rs.getString("student_id"));
                student.setFirstName(rs.getString("first_name"));
                student.setLastName(rs.getString("last_name"));
                student.setGrade(rs.getString("grade_name"));
                student.setNic(rs.getString("nic"));
                student.setPhone(rs.getString("phone"));
                student.setEmail(rs.getString("email"));
                student.setStatus(rs.getBoolean("is_active") ? "Active" : "Inactive");

                tableModel.addRow(new Object[]{
                    student.getPersonId(),
                    student.getFullName(),
                    student.getGrade(),
                    student.getNic(),
                    student.getPhone(),
                    student.getEmail(),
                    student.getStatus()
                });
            }

            rs.close();

        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error searching students: " + e.getMessage());
        }
    }

    private void addStudent() {
        Window parent = SwingUtilities.getWindowAncestor(this);
        new StudentRegistration((Frame) parent).setVisible(true);
        loadStudentData();
    }

    private void editStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionIcon.showWarningDialog(this, "Please select a student to edit.");
            return;
        }

        String studentId = tableModel.getValueAt(selectedRow, 0).toString();
        Window parent = SwingUtilities.getWindowAncestor(this);
        new StudentEditDialog((Frame) parent, studentId).setVisible(true);
        loadStudentData();
    }

    private void deleteStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionIcon.showWarningDialog(this, "Please select a student to delete.");
            return;
        }

        String studentId = tableModel.getValueAt(selectedRow, 0).toString();
        String studentName = tableModel.getValueAt(selectedRow, 1).toString();

        int confirm = JOptionIcon.showConfirmDialog(this,
                "Are you sure you want to delete student " + studentName + " (" + studentId + ")?\n"
                + "This will also remove the student's user account.\n"
                + "The guardian record will only be removed if no other students share the same guardian.");

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // 1. Look up linked user_id and guardian_id before deleting the student row
                ResultSet rs = MySQL.executeSearch(
                        "SELECT user_id, guardian_id FROM student WHERE student_id = '" + studentId + "'");
                int userId = -1;
                int guardianId = -1;
                if (rs.next()) {
                    userId = rs.getInt("user_id");
                    if (rs.wasNull()) userId = -1;
                    guardianId = rs.getInt("guardian_id");
                    if (rs.wasNull()) guardianId = -1;
                }
                rs.close();

                // 2. Delete student first (removes this student's FK references)
                MySQL.executeIUD("DELETE FROM student WHERE student_id = '" + studentId + "'");

                // 3. Only delete the guardian if no other student still references it
                //    (e.g. a parent with 2 children — shared guardian must be kept)
                if (guardianId > 0) {
                    ResultSet guardianCheck = MySQL.executeSearch(
                            "SELECT COUNT(*) AS cnt FROM student WHERE guardian_id = " + guardianId);
                    int otherStudentsWithSameGuardian = 0;
                    if (guardianCheck.next()) {
                        otherStudentsWithSameGuardian = guardianCheck.getInt("cnt");
                    }
                    guardianCheck.close();

                    if (otherStudentsWithSameGuardian == 0) {
                        MySQL.executeIUD("DELETE FROM guardian WHERE guardian_id = " + guardianId);
                    }
                }

                // 4. Delete linked user account (each student has their own account)
                if (userId > 0) {
                    MySQL.executeIUD("DELETE FROM user WHERE user_id = " + userId);
                }

                loadStudentData();
                JOptionIcon.showSuccessDialog(this, "Student deleted successfully.");

            } catch (Exception e) {
                JOptionIcon.showErrorDialog(this, "Error deleting student: " + e.getMessage());
            }
        }
    }

    private void viewStudentDetails() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionIcon.showWarningDialog(this, "Please select a student to view.");
            return;
        }

        String studentId = tableModel.getValueAt(selectedRow, 0).toString();
        new StudentView(studentId).setVisible(true);
    }

    private void markAttendance() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionIcon.showWarningDialog(this, "Please select a student to mark attendance.");
            return;
        }

        String studentId = tableModel.getValueAt(selectedRow, 0).toString();
        Window parent = SwingUtilities.getWindowAncestor(this);
        new StudentAttendanceDialog((Frame) parent, studentId).setVisible(true);
    }

    private void generateQRCode() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionIcon.showWarningDialog(this, "Please select a student to generate QR code.");
            return;
        }

        Object idObj = tableModel.getValueAt(selectedRow, 0);
        Object nameObj = tableModel.getValueAt(selectedRow, 1);

        if (idObj == null || idObj.toString().trim().isEmpty()) {
            JOptionIcon.showErrorDialog(this, "Student ID is missing. Cannot generate QR code.");
            return;
        }

        String studentId = idObj.toString();
        String studentName = nameObj != null ? nameObj.toString() : "Unknown";

        try {
            lk.educore.schoolms.model.QRCodeGenerator.generateStudentQR(studentId, studentName);
            JOptionIcon.showSuccessDialog(this, "QR code generated successfully for " + studentName);
        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error generating QR code: " + e.getMessage());
        }
    }

    private void generateStudentReport() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("REPORT_TITLE", "All Students Report");
        ReportGenerator.generateReport("StudentReport", parameters, "Student Report");
    }

    private void applyButtonHoverEffect(JButton button, Color normalColor, Color hoverColor) {
        button.setBackground(normalColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(normalColor);
            }
        });
    }

    private void applyPrimaryButtonEffect(JButton button) {
        button.setBackground(new Color(37, 99, 235));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(new Color(29, 78, 216));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(new Color(37, 99, 235));
            }
        });
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

    private void applyDestructiveButtonEffect(JButton button) {
        button.setBackground(new Color(220, 38, 38));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(new Color(185, 28, 28));
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(new Color(220, 38, 38));
                }
            }
        });
    }
}
