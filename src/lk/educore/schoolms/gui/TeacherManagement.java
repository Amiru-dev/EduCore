package lk.educore.schoolms.gui;

import lk.educore.schoolms.model.MySQL;
import lk.educore.schoolms.validate.JOptionIcon;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

/**
 * Teacher Management Panel
 * Main CRUD interface for teacher management with tabs for info, attendance, leave, salary, and charts
 */
public class TeacherManagement extends JPanel {

    private JTable teacherTable;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JComboBox<String> cmbSearchBy;
    private JButton btnAdd, btnEdit, btnDelete, btnView, btnRefresh, btnAttendance, btnLeave, btnSalary, btnQRCode;
    private JTabbedPane tabbedPane;

    public TeacherManagement() {
        initComponents();
        loadTeacherData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Header panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Tabbed pane
        tabbedPane = new JTabbedPane();
        
        // Teacher Info Tab
        JPanel infoPanel = createInfoPanel();
        tabbedPane.addTab("Teacher Info", infoPanel);
        
        // Attendance Tab
        JPanel attendancePanel = createAttendancePanel();
        tabbedPane.addTab("Attendance", attendancePanel);
        
        // Leave Tab
        JPanel leavePanel = createLeavePanel();
        tabbedPane.addTab("Leave Management", leavePanel);
        
        // Salary Tab
        JPanel salaryPanel = createSalaryPanel();
        tabbedPane.addTab("Salary", salaryPanel);
        
        // Charts Tab
        JPanel chartsPanel = createChartsPanel();
        tabbedPane.addTab("Analytics", chartsPanel);

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Title
        JLabel titleLabel = new JLabel("Teacher Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        panel.add(titleLabel, BorderLayout.WEST);

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        cmbSearchBy = new JComboBox<>(new String[]{"NIC", "Name", "Type", "Status"});
        txtSearch = new JTextField(20);
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                searchTeachers();
            }
        });
        
        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(e -> searchTeachers());
        
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
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        
        btnAdd = new JButton("Add Teacher");
        btnAdd.addActionListener(e -> addTeacher());
        
        btnEdit = new JButton("Edit");
        btnEdit.addActionListener(e -> editTeacher());
        
        btnDelete = new JButton("Disable");
        btnDelete.addActionListener(e -> deleteTeacher());
        
        btnView = new JButton("View Details");
        btnView.addActionListener(e -> viewTeacherDetails());
        
        btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> loadTeacherData());
        
        btnAttendance = new JButton("Mark Attendance");
        btnAttendance.addActionListener(e -> markAttendance());
        
        btnLeave = new JButton("Apply Leave");
        btnLeave.addActionListener(e -> applyLeave());
        
        btnSalary = new JButton("Manage Salary");
        btnSalary.addActionListener(e -> manageSalary());
        
        btnQRCode = new JButton("Generate QR");
        btnQRCode.addActionListener(e -> generateQRCode());

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnView);
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnAttendance);
        buttonPanel.add(btnLeave);
        buttonPanel.add(btnSalary);
        buttonPanel.add(btnQRCode);

        panel.add(buttonPanel, BorderLayout.NORTH);

        // Table
        tableModel = new DefaultTableModel(
            new Object[]{"NIC", "Name", "Type", "Email", "Phone", "Salary", "Status"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        teacherTable = new JTable(tableModel);
        teacherTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        teacherTable.setRowHeight(25);
        teacherTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        // Add mouse listener for double-click
        teacherTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    viewTeacherDetails();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(teacherTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createAttendancePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnRefresh = new JButton("Refresh Attendance");
        topPanel.add(btnRefresh);
        panel.add(topPanel, BorderLayout.NORTH);

        DefaultTableModel attendanceModel = new DefaultTableModel(
                new Object[]{"ID", "NIC", "Name", "Date", "Check In", "Check Out"}, 0
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

        java.awt.event.ActionListener loadAction = e -> {
            try {
                attendanceModel.setRowCount(0);
                ResultSet rs = MySQL.executeSearch("SELECT a.attendance_id, a.teacher_NIC, "
                        + "CONCAT(t.first_name, ' ', t.last_name) as name, "
                        + "a.attendance_date, a.check_in, a.check_out "
                        + "FROM teacher_attendance a "
                        + "INNER JOIN teacher t ON a.teacher_NIC = t.NIC "
                        + "ORDER BY a.attendance_date DESC LIMIT 100");

                while (rs.next()) {
                    attendanceModel.addRow(new Object[]{
                        rs.getString("attendance_id"), 
                        rs.getString("teacher_NIC"), 
                        rs.getString("name"),
                        rs.getString("attendance_date"), 
                        rs.getString("check_in"), 
                        rs.getString("check_out")
                    });
                }
                rs.close();
            } catch (Exception ex) {
                JOptionIcon.showErrorDialog(this, "Error loading attendance data: " + ex.getMessage());
            }
        };

        btnRefresh.addActionListener(loadAction);
        SwingUtilities.invokeLater(() -> loadAction.actionPerformed(null));
        
        return panel;
    }

    private JPanel createLeavePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnRefresh = new JButton("Refresh Leave");
        topPanel.add(btnRefresh);
        panel.add(topPanel, BorderLayout.NORTH);

        DefaultTableModel leaveModel = new DefaultTableModel(
                new Object[]{"ID", "NIC", "Name", "Type", "Start Date", "End Date", "Status"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable leaveTable = new JTable(leaveModel);
        leaveTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        leaveTable.setRowHeight(25);
        leaveTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        JScrollPane scrollPane = new JScrollPane(leaveTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        java.awt.event.ActionListener loadAction = e -> {
            try {
                leaveModel.setRowCount(0);
                ResultSet rs = MySQL.executeSearch("SELECT l.leave_id, l.teacher_NIC, "
                        + "CONCAT(t.first_name, ' ', t.last_name) as name, "
                        + "lt.leave_type_name, l.start_date, l.end_date, l.status "
                        + "FROM teacher_leave l "
                        + "INNER JOIN teacher t ON l.teacher_NIC = t.NIC "
                        + "INNER JOIN leave_type lt ON l.leave_type_id = lt.leave_type_id "
                        + "ORDER BY l.leave_id DESC LIMIT 100");

                while (rs.next()) {
                    leaveModel.addRow(new Object[]{
                        rs.getString("leave_id"), 
                        rs.getString("teacher_NIC"), 
                        rs.getString("name"),
                        rs.getString("leave_type_name"), 
                        rs.getString("start_date"), 
                        rs.getString("end_date"),
                        rs.getString("status")
                    });
                }
                rs.close();
            } catch (Exception ex) {
                JOptionIcon.showErrorDialog(this, "Error loading leave data: " + ex.getMessage());
            }
        };

        btnRefresh.addActionListener(loadAction);
        SwingUtilities.invokeLater(() -> loadAction.actionPerformed(null));
        
        return panel;
    }

    private JPanel createSalaryPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnRefresh = new JButton("Refresh Salary");
        topPanel.add(btnRefresh);
        panel.add(topPanel, BorderLayout.NORTH);

        DefaultTableModel salaryModel = new DefaultTableModel(
                new Object[]{"NIC", "Name", "Type", "Salary"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable salaryTable = new JTable(salaryModel);
        salaryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        salaryTable.setRowHeight(25);
        salaryTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        JScrollPane scrollPane = new JScrollPane(salaryTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        java.awt.event.ActionListener loadAction = e -> {
            try {
                salaryModel.setRowCount(0);
                ResultSet rs = MySQL.executeSearch("SELECT t.NIC, "
                        + "CONCAT(t.first_name, ' ', t.last_name) as name, "
                        + "tt.type_name, t.salary "
                        + "FROM teacher t "
                        + "INNER JOIN teacher_type tt ON t.teacher_type_id = tt.teacher_type_id "
                        + "ORDER BY t.first_name ASC LIMIT 100");

                while (rs.next()) {
                    salaryModel.addRow(new Object[]{
                        rs.getString("NIC"), 
                        rs.getString("name"),
                        rs.getString("type_name"), 
                        String.format("Rs. %,.2f", rs.getDouble("salary"))
                    });
                }
                rs.close();
            } catch (Exception ex) {
                JOptionIcon.showErrorDialog(this, "Error loading salary data: " + ex.getMessage());
            }
        };

        btnRefresh.addActionListener(loadAction);
        SwingUtilities.invokeLater(() -> loadAction.actionPerformed(null));
        
        return panel;
    }

    private JPanel createChartsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        
        JPanel cardTotal = createStatCard("Total Teachers", "Loading...");
        JPanel cardActive = createStatCard("Active Teachers", "Loading...");
        JPanel cardMale = createStatCard("Male Teachers", "Loading...");
        JPanel cardFemale = createStatCard("Female Teachers", "Loading...");
        
        statsPanel.add(cardTotal);
        statsPanel.add(cardActive);
        statsPanel.add(cardMale);
        statsPanel.add(cardFemale);
        
        panel.add(statsPanel, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnRefresh = new JButton("Refresh Analytics");
        bottomPanel.add(btnRefresh);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        
        java.awt.event.ActionListener loadAction = e -> {
            try {
                ResultSet rs = MySQL.executeSearch("SELECT COUNT(*) as total, SUM(IF(status_id=1, 1, 0)) as active, SUM(IF(gender='Male', 1, 0)) as male, SUM(IF(gender='Female', 1, 0)) as female FROM teacher");
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

    private void loadTeacherData() {
        try {
            tableModel.setRowCount(0);
            
            ResultSet rs = MySQL.executeSearch("SELECT t.NIC, t.first_name, t.last_name, tt.type_name, t.email, t.phone, t.salary, ts.status_name " +
                          "FROM teacher t " +
                          "JOIN teacher_type tt ON t.teacher_type_id = tt.teacher_type_id " +
                          "JOIN teacher_status ts ON t.status_id = ts.status_id " +
                          "ORDER BY t.first_name, t.last_name");
            
            while (rs.next()) {
                String nic = rs.getString("NIC");
                String name = rs.getString("first_name") + " " + rs.getString("last_name");
                String type = rs.getString("type_name");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                double salary = rs.getDouble("salary");
                String status = rs.getString("status_name");
                
                tableModel.addRow(new Object[]{nic, name, type, email, phone, String.format("Rs. %,.2f", salary), status});
            }
            
            rs.close();
            
        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error loading teacher data: " + e.getMessage());
        }
    }

    private void searchTeachers() {
        try {
            String searchTerm = txtSearch.getText().trim();
            String searchBy = (String) cmbSearchBy.getSelectedItem();
            
            if (searchTerm.isEmpty()) {
                loadTeacherData();
                return;
            }
            
            tableModel.setRowCount(0);
            
            String query;
            
            switch (searchBy) {
                case "NIC":
                    query = "SELECT t.NIC, t.first_name, t.last_name, tt.type_name, t.email, t.phone, t.salary, ts.status_name " +
                            "FROM teacher t " +
                            "JOIN teacher_type tt ON t.teacher_type_id = tt.teacher_type_id " +
                            "JOIN teacher_status ts ON t.status_id = ts.status_id " +
                            "WHERE t.NIC LIKE '%" + searchTerm + "%' " +
                            "ORDER BY t.first_name, t.last_name";
                    break;
                case "Name":
                    query = "SELECT t.NIC, t.first_name, t.last_name, tt.type_name, t.email, t.phone, t.salary, ts.status_name " +
                            "FROM teacher t " +
                            "JOIN teacher_type tt ON t.teacher_type_id = tt.teacher_type_id " +
                            "JOIN teacher_status ts ON t.status_id = ts.status_id " +
                            "WHERE t.first_name LIKE '%" + searchTerm + "%' OR t.last_name LIKE '%" + searchTerm + "%' " +
                            "ORDER BY t.first_name, t.last_name";
                    break;
                case "Type":
                    query = "SELECT t.NIC, t.first_name, t.last_name, tt.type_name, t.email, t.phone, t.salary, ts.status_name " +
                            "FROM teacher t " +
                            "JOIN teacher_type tt ON t.teacher_type_id = tt.teacher_type_id " +
                            "JOIN teacher_status ts ON t.status_id = ts.status_id " +
                            "WHERE tt.type_name LIKE '%" + searchTerm + "%' " +
                            "ORDER BY t.first_name, t.last_name";
                    break;
                case "Status":
                    query = "SELECT t.NIC, t.first_name, t.last_name, tt.type_name, t.email, t.phone, t.salary, ts.status_name " +
                            "FROM teacher t " +
                            "JOIN teacher_type tt ON t.teacher_type_id = tt.teacher_type_id " +
                            "JOIN teacher_status ts ON t.status_id = ts.status_id " +
                            "WHERE ts.status_name LIKE '%" + searchTerm + "%' " +
                            "ORDER BY t.first_name, t.last_name";
                    break;
                default:
                    query = "SELECT t.NIC, t.first_name, t.last_name, tt.type_name, t.email, t.phone, t.salary, ts.status_name " +
                            "FROM teacher t " +
                            "JOIN teacher_type tt ON t.teacher_type_id = tt.teacher_type_id " +
                            "JOIN teacher_status ts ON t.status_id = ts.status_id " +
                            "ORDER BY t.first_name, t.last_name";
            }
            
            ResultSet rs = MySQL.executeSearch(query);
            
            while (rs.next()) {
                String nic = rs.getString("NIC");
                String name = rs.getString("first_name") + " " + rs.getString("last_name");
                String type = rs.getString("type_name");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                double salary = rs.getDouble("salary");
                String status = rs.getString("status_name");
                
                tableModel.addRow(new Object[]{nic, name, type, email, phone, String.format("Rs. %,.2f", salary), status});
            }
            
            rs.close();
            
        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error searching teachers: " + e.getMessage());
        }
    }

    private void addTeacher() {
        Window parent = SwingUtilities.getWindowAncestor(this);
        TeacherDialog dialog = new TeacherDialog(parent, null);
        dialog.setVisible(true);
        loadTeacherData();
    }

    private void editTeacher() {
        int selectedRow = teacherTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionIcon.showWarningDialog(this, "Please select a teacher to edit.");
            return;
        }
        
        String nic = tableModel.getValueAt(selectedRow, 0).toString();
        Window parent = SwingUtilities.getWindowAncestor(this);
        TeacherDialog dialog = new TeacherDialog(parent, nic);
        dialog.setVisible(true);
        loadTeacherData();
    }

    private void deleteTeacher() {
        int selectedRow = teacherTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionIcon.showWarningDialog(this, "Please select a teacher to delete.");
            return;
        }
        
        String nic = tableModel.getValueAt(selectedRow, 0).toString();
        String teacherName = tableModel.getValueAt(selectedRow, 1).toString();
        
        int confirm = JOptionIcon.showConfirmDialog(this, 
            "Are you sure you want to delete teacher " + teacherName + " (" + nic + ")?");
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String query = "UPDATE teacher SET status_id = 2 WHERE NIC = '" + nic + "'"; // 2 = Inactive
                MySQL.executeIUD(query);
                
                JOptionIcon.showSuccessDialog(this, "Teacher deleted successfully.");
                loadTeacherData();
                
            } catch (Exception e) {
                JOptionIcon.showErrorDialog(this, "Error deleting teacher: " + e.getMessage());
            }
        }
    }

    private void viewTeacherDetails() {
        int selectedRow = teacherTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionIcon.showWarningDialog(this, "Please select a teacher to view.");
            return;
        }
        
        String nic = tableModel.getValueAt(selectedRow, 0).toString();
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Teacher Details", JDialog.ModalityType.APPLICATION_MODAL);
        dialog.add(new TeacherInformation(nic));
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void markAttendance() {
        int selectedRow = teacherTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionIcon.showWarningDialog(this, "Please select a teacher to mark attendance.");
            return;
        }
        
        String nic = tableModel.getValueAt(selectedRow, 0).toString();
        
        int confirm = JOptionIcon.showConfirmDialog(this, "Mark attendance for today (Check In/Out) for teacher " + nic + "?");
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                java.text.SimpleDateFormat sdfDate = new java.text.SimpleDateFormat("yyyy-MM-dd");
                java.text.SimpleDateFormat sdfTime = new java.text.SimpleDateFormat("HH:mm:ss");
                java.text.SimpleDateFormat sdfDateTime = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                java.util.Date now = new java.util.Date();
                
                String date = sdfDate.format(now);
                String time = sdfTime.format(now);
                String datetime = sdfDateTime.format(now);
                
                ResultSet rs = MySQL.executeSearch("SELECT * FROM teacher_attendance WHERE teacher_NIC = '" + nic + "' AND attendance_date = '" + date + "'");
                if (rs.next()) {
                    if (rs.getString("check_out") == null) {
                        MySQL.executeIUD("UPDATE teacher_attendance SET check_out = '" + time + "' WHERE teacher_NIC = '" + nic + "' AND attendance_date = '" + date + "'");
                        JOptionIcon.showSuccessDialog(this, "Check Out recorded successfully.");
                    } else {
                        JOptionIcon.showWarningDialog(this, "Attendance already fully marked for today.");
                    }
                } else {
                    MySQL.executeIUD("INSERT INTO teacher_attendance (teacher_NIC, date_time, attendance_date, check_in) VALUES ('" + nic + "', '" + datetime + "', '" + date + "', '" + time + "')");
                    JOptionIcon.showSuccessDialog(this, "Check In recorded successfully.");
                }
                rs.close();
            } catch (Exception e) {
                JOptionIcon.showErrorDialog(this, "Error marking attendance: " + e.getMessage());
            }
        }
    }

    private void applyLeave() {
        int selectedRow = teacherTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionIcon.showWarningDialog(this, "Please select a teacher to apply leave.");
            return;
        }
        
        String nic = tableModel.getValueAt(selectedRow, 0).toString();
        
        JTextField txtStartDate = new JTextField(10);
        JTextField txtEndDate = new JTextField(10);
        JComboBox<String> cmbLeaveType = new JComboBox<>();
        try {
            ResultSet rs = MySQL.executeSearch("SELECT * FROM leave_type");
            while (rs.next()) {
                cmbLeaveType.addItem(rs.getString("leave_type_id") + " - " + rs.getString("leave_type_name"));
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.add(new JLabel("Leave Type:"));
        panel.add(cmbLeaveType);
        panel.add(new JLabel("Start Date (yyyy-MM-dd):"));
        panel.add(txtStartDate);
        panel.add(new JLabel("End Date (yyyy-MM-dd):"));
        panel.add(txtEndDate);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Apply Leave for " + nic, JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String typeStr = cmbLeaveType.getSelectedItem().toString();
                String typeId = typeStr.split(" - ")[0];
                String startDate = txtStartDate.getText();
                String endDate = txtEndDate.getText();
                
                MySQL.executeIUD("INSERT INTO teacher_leave (teacher_NIC, leave_type_id, start_date, end_date, status) VALUES ('" + nic + "', " + typeId + ", '" + startDate + "', '" + endDate + "', 'Pending')");
                JOptionIcon.showSuccessDialog(this, "Leave applied successfully.");
            } catch (Exception e) {
                JOptionIcon.showErrorDialog(this, "Error applying leave: " + e.getMessage());
            }
        }
    }

    private void manageSalary() {
        int selectedRow = teacherTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionIcon.showWarningDialog(this, "Please select a teacher to manage salary.");
            return;
        }
        
        String nic = tableModel.getValueAt(selectedRow, 0).toString();
        String currentSalary = tableModel.getValueAt(selectedRow, 5).toString().replace("Rs. ", "").replace(",", "");
        
        String newSalary = JOptionPane.showInputDialog(this, "Enter new salary for teacher " + nic + ":", currentSalary);
        if (newSalary != null && !newSalary.trim().isEmpty()) {
            try {
                double salary = Double.parseDouble(newSalary);
                MySQL.executeIUD("UPDATE teacher SET salary = " + salary + " WHERE NIC = '" + nic + "'");
                JOptionIcon.showSuccessDialog(this, "Salary updated successfully.");
                loadTeacherData();
            } catch (NumberFormatException e) {
                JOptionIcon.showErrorDialog(this, "Invalid salary amount.");
            } catch (Exception e) {
                JOptionIcon.showErrorDialog(this, "Error updating salary: " + e.getMessage());
            }
        }
    }

    private void generateQRCode() {
        int selectedRow = teacherTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionIcon.showWarningDialog(this, "Please select a teacher to generate QR code.");
            return;
        }
        
        String nic = tableModel.getValueAt(selectedRow, 0).toString();
        String teacherName = tableModel.getValueAt(selectedRow, 1).toString();
        
        try {
            lk.educore.schoolms.model.QRCodeGenerator.generateTeacherQR(nic, teacherName);
            JOptionIcon.showSuccessDialog(this, "QR code generated successfully for " + teacherName);
        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error generating QR code: " + e.getMessage());
        }
    }
}