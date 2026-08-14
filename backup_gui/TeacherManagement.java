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
        
        btnDelete = new JButton("Delete");
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
        panel.add(new JLabel("Teacher Attendance Management - Coming Soon", SwingConstants.CENTER), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createLeavePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.add(new JLabel("Leave Management - Coming Soon", SwingConstants.CENTER), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createSalaryPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.add(new JLabel("Salary Management - Coming Soon", SwingConstants.CENTER), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createChartsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.add(new JLabel("Teacher Analytics - Coming Soon", SwingConstants.CENTER), BorderLayout.CENTER);
        return panel;
    }

    private void loadTeacherData() {
        try {
            tableModel.setRowCount(0);
            
            Connection conn = MySQL.getConnection();
            String query = "SELECT t.NIC, t.first_name, t.last_name, tt.type_name, t.email, t.phone, t.salary, ts.status_name " +
                          "FROM teacher t " +
                          "JOIN teacher_type tt ON t.teacher_type_id = tt.teacher_type_id " +
                          "JOIN teacher_status ts ON t.status_id = ts.status_id " +
                          "ORDER BY t.first_name, t.last_name";
            
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            
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
            stmt.close();
            conn.close();
            
        } catch (SQLException e) {
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
            
            Connection conn = MySQL.getConnection();
            String query;
            
            switch (searchBy) {
                case "NIC":
                    query = "SELECT t.NIC, t.first_name, t.last_name, tt.type_name, t.email, t.phone, t.salary, ts.status_name " +
                            "FROM teacher t " +
                            "JOIN teacher_type tt ON t.teacher_type_id = tt.teacher_type_id " +
                            "JOIN teacher_status ts ON t.status_id = ts.status_id " +
                            "WHERE t.NIC LIKE ? " +
                            "ORDER BY t.first_name, t.last_name";
                    break;
                case "Name":
                    query = "SELECT t.NIC, t.first_name, t.last_name, tt.type_name, t.email, t.phone, t.salary, ts.status_name " +
                            "FROM teacher t " +
                            "JOIN teacher_type tt ON t.teacher_type_id = tt.teacher_type_id " +
                            "JOIN teacher_status ts ON t.status_id = ts.status_id " +
                            "WHERE t.first_name LIKE ? OR t.last_name LIKE ? " +
                            "ORDER BY t.first_name, t.last_name";
                    break;
                case "Type":
                    query = "SELECT t.NIC, t.first_name, t.last_name, tt.type_name, t.email, t.phone, t.salary, ts.status_name " +
                            "FROM teacher t " +
                            "JOIN teacher_type tt ON t.teacher_type_id = tt.teacher_type_id " +
                            "JOIN teacher_status ts ON t.status_id = ts.status_id " +
                            "WHERE tt.type_name LIKE ? " +
                            "ORDER BY t.first_name, t.last_name";
                    break;
                case "Status":
                    query = "SELECT t.NIC, t.first_name, t.last_name, tt.type_name, t.email, t.phone, t.salary, ts.status_name " +
                            "FROM teacher t " +
                            "JOIN teacher_type tt ON t.teacher_type_id = tt.teacher_type_id " +
                            "JOIN teacher_status ts ON t.status_id = ts.status_id " +
                            "WHERE ts.status_name LIKE ? " +
                            "ORDER BY t.first_name, t.last_name";
                    break;
                default:
                    query = "SELECT t.NIC, t.first_name, t.last_name, tt.type_name, t.email, t.phone, t.salary, ts.status_name " +
                            "FROM teacher t " +
                            "JOIN teacher_type tt ON t.teacher_type_id = tt.teacher_type_id " +
                            "JOIN teacher_status ts ON t.status_id = ts.status_id " +
                            "ORDER BY t.first_name, t.last_name";
            }
            
            PreparedStatement stmt = conn.prepareStatement(query);
            
            if (searchBy.equals("Name")) {
                stmt.setString(1, "%" + searchTerm + "%");
                stmt.setString(2, "%" + searchTerm + "%");
            } else {
                stmt.setString(1, "%" + searchTerm + "%");
            }
            
            ResultSet rs = stmt.executeQuery();
            
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
            stmt.close();
            conn.close();
            
        } catch (SQLException e) {
            JOptionIcon.showErrorDialog(this, "Error searching teachers: " + e.getMessage());
        }
    }

    private void addTeacher() {
        JOptionIcon.showInfoDialog(this, "Add Teacher - Coming Soon");
    }

    private void editTeacher() {
        int selectedRow = teacherTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionIcon.showWarningDialog(this, "Please select a teacher to edit.");
            return;
        }
        
        String nic = tableModel.getValueAt(selectedRow, 0).toString();
        JOptionIcon.showInfoDialog(this, "Edit Teacher: " + nic + " - Coming Soon");
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
                Connection conn = MySQL.getConnection();
                String query = "UPDATE teacher SET status_id = 2 WHERE NIC = ?"; // 2 = Inactive
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, nic);
                stmt.executeUpdate();
                stmt.close();
                conn.close();
                
                JOptionIcon.showSuccessDialog(this, "Teacher deleted successfully.");
                loadTeacherData();
                
            } catch (SQLException e) {
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
        JOptionIcon.showInfoDialog(this, "View Teacher Details: " + nic + " - Coming Soon");
    }

    private void markAttendance() {
        int selectedRow = teacherTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionIcon.showWarningDialog(this, "Please select a teacher to mark attendance.");
            return;
        }
        
        String nic = tableModel.getValueAt(selectedRow, 0).toString();
        JOptionIcon.showInfoDialog(this, "Mark Attendance for: " + nic + " - Coming Soon");
    }

    private void applyLeave() {
        int selectedRow = teacherTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionIcon.showWarningDialog(this, "Please select a teacher to apply leave.");
            return;
        }
        
        String nic = tableModel.getValueAt(selectedRow, 0).toString();
        JOptionIcon.showInfoDialog(this, "Apply Leave for: " + nic + " - Coming Soon");
    }

    private void manageSalary() {
        int selectedRow = teacherTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionIcon.showWarningDialog(this, "Please select a teacher to manage salary.");
            return;
        }
        
        String nic = tableModel.getValueAt(selectedRow, 0).toString();
        JOptionIcon.showInfoDialog(this, "Manage Salary for: " + nic + " - Coming Soon");
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